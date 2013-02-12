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

package com.isotrol.impe3.pms.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.isotrol.impe3.api.DeviceCapabilitiesProvider;
import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.LocaleRouter;
import com.isotrol.impe3.api.PageResolver;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.RoutingBase;
import com.isotrol.impe3.api.RoutingDomain;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.Pages;
import com.isotrol.impe3.core.PortalCacheModel;
import com.isotrol.impe3.core.engine.EngineURIGenerator;
import com.isotrol.impe3.core.engine.PortalRouter;
import com.isotrol.impe3.core.impl.BaseModelImpl;
import com.isotrol.impe3.core.modules.StartedModule;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.PageLoader;
import com.isotrol.impe3.pms.core.PortalLoader;
import com.isotrol.impe3.pms.core.obj.PortalObject;
import com.isotrol.impe3.pms.core.obj.PortalPagesObject;
import com.isotrol.impe3.pms.core.obj.PortalsObject;
import com.isotrol.impe3.pms.core.obj.StartedComponents;
import com.isotrol.impe3.pms.model.ConnectorEntity;
import com.isotrol.impe3.pms.model.NameValue;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEntity;


/**
 * Portal loading support.
 * @author Andres Rodriguez.
 */
final class PortalLoadingSupport {
	private final PortalsObject portals;
	private final PortalObject portalObject;
	private final PortalLoader portalLoader;
	private final EngineModel engineModel;
	private final PortalEntity entity;
	private final BasePMSModel baseModel;
	private final Portal portal;
	/** Portal cache model. */
	private final PortalCacheModel cacheModel;
	private StartedComponents components;
	private URIGenerator urig;
	private Pages pages = null;
	/** Device capabilities provider. */
	private final DeviceCapabilitiesProvider deviceCaps;
	/** Device router. */
	private final DeviceRouter deviceRouter;
	/** Locale router. */
	private final LocaleRouter localeRouter;
	/** Page resolver. */
	private final PageResolver pageResolver;
	/** Node repositories. */
	private final Function<UUID, NodeRepository> nodeRepositories;
	private final PathSegments path;

	PortalLoadingSupport(BaseEngineModel engineModel, PortalsObject portals, PortalDfn dfn, PortalLoader portalLoader,
		PageLoader pageLoader) {
		this.portals = portals;
		this.portalLoader = portalLoader;
		this.engineModel = engineModel;
		this.entity = dfn.getEntity();
		this.portalObject = portals.get(entity.getId());
		// Device capabilities provider
		this.deviceCaps = getConnector(DeviceCapabilitiesProvider.class, dfn.getDeviceCapsConnector(),
			dfn.getDeviceCapsBean(), null);
		// Device resolver
		this.deviceRouter = getConnector(DeviceRouter.class, dfn.getDeviceConnector(), dfn.getDeviceBean(),
			engineModel.getDeviceRouter());
		// Locale resolver
		this.localeRouter = getConnector(LocaleRouter.class, dfn.getLocaleConnector(), dfn.getLocaleBean(),
			engineModel.getLocaleRouter());
		// Base model version
		final UUID version;
		if (EngineMode.ONLINE == engineModel.getMode()) {
			version = engineModel.getModelInfo().getVersion();
		} else {
			int v = dfn.getEntity().getOfflineVersion();
			version = new UUID(v, v);
		}
		// Base model
		final BaseModel level2 = new BaseModelImpl(entity.getId(), version, engineModel, deviceRouter, localeRouter);
		this.baseModel = new BasePMSModel(level2, engineModel.getConnectors());
		// Default node repositories
		final NodeRepository nr = getConnector(NodeRepository.class, dfn.getNrConnector(), dfn.getNrBean(), null);
		this.nodeRepositories = new Function<UUID, NodeRepository>() {
			public NodeRepository apply(UUID from) {
				return nr;
			}
		};
		// Page resolver
		final ConnectorEntity routerEntity = dfn.getRouterConnector();
		final String routerBean = dfn.getRouterBean();
		PageResolver pr = null;
		try {
			if (routerEntity != null && routerBean != null) {
				StartedModule<?> module = baseModel.getConnectors().apply(routerEntity.getId());
				pr = (PageResolver) module.getProvision(routerBean);
			}
		} catch (RuntimeException e) {
			Loggers.pms().warn(String.format("Unable to get page resolver for portal [%s]", entity.getId()), e);
		}
		this.pageResolver = pr;
		final NameValue nv = dfn.getName();
		this.path = (nv != null) ? PathSegments.of(nv.getPath(), false) : PathSegments.of();
		// Portal object
		portal = portals.get(baseModel.getId()).start(baseModel, portals);
		// Cache model
		cacheModel = this.portalObject.getCacheModel(portals);
	}

	private <T> T getConnector(Class<T> type, ConnectorEntity entity, String bean, T defaultCnn) {
		try {
			if (entity != null && bean != null) {
				StartedModule<?> module = engineModel.getConnector(entity.getId());
				return type.cast(module.getProvision(bean));
			}
		} catch (RuntimeException e) {
			Loggers.pms().warn(String.format("Unable to get [%s] for portal [%s]", type.getName(), entity.getId()), e);
		}
		return defaultCnn;
	}

	public final int getVersion() {
		return entity.getOfflineVersion();
	}

	PathSegments getPath() {
		return path;
	}

	EngineModel getEngineModel() {
		return engineModel;
	}

	BasePMSModel getBaseModel() {
		return baseModel;
	}

	Portal getPortal() {
		return portal;
	}

	PortalCacheModel getCacheModel() {
		return cacheModel;
	}

	DeviceCapabilitiesProvider getDeviceCaps() {
		return deviceCaps;
	}

	DeviceRouter getPortalDeviceRouter() {
		return deviceRouter;
	}

	LocaleRouter getPortalLocaleRouter() {
		return localeRouter;
	}

	PageResolver getPageResolver() {
		return pageResolver;
	}

	Function<UUID, NodeRepository> getNodeRepositories() {
		return nodeRepositories;
	}

	URIGenerator loadURIGenerator() {
		if (urig == null) {
			final RoutingBase base;
			final PathSegments basePath;
			final RoutingDomain rd = checkNotNull(engineModel.getRoutingDomains().get(portalObject.getDomainId()),
				"Unable to find routing domain");
			if (baseModel.getMode() == EngineMode.ONLINE) {
				base = rd.getOnline();
				basePath = this.path;
			} else {
				base = rd.getOffline();
				basePath = PathSegments.segment(baseModel.getId().toString().toLowerCase(), true);
			}
			final Supplier<PathSegments> supplier = new Supplier<PathSegments>() {
				public PathSegments get() {
					return basePath;
				}
			};
			final PortalRouter router = new PMSPortalRouter(getPortal(), deviceRouter, localeRouter, pageResolver,
				supplier);
			urig = new EngineURIGenerator(getPortal(), base.getBase(), base.getAbsoluteBase(), router);
		}
		return urig;
	}

	StartedComponents loadComponents() throws PMSException {
		checkState(this.urig != null);
		if (this.components == null) {
			this.components = portalLoader.loadComponents(portals, portalObject).start(baseModel,
				baseModel.getConnectors(), urig);
		}
		return components;
	}

	Pages loadPages() throws PMSException {
		if (pages != null) {
			return pages;
		}
		checkState(components != null);
		PortalPagesObject portalPages = portalLoader.loadPages(portals, portalObject);
		pages = portalPages.start(portal, baseModel, components);
		return pages;
	}
}
