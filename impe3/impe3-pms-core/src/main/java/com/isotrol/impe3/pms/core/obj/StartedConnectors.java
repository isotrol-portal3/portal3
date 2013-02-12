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


import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.core.modules.StartedModule;


/**
 * A collection of running connectors.
 * @author Andres Rodriguez
 */
public final class StartedConnectors extends StartedModules {
	/** Instantiated connectors. */
	private final ImmutableMap<UUID, StartedModule<?>> connectors;
	/** Stop list. */
	private final ImmutableList<StartedModule<?>> connectorStopList;

	/**
	 * Constructor.
	 * @param connectors Instantiated connectors.
	 * @param connectorStopList Stop list.
	 */
	StartedConnectors(Map<UUID, StartedModule<?>> connectors, List<StartedModule<?>> connectorStopList) {
		this.connectors = ImmutableMap.copyOf(connectors);
		this.connectorStopList = ImmutableList.copyOf(connectorStopList);
	}

	public StartedModule<?> apply(UUID from) {
		return connectors.get(from);
	}

	@Override
	Iterable<StartedModule<?>> getStopList() {
		return connectorStopList;
	}
}
