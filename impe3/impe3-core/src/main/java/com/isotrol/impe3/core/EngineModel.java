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

package com.isotrol.impe3.core;


import java.util.UUID;

import com.isotrol.impe3.api.RoutingDomains;
import com.isotrol.impe3.core.modules.StartedModule;


/**
 * Interface to access an engine model.
 * @author Andres Rodriguez
 */
public interface EngineModel extends BaseModel {
	/**
	 * Returns the available routing domains.
	 * @return The available routing domains.
	 */
	RoutingDomains getRoutingDomains();
	
	/**
	 * Returns an started connector module.
	 * @param id Connector id.
	 * @return Connector module.
	 */
	StartedModule<?> getConnector(UUID id);

	/**
	 * Returns a portal model by id.
	 * @param id Portal Id.
	 * @return The requested model or {@code null} if it is not found.
	 */
	PortalModel getPortal(UUID id);

	/**
	 * Returns the currently loaded portals. Only available in online mode.
	 * @return The currently loaded portals.
	 * @throws UnsupportedOperationException in offline mode.
	 */
	Iterable<PortalModel> getPortals();

}
