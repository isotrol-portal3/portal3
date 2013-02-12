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

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.model.PortalDfn;


/**
 * Portal model loader service.
 * @author Andres Rodriguez.
 */
public interface PortalModelLoader {
	/**
	 * Loads an offline portal model.
	 * @param engine Offline engine.
	 * @param portalId Portal Id.
	 * @return The requested portal model.
	 * @throws PMSException
	 */
	OfflinePortalModel getOffline(OfflineEngineModel engine, UUID portalId) throws PMSException;

	/**
	 * Loads an offline portal model if it has changed.
	 * @param engine Offline engine.
	 * @param portal Current model.
	 * @return The requested portal model.
	 * @throws PMSException
	 */
	OfflinePortalModel getOffline(OfflineEngineModel engine, OfflinePortalModel portal) throws PMSException;

	/**
	 * Loads an online portal model.
	 * @param engine Online engine.
	 * @param portalDfn Portal definition.
	 * @return The requested portal model.
	 * @throws PMSException
	 */
	OnlinePortalModel getOnline(BaseEngineModel engine, PortalDfn portalDfn) throws PMSException;

}
