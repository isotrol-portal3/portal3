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

import java.util.UUID;

import com.isotrol.impe3.api.RoutingDomains;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.core.modules.StartedModule;


/**
 * Base PMS Engine Model implementation.
 * @author Andres Rodriguez.
 */
public class BaseEngineModel extends BasePMSModel implements EngineModel {
	/** The available routing domains. */
	private final RoutingDomains routingDomains;

	/**
	 * Constructor.
	 * @param model Base model.
	 * @param routingDomains The available routing domains.
	 */
	BaseEngineModel(BasePMSModel model, RoutingDomains routingDomains) {
		super(model);
		this.routingDomains = checkNotNull(routingDomains, "The routing domains must be provided");
	}

	/**
	 * Copy constructor.
	 * @param model Original model.
	 */
	BaseEngineModel(BaseEngineModel model) {
		super(model);
		this.routingDomains = model.routingDomains;
	}

	/**
	 * @see com.isotrol.impe3.core.EngineModel#getRoutingDomains()
	 */
	public RoutingDomains getRoutingDomains() {
		return routingDomains;
	}

	/**
	 * @see com.isotrol.impe3.core.EngineModel#getConnector(java.util.UUID)
	 */
	public StartedModule<?> getConnector(UUID id) {
		return getConnectors().apply(id);
	}

	public PortalModel getPortal(UUID id) {
		throw new UnsupportedOperationException();
	}

	public Iterable<PortalModel> getPortals() {
		throw new UnsupportedOperationException();
	}
}
