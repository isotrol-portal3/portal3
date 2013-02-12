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
import com.isotrol.impe3.pms.api.GlobalAuthorityMap;


/**
 * DTO for global authorities.
 * @author Andres Rodriguez
 */
public final class GlobalAuthorityDTO extends AbstractAuthDTO {
	/** Authority. */
	private final GlobalAuthority authority;
	/** Implied authorities. */
	private final List<GlobalAuthorityDTO> implied;

	/**
	 * Default constructor.
	 * @param ga Global Authority.
	 * @param names Display names map.
	 */
	public GlobalAuthorityDTO(GlobalAuthority ga, Map<GlobalAuthority, String> names) {
		super(names.get(ga));
		this.authority = ga;
		final Set<GlobalAuthority> set = GlobalAuthorityMap.getImplied(ga);
		this.implied = new ArrayList<GlobalAuthorityDTO>(set.size());
		for (GlobalAuthority iga : set) {
			this.implied.add(new GlobalAuthorityDTO(iga, names));
		}
	}

	/**
	 * Returns the global authority.
	 * @return The global authority.
	 */
	public GlobalAuthority getAuthority() {
		return authority;
	}

	/**
	 * Returns the implied authorities.
	 * @return The implied authorities.
	 */
	public List<GlobalAuthorityDTO> getImplied() {
		return implied;
	}
}
