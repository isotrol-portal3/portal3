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

package com.isotrol.impe3.pms.api.user;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.PortalAuthorityMap;


/**
 * DTO for portal authorities.
 * @author Andres Rodriguez
 */
public final class PortalAuthorityDTO extends AbstractAuthDTO {
	/** Authority. */
	private final PortalAuthority authority;
	/** Implied authorities. */
	private final List<PortalAuthorityDTO> implied;

	/**
	 * Default constructor.
	 * @param pa Portal Authority.
	 * @param names Display names map.
	 */
	public PortalAuthorityDTO(PortalAuthority pa, Map<PortalAuthority, String> names) {
		super(names.get(pa));
		this.authority = pa;
		final Set<PortalAuthority> set = PortalAuthorityMap.getImplied(pa);
		this.implied = new ArrayList<PortalAuthorityDTO>(set.size());
		for (PortalAuthority ipa : set) {
			this.implied.add(new PortalAuthorityDTO(ipa, names));
		}
	}

	/**
	 * Returns the portal authority.
	 * @return The portal authority.
	 */
	public PortalAuthority getAuthority() {
		return authority;
	}

	/**
	 * Returns the implied authorities.
	 * @return The implied authorities.
	 */
	public List<PortalAuthorityDTO> getImplied() {
		return implied;
	}
}
