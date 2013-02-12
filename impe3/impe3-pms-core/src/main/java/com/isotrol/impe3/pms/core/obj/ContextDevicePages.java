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


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.PAGE;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.isotrol.impe3.core.PageMapKey;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PagesPB;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.ComponentKey;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;


/**
 * Portal device pages level context.
 * @author Andres Rodriguez
 */
public class ContextDevicePages extends ContextPortal {
	/** Device pages. */
	private final DevicePagesObject pages;

	/**
	 * Constructor
	 * @param context Portal context.
	 * @param pages Device pages.
	 */
	ContextDevicePages(ContextPortal context, DevicePagesObject pages) {
		super(context);
		this.pages = pages;
	}

	public DevicePagesObject getPages() {
		return pages;
	}

	public DeviceObject getDevice() throws PMSException {
		final UUID deviceId = pages.getDevice();
		return getDevices().load(deviceId);
	}

	public Inherited<PageLoc> getLoc(PageMapKey key) throws PMSException {
		final PageObject p = pages.getByKey(key);
		if (p != null) {
			return p.toLoc(getPortalId());
		}
		return null;
	}

	private Collection<PageObject> special() throws PMSException {
		return pages.byClass(PageClass.SPECIAL).values();
	}

	private Collection<PageObject> error() throws PMSException {
		return pages.getErrorPages().values();
	}

	private Iterable<Inherited<PageSelDTO>> toIS(Iterable<PageObject> pages) throws PMSException {
		return transform(pages, PageObject.map2sel(getPortalId()));
	}

	// ******************** BEGIN CONTEXT METHODS

	/**
	 * Returns the set of templates allowed to be used by a page. If the page is a template itself, it is not included
	 * in the returned collection.
	 * @param pageId Page Id.
	 * @return The set of allowed templates. If the argument is {@code null} or it is not found in the portal all
	 * templates are returned.
	 */
	public Set<PageObject> getAllowedTemplates(UUID pageId) throws PMSException {
		final Set<PageObject> set = Sets.newHashSet(pages.templates().values());
		set.removeAll(getForbiddenTemplates(pageId));
		return set;
	}

	/**
	 * Returns the set of templates that can't be selected for a page. If the page is a template itself, it is included
	 * in the returned collection.
	 * @param pageId Page Id.
	 * @return The set of used templates. If the argument is {@code null} or it is not found or it is not a template in
	 * the portal an empty set is returned.
	 */
	private Set<PageObject> getForbiddenTemplates(UUID pageId) throws PMSException {
		if (pageId == null || !pages.containsKey(pageId)) {
			return ImmutableSet.of();
		}
		final PageObject page = pages.get(pageId);
		if (PageClass.TEMPLATE != page.getPageClass()) {
			return ImmutableSet.of();
		}
		final Set<PageObject> set = newHashSet();
		getForbiddenTemplates(set, page);
		return set;
	}

	private void getForbiddenTemplates(Set<PageObject> set, PageObject page) throws PMSException {
		if (set.add(page)) {
			for (PageObject other : pages.templates().values()) {
				if (page.equals(pages.getTemplate(other))) {
					getForbiddenTemplates(set, other);
				}
			}
		}
	}

	/**
	 * Returns all the available template pages in the context.
	 * @param pageId Page which forbidden templates will be filtered out.
	 * @return The list of available template pages in the context, allowed for the specified page.
	 * @throws PMSException
	 */
	public List<Inherited<PageSelDTO>> getTemplates(UUID pageId) throws PMSException {
		return newArrayList(toIS(getAllowedTemplates(pageId)));
	}

	public List<Inherited<PageSelDTO>> getSpecialPages() throws PMSException {
		return newArrayList(toIS(special()));
	}

	public List<Inherited<PageSelDTO>> getErrorPages() throws PMSException {
		return newArrayList(toIS(error()));
	}

	final PageTemplateDTO newTemplate(UUID pageId) throws PMSException {
		final PageTemplateDTO dto = new PageTemplateDTO();
		dto.setPortalId(getPortalId().toString());
		dto.setDeviceId(pages.getDevice().toString());
		// Selectable content types and categories
		dto.setContentTypes(getContentTypes().map2sel());
		dto.setCategories(getCategories().map2tree());
		// Selectable templates
		dto.setTemplates(getTemplates(pageId));
		return dto;
	}

	public PageTemplateDTO getNewPageTemplate(PageClass pageClass) throws PMSException {
		checkNotNull(pageClass);
		final PageTemplateDTO dto = newTemplate(null);
		dto.setPageClass(pageClass);
		final List<ComponentInPageTemplateDTO> components = Lists.newArrayList();
		if (PageClass.TEMPLATE == pageClass) {
			components.add(PaletteItem.space().newCIPTemplate(this));
		}
		dto.setComponents(components);
		return dto;
	}

	public PageTemplateDTO getPageTemplate(String pageId) throws PMSException {
		return pages.load(pageId).toDTO(this);
	}

	public PaletteItem getPaletteItem(ComponentKey componentKey) throws PMSException {
		final PaletteKey k = PaletteKey.fromComponentKey(componentKey);
		final PaletteItem p = getComponents().getPaletteItem(k);
		return p;
	}

	public PaletteItem getPaletteItemOrSpace(ComponentKey componentKey) throws PMSException {
		if (componentKey == null) {
			return PaletteItem.space();
		}
		return getPaletteItem(componentKey);
	}

	public ComponentInPageTemplateDTO newComponentTemplate(ComponentKey componentKey) throws PMSException {
		final PaletteItem p = getPaletteItem(componentKey);
		return p.newCIPTemplate(this);
	}

	public boolean isOverridenPage(UUID pageId) throws PMSException {
		if (pages.containsKey(pageId)) {
			return false;
		}
		PortalObject parent = getParentPortal(getPortalId());
		while (parent != null) {
			if (getGlobal().toPortal(parent.getId()).toDevice(pages.getDevice()).pages.containsKey(pageId)) {
				return true;
			}
			parent = getParentPortal(parent.getId());
		}
		NotFoundProviders.PAGE.checkNotNull(parent, pageId);
		return false;
	}

	public boolean isOverridenPage(String pageId) throws PMSException {
		return isOverridenPage(NotFoundProviders.PAGE.toUUID(pageId));
	}

	public boolean isInheritedPage(UUID pageId) throws PMSException {
		PageObject page = pages.load(pageId);
		return page.isInherited(getPortalId());
	}

	public boolean isInheritedPage(String pageId) throws PMSException {
		return isInheritedPage(PAGE.toUUID(pageId));
	}

	public List<FrameDTO> getLayout(UUID pageId) throws PMSException {
		return pages.load(pageId).getLayout(pages);
	}

	public CIPsObject getFullCIPs(UUID pageId) throws PMSException {
		return pages.load(pageId).getFullCIPs(pages);
	}

	public LayoutObject getFullLayout(UUID pageId) throws PMSException {
		return pages.load(pageId).getFullLayout(pages);
	}

	public final PagesPB exportPages(FileManager fileManager, PageClass pageClass) throws PMSException {
		return pages.export(fileManager, pageClass);
	}

	public final PagesPB exportPages(FileManager fileManager) throws PMSException {
		return pages.export(fileManager);
	}

	// ******************** END CONTEXT METHODS

}
