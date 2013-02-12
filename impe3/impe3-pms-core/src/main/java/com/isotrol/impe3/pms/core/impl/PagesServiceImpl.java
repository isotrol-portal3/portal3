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

package com.isotrol.impe3.pms.core.impl;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.any;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.LayoutResponse;
import com.isotrol.impe3.core.PageMapKey;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.engine.DefaultOfflineEngine;
import com.isotrol.impe3.pbuf.BaseProtos.ProvisionPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.CipPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.LayoutPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PagePB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PagesPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PagePB.PageClassPB;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.page.CategoryPageDTO;
import com.isotrol.impe3.pms.api.page.CategoryPagesDTO;
import com.isotrol.impe3.pms.api.page.ColumnDTO;
import com.isotrol.impe3.pms.api.page.ComponentInPageDTO;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.ComponentKey;
import com.isotrol.impe3.pms.api.page.ContentPageDTO;
import com.isotrol.impe3.pms.api.page.ContentPagesDTO;
import com.isotrol.impe3.pms.api.page.DefaultPagesDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.LayoutItemDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageDTO;
import com.isotrol.impe3.pms.api.page.PageDeviceDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PagesService;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.api.page.StylesheetDTO;
import com.isotrol.impe3.pms.core.ConfigurationManager;
import com.isotrol.impe3.pms.core.ExportJobManager;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.PageManager;
import com.isotrol.impe3.pms.core.PortalManager;
import com.isotrol.impe3.pms.core.engine.EngineModelLoader;
import com.isotrol.impe3.pms.core.obj.CIPObject;
import com.isotrol.impe3.pms.core.obj.CIPsObject;
import com.isotrol.impe3.pms.core.obj.CategoriesObject;
import com.isotrol.impe3.pms.core.obj.CategoryObject;
import com.isotrol.impe3.pms.core.obj.ContentTypeObject;
import com.isotrol.impe3.pms.core.obj.ContextDevicePages;
import com.isotrol.impe3.pms.core.obj.DevicePagesObject;
import com.isotrol.impe3.pms.core.obj.LayoutObject;
import com.isotrol.impe3.pms.core.obj.PageObject;
import com.isotrol.impe3.pms.core.obj.PaletteItem;
import com.isotrol.impe3.pms.core.support.InUseProviders;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.core.support.PageMapKeySupport;
import com.isotrol.impe3.pms.model.CIPValue;
import com.isotrol.impe3.pms.model.ComponentEntity;
import com.isotrol.impe3.pms.model.ConfigurationEntity;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.ExportJobType;
import com.isotrol.impe3.pms.model.LayoutValue;
import com.isotrol.impe3.pms.model.PageDfn;
import com.isotrol.impe3.pms.model.PageEntity;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEntity;


/**
 * Implementation of PagesService.
 * @author Andres Rodriguez.
 */
@Service("pagesService")
public final class PagesServiceImpl extends AbstractContextService implements PagesService {
	private static final Function<LayoutResponse.Style, StylesheetDTO> STYLE_DTO = new Function<LayoutResponse.Style, StylesheetDTO>() {
		public StylesheetDTO apply(LayoutResponse.Style from) {
			StylesheetDTO dto = new StylesheetDTO();
			dto.setUrl(from.getUri().toASCIIString());
			dto.setMedia(from.getMedia());
			return dto;
		}
	};

	private PortalManager portalManager;
	private ConfigurationManager configurationManager;
	/** Page manager. */
	@Autowired
	private PageManager pageManager;
	@Autowired
	private EngineModelLoader engineLoader;
	/** Export job manager. */
	@Autowired
	private ExportJobManager exportJobManager;
	/** File manager. */
	@Autowired
	private FileManager fileManager;

	/** Default constructor. */
	public PagesServiceImpl() {
	}

	@Autowired
	public void setPortalManager(PortalManager portalManager) {
		this.portalManager = portalManager;
	}

	@Autowired
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.PAGE;
	}

	/**
	 * Loads the device pages level context.
	 * @param loc Portal pages locator.
	 * @return The context.
	 */
	private ContextDevicePages loadContextDP(PortalPagesLoc loc) throws PMSException {
		return loadContextGlobal().toPortal(loc.getPortalId()).toDevice(loc.getDeviceId());
	}

	private static boolean space(boolean had, boolean has) {
		if (has && had) {
			throw new IllegalStateException();
		}
		return has;
	}

	private static boolean checkSpaces(Iterable<ComponentInPageDTO> dtos) {
		boolean has = false;
		if (dtos != null) {
			for (ComponentInPageDTO dto : dtos) {
				if (dto == null) {
					throw new IllegalArgumentException();
				}
				has = space(has, dto.isSpace());
				final List<ComponentInPageDTO> children = dto.getChildren();
				if (children != null && !children.isEmpty()) {
					if (dto.isSpace()) {
						throw new IllegalArgumentException();
					}
					has = space(has, checkSpaces(children));
				}
			}
		}
		return has;
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PAGE, portal=PortalAuthority.PAGE)
	public void delete(PageLoc loc) throws PMSException {
		final PageSelector selector = new PageSelector(loc);
		final PageObject page = selector.ctx.getPages().load(loc.getId());
		NotFoundProviders.PAGE.checkCondition(!selector.ctx.isInheritedPage(page.getId()), loc.getId());
		final PageDfn dfn = selector.load(loc.getId());
		final PageEntity entity = dfn.getPage();
		final UUID pageId = entity.getId();
		final boolean used = getDao().isUsed(getEnvironmentId(), pageId, PageEntity.USED);
		InUseProviders.PAGE.checkUsed(used, pageId);
		// Delete the dfn
		entity.getPortal().getCurrent().getPages().remove(dfn);
		getDao().delete(dfn);
		// If it is the last definition the entity is deleted
		if (!getDao().hasRows(PageEntity.DFNS, getEnvironmentId(), pageId)) {
			getDao().delete(entity);
		}
		selector.touch();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getDefaultDeviceId(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public String getDefaultDeviceId(String portalId) throws PMSException {
		return loadPortal(portalId).getDefaultDevice().getId().toString();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getPageDevices(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public List<PageDeviceDTO> getPageDevices(String portalId) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getPageDeviceDTOs();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getDefaultPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public DefaultPagesDTO getDefaultPages(final PortalPagesLoc loc) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final DefaultPagesDTO dto = new DefaultPagesDTO();
		// Main page
		dto.setMainPage(ctx.getLoc(PageMapKey.main()));
		// Default page
		dto.setDefaultPage(ctx.getLoc(PageMapKey.defaultPage()));
		// Default page
		dto.setDefaultErrorPage(ctx.getLoc(PageMapKey.error()));
		return dto;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getTemplatePages(com.isotrol.impe3.pms.api.page.PortalPagesLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public List<Inherited<PageSelDTO>> getTemplatePages(PortalPagesLoc loc) throws PMSException {
		return loadContextDP(loc).getTemplates(null);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getSpecialPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public List<Inherited<PageSelDTO>> getSpecialPages(PortalPagesLoc loc) throws PMSException {
		return loadContextDP(loc).getSpecialPages();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getErrorPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public List<Inherited<PageSelDTO>> getErrorPages(PortalPagesLoc loc) throws PMSException {
		return loadContextDP(loc).getErrorPages();
	}

	private CategoryPagesDTO getCategoryPagesDTO(PortalPagesLoc loc, PageMapKey defaultKey, final KeyBuilder only,
		final KeyBuilder children) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final CategoryPagesDTO dto = new CategoryPagesDTO();
		dto.setDefaultPage(defaultKey != null ? ctx.getLoc(defaultKey) : null);
		final CategoriesObject categories = ctx.getCategories();
		if (categories.isEmpty()) {
			// TODO: should not happen.
			dto.setChildren(Lists.<CategoryPageDTO> newArrayListWithCapacity(0));
			return dto;
		}
		final Function<CategoryObject, CategoryPageDTO> toCP = new Function<CategoryObject, CategoryPageDTO>() {
			public CategoryPageDTO apply(CategoryObject from) {
				try {
					CategoryPageDTO dto = new CategoryPageDTO();
					final UUID id = from.getId();
					dto.setCategory(from.toSelDTO());
					dto.setOnlyThis(ctx.getLoc(only.apply(id)));
					dto.setThisAndChildren(ctx.getLoc(children.apply(id)));
					dto.setChildren(Mappers.list(categories.getChildren(id), this));
					return dto;
				} catch (PMSException e) {
					// TODO
					return null;
				}
			}
		};
		final UUID rootId = categories.getRoot().getId();
		dto.setChildren(Mappers.list(categories.getChildren(rootId), toCP));
		dto.calc();
		return dto;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getCategoryPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public CategoryPagesDTO getCategoryPages(PortalPagesLoc loc) throws PMSException {
		return getCategoryPagesDTO(loc, PageMapKey.category(), KeyBuilders.CATEGORY_ONLY, KeyBuilders.CATEGORY_CHILDREN);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getContentPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public ContentPagesDTO getContentPages(PortalPagesLoc loc) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final ContentPagesDTO dto = new ContentPagesDTO();
		dto.setDefaultPage(ctx.getLoc(PageMapKey.content()));
		final Function<ContentTypeObject, ContentPageDTO> toCP = new Function<ContentTypeObject, ContentPageDTO>() {
			public ContentPageDTO apply(ContentTypeObject from) {
				try {
					ContentPageDTO dto = new ContentPageDTO();
					final UUID id = from.getId();
					dto.setContentType(from.toSelDTO());
					dto.setPage(ctx.getLoc(PageMapKey.content(id)));
					dto.setWithCategory(any(ctx.getPages().getPageMapKeys(), PageMapKey.contentWithCategory(id)));
					return dto;
				} catch (PMSException e) {
					// TODO
					return null;
				}
			}
		};
		dto.setChildren(Mappers.list(ctx.getContentTypes().values(), toCP));
		return dto;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getCategoryContentPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc,
	 * java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public CategoryPagesDTO getCategoryContentPages(PortalPagesLoc loc, String contentTypeId) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final UUID id = ctx.getContentTypes().load(contentTypeId).getId();
		final KeyBuilder only = new KeyBuilder() {
			public PageMapKey apply(UUID from) {
				return PageMapKey.content(PageMapKey.category(from, false), id);
			}
		};
		final KeyBuilder children = new KeyBuilder() {
			public PageMapKey apply(UUID from) {
				return PageMapKey.content(PageMapKey.category(from, true), id);
			}
		};
		return getCategoryPagesDTO(loc, PageMapKey.contentType(), only, children);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getContentTypePages(com.isotrol.impe3.pms.api.page.PortalPagesLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public ContentPagesDTO getContentTypePages(PortalPagesLoc loc) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final ContentPagesDTO dto = new ContentPagesDTO();
		dto.setDefaultPage(ctx.getLoc(PageMapKey.contentType()));
		final Function<ContentTypeObject, ContentPageDTO> toCP = new Function<ContentTypeObject, ContentPageDTO>() {
			public ContentPageDTO apply(ContentTypeObject from) {
				try {
					ContentPageDTO dto = new ContentPageDTO();
					final UUID id = from.getId();
					dto.setContentType(from.toSelDTO());
					dto.setPage(ctx.getLoc(PageMapKey.contentType(id)));
					dto.setWithCategory(any(ctx.getPages().getPageMapKeys(), PageMapKey.contentTypeWithCategory(id)));
					return dto;
				} catch (PMSException e) {
					// TODO
					return null;
				}
			}
		};
		dto.setChildren(Mappers.list(ctx.getContentTypes().values(), toCP));
		return dto;
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public CategoryPagesDTO getCategoryContentTypePages(PortalPagesLoc loc, String contentTypeId) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final UUID id = ctx.getContentTypes().load(contentTypeId).getId();
		final KeyBuilder only = new KeyBuilder() {
			public PageMapKey apply(UUID from) {
				return PageMapKey.contentType(PageMapKey.category(from, false), id);
			}
		};
		final KeyBuilder children = new KeyBuilder() {
			public PageMapKey apply(UUID from) {
				return PageMapKey.contentType(PageMapKey.category(from, true), id);
			}
		};
		return getCategoryPagesDTO(loc, PageMapKey.contentType(), only, children);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#newTemplate(com.isotrol.impe3.pms.api.page.PortalPagesLoc,
	 * com.isotrol.impe3.pms.api.page.PageClass)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PAGE, portal=PortalAuthority.PAGE)
	public PageTemplateDTO newTemplate(PortalPagesLoc loc, PageClass pageClass) throws PMSException {
		return loadContextDP(loc).getNewPageTemplate(pageClass);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getPalette(com.isotrol.impe3.pms.api.page.PortalPagesLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PAGE, portal=PortalAuthority.PAGE)
	public List<PaletteDTO> getPalette(PortalPagesLoc loc) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		return ctx.getPaletteDTO();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#get(com.isotrol.impe3.pms.api.page.PageLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public PageTemplateDTO get(PageLoc loc) throws PMSException {
		return loadContextDP(loc).getPageTemplate(loc.getId());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#newComponentTemplate(com.isotrol.impe3.pms.api.page.PortalPagesLoc,
	 * com.isotrol.impe3.pms.api.page.ComponentKey)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PAGE, portal=PortalAuthority.PAGE)
	public ComponentInPageTemplateDTO newComponentTemplate(PortalPagesLoc loc, ComponentKey componentKey)
		throws PMSException {
		return loadContextDP(loc).newComponentTemplate(componentKey);
	}

	private PortalEntity touch(ContextDevicePages ctx) throws PMSException {
		PortalEntity e = findById(PortalEntity.class, ctx.getPortalId());
		portalManager.touchPages(ctx.getPortals(), e);
		return e;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#save(com.isotrol.impe3.pms.api.page.PageDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PAGE, portal=PortalAuthority.PAGE)
	public PageTemplateDTO save(PageDTO dto) throws PMSException {
		final String pageId = save(loadContextDP(dto), dto);
		final PageLoc loc = new PageLoc(dto);
		loc.setId(pageId);
		return get(loc);
	}

	private String save(ContextDevicePages ctx, PageDTO dto) throws PMSException {
		final PageClass pageClass = checkNotNull(dto.getPageClass());
		final List<ComponentInPageDTO> children = checkNotNull(dto.getComponents());
		checkArgument(all(children, notNull()));
		checkSpaces(children);
		final boolean isInherited;
		final boolean isNew;
		if (dto.getId() == null) {
			isNew = true;
			isInherited = false;
		} else {
			isInherited = ctx.isInheritedPage(dto.getId());
			isNew = isInherited;
		}
		PortalEntity portal = touch(ctx);
		final PageDfn dfn;
		final String pageId;
		final UUID pageUUID;
		if (isNew) {
			final PageEntity entity = new PageEntity();
			entity.setPortal(portal);
			entity.setDevice(findById(DeviceEntity.class, ctx.getPages().getDevice()));
			entity.setPageClass(pageClass);
			saveNewEntity(entity);
			dfn = new PageDfn();
			dfn.setPage(entity);
			if (isInherited) {
				PageObject original = ctx.getPages().load(dto.getId());
				PageDfn originalDfn = findById(PageDfn.class, original.getDefinitionId());
				dfn.getLayout().putAll(originalDfn.getLayout());
			}
			saveNewEntity(dfn);
			portal.getCurrent().getPages().add(dfn);
			pageUUID = entity.getId();
			pageId = pageUUID.toString().toLowerCase();
		} else {
			pageId = dto.getId();
			pageUUID = NotFoundProviders.PAGE.toUUID(pageId);
			dfn = new DfnMap(portal.getCurrent()).get(pageUUID);
		}
		// Begin template selection
		String id = dto.getTemplate();
		final PageEntity entity;
		if (id == null) {
			entity = null;
		} else {
			final UUID tid = NotFoundProviders.PAGE.toUUID(id);
			final Set<PageObject> allowed = ctx.getAllowedTemplates(pageUUID);
			NotFoundProviders.PAGE.checkCondition(allowed.contains(ctx.getPages().get(tid)), id);
			entity = findById(PageEntity.class, tid);
		}
		dfn.setTemplate(entity);
		// End template selection
		id = dto.getCategory();
		dfn.setCategory(id != null ? loadCategory(id) : null);
		id = dto.getContentType();
		dfn.setContentType(id != null ? loadContentType(id) : null);
		dfn.setUmbrella(dto.isUmbrella());
		dfn.setName(dto.getName());
		dfn.setDescription(dto.getDescription());
		dfn.setUpdated(loadUser());
		// Components
		new CIPLoader(ctx, dfn).load(dto.getComponents());
		// Done
		sync();
		return pageId;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getLayout(com.isotrol.impe3.pms.api.page.PageLoc)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public LayoutDTO getLayout(PageLoc loc) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final PageObject page = ctx.getPages().load(loc.getId());
		NotFoundProviders.PAGE.checkCondition(ctx.getDevice().isLayout(), loc.getId());
		final LayoutDTO dto = new LayoutDTO();
		dto.setPortalId(loc.getPortalId());
		dto.setDeviceId(loc.getDeviceId());
		dto.setId(loc.getId());
		final List<StylesheetDTO> stylesheets = Lists.newArrayList();
		dto.setStylesheets(stylesheets);
		final List<StylesheetDTO> ie6stylesheets = Lists.newArrayList();
		dto.setIE6Stylesheets(ie6stylesheets);
		final List<StylesheetDTO> ie7stylesheets = Lists.newArrayList();
		dto.setIE7Stylesheets(ie7stylesheets);
		final List<StylesheetDTO> ie8stylesheets = Lists.newArrayList();
		dto.setIE8Stylesheets(ie8stylesheets);
		dto.setWidth(ctx.getDevice().getWidth());
		final LayoutResponse response;
		final CIPsObject cips = ctx.getFullCIPs(page.getId());
		if (cips.isEmpty()) {
			response = null;
		} else {
			EngineModel model = engineLoader.getOffline(getEnvironment().getName());
			final DefaultOfflineEngine engine = new DefaultOfflineEngine(model);
			response = engine.getLayout(ctx.getPortalId(), page.getId(), ctx.getLocale());
			stylesheets.addAll(Lists.transform(response.getStyles(), STYLE_DTO));
			ie6stylesheets.addAll(Lists.transform(response.getIE6Styles(), STYLE_DTO));
			ie7stylesheets.addAll(Lists.transform(response.getIE7Styles(), STYLE_DTO));
			ie8stylesheets.addAll(Lists.transform(response.getIE8Styles(), STYLE_DTO));
		}
		// Items
		final List<LayoutItemDTO> items = getLayoutItems(ctx, page, cips, response);
		dto.setItems(items);
		// Frames
		dto.setFrames(ctx.getLayout(page.getId()));
		return dto;
	}

	private List<LayoutItemDTO> getLayoutItems(ContextDevicePages ctx, PageObject page, CIPsObject cips,
		LayoutResponse response) throws PMSException {
		final List<LayoutItemDTO> items = Lists.newArrayList();
		final CIPsObject tplCips;
		final LayoutObject tplLayout;
		final PageObject template = ctx.getPages().getTemplate(page);
		if (template == null) {
			tplCips = null;
			tplLayout = null;
		} else {
			tplCips = ctx.getFullCIPs(template.getId());
			tplLayout = ctx.getFullLayout(template.getId());
		}
		for (CIPObject cip : cips.values()) {
			if (tplCips != null && tplCips.containsKey(cip.getId())) {
				// Is from a template. Check if it is used.
				if (!tplLayout.isUsed(cip.getId())) {
					continue;
				}
			}
			final PaletteItem p = ctx.getPaletteItem(cip.getKey());
			if (p.isVisual()) {
				final LayoutItemDTO item = new LayoutItemDTO();
				final String id = cip.getId().toString().toLowerCase();
				item.setId(id);
				item.setName(cip.getName());
				if (!p.isSpace()) {
					item.setDescription(p.getDescription(ctx));
				}
				item.setSpace(p.isSpace());
				if (!p.isSpace()) {
					item.setMarkup(response.getMarkups().get(cip.getId()));
				}
				items.add(item);
			}
		}
		return items;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#setLayout(com.isotrol.impe3.pms.api.page.PageLoc,
	 * java.util.List)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PAGE, portal=PortalAuthority.PAGE)
	public LayoutDTO setLayout(PageLoc loc, List<FrameDTO> frames) throws PMSException {
		if (frames != null) {
			loc = setLayoutInternal(loc, frames);
		}
		return getLayout(loc);
	}

	private PageLoc setLayoutInternal(PageLoc loc, List<FrameDTO> frames) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final PageObject original = ctx.getPages().load(loc.getId());
		NotFoundProviders.PAGE.checkCondition(ctx.getDevice().isLayout(), loc.getId());
		if (ctx.isInheritedPage(original.getId())) {
			PortalEntity portal = touch(ctx);
			PageDfn originalDfn = findById(PageDfn.class, original.getDefinitionId());
			PageEntity originalEntity = originalDfn.getPage();
			final PageEntity entity = new PageEntity();
			entity.setPortal(portal);
			entity.setDevice(originalEntity.getDevice());
			entity.setPageClass(originalEntity.getPageClass());
			saveNewEntity(entity);
			PageDfn dfn = pageManager.copyDfn(originalDfn, entity);
			portal.getCurrent().getPages().add(dfn);
			UUID pageId = entity.getId();
			sync();
			loc = new PageLoc(loc, pageId.toString());
		}
		final PageSelector selector = new PageSelector(loc);
		selector.touch();
		final PageDfn dfn = selector.load(loc.getId());
		dfn.setUpdated(loadUser());
		new LayoutLoader(selector, dfn).save(frames);
		sync();
		return loc;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.page.PagesService#isCIPinLayout(com.isotrol.impe3.pms.api.page.PageLoc,
	 * java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PAGE, portal=PortalAuthority.PAGE)
	public boolean isCIPinLayout(PageLoc loc, String cipId) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final PageObject page = ctx.getPages().load(loc.getId());
		if (cipId == null) {
			return false;
		}
		return ctx.getFullLayout(page.getId()).isUsed(NotFoundProviders.COMPONENT.toUUID(cipId));
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public String exportAll(PortalPagesLoc loc) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		return exportJobManager.create(ExportJobType.PORTAL_PAGE_ALL, ctx.getPortalId(), ctx.getDevice().getId(),
			null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PAGE, portal=PortalAuthority.PAGE)
	public void importPages(PortalPagesLoc loc, String fileId, boolean overwrite) throws PMSException {
		loadContextDP(loc);
		try {
			PagesPB pages = fileManager.parseImportFile(fileId, PagesPB.newBuilder(), true).build();
			for (PagePB page : pages.getPagesList()) {
				importPage(loc, page, overwrite);
			}
		}
		finally {
			purge();
		}
	}

	private void importPage(PortalPagesLoc loc, PagePB page, boolean overwrite) throws PMSException {
		final ContextDevicePages ctx = loadContextDP(loc);
		final DevicePagesObject pages = ctx.getPages();
		final PageObject existing;
		if (page.getPageClass() == PageClassPB.TEMPLATE) {
			NotFoundProviders.PAGE.checkCondition(page.hasName(), (String) null);
			existing = pages.getTemplateByName(page.getName());
		} else {
			existing = pages.getByKey(PageMapKeySupport.of(page));
		}
		if (!overwrite && existing != null && !pages.isInheritedPage(existing)) {
			return;
		}
		final PageDTO dto;
		if (existing != null) {
			PageLoc ploc = new PageLoc(loc, existing.getStringId());
			dto = get(ploc).toPageDTO();
		} else {
			dto = new PageDTO();
			dto.setPortalId(loc.getPortalId());
			dto.setDeviceId(loc.getDeviceId());
			dto.setPageClass(PageObject.toPageClass(page.getPageClass()));
			dto.setUmbrella(page.getUmbrella());
			if (page.hasCategoryId()) {
				dto.setCategory(page.getCategoryId());
			}
			if (page.hasContentTypeId()) {
				dto.setContentType(page.getContentTypeId());
			}
		}
		if (page.hasTemplateName()) {
			String tplName = page.getTemplateName();
			PageObject tpl = NotFoundProviders.PAGE.checkNotNull(pages.getTemplateByName(tplName), tplName);
			dto.setTemplate(tpl.getStringId());
		} else {
			dto.setTemplate(null);
		}
		if (page.hasName()) {
			dto.setName(page.getName());
		}
		if (page.hasDescription()) {
			dto.setDescription(page.getDescription());
		}
		// Components
		final Map<UUID, UUID> cipIds = Maps.newHashMap();
		dto.setComponents(importCIPs(page.getCipsList(), cipIds));
		// Save the page
		final String pageId = save(ctx, dto);
		final PageLoc pageLoc = new PageLoc(loc, pageId);
		// Layout
		setLayoutInternal(pageLoc, importFrames(page.getLayoutList(), cipIds));
	}

	private List<ComponentInPageDTO> importCIPs(List<CipPB> cips, Map<UUID, UUID> map) throws PMSException {
		final List<ComponentInPageDTO> list = Lists.newArrayListWithCapacity(cips.size());
		for (CipPB cip : cips) {
			UUID oldId = NotFoundProviders.COMPONENT.toUUID(cip.getCipId());
			UUID newId = newUUID();
			map.put(oldId, newId);
			final ComponentInPageDTO dto = new ComponentInPageDTO();
			dto.setId(newId.toString());
			dto.setName(cip.getName());
			if (cip.hasComponent()) {
				final ComponentKey key = new ComponentKey();
				final ProvisionPB pb = cip.getComponent();
				key.setBean(pb.getBean());
				key.setInstanceId(pb.getInstanceId());
				dto.setComponent(key);
			}
			if (cip.hasConfiguration()) {
				dto.setConfiguration(configurationManager.pb2dto(cip.getConfiguration()));
			}
			dto.setChildren(importCIPs(cip.getChildrenList(), map));
			list.add(dto);
		}
		return list;
	}

	private List<FrameDTO> importFrames(List<LayoutPB> los, Map<UUID, UUID> map) throws PMSException {
		if (los == null || los.isEmpty()) {
			return ImmutableList.of();
		}
		final Map<Integer, LayoutValue> lvs = Maps.newHashMap();
		importFramesRec(los, map, lvs, null, 0);
		return LayoutObject.of(lvs.entrySet()).toDTO();
	}

	private int importFramesRec(List<LayoutPB> los, Map<UUID, UUID> map, Map<Integer, LayoutValue> lvs, Integer parent,
		int first) throws PMSException {
		int key = first;
		for (int i = 0; i < los.size(); i++) {
			LayoutPB lo = los.get(i);
			LayoutValue v = new LayoutValue();
			if (lo.hasCipId()) {
				final UUID origId = NotFoundProviders.COMPONENT.toUUID(lo.getCipId());
				final UUID newCipId = NotFoundProviders.COMPONENT.checkNotNull(map.get(origId), origId);
				v.setComponent(newCipId);
			}
			if (lo.hasName()) {
				v.setName(lo.getName());
			}
			if (lo.hasWidth()) {
				v.setWidth(lo.getWidth());
			}
			v.setPosition(i);
			v.setParent(parent);
			lvs.put(key, v);
			key = importFramesRec(lo.getChildrenList(), map, lvs, key, key + 1);
		}
		return key;
	}

	// ------------------------------------------------------------------------

	private class Pages {
		final PortalPagesLoc loc;
		final PortalEntity portal;

		Pages(PortalPagesLoc loc) throws PMSException {
			this.loc = loc;
			this.portal = loadPortal(loc.getPortalId());
		}
	}

	private final class PageSelector extends Pages implements Function<PageDfn, PageSelDTO> {
		private final ContextDevicePages ctx;

		PageSelector(PortalPagesLoc loc) throws PMSException {
			super(loc);
			ctx = loadContextDP(loc);
		}

		PageDfn load(String id) throws PMSException {
			final PageObject page = ctx.getPages().load(id);
			return NotFoundProviders.PAGE.checkNotNull(findById(PageDfn.class, page.getDefinitionId()), id);
		}

		void touch() throws PMSException {
			portalManager.touchPages(ctx.getPortals(), portal);
		}

		public PageSelDTO apply(PageDfn from) {
			if (from == null) {
				return null;
			}
			final PageSelDTO dto = new PageSelDTO();
			dto.setPortalId(loc.getPortalId());
			dto.setDeviceId(loc.getDeviceId());
			dto.setId(from.getPage().getId().toString().toLowerCase());
			dto.setPageClass(from.getPage().getPageClass());
			dto.setName(from.getName());
			dto.setDescription(from.getDescription());
			// dto.setUmbrella(from.isUmbrella());
			// dto.setCategory(cg.map2sel(from.getCategory()));
			// dto.setContentType(ct.map2sel(from.getContentType()));
			return dto;
		}

	}

	// ------------------------------------------------------------------------
	// LOADERS
	// ------------------------------------------------------------------------

	private class CIPLoader {
		private final ContextDevicePages ctx;
		private final PageDfn dfn;
		private final Map<UUID, CIPValue> map;
		private final CIPsObject cips;
		final Set<UUID> updated;

		CIPLoader(ContextDevicePages ctx, PageDfn dfn) {
			this.ctx = ctx;
			this.dfn = dfn;
			this.map = dfn.getComponents();
			this.updated = Sets.newHashSet(map.keySet());
			this.cips = CIPsObject.of(dfn);
		}

		void load(List<ComponentInPageDTO> dtos) throws PMSException {
			int pos = 0;
			for (ComponentInPageDTO dto : dtos) {
				update(dto, null, pos++);
			}
			// Deleted
			if (!updated.isEmpty()) {
				Iterable<UUID> components = Iterables.filter(
					Iterables.transform(dfn.getLayout().values(), LayoutValue.GET_COMPONENT), Predicates.notNull());
				for (UUID id : updated) {
					if (Iterables.any(components, Predicates.equalTo(id))) {
						throw new IllegalStateException(); // TODO
					}
					map.remove(id);
				}
			}
		}

		void update(ComponentInPageDTO dto, UUID parent, int pos) throws PMSException {
			final UUID id = NotFoundProviders.COMPONENT.toUUID(dto.getId());
			CIPValue v = map.get(id);
			final PaletteItem item = ctx.getPaletteItemOrSpace(dto.getComponent());
			if (v == null) {
				// New component
				// TODO: validate unique id.
				v = new CIPValue();
				if (!item.isSpace()) {
					final ComponentEntity ce = NotFoundProviders.COMPONENT.checkNotNull(
						findById(ComponentEntity.class, item.getInstanceId()), item.getInstanceId());
					v.setComponent(ce);
					v.setBean(item.getBean());
				}
				map.put(id, v);
			} else {
				checkArgument(cips.get(id).isSameItem(item)); // TODO: exception
				updated.remove(id);
			}
			v.setName(dto.getName());
			v.setParent(parent);
			v.setPosition(pos);
			final ConfigurationDefinition<?> config = item.getConfigurationDefinition();
			final ConfigurationEntity actual = v.getConfiguration();
			if (config == null) {
				if (actual != null) {
					configurationManager.delete(v.getConfiguration());
					v.setConfiguration(null);
				}
			} else {
				// TODO: mandatory config
				if (actual == null) {
					v.setConfiguration(configurationManager.create(config, dto.getConfiguration()));
				} else {
					configurationManager.update(config, actual, dto.getConfiguration());
				}
			}
			final List<ComponentInPageDTO> children = dto.getChildren();
			if (children != null && !children.isEmpty()) {
				int position = 0;
				for (ComponentInPageDTO child : children) {
					update(child, id, position++);
				}
			}
		}

	}

	private class LayoutLoader {
		private final PageSelector selector;
		private final Map<UUID, CIPValue> components;
		private final Map<Integer, LayoutValue> layout;
		private final boolean isTemplate;
		int n = 0;

		LayoutLoader(PageSelector selector, PageDfn dfn) {
			this.selector = selector;
			this.components = dfn.getComponents();
			this.layout = dfn.getLayout();
			this.isTemplate = dfn.getPage().getPageClass() == PageClass.TEMPLATE;
		}

		void save(List<FrameDTO> frames) throws PMSException {
			layout.clear();
			int i = 0;
			for (FrameDTO dto : frames) {
				update(dto, null, i++);
			}
		}

		void update(FrameDTO dto, Integer parent, int order) throws PMSException {
			Preconditions.checkArgument(!dto.isFill());
			final Integer id = n++;
			final LayoutValue v = new LayoutValue();
			if (dto.isComponent()) {
				// TODO: errors
				final UUID cipId = NotFoundProviders.COMPONENT.toUUID(dto.getComponent());
				final CIPValue cv = Preconditions.checkNotNull(components.get(cipId));
				final PaletteItem item = selector.ctx.getPaletteItem(cv);
				checkArgument(item.isVisual());
				checkArgument(!item.isSpace() || isTemplate);
				v.setComponent(cipId);
			} else if (dto.isColumns()) {
				final List<ColumnDTO> columns = dto.getColumns();
				if (columns != null && !columns.isEmpty()) {
					int o = 0;
					for (ColumnDTO column : columns) {
						o = update(column, id, o);
					}
				} else {
					return;
				}
			} else {
				return;
			}
			v.setParent(parent);
			v.setPosition(order);
			v.setName(dto.getName());
			layout.put(id, v);
		}

		int update(ColumnDTO dto, Integer parent, int order) throws PMSException {
			final List<FrameDTO> children = dto.getFrames();
			if (children == null || children.isEmpty()) {
				return order;
			}
			final LayoutValue v = new LayoutValue();
			v.setWidth(dto.getWidth());
			v.setName(dto.getName());
			v.setParent(parent);
			v.setPosition(order);
			final Integer id = n++;
			layout.put(id, v);
			int i = 0;
			for (FrameDTO frame : children) {
				update(frame, id, i++);
			}
			return order + 1;
		}
	}

	/*
	 * START KEY BUILDERS
	 */

	/** Key builder interface. */
	private interface KeyBuilder extends Function<UUID, PageMapKey> {
	}

	private enum KeyBuilders implements KeyBuilder {
		CATEGORY_ONLY {
			public PageMapKey apply(UUID from) {
				return PageMapKey.category(from, false);
			}
		},
		CATEGORY_CHILDREN {
			public PageMapKey apply(UUID from) {
				return PageMapKey.category(from, true);
			}
		}
	}

	/*
	 * END KEY BUILDERS
	 */

	/*
	 * Definition to entity mapping.
	 */

	private static final Function<PageDfn, UUID> DFN2ID = new Function<PageDfn, UUID>() {
		public UUID apply(PageDfn from) {
			return from.getPage().getId();
		}
	};

	private static final class DfnMap extends ForwardingMap<UUID, PageDfn> {
		private final ImmutableMap<UUID, PageDfn> map;

		DfnMap(PortalDfn dfn) {
			map = Maps.uniqueIndex(dfn.getPages(), DFN2ID);
		}

		@Override
		protected Map<UUID, PageDfn> delegate() {
			return map;
		}
	}

}
