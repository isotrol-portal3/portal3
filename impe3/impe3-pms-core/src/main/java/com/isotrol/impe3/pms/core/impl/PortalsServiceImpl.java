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
import static com.google.common.collect.Maps.filterValues;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.ROUTING_DOMAIN;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.DeviceCapabilitiesProvider;
import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.LocaleRouter;
import com.isotrol.impe3.api.PageResolver;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.config.PortalConfigurationDefinition;
import com.isotrol.impe3.core.engine.DefaultOfflineEngine;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.nr.api.FilterType;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.pbuf.BaseProtos.StringEntryPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.BasesPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PortalNamePB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PropertiesPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.SetPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.SetsPB;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ProvidedDTO;
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.api.portal.BasesDTO;
import com.isotrol.impe3.pms.api.portal.ContentTypeInPortalDTO;
import com.isotrol.impe3.pms.api.portal.DiPDTO;
import com.isotrol.impe3.pms.api.portal.IllegalPortalParentException;
import com.isotrol.impe3.pms.api.portal.PortalCacheDTO;
import com.isotrol.impe3.pms.api.portal.PortalCategoryMode;
import com.isotrol.impe3.pms.api.portal.PortalConfigurationSelDTO;
import com.isotrol.impe3.pms.api.portal.PortalDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalIATemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalParentDTO;
import com.isotrol.impe3.pms.api.portal.PortalTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalTreeDTO;
import com.isotrol.impe3.pms.api.portal.PortalURLsDTO;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.api.portal.PropertiesDTO;
import com.isotrol.impe3.pms.api.portal.SetFilterDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.core.DeviceManager;
import com.isotrol.impe3.pms.core.ExportJobManager;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.PortalConfigurationManager;
import com.isotrol.impe3.pms.core.PortalManager;
import com.isotrol.impe3.pms.core.engine.EngineModelLoader;
import com.isotrol.impe3.pms.core.obj.ConnectorObject;
import com.isotrol.impe3.pms.core.obj.ContentTypesObject;
import com.isotrol.impe3.pms.core.obj.Context2;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.ContextPortal;
import com.isotrol.impe3.pms.core.obj.DeviceObject;
import com.isotrol.impe3.pms.core.obj.MessageMappers;
import com.isotrol.impe3.pms.core.obj.PortalObject;
import com.isotrol.impe3.pms.core.obj.PortalsObject;
import com.isotrol.impe3.pms.core.support.InUseProviders;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.MoreLocales;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.ConfigurationEntity;
import com.isotrol.impe3.pms.model.ConnectorEntity;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.ExportJobType;
import com.isotrol.impe3.pms.model.PortalCacheValue;
import com.isotrol.impe3.pms.model.PortalConfigurationValue;
import com.isotrol.impe3.pms.model.PortalDeviceValue;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEdition;
import com.isotrol.impe3.pms.model.PortalEntity;
import com.isotrol.impe3.pms.model.RoutingDomainEntity;
import com.isotrol.impe3.pms.model.SetFilterValue;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 * Implementation of PortalsService.
 * @author Andres Rodriguez.
 */
@Service("portalsService")
public final class PortalsServiceImpl extends AbstractPortalService<PortalEntity, PortalDfn, PortalEdition> implements
	PortalsService {
	/** Portal manager. */
	private PortalManager portalManager;
	/** Engine model loader. */
	@Autowired
	private EngineModelLoader engineLoader;
	/** Export job manager. */
	@Autowired
	private ExportJobManager exportJobManager;
	/** File manager. */
	@Autowired
	private FileManager fileManager;
	/** Configuration manager */
	@Autowired
	private PortalConfigurationManager portalConfigurationManager;
	
	/** Default constructor. */
	public PortalsServiceImpl() {
	}

	@Autowired
	public void setPortalManager(PortalManager portalManager) {
		this.portalManager = portalManager;
	}

	/**
	 * @return the configurationManager
	 */
	public PortalConfigurationManager getPortalConfigurationManager() {
		return portalConfigurationManager;
	}

	/**
	 * @param configurationManager the configurationManager to set
	 */
	public void setPortalConfigurationManager(PortalConfigurationManager portalConfigurationManager) {
		this.portalConfigurationManager = portalConfigurationManager;
	}

	private PortalDfn getUpdateDfn(ContextPortal ctx) throws PMSException {
		final PortalEntity portal = load(ctx.getPortalId());
		return portalManager.touchOffline(portal);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#delete(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public PortalTreeDTO delete(String id) throws PMSException {
		// 1 - Find portal.
		final ContextPortal ctx = loadContextGlobal().toPortal(id);
		// 2 - Check not in use.
		InUseProviders.PORTAL.checkUsed(!ctx.getPortals().getChildrenKeys(ctx.getPortalId()).isEmpty(), id);
		// 3 - Delete
		loadPortal(ctx.getPortalId()).setDeleted(true);
		// 4 - Touch environment.
		getEnvironment().touchPortalVersion(loadUser());
		// Sync and return updated tree.
		sync();
		return getPortals();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getName(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public PortalNameDTO getName(String id) throws PMSException {
		return loadContextGlobal().toPortal(id).toPortalNameDTO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#create(com.isotrol.impe3.pms.api.portal.PortalNameDTO,
	 * java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET)
	public String create(PortalNameDTO dto, String parentId) throws PMSException {
		checkNotNull(dto);
		checkNotNull(MoreLocales.VALID.apply(dto.getDefaultLocale()));
		checkArgument(dto.getLocales() == null || Iterables.all(dto.getLocales().keySet(), MoreLocales.VALID));
		checkNotNull(dto.getName());
		checkNotNull(dto.getName().getDisplayName());
		final ContextGlobal global = loadContextGlobal();
		final PortalObject parent;
		final PortalEntity parentEntity;
		if (parentId != null) {
			parent = global.getPortals().load(parentId);
			parentEntity = load(parent.getId());
		} else {
			parent = null;
			parentEntity = null;
		}
		final PortalEntity entity = new PortalEntity();
		final DeviceEntity device = getDao().getDeviceByName(DeviceManager.DEFAULT);
		entity.setDefaultDevice(device);
		final PortalDfn dfn = new PortalDfn();
		dfn.setRoutingDomain(findById(RoutingDomainEntity.class, global.getDomains().loadDefault().getId()));
		getEnvironment().touchPortalVersion(loadUser());
		dfn.setName(Mappers.DTO2NAME.apply(dto.getName()));
		dfn.setDescription(dto.getDescription());
		dfn.setDefaultLocale(dto.getDefaultLocale());
		dfn.getLocales().clear();
		if (dto.getLocales() != null) {
			dfn.getLocales().addAll(dto.getLocales().keySet());
		}
		dfn.getL7DNames().clear();
		if (dto.getLocales() != null) {
			dfn.getL7DNames().putAll(filterValues(dto.getLocales(), notNull()));
		}
		dfn.setParent(parentEntity);
		// Devices
		dfn.setInheritedDevices(parent != null);
		final UUID id = saveNewEntity(entity, dfn);
		return id.toString();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#setName(com.isotrol.impe3.pms.api.portal.PortalNameDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public void setName(PortalNameDTO dto) throws PMSException {
		checkNotNull(dto);
		checkNotNull(MoreLocales.VALID.apply(dto.getDefaultLocale()));
		checkArgument(dto.getLocales() == null || Iterables.all(dto.getLocales().keySet(), MoreLocales.VALID));
		checkNotNull(dto.getName());
		checkNotNull(dto.getName().getDisplayName());
		final String id = dto.getId();
		final PortalEntity entity = load(id);
		final PortalDfn dfn = portalManager.touchOffline(entity);
		dfn.setName(Mappers.DTO2NAME.apply(dto.getName()));
		dfn.setDescription(dto.getDescription());
		dfn.setDefaultLocale(dto.getDefaultLocale());
		dfn.getLocales().clear();
		if (dto.getLocales() != null) {
			dfn.getLocales().addAll(dto.getLocales().keySet());
		}
		dfn.getL7DNames().clear();
		if (dto.getLocales() != null) {
			dfn.getL7DNames().putAll(filterValues(dto.getLocales(), notNull()));
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#get(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public PortalTemplateDTO get(String id) throws PMSException {
		return loadContextGlobal().toPortal(id).toPortalTemplateDTO();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getParent(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET)
	public PortalParentDTO getParent(String id) throws PMSException {
		final PortalsObject portals = loadContextGlobal().getPortals();
		final PortalObject portal = portals.load(id);
		return portals.getParent(portal);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getAvailableBases(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public List<BaseDTO> getAvailableBases(String portalId) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getAvailableBases();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getBases(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public BasesDTO getBases(String portalId) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(portalId);
		return ctx.getBases();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getAvailableProperties(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public List<PropertyDTO> getAvailableProperties(String portalId) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getAvailableProperties();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getProperties(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public PropertiesDTO getProperties(String portalId) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(portalId);
		return ctx.getProperties();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getPortals()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_TREE)
	public PortalTreeDTO getPortals() {
		return loadContextGlobal().getPortals().map2tree();
	}

	private void fill(Context2 ctx, PortalDfn dfn, PortalDTO dto) throws PMSException {
		dfn.setRoutable(dto.isRoutable());
		dfn.setTag(dto.getTag());
		final ProvidedDTO router = dto.getRouter();
		ConnectorObject cnn = ctx.checkProvided(PageResolver.class, router);
		if (cnn == null) {
			dfn.setRouterConnector(null);
			dfn.setRouterBean(null);
		} else {
			ConnectorEntity entity = loadConnectorEntity(cnn.getId());
			dfn.setRouterConnector(entity);
			dfn.setRouterBean(router.getBean());
		}
		final ProvidedDTO locale = dto.getLocale();
		cnn = ctx.checkProvided(LocaleRouter.class, locale);
		if (cnn == null) {
			dfn.setLocaleConnector(null);
			dfn.setLocaleBean(null);
		} else {
			ConnectorEntity entity = loadConnectorEntity(cnn.getId());
			dfn.setLocaleConnector(entity);
			dfn.setLocaleBean(locale.getBean());
		}
		final ProvidedDTO device = dto.getDevice();
		cnn = ctx.checkProvided(DeviceRouter.class, device);
		if (cnn == null) {
			dfn.setDeviceConnector(null);
			dfn.setDeviceBean(null);
		} else {
			ConnectorEntity entity = loadConnectorEntity(cnn.getId());
			dfn.setDeviceConnector(entity);
			dfn.setDeviceBean(device.getBean());
		}
		final ProvidedDTO deviceCaps = dto.getDeviceCaps();
		cnn = ctx.checkProvided(DeviceCapabilitiesProvider.class, deviceCaps);
		if (cnn == null) {
			dfn.setDeviceCapsConnector(null);
			dfn.setDeviceCapsBean(null);
		} else {
			ConnectorEntity entity = loadConnectorEntity(cnn.getId());
			dfn.setDeviceCapsConnector(entity);
			dfn.setDeviceCapsBean(deviceCaps.getBean());
		}
		final ProvidedDTO nr = dto.getNodeRepository();
		cnn = ctx.checkProvided(NodeRepository.class, nr);
		if (cnn == null) {
			dfn.setNrConnector(null);
			dfn.setNrBean(null);
		} else {
			ConnectorEntity entity = loadConnectorEntity(cnn.getId());
			dfn.setNrConnector(entity);
			dfn.setNrBean(nr.getBean());
		}
		dfn.setUncategorized(dto.getUncategorized());
		dfn.setDue(dto.getDue());
		dfn.setSessionCSRF(dto.getSessionCSRF());
		final String rdId = dto.getDomain();
		ROUTING_DOMAIN.checkNotNull(rdId, rdId);
		final RoutingDomainEntity rde = findById(RoutingDomainEntity.class, ROUTING_DOMAIN.toUUID(rdId));
		ROUTING_DOMAIN.checkNotNull(rde, rdId);
		dfn.setRoutingDomain(rde);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#save(com.isotrol.impe3.pms.api.portal.PortalDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public void save(PortalDTO dto) throws PMSException {
		checkNotNull(dto);
		checkNotNull(MoreLocales.VALID.apply(dto.getDefaultLocale()));
		checkArgument(dto.getLocales() == null || Iterables.all(dto.getLocales().keySet(), MoreLocales.VALID));
		final String id = dto.getId();
		final PortalEntity entity = load(id);
		final PortalDfn dfn = portalManager.touchOffline(entity);
		fill(loadContextGlobal(), dfn, dto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#setParent(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET)
	public void setParent(String id, String parentId) throws PMSException {
		final PortalsObject portals = loadContextGlobal().getPortals();
		final PortalObject portal = portals.load(id);
		final PortalObject parent = parentId != null ? portals.load(parentId) : null;
		if (Objects.equal(parent, portals.getParent(portal.getId()))) {
			// Nothing to do
			return;
		}
		// Validation: legal parent
		final UUID portalUUID = portal.getId();
		final UUID parentUUID = parent != null ? parent.getId() : null;
		if (parentUUID != null) {
			if (portalUUID.equals(parentUUID) || portals.getDescendantsKeys(portalUUID).contains(parentUUID)) {
				throw new IllegalPortalParentException(id, parentId);
			}
		}
		// Perform the update
		final PortalEntity entity = load(portalUUID);
		final PortalDfn dfn = portalManager.touchHierarchy(portals, entity);
		final PortalEntity parentEntity = parentUUID != null ? load(parentUUID) : null;
		dfn.setParent(parentEntity);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#setBases(java.lang.String, java.util.List)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_BASE, portal = PortalAuthority.BASE)
	public BasesDTO setBases(String portalId, List<BaseDTO> bases) throws PMSException {
		checkNotNull(bases);
		checkArgument(all(bases, notNull()));
		final PortalEntity portal = load(portalId);
		final PortalDfn dfn = portalManager.touchOffline(portal);
		final Map<String, String> map = dfn.getBases();
		map.clear();
		if (bases != null && !bases.isEmpty()) {
			for (BaseDTO base : bases) {
				map.put(checkNotNull(base.getKey()), checkNotNull(base.getUri()));
			}
		}
		sync();
		return getBases(portalId);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#setProperties(java.lang.String, java.util.List)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PROPERTY, portal = PortalAuthority.PROPERTY)
	public PropertiesDTO setProperties(String portalId, List<PropertyDTO> properties) throws PMSException {
		checkNotNull(properties);
		checkArgument(all(properties, notNull()));
		final PortalEntity portal = load(portalId);
		final PortalDfn dfn = portalManager.touchOffline(portal);
		final Map<String, String> map = dfn.getProperties();
		map.clear();
		if (properties != null && !properties.isEmpty()) {
			for (PropertyDTO prop : properties) {
				map.put(checkNotNull(prop.getName()), checkNotNull(prop.getValue()));
			}
		}
		sync();
		return getProperties(portalId);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getIA(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public PortalIATemplateDTO getIA(String id) throws PMSException {
		final PortalEntity portal = load(id);
		final PortalDfn dfn = portal.getCurrent();
		final PortalIATemplateDTO dto = new PortalIATemplateDTO();
		dto.setCategoryTree(loadCategories().map2tree());
		dto.setMode(PortalCategoryMode.ROOT);
		dto.setCategories(new ArrayList<CategorySelDTO>(0));
		dto.setInheritedContentTypes(new HashSet<ContentTypeSelDTO>());
		final ContentTypesObject ctSel = loadContentTypes();
		final Function<ContentTypeEntity, ContentTypeInPortalDTO> ctip = new Function<ContentTypeEntity, ContentTypeInPortalDTO>() {
			public ContentTypeInPortalDTO apply(ContentTypeEntity from) {
				final ContentTypeInPortalDTO dto = new ContentTypeInPortalDTO();
				dto.setIncluded(dfn.getContentTypes().contains(from));
				dto.setContentType(ctSel.map2sel(from));
				return dto;
			}
		};
		dto.setContentTypes(Mappers.list(getEnvironment().getOfflineContentTypes(), ctip));
		// TODO portal inheritance
		return dto;
	}

	private UriBuilder getOfflineURIBuilder(PortalEntity portal) throws PMSException {
		final UriBuilder b = UriBuilder.fromUri(portal.getCurrent().getRoutingDomain().getOfflineBase());
		b.path(portal.getId().toString());
		return b;
	}

	private UriBuilder getOnlineURIBuilder(PortalEntity portal) throws PMSException {
		final UriBuilder b = UriBuilder.fromUri(portal.getCurrent().getRoutingDomain().getOnlineBase());
		// TODO Change from current to edition dfn
		final String path = portal.getCurrent().getName().getPath();
		if (path != null && path.length() > 0) {
			b.path(path);
		}
		return b;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getURLs(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public PortalURLsDTO getURLs(String portalId) throws PMSException {
		final PortalURLsDTO dto = new PortalURLsDTO();
		final PortalEntity portal = load(portalId);
		final UriBuilder offline = getOfflineURIBuilder(portal);
		dto.setOffline(offline.build().toASCIIString());
		dto.setPMS(offline.queryParam(PortalManager.MASK_ERROR, "").build().toASCIIString());
		final UriBuilder online = getOnlineURIBuilder(portal);
		if (online != null) {
			dto.setOnline(online.build().toASCIIString());
		}
		return dto;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getOfflineURL(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public String getOfflineURL(String portalId) throws PMSException {
		final PortalEntity portal = load(portalId);
		final UriBuilder b = getOfflineURIBuilder(portal);
		return b.build().toASCIIString();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getOnlineURL(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public String getOnlineURL(String portalId) throws PMSException {
		final PortalEntity portal = load(portalId);
		final UriBuilder b = getOnlineURIBuilder(portal);
		return b.build().toASCIIString();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#isOfflineReady(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public boolean isOfflineReady(String portalId) throws PMSException {
		final PortalEntity portal = load(portalId);
		try {
			final EngineModel model = engineLoader.getOffline(getEnvironment().getName());
			final DefaultOfflineEngine engine = new DefaultOfflineEngine(model);
			final PathSegments path = PathSegments.of(false, portal.getId().toString().toLowerCase());
			final Response response = engine.process(path, new MockHttpHeaders(), RequestContexts.http()).getResponse();
			return response.getStatus() == 200;
		} catch (Exception e) {}
		return false;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getSetFilters(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public List<SetFilterDTO> getSetFilters(String portalId) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(portalId);
		return ctx.getSetFilters();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#putSetFilter(java.lang.String,
	 * com.isotrol.impe3.pms.api.portal.SetFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public List<SetFilterDTO> putSetFilter(String portalId, SetFilterDTO filter) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(portalId);
		checkNotNull(filter);
		String set = checkNotNull(filter.getName());
		SetFilterValue v = new SetFilterValue(checkNotNull(filter.getType()), filter.getDescription());
		Map<String, SetFilterValue> current = ctx.getPortal().getSetFilters();
		if (current.containsKey(set) && current.get(set).equals(v)) {
			return ctx.getSetFilters();
		}
		final PortalEntity portal = load(portalId);
		final PortalDfn dfn = portalManager.touchOffline(portal);
		dfn.getSetFilters().put(set, v);
		sync();
		return getSetFilters(portalId);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#removeSetFilter(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public List<SetFilterDTO> removeSetFilter(String portalId, String filter) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(portalId);
		if (filter == null || !ctx.getPortal().getSetFilters().containsKey(filter)) {
			return ctx.getSetFilters();
		}
		final PortalEntity portal = load(portalId);
		final PortalDfn dfn = portalManager.touchOffline(portal);
		dfn.getSetFilters().remove(filter);
		sync();
		return getSetFilters(portalId);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#clearSetFilters(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public List<SetFilterDTO> clearSetFilters(String portalId) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(portalId);
		if (ctx.getPortal().getSetFilters().isEmpty()) {
			return ctx.getSetFilters();
		}
		final PortalEntity portal = load(portalId);
		final PortalDfn dfn = portalManager.touchOffline(portal);
		dfn.getSetFilters().clear();
		sync();
		return getSetFilters(portalId);
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public String exportName(String id) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(id);
		return exportJobManager.create(ExportJobType.PORTAL_NAME, ctx.getPortalId(), null, null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public void importName(String id, String fileId) throws PMSException {
		// Fetch portal
		loadContextGlobal().toPortal(id);
		// Load file
		final PortalNamePB msg = fileManager.parseImportFile(fileId, PortalNamePB.newBuilder(), true).build();
		// Import data
		try {
			checkNotNull(MoreLocales.VALID.apply(msg.getDefaultLocale()));
			checkArgument(Iterables.all(MessageMappers.seKeys(msg.getLocalesList()), MoreLocales.VALID));
			final PortalEntity entity = load(id);
			final PortalDfn dfn = portalManager.touchOffline(entity);
			dfn.setName(Mappers.PB2NAME.apply(msg.getName()));
			dfn.setDescription(msg.getDescription());
			dfn.setDefaultLocale(msg.getDefaultLocale());
			final Map<String, String> locales = PortalObject.fromNullName(MessageMappers.seMap(msg.getLocalesList()));
			dfn.getLocales().clear();
			dfn.getLocales().addAll(locales.keySet());
			dfn.getL7DNames().clear();
			dfn.getL7DNames().putAll(filterValues(locales, notNull()));
		}
		finally {
			purge();
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public String exportConfig(String id) throws PMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public void importConfig(String id, String fileId) throws PMSException {
		try {}
		finally {
			purge();
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public String exportBases(String id) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(id);
		return exportJobManager.create(ExportJobType.PORTAL_BASE, ctx.getPortalId(), null, null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public void importBases(String id, String fileId, boolean remove) throws PMSException {
		// Fetch portal
		loadContextGlobal().toPortal(id);
		// Load file
		final BasesPB msg = fileManager.parseImportFile(fileId, BasesPB.newBuilder(), true).build();
		try {
			final PortalEntity portal = load(id);
			final PortalDfn dfn = portalManager.touchOffline(portal);
			final Map<String, String> map = dfn.getBases();
			if (remove) {
				map.clear();
			}
			for (StringEntryPB e : msg.getBasesList()) {
				map.put(e.getKey(), e.getValue());
			}
		}
		finally {
			purge();
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public String exportProperties(String id) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(id);
		return exportJobManager.create(ExportJobType.PORTAL_PROP, ctx.getPortalId(), null, null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_PROPERTY, portal = PortalAuthority.PROPERTY)
	public void importProperties(String id, String fileId, boolean remove) throws PMSException {
		// Fetch portal
		loadContextGlobal().toPortal(id);
		// Load file
		final PropertiesPB msg = fileManager.parseImportFile(fileId, PropertiesPB.newBuilder(), true).build();
		try {
			final PortalEntity portal = load(id);
			final PortalDfn dfn = portalManager.touchOffline(portal);
			final Map<String, String> map = dfn.getProperties();
			if (remove) {
				map.clear();
			}
			for (StringEntryPB e : msg.getPropertiesList()) {
				map.put(e.getKey(), e.getValue());
			}
		}
		finally {
			purge();
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public String exportSetFilters(String id) throws PMSException {
		final ContextPortal ctx = loadContextGlobal().toPortal(id);
		return exportJobManager.create(ExportJobType.PORTAL_SET, ctx.getPortalId(), null, null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public void importSetFilters(String id, String fileId, boolean remove) throws PMSException {
		// Fetch portal
		loadContextGlobal().toPortal(id);
		// Load file
		final SetsPB msg = fileManager.parseImportFile(fileId, SetsPB.newBuilder(), true).build();
		try {
			final PortalEntity portal = load(id);
			final PortalDfn dfn = portalManager.touchOffline(portal);
			final Map<String, SetFilterValue> map = dfn.getSetFilters();
			if (remove) {
				map.clear();
			}
			for (SetPB s : msg.getSetsList()) {
				final FilterType type;
				switch (s.getType()) {
					case OPTIONAL:
						type = FilterType.OPTIONAL;
						break;
					case REQUIRED:
						type = FilterType.REQUIRED;
						break;
					case FORBIDDEN:
						type = FilterType.FORBIDDEN;
						break;
					default:
						throw new IllegalStateException();
				}
				SetFilterValue v = new SetFilterValue(type, s.hasDescription() ? s.getDescription() : null);
				map.put(s.getName(), v);
			}
		}
		finally {
			purge();
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getPortalDevices(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public PortalDevicesTemplateDTO getPortalDevices(String id) throws PMSException {
		return loadContextGlobal().toPortal(id).getPortalDevicesTemplateDTO();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#setPortalDevices(com.isotrol.impe3.pms.api.portal.PortalDevicesDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public void setPortalDevices(PortalDevicesDTO devices) throws PMSException {
		NotFoundProviders.PORTAL.checkNotNull(devices, (String) null);
		NotFoundProviders.PORTAL.checkNotNull(devices.getId(), (String) null);
		final ContextPortal ctx = loadContextGlobal().toPortal(devices.getId());
		final PortalObject parent = ctx.getParent();
		NotFoundProviders.PORTAL.checkCondition(parent != null || !devices.isInherited(), devices.getId());
		if (devices.isInherited()) {
			if (ctx.isInheritedDevices()) {
				// Nothing to do
				return;
			}
			final PortalDfn dfn = getUpdateDfn(ctx);
			dfn.setInheritedDevices(true);
			dfn.getDevices().clear();
		} else {
			NotFoundProviders.PORTAL.checkNotNull(devices.getDefaultId(), (String) null);
			final Map<String, DiPDTO> deviceNames = devices.getUses();
			if (deviceNames == null || !deviceNames.containsKey(devices.getDefaultId())) {
				throw new PMSException();
			}
			final DeviceEntity defaultDevice = loadDevice(ctx, devices.getDefaultId());
			final Map<DeviceEntity, PortalDeviceValue> map = Maps.newHashMap();
			final Set<String> names = Sets.newHashSet();
			for (Entry<String, DiPDTO> e : devices.getUses().entrySet()) {
				final DiPDTO dip = e.getValue();
				final String name = dip.getName();
				if (!names.add(name)) {
					throw new PMSException();
				}
				final DeviceEntity de = loadDevice(ctx, e.getKey());
				if (map.containsKey(de)) {
					throw new PMSException();
				}
				map.put(de, new PortalDeviceValue(name, defaultDevice.equals(de), dip.getUse()));
			}
			final PortalDfn dfn = getUpdateDfn(ctx);
			dfn.setInheritedDevices(false);
			dfn.getDevices().clear();
			dfn.getDevices().putAll(map);
		}
	}

	private DeviceEntity loadDevice(ContextPortal ctx, String deviceId) throws PMSException {
		DeviceObject d = ctx.getDevices().load(deviceId);
		return NotFoundProviders.DEVICE.checkNotNull(findById(DeviceEntity.class, d.getId()), deviceId);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getPortalCache(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public PortalCacheDTO getPortalCache(String id) throws PMSException {
		return loadContextGlobal().toPortal(id).getPortalCacheDTO();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#setPortalCache(com.isotrol.impe3.pms.api.portal.PortalCacheDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal = PortalAuthority.SET)
	public void setPortalCache(PortalCacheDTO cache) throws PMSException {
		NotFoundProviders.PORTAL.checkNotNull(cache, (String) null);
		NotFoundProviders.PORTAL.checkNotNull(cache.getId(), (String) null);
		final ContextPortal ctx = loadContextGlobal().toPortal(cache.getId());
		final PortalObject parent = ctx.getParent();
		if (parent != null && ctx.isInheritedCache() && cache.isInherited()) {
			return; // Nothing to do
		}
		final PortalDfn dfn = getUpdateDfn(ctx);
		dfn.setCache(new PortalCacheValue(cache.isInherited(), cache.isActive(), cache.isPublicCache(), cache
			.getModification(), cache.getExpiration(), cache.getETagMode()));
	}

	/**
	 * Mock for JAX-RS' HttpHeaders.
	 * @author Andres Rodriguez
	 */
	class MockHttpHeaders implements HttpHeaders {
		public MockHttpHeaders() {
			// TODO Auto-generated constructor stub
		}

		public List<Locale> getAcceptableLanguages() {
			return ImmutableList.of();
		}

		public List<MediaType> getAcceptableMediaTypes() {
			return ImmutableList.of();
		}

		public Map<String, Cookie> getCookies() {
			return ImmutableMap.of();
		}

		public Locale getLanguage() {
			return new Locale("es");
		}

		public MediaType getMediaType() {
			return MediaType.TEXT_HTML_TYPE;
		}

		public List<String> getRequestHeader(String name) {
			return ImmutableList.of();
		}

		public MultivaluedMap<String, String> getRequestHeaders() {
			return new MultivaluedMapImpl();
		}
	}

	
	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getAvailableProperties(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public List<PortalConfigurationSelDTO> getPortalConfigurations(String portalId) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getPortalConfigurations();
	}
	
	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getAvailableProperties(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public ConfigurationTemplateDTO getPortalConfiguration(String portalId, String beanName) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getPortalConfiguration(beanName);
	}
	
	/**
	 * Save portal configuration.
	 * @param portalId Portal id.
	 * @param beanName Bean name.
	 * @param config Config to save.
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal=PortalAuthority.SET)
	public ConfigurationTemplateDTO savePortalConfiguration(String portalId, String beanName,
		List<ConfigurationItemDTO> config) throws PMSException {
		final ContextPortal context = loadContextGlobal().toPortal(portalId);
		overrideConfiguration(context, beanName, config);
		return getPortalConfiguration(portalId, beanName);
	}

	private void overrideConfiguration(ContextPortal context, String beanName, List<ConfigurationItemDTO> config)
		throws PMSException {
		final PortalConfigurationDefinition<?> cdef = context.getPortalConfigurationsDef().get(beanName);
//		NotFoundProviders.COMPONENT.checkNotNull(c.overridesConfiguration(), beanName);
		// Update it
		final PortalEntity entity = loadPortal(context.getPortalId());
		final PortalDfn portalDfn = portalManager.touchOffline(entity);
		portalManager.touchComponents(context.getPortals(), entity);
		PortalConfigurationValue pcv = portalDfn.getPortalConfiguration().get(beanName);
		if (pcv == null) {
			pcv = new PortalConfigurationValue();
			portalDfn.getPortalConfiguration().put(beanName, pcv);
		}
		ConfigurationEntity ce = pcv.getPortalConfiguration();
		if (ce == null) {
			ce = portalConfigurationManager.create(cdef, config);
		} else {
			ce = portalConfigurationManager.update(cdef, ce, config);
		}
		pcv.setPortalConfiguration(ce);
	}
	
	/**
	 * Clear portal configuration.
	 * @param portalId Portal id.
	 * @param beanName Bean name.
	 * @return ConfigurationTemplateDTO.
	 * @throws PMSException.
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_SET, portal=PortalAuthority.SET)
	public ConfigurationTemplateDTO clearConfiguration(String portalId, String beanName) throws PMSException {
		final ContextPortal context = loadContextGlobal().toPortal(portalId);
		
		final PortalEntity entity = loadPortal(context.getPortalId());
		final PortalDfn portalDfn = portalManager.touchOffline(entity);
		portalManager.touchComponents(context.getPortals(), entity);
		
		if (portalDfn.getPortalConfiguration() != null && portalDfn.getPortalConfiguration().get(beanName) != null) {
			portalConfigurationManager.delete(portalDfn.getPortalConfiguration().get(beanName).getPortalConfiguration());
		}
		sync();
		return getPortalConfiguration(portalId, beanName);
	}
	
	/**
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getAvailableProperties(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal = PortalAuthority.GET)
	public ConfigurationTemplateDTO getInheritedPortalConfiguration(String portalId, String beanName) throws PMSException {
		UUID parentUuid = loadContextGlobal().toPortal(portalId).getParentPortalId(UUID.fromString(portalId));
		
		return loadContextGlobal().toPortal(portalId).getPortalConfiguration(beanName);
	}
}
