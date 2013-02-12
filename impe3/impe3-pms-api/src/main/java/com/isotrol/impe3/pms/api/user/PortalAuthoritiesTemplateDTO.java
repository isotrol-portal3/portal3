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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.pms.api.GlobalRole;
import com.isotrol.impe3.pms.api.PortalAuthority;


/**
 * DTO for portal authorities management.
 * @author Andres Rodriguez
 */
public class PortalAuthoritiesTemplateDTO {
	/**
	 * Builds a list of granted authorities.
	 * @param sa Granted authorities.
	 * @param names Authorities display names.
	 * @return The requested list.
	 */
	private static List<Granted<PortalAuthorityDTO>> authorities(Set<PortalAuthority> sa,
		Map<PortalAuthority, String> names) {
		final List<Granted<PortalAuthorityDTO>> authorities = new ArrayList<Granted<PortalAuthorityDTO>>(GlobalRole
			.values().length);
		for (PortalAuthority ga : PortalAuthority.values()) {
			authorities.add(Granted.of(sa != null && sa.contains(ga), new PortalAuthorityDTO(ga, names)));
		}
		return authorities;
	}

	/** Authorities. */
	private final List<Granted<PortalAuthorityDTO>> authorities;

	/**
	 * Constructor based on a previous user.
	 * @param authorities Portal authorities.
	 * @param names Authorities display names.
	 */
	public PortalAuthoritiesTemplateDTO(Set<PortalAuthority> authorities, Map<PortalAuthority, String> names) {
		this.authorities = authorities(authorities, names);
	}

	/**
	 * Returns the authorities.
	 * @return The authorities.
	 */
	public List<Granted<PortalAuthorityDTO>> getAuthorities() {
		return authorities;
	}

	public Set<PortalAuthority> toSet() {
		final Set<PortalAuthority> set = new HashSet<PortalAuthority>();
		for (Granted<PortalAuthorityDTO> a : authorities) {
			if (a.isGranted()) {
				set.add(a.get().getAuthority());
			}
		}
		return set;
	}
}
