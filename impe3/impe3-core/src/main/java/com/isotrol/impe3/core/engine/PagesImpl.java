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

package com.isotrol.impe3.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Maps.filterValues;
import static com.google.common.collect.Maps.uniqueIndex;
import static com.isotrol.impe3.api.Identifiable.ID;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.Page;
import com.isotrol.impe3.core.PageContext;
import com.isotrol.impe3.core.PageMapKey;
import com.isotrol.impe3.core.Pages;
import com.isotrol.impe3.core.PortalModel;


/**
 * Collection of pages in a portal.
 * @author Andres Rodriguez
 */
public final class PagesImpl implements Pages {
	private final Portal portal;
	private final ImmutableMap<UUID, Page> byId;
	private final ImmutableMap<UUID, ByDevice> byDevice;
	private final ImmutableMap<UUID, CIP> cips;
	private final CategoryRoutingMap categoryRouting;
	private final ContentTypeRoutingMap navigation;
	private final ContentTypeRoutingMap content;
	private final Logger logger = Loggers.page();

	public PagesImpl(final Portal portal, final Iterable<Page> pages) {
		this.portal = checkNotNull(portal);
		this.byId = uniqueIndex(pages, ID);
		final Map<UUID, CIP> cips = Maps.newHashMap();
		final Map<PageMapKey, Page> keys = Maps.newHashMap();
		for (Page page : pages) {
			final PageMapKey k = page.getKey();
			if (k != null) {
				keys.put(k, page);
			}
			for (CIP cip : page.getComponents().values()) {
				cips.put(cip.getId(), cip);
			}
		}
		this.cips = ImmutableMap.copyOf(cips);
		this.categoryRouting = new CategoryRoutingMap(portal.getCategories());
		final ContentTypes contentTypes = portal.getContentTypes();
		this.navigation = ContentTypeRoutingMap.navigation(contentTypes);
		this.content = ContentTypeRoutingMap.content(contentTypes);
		ImmutableMap.Builder<UUID, ByDevice> byDeviceBuilder = ImmutableMap.builder();
		for (Device d : Sets.newHashSet(Iterables.transform(byId.values(), Page.DEVICE))) {
			byDeviceBuilder.put(d.getId(), new ByDevice(d));
		}
		this.byDevice = byDeviceBuilder.build();
	}

	/**
	 * Returns a page by id.
	 * @param id Page Id.
	 * @return The requested page or {@code null} if it is not found.
	 */
	public Page getPage(UUID id) {
		return byId.get(id);
	}

	/**
	 * @see com.isotrol.impe3.core.Pages#getCIP(java.util.UUID)
	 */
	public CIP getCIP(UUID cipId) {
		return cips.get(cipId);
	}

	/**
	 * @see com.isotrol.impe3.core.Pages#getPage(com.isotrol.impe3.api.Route)
	 */
	public Page getPage(Route route) {
		final Device device = route.getDevice();
		final ByDevice map = byDevice.get(device.getId());
		return map != null ? map.getPage(route.getPage()) : null;
	}

	private void log(String msg, PathSegments path) {
		if (logger.isTraceEnabled()) {
			logger.trace("Resolving {}: " + msg, new Object[] {path.toString()});
		}
	}

	private void log(String msg, PageMapKey k) {
		if (logger.isTraceEnabled()) {
			logger.trace("PageMapKey [{}]: " + msg, new Object[] {k.toString()});
		}
	}

	private void log(ContentType contentType) {
		if (logger.isTraceEnabled()) {
			if (contentType != null) {
				logger.trace("Found content type [{}]", new Object[] {contentType.getDefaultName().getDisplayName()});
			} else {
				logger.trace("Content type not found");
			}
		}
	}

	/**
	 * @see com.isotrol.impe3.core.Pages#getPage(com.isotrol.impe3.core.PortalModel,
	 * com.isotrol.impe3.api.PortalRequestContext, com.isotrol.impe3.api.PathSegments,
	 * com.isotrol.impe3.api.LocalParams, com.isotrol.impe3.api.PageKey)
	 */
	public PageContext getPage(PortalModel model, PortalRequestContext context, PathSegments path, LocalParams params,
		PageKey previous) {
		final Device device = context.getDevice();
		final ByDevice map = byDevice.get(device.getId());
		if (map == null) {
			Loggers.core().error("No pages defined for device [%s]", device);
			return null;
		}
		return map.getPage(model, context, path, params, previous);
	}

	/*
	 * By Device
	 */
	private class ByDevice {
		private final ImmutableMap<PageMapKey, Page> byKey;

		ByDevice(Device device) {
			final Map<PageMapKey, Page> keys = Maps.newHashMap();
			for (Page page : filterValues(byId, compose(equalTo(device), Page.DEVICE)).values()) {
				final PageMapKey k = page.getKey();
				if (k != null) {
					keys.put(k, page);
				}
			}
			this.byKey = ImmutableMap.copyOf(keys);
		}

		/**
		 * Returns a page by page key.
		 * @param key Page Key.
		 * @return The requested page or {@code null} if it no suitable page is found.
		 */
		Page getPage(PageKey key) {
			PageMapKey k = PageMapKey.of(key);
			if (logger.isTraceEnabled()) {
				log("Requested", k);
				logger.trace("Known pages [{}]", new Object[] {byKey.keySet()});
			}
			Page p = null;
			while (k != null) {
				p = byKey.get(k);
				if (k == PageMapKey.defaultPage()) {
					Loggers.page().info("Default page reached.");
					break;
				}
				if (p != null) {
					log("Found", k);
					break;
				}
				log("Not Found. Trying parent.", k);
				k = k.getParent(portal);
				log("Next try...", k);
			}
			return p;
		}

		PageContext getPage(PortalModel model, PortalRequestContext context, PathSegments path, LocalParams params,
			PageKey previous) {
			// If a custom route has been set, we try that
			if (previous != null) {
				return tryKey(model, context, path, params, previous);
			}
			// Try main page
			Step step = tryMain(model, context, path, params);
			log("Trying main page", path);
			if (step.isDone()) {
				return step.getContext();
			}
			// Try a special page.
			log("Trying special page", path);
			step = trySpecial(model, context, path, params);
			if (step.isDone()) {
				return step.getContext();
			}
			// Try a category page
			log("Trying category page", path);
			step = tryCategory(model, context, path, params);
			if (step.isDone()) {
				return step.getContext();
			}
			// Try a content page
			log("Trying content-related page", path);
			return tryContent(null, model, context, path, params);
		}

		private PageContext create(PortalModel model, PortalRequestContext context, PathSegments path,
			LocalParams params, PageKey key, Page page) {
			PageRequestContext pageContext = RequestContexts.firstRequestedPage(context, path, key, params);
			return new PageContext(model, pageContext, page);
		}

		private PageContext tryKey(PortalModel model, PortalRequestContext context, PathSegments path,
			LocalParams params, PageKey key) {
			final Page page = getPage(key);
			if (page == null) {
				return null;
			}
			return create(model, context, path, params, key, page);
		}

		private Step tryMain(PortalModel model, PortalRequestContext context, PathSegments path, LocalParams params) {
			if (path.isEmpty()) {
				return done(tryKey(model, context, path, params, PageKey.main()));
			}
			return TRY_OTHER;
		}

		private Step trySpecial(PortalModel model, PortalRequestContext context, PathSegments path, LocalParams params) {
			final String name = path.join();
			final PageMapKey pageKey = PageMapKey.special(name);
			final Page page = byKey.get(pageKey);
			if (page == null) {
				return TRY_OTHER;
			}
			return done(create(model, context, PathSegments.of(), params, PageKey.special(name), page));
		}

		private Step tryCategory(PortalModel model, PortalRequestContext context, PathSegments path, LocalParams params) {
			final Category root = portal.getCategories().getRoot();
			if (root == null) {
				return TRY_OTHER;
			}
			final Category next = categoryRouting.get(root, context.getLocale(), path.head());
			if (next == null) {
				return TRY_OTHER;
			}
			return done(followCategory(next, model, context, path.consume(), params));
		}

		private PageContext followCategory(Category category, PortalModel model, PortalRequestContext context,
			PathSegments path, LocalParams params) {
			final NavigationKey nk = NavigationKey.category(category);
			if (path.isEmpty()) {
				return tryKey(model, context, path, params, PageKey.navigation(nk));
			}
			final Category next = categoryRouting.get(category, context.getLocale(), path.head());
			if (next == null) {
				return tryContent(nk, model, context, path, params);
			}
			return followCategory(next, model, context, path.consume(), params);
		}

		private PageContext tryContent(NavigationKey nk, PortalModel model, PortalRequestContext context,
			PathSegments path, LocalParams params) {
			final String head = path.head();
			final ContentType contentType;
			if (path.size() == 1) {
				log("Trying content type page", path);
				// Content type page
				contentType = navigation.get(context.getLocale(), head);
				log(contentType);
				if (contentType != null) {
					return tryKey(model, context, path.consume(), params, PageKey.contentType(nk, contentType));
				}
			} else {
				// Content page
				log("Trying content page", path);
				contentType = content.get(context.getLocale(), head);
				log(contentType);
				if (contentType != null) {
					final String contentId = path.get(1);
					final ContentKey ck = ContentKey.of(contentType, contentId);
					return tryKey(model, context, path.consume(2), params, PageKey.content(nk, ck));
				}
			}
			return null;
		}
	}

	/*
	 * Continuation classes and methods.
	 */

	private static abstract class Step {
		abstract boolean isDone();

		abstract PageContext getContext();
	}

	private static Step done(final PageContext context) {
		return new Step() {
			@Override
			PageContext getContext() {
				return context;
			}

			@Override
			boolean isDone() {
				return true;
			}
		};
	}

	private static final Step TRY_OTHER = new Step() {
		@Override
		PageContext getContext() {
			throw new IllegalStateException();
		}

		@Override
		boolean isDone() {
			return false;
		}
	};
}
