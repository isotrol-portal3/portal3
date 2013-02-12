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


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.LocaleRouter;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.ImpeIAModel;
import com.isotrol.impe3.core.ImpeModel;
import com.isotrol.impe3.core.impl.BaseModelImpl;
import com.isotrol.impe3.core.impl.ImpeIAModelImpl;
import com.isotrol.impe3.core.impl.ImpeModelImpl;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.CategoryManager;
import com.isotrol.impe3.pms.core.ConnectorManager;
import com.isotrol.impe3.pms.core.ContentTypeManager;
import com.isotrol.impe3.pms.core.DeviceManager;
import com.isotrol.impe3.pms.core.EnvironmentManager;
import com.isotrol.impe3.pms.core.RoutingDomainManager;
import com.isotrol.impe3.pms.core.obj.CategoriesObject;
import com.isotrol.impe3.pms.core.obj.ConnectorsObject;
import com.isotrol.impe3.pms.core.obj.ContentTypesObject;
import com.isotrol.impe3.pms.core.obj.StartedConnectors;
import com.isotrol.impe3.pms.model.EnvironmentEntity;


/**
 * Engine model loader service.
 * @author Andres Rodriguez.
 */
@Service
public class EngineModelLoaderImpl implements EngineModelLoader {
	private final EnvironmentManager environmentManager;
	private final FileLoader fileLoader;
	private final PortalModelLoader portalLoader;
	private final DeviceManager deviceManager;
	/** Device router. */
	private final DeviceRouter deviceRouter;
	/** Locale router. */
	private final LocaleRouter localeRouter;
	/** Content type manager. */
	@Autowired
	private ContentTypeManager contentTypeManager;
	/** Category manager. */
	@Autowired
	private CategoryManager categoryManager;
	/** Connector manager. */
	@Autowired
	private ConnectorManager connectorManager;
	/** Routing domain manager. */
	@Autowired
	private RoutingDomainManager routingDomainManager;
	/** Current offline model. */
	private OfflineEngineModel offlineModel;
	/** Current online model. */
	private OnlineEngineModel onlineModel;

	@Autowired
	public EngineModelLoaderImpl(final EnvironmentManager environmentManager, final FileLoader fileLoader,
		final PortalModelLoader portalLoader, final DeviceManager deviceManager, final DeviceRouter deviceRouter,
		final LocaleRouter localeRouter) {
		this.environmentManager = environmentManager;
		this.fileLoader = fileLoader;
		this.portalLoader = portalLoader;
		this.deviceManager = deviceManager;
		this.deviceRouter = deviceRouter;
		this.localeRouter = localeRouter;
	}

	private BaseEngineModel create(UUID id, UUID version, EngineMode mode) {
		final ImpeModel level0 = new ImpeModelImpl(id, version, mode, fileLoader, deviceManager.load(id).getDevices());
		final ContentTypesObject contentTypesObject;
		final CategoriesObject categoriesObject;
		if (mode == EngineMode.OFFLINE) {
			contentTypesObject = contentTypeManager.loadOffline(id);
			categoriesObject = categoryManager.loadOffline(id);
		} else {
			contentTypesObject = contentTypeManager.loadOnline(id);
			categoriesObject = categoryManager.loadOnline(id);
		}
		final ContentTypes contentTypes = contentTypesObject.map2api();
		final Categories categories = categoriesObject.map2api();
		final ImpeIAModel level1 = new ImpeIAModelImpl(level0, contentTypes, categories);
		final BaseModel level2 = new BaseModelImpl(level1, deviceRouter, localeRouter);
		// Let's start the connectors
		final ConnectorsObject cnns;
		if (mode == EngineMode.OFFLINE) {
			cnns = connectorManager.loadOffline(id);
		} else {
			cnns = connectorManager.loadOnline(id);
		}
		final StartedConnectors started = cnns.start(level1);
		final BasePMSModel level3 = new BasePMSModel(level2, started);
		return new BaseEngineModel(level3, routingDomainManager.loadOffline(id).start());
	}

	private UUID version2UUID(int version) {
		return new UUID(version, version);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.engine.EngineModelLoader#getOffline(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public EngineModel getOffline(String envName) {
		final EnvironmentEntity e = environmentManager.getEnvironment(envName);
		if (e == null) {
			return null;
		}
		final OfflineEngineModel model;
		synchronized (this) {
			UUID version = version2UUID(e.getOfflineVersion());
			if (offlineModel != null) {
				if (offlineModel.getModelInfo().isDifferentVersion(version)) {
					offlineModel.stop();
					offlineModel = null;
				}
			}
			if (offlineModel == null) {
				final BaseEngineModel bem = create(e.getId(), version, EngineMode.OFFLINE);
				offlineModel = new OfflineEngineModel(bem, portalLoader);
			}
			model = offlineModel;
		}
		model.beginRequest();
		return model;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.engine.EngineModelLoader#getOnline(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public EngineModel getOnline(String envName) throws PMSException {
		final EnvironmentEntity e = environmentManager.getEnvironment(envName);
		if (e == null) {
			return null;
		}
		if (e.getCurrent() == null) {
			return null;
		}
		final OnlineEngineModel model;
		synchronized (this) {
			UUID version = version2UUID(e.getOnlineVersion());
			if (onlineModel != null) {
				if (onlineModel.getModelInfo().isDifferentVersion(version)
					|| !Objects.equal(onlineModel.getEditionId(), e.getCurrentId())) {
					onlineModel.stop();
					onlineModel = null;
				}
			}
			if (onlineModel == null) {
				final BaseEngineModel bem = create(e.getId(), version, EngineMode.ONLINE);
				onlineModel = new OnlineEngineModel(e.getCurrent(), RoutingDomainManager.DEFAULT, bem, portalLoader);
			}
			model = onlineModel;
		}
		model.beginRequest();
		return model;
	}
}
