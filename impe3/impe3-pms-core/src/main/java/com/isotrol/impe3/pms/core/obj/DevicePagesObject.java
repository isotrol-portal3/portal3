/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.isotrol.impe3.pms.core.obj.PageObject.PAGE_CLASS;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.PAGE;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.api.Identifiable;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.PageMapKey;
import com.isotrol.impe3.core.support.IdentifiableMaps;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PagesPB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.core.FileManager;


/**
 * Collection of pages with the same portal/device domain object.
 * @author Andres Rodriguez
 */
public abstract class DevicePagesObject extends PagesObject {
	/**
	 * Returns the empty collection.
	 * @param portal Portal Id.
	 * @param device Device Id.
	 * @return The requested collection.
	 */
	static DevicePagesObject of(UUID portal, UUID device) {
		return new Empty(portal, device);
	}

	/**
	 * Builds a collection from a set of pages.
	 * @param portal Portal Id.
	 * @param device Device Id.
	 * @param pages Pages.
	 * @param parent Parent pages.
	 * @return The requested collection.
	 */
	static DevicePagesObject of(UUID portal, UUID device, Iterable<PageObject> pages, DevicePagesObject parent) {
		if (Iterables.isEmpty(pages) && (parent == null || parent.isEmpty())) {
			return of(portal, device);
		}
		return new Pages(portal, device, pages, parent);
	}

	/** Device Id. */
	private final UUID device;

	/**
	 * Constructor.
	 * @param portal Portal Id.
	 * @param device Device Id.
	 */
	DevicePagesObject(UUID portal, UUID device) {
		super(portal);
		this.device = checkNotNull(device);
	}

	/**
	 * Returns the device id.
	 * @return the device id.
	 */
	public final UUID getDevice() {
		return device;
	}

	/**
	 * Filters this collection by page class.
	 * @param pageClass Page class.
	 * @return The filtered collection.
	 */
	public final Map<UUID, PageObject> byClass(PageClass pageClass) {
		return Maps.filterValues(this, compose(equalTo(pageClass), PAGE_CLASS));
	}

	/**
	 * Returns the portal error pages, except the default.
	 * @return The portal error pages, except the default.
	 */
	public final Map<UUID, PageObject> getErrorPages() {
		final Map<UUID, PageObject> errorPages = byClass(PageClass.ERROR);
		final PageObject defaultError = getByKey(PageMapKey.error());
		if (defaultError == null) {
			return errorPages;
		}
		return Maps.filterValues(errorPages, not(equalTo(defaultError)));
	}

	/**
	 * Returns a page by key.
	 * @param key Requested key.
	 * @return The requested page or null if not found.
	 */
	public abstract PageObject getByKey(PageMapKey key);

	/**
	 * Returns the available page map keys.
	 * @return The available keys.
	 */
	public abstract Set<PageMapKey> getPageMapKeys();

	/**
	 * Returns the discarded pages because of duplicate page keys.
	 * @return The duplicate pages..
	 */
	public abstract Set<PageObject> getDuplicates();

	/**
	 * Returns the page by key map.
	 * @return The requested map.
	 */
	abstract Map<PageMapKey, PageObject> byKey();

	/**
	 * Returns a template by name.
	 * @param name Template name.
	 * @return The requested template or null if not found.
	 */
	public final PageObject getTemplateByName(String name) {
		return templates().get(name);
	}

	/**
	 * Returns the templates by name map.
	 * @return The requested map.
	 */
	abstract Map<String, PageObject> templates();

	/**
	 * Returns all the templates in the hierarchy by key.
	 * @return The requested map.
	 */
	abstract Map<UUID, PageObject> allTemplates();

	public boolean isInheritedPage(PageObject page) throws PMSException {
		checkNotNull(page);
		checkArgument(containsKey(page.getId()));
		return page.isInherited(getPortal());
	}

	private Predicate<PageObject> own() {
		return not(PageObject.inherited(getPortal()));
	}

	private Iterable<PageObject> exportedTemplates(Iterable<PageObject> pages) throws PMSException {
		final List<PageObject> templates = Lists.newArrayList(pages);
		if (templates.size() > 1) {
			final Map<PageObject, Integer> om = calculateTplLevel(ImmutableSet.copyOf(templates), templates.get(0),
				Maps.<PageObject, Integer> newHashMap());
			Collections.sort(templates, Ordering.natural().onResultOf(Functions.forMap(om, 0)));
		}
		return templates;
	}

	private PagesPB export(FileManager fileManager, Iterable<PageObject> pages) throws PMSException {
		final PagesPB.Builder b = PagesPB.newBuilder();
		for (PageObject p : pages) {
			b.addPages(p.export(fileManager, this));
		}
		return b.build();
	}

	final PagesPB export(FileManager fileManager, PageClass pageClass) throws PMSException {
		Iterable<PageObject> pages = filter(byClass(pageClass).values(), own());
		if (PageClass.TEMPLATE == pageClass) {
			pages = exportedTemplates(pages);
		}
		return export(fileManager, pages);

	}

	final PagesPB export(FileManager fileManager) throws PMSException {
		final Iterable<PageObject> pages = filter(values(), own());
		final Predicate<PageObject> isTemplate = compose(equalTo(PageClass.TEMPLATE), PAGE_CLASS);
		final Iterable<PageObject> tpls = filter(pages, isTemplate);
		final Iterable<PageObject> others = filter(pages, not(isTemplate));
		return export(fileManager, concat(exportedTemplates(tpls), others));
	}

	private Map<PageObject, Integer> calculateTplLevel(Set<PageObject> pages, PageObject page,
		Map<PageObject, Integer> map) throws PMSException {
		if (!map.containsKey(page)) {
			PageObject template = getTemplate(page);
			if (template == null || !pages.contains(template)) {
				map.put(page, 0);
			} else {
				calculateTplLevel(pages, template, map);
				map.put(page, map.get(template) + 1);
			}
		}
		return map;
	}

	/**
	 * Returns the template to use for a certain page.
	 * @param page The page which template is requested.
	 * @return The template to use or {@code null} if the page has no template.
	 */
	public final PageObject getTemplate(PageObject page) throws PMSException {
		final UUID templateId = page.getTemplateId();
		if (templateId == null) {
			return null;
		}
		final String name = PAGE.checkNotNull(allTemplates().get(templateId), templateId).getName();
		final PageObject template = PAGE.checkNotNull(templates().get(name), templateId);
		return template;
	}

	private static final class Empty extends DevicePagesObject {
		/**
		 * Constructor.
		 * @param portal Portal Id.
		 * @param device Device Id.
		 */
		Empty(UUID portal, UUID device) {
			super(portal, device);
		}

		@Override
		protected Map<UUID, PageObject> delegate() {
			return ImmutableMap.of();
		}

		@Override
		public PageObject getByKey(PageMapKey key) {
			return null;
		}

		@Override
		public Set<PageMapKey> getPageMapKeys() {
			return ImmutableSet.of();
		}

		public Set<PageObject> getDuplicates() {
			return ImmutableSet.of();
		}

		@Override
		Map<PageMapKey, PageObject> byKey() {
			return ImmutableMap.of();
		}

		@Override
		Map<String, PageObject> templates() {
			return ImmutableMap.of();
		}

		@Override
		Map<UUID, PageObject> allTemplates() {
			return ImmutableMap.of();
		}

	}

	private static final class Pages extends DevicePagesObject {
		/** Pages map. */
		private final ImmutableMap<UUID, PageObject> map;
		/** By key index. */
		private final ImmutableMap<PageMapKey, PageObject> byKey;
		/** By key index. */
		private final ImmutableMap<String, PageObject> templates;
		/** Duplicate key pages. */
		private final ImmutableSet<PageObject> duplicates;
		/** All templates. */
		private final ImmutableMap<UUID, PageObject> allTemplates;

		/**
		 * Constructor.
		 * @param portal Portal Id.
		 * @param device Device Id.
		 * @param pages Pages.
		 * @param parent Parent pages.
		 */
		private Pages(UUID portal, UUID device, Iterable<PageObject> pages, DevicePagesObject parent) {
			super(portal, device);
			final Map<PageMapKey, PageObject> keys = Maps.newHashMap();
			final Map<String, PageObject> ownTpl = Maps.newHashMap();
			final ImmutableSet.Builder<PageObject> dups = ImmutableSet.builder();
			for (PageObject p : pages) {
				final PageMapKey k = p.getKey();
				if (k != null) {
					if (keys.containsKey(k)) {
						Loggers.pms().warn("Duplicate page key [{}]. Keeping one page.", new Object[] {k});
						dups.add(p);
					} else {
						keys.put(k, p);
					}
				} else if (PageClass.TEMPLATE == p.getPageClass()) {
					ownTpl.put(p.getName(), p);
				}
			}
			final Map<PageMapKey, PageObject> base = Maps.newHashMap();
			final Map<String, PageObject> baseTpl = Maps.newHashMap();
			final ImmutableMap<UUID, PageObject> ownTplById = Maps.uniqueIndex(ownTpl.values(), Identifiable.ID);
			if (parent != null) {
				base.putAll(parent.byKey());
				baseTpl.putAll(parent.templates());
				final Map<UUID, PageObject> baseAllTpl = Maps.newHashMap(parent.allTemplates());
				baseAllTpl.putAll(ownTplById);
				this.allTemplates = ImmutableMap.copyOf(baseAllTpl);
			} else {
				this.allTemplates = ownTplById;
			}
			base.putAll(keys);
			baseTpl.putAll(ownTpl);
			this.byKey = ImmutableMap.copyOf(base);
			this.templates = ImmutableMap.copyOf(baseTpl);
			this.duplicates = dups.build();
			this.map = IdentifiableMaps.immutableOf(Iterables.concat(this.byKey.values(), this.templates.values(),
				this.duplicates));
		}

		@Override
		protected Map<UUID, PageObject> delegate() {
			return map;
		}

		@Override
		public PageObject getByKey(PageMapKey key) {
			return byKey.get(key);
		}

		@Override
		public Set<PageMapKey> getPageMapKeys() {
			return byKey.keySet();
		}

		@Override
		public Set<PageObject> getDuplicates() {
			return duplicates;
		}

		@Override
		Map<PageMapKey, PageObject> byKey() {
			return byKey;
		}

		@Override
		Map<String, PageObject> templates() {
			return templates;
		}

		@Override
		Map<UUID, PageObject> allTemplates() {
			return allTemplates;
		}

	}
}
