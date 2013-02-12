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

import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;


/**
 * DTO for global roles.
 * @author Andres Rodriguez
 */
public final class GlobalRoleDTO extends AbstractAuthDTO {
	/** Role. */
	private final GlobalRole role;
	/** Implied authorities. */
	private final List<GlobalAuthorityDTO> implied;

	/**
	 * Default constructor.
	 * @param gr Global Role.
	 * @param names Display names map.
	 * @param authNames Authorities display names.
	 */
	public GlobalRoleDTO(GlobalRole gr, Map<GlobalRole, String> names, Map<GlobalAuthority, String> authNames) {
		super(names.get(gr));
		this.role = gr;
		final Set<GlobalAuthority> set = gr.getAuthorities();
		this.implied = new ArrayList<GlobalAuthorityDTO>(set.size());
		for (GlobalAuthority iga : set) {
			this.implied.add(new GlobalAuthorityDTO(iga, authNames));
		}
	}

	/**
	 * Returns the global authority.
	 * @return The global authority.
	 */
	public GlobalRole getRole() {
		return role;
	}

	/**
	 * Returns the implied authorities.
	 * @return The implied authorities.
	 */
	public List<GlobalAuthorityDTO> getImplied() {
		return implied;
	}
}
