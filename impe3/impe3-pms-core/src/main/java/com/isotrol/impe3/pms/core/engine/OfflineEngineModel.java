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

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.pms.api.PMSException;


/**
 * Offline PMS Engine Model implementation.
 * @author Andres Rodriguez.
 */
public final class OfflineEngineModel extends AbstractEngineModel {
	/** Portal loader. */
	private final PortalModelLoader portalLoader;
	/** Loaded portals. */
	private final Map<UUID, OfflinePortalModel> portals;

	public OfflineEngineModel(BaseEngineModel model, PortalModelLoader portalLoader) {
		super(model);
		this.portalLoader = checkNotNull(portalLoader);
		this.portals = Maps.newHashMap();
	}

	/**
	 * @see com.isotrol.impe3.core.EngineModel#getPortal(java.util.UUID)
	 */
	public synchronized PortalModel getPortal(UUID id) {
		OfflinePortalModel pm = portals.get(id);
		try {
			if (pm == null) {
				pm = portalLoader.getOffline(this, id);
			} else {
				OfflinePortalModel created = portalLoader.getOffline(this, pm);
				if (created != pm) {
					pm.stop();
					pm = created;
				}
			}
			portals.put(id, pm);

		} catch (PMSException e) {
			throw new IllegalArgumentException(e);
		}
		pm.beginRequest();
		return pm;
	}

	@Override
	void stopChildren() {
		for (OfflinePortalModel pm : portals.values()) {
			pm.stop();
		}
		portals.clear();
	}

}
