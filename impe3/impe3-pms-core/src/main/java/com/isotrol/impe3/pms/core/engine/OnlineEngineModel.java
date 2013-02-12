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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEdition;


/**
 * Online PMS Engine Model implementation.
 * @author Andres Rodriguez.
 */
public final class OnlineEngineModel extends AbstractEngineModel {
	private static final Function<PortalEdition, PortalDfn> PORTAL = new Function<PortalEdition, PortalDfn>() {
		public PortalDfn apply(PortalEdition from) {
			return from.getPublished();
		};
	};

	/** Edition UUID. */
	private final UUID editionId;
	/** Portals. */
	private final ImmutableMap<UUID, OnlinePortalModel> portals;

	public OnlineEngineModel(EditionEntity edition, String routingDomain, BaseEngineModel model,
		PortalModelLoader portalLoader) throws PMSException {
		super(model);
		this.editionId = edition.getId();
		ImmutableMap.Builder<UUID, OnlinePortalModel> builder = ImmutableMap.builder();
		for (PortalDfn dfn : Iterables.transform(edition.getPortals(), PORTAL)) {
			// Temporal: see #15633
			// if (Objects.equal(routingDomain, dfn.getRoutingDomain().getName())) {
			final OnlinePortalModel portalModel = portalLoader.getOnline(model, dfn);
			builder.put(portalModel.getId(), portalModel);
			// }
		}
		this.portals = builder.build();
	}

	public UUID getEditionId() {
		return editionId;
	}

	/**
	 * @see com.isotrol.impe3.core.EngineModel#getPortal(java.util.UUID)
	 */
	public PortalModel getPortal(UUID id) {
		return portals.get(id);
	}

	/**
	 * @see com.isotrol.impe3.core.EngineModel#getPortals()
	 */
	public Iterable<PortalModel> getPortals() {
		return Iterables.filter(portals.values(), PortalModel.class);
	}

	@Override
	void shutdownChildren() {
		for (OnlinePortalModel opm : portals.values()) {
			opm.stopComponents();
		}
	}
}
