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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.isotrol.impe3.api.Identifiable;
import com.isotrol.impe3.api.RoutingDomain;
import com.isotrol.impe3.pms.api.rd.RoutingDomainDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainSelDTO;


/**
 * Value that represents a routing domain.
 * @author Andres Rodriguez
 */
public final class RoutingDomainObject implements Identifiable {
	/** Routing domain. */
	private final RoutingDomain domain;

	/**
	 * Constructor.
	 * @param domain Routing domain.
	 */
	RoutingDomainObject(RoutingDomain domain) {
		this.domain = checkNotNull(domain);
	}

	public UUID getId() {
		return domain.getId();
	}

	public RoutingDomain getDomain() {
		return domain;
	}

	public String getName() {
		return domain.getName();
	}

	<T extends RoutingDomainSelDTO> T fill(T dto) {
		dto.setId(getId().toString().toLowerCase());
		dto.setName(domain.getName());
		dto.setDescription(domain.getDescription());
		return dto;
	}

	public RoutingDomainSelDTO toSel() {
		final RoutingDomainSelDTO dto = fill(new RoutingDomainSelDTO());
		return dto;
	}

	public RoutingDomainDTO toDTO() {
		final RoutingDomainDTO dto = fill(new RoutingDomainDTO());
		dto.setOnlineBase(domain.getOnline().getBase().toASCIIString());
		dto.setOfflineBase(domain.getOffline().getBase().toASCIIString());
		dto.setOnlineAbsBase(domain.getOnline().getAbsoluteBase().toASCIIString());
		dto.setOfflineAbsBase(domain.getOffline().getAbsoluteBase().toASCIIString());
		return dto;
	}

}
