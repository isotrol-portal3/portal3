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


import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;


/**
 * DTO for user mangement.
 * @author Andres Rodriguez
 */
public class UserDTO extends UserSelDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 1591425780854399004L;
	/** Roles. */
	private Set<GlobalRole> roles;
	/** Authorities. */
	private Set<GlobalAuthority> authorities;

	/** Default constructor. */
	public UserDTO() {
	}

	/**
	 * Copy constructor.
	 * @param dto Source DTO.
	 */
	UserDTO(UserSelDTO dto) {
		super(dto);
	}

	/**
	 * Returns the roles.
	 * @return The roles.
	 */
	public Set<GlobalRole> getRoles() {
		return roles;
	}

	/**
	 * Sets the user roles.
	 * @param roles The user roles.
	 */
	public void setRoles(Set<GlobalRole> roles) {
		this.roles = roles;
	}

	/**
	 * Returns the authorities.
	 * @return The authorities.
	 */
	public Set<GlobalAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * Sets the user authorities.
	 * @param authorities The user authorities.
	 */
	public void setAuthorities(Set<GlobalAuthority> authorities) {
		this.authorities = authorities;
	}
	
	/**
	 * Converts the DTO to a template DTO.
	 * @param names Display names map.
	 * @param authNames Authorities display names.
	 * @return The template DTO.
	 */
	public UserTemplateDTO toTemplate(Map<GlobalRole, String> names, Map<GlobalAuthority, String> authNames) {
		return new UserTemplateDTO(this, names, authNames);
	}
	
}
