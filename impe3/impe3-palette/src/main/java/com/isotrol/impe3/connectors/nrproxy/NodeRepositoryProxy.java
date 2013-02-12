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

package com.isotrol.impe3.connectors.nrproxy;


import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.support.nr.ForwardingNodeRepository;


/**
 * A proxy for NodeRepository providing differente repositories for online and offline modes.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public class NodeRepositoryProxy extends ForwardingNodeRepository {
	/** Offline repository. */
	private NodeRepository offline;
	/** Online repository. */
	private NodeRepository online;
	/** Engine mode. */
	private EngineMode mode;

	/** Default constructor. */
	public NodeRepositoryProxy() {
	}

	public void setMode(EngineMode mode) {
		this.mode = mode;
	}

	public void setOffline(NodeRepository offline) {
		this.offline = offline;
	}

	public void setOnline(NodeRepository online) {
		this.online = online;
	}

	@Override
	protected NodeRepository delegate() {
		if (EngineMode.ONLINE == mode) {
			return online;
		}
		return offline;
	}
}
