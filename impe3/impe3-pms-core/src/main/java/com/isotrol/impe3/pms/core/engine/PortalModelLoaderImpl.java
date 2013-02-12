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


import static com.isotrol.impe3.pms.core.support.NotFoundProviders.PORTAL;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.PageLoader;
import com.isotrol.impe3.pms.core.PortalLoader;
import com.isotrol.impe3.pms.core.impl.AbstractService;
import com.isotrol.impe3.pms.core.obj.PortalsObject;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEntity;


/**
 * Portal model loader service.
 * @author Andres Rodriguez.
 */
@Service
public class PortalModelLoaderImpl extends AbstractService implements PortalModelLoader {
	@Autowired
	private PageLoader pageLoader;
	@Autowired
	private PortalLoader portalLoader;

	public PortalModelLoaderImpl() {
	}

	private PortalsObject getPortals(EngineModel engine) throws PMSException {
		if (engine.getMode() == EngineMode.OFFLINE) {
			return portalLoader.loadOffline(engine.getId());
		}
		return portalLoader.loadOnline(engine.getId());
	}

	private PortalEntity getEntity(OfflineEngineModel engine, UUID id) throws PMSException {
		final PortalEntity entity = load(PortalEntity.class, id, PORTAL);
		PORTAL.checkCondition(entity.getEnvironment().getId().equals(engine.getId()), id);
		return entity;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.engine.PortalModelLoader#getOffline(com.isotrol.impe3.pms.core.engine.OfflineEngineModel,
	 * java.util.UUID)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public OfflinePortalModel getOffline(OfflineEngineModel engine, UUID portalId) throws PMSException {
		final PortalEntity entity = getEntity(engine, portalId);
		return new OfflinePortalModel(new PortalLoadingSupport(engine, getPortals(engine), entity.getCurrent(),
			portalLoader, pageLoader));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.engine.PortalModelLoader#getOffline(com.isotrol.impe3.pms.core.engine.OfflineEngineModel,
	 * com.isotrol.impe3.pms.core.engine.OfflinePortalModel)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public OfflinePortalModel getOffline(OfflineEngineModel engine, OfflinePortalModel portal) throws PMSException {
		final PortalEntity entity = getEntity(engine, portal.getId());
		if (entity.getOfflineVersion() == portal.getVersion()) {
			return portal;
		}
		return new OfflinePortalModel(new PortalLoadingSupport(engine, getPortals(engine), entity.getCurrent(),
			portalLoader, pageLoader));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.engine.PortalModelLoader#getOnline(com.isotrol.impe3.pms.core.engine.BaseEngineModel,
	 * com.isotrol.impe3.pms.model.PortalDfn)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public OnlinePortalModel getOnline(BaseEngineModel engine, PortalDfn portalDfn) throws PMSException {
		return new OnlinePortalModel(new PortalLoadingSupport(engine, getPortals(engine), portalDfn, portalLoader,
			pageLoader));
	}
}
