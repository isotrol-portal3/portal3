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

import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;


/**
 * DTO for user mangement.
 * @author Andres Rodriguez
 */
public class UserTemplateDTO extends UserSelDTO {
	/**
	 * Builds a list of granted roles.
	 * @param sr Granted roles.
	 * @param names Roles display names.
	 * @param authNames Authorities display names.
	 * @return The requested list.
	 */
	private static List<Granted<GlobalRoleDTO>> roles(Set<GlobalRole> sr, Map<GlobalRole, String> names,
		Map<GlobalAuthority, String> authNames) {
		final List<Granted<GlobalRoleDTO>> roles = new ArrayList<Granted<GlobalRoleDTO>>(GlobalAuthority.values().length);
		for (GlobalRole gr : GlobalRole.values()) {
			roles.add(Granted.of(sr != null && sr.contains(gr), new GlobalRoleDTO(gr, names, authNames)));
		}
		return roles;
	}

	/**
	 * Builds a list of granted authorities.
	 * @param sa Granted authorities.
	 * @param authNames Authorities display names.
	 * @return The requested list.
	 */
	private static List<Granted<GlobalAuthorityDTO>> authorities(Set<GlobalAuthority> sa,
		Map<GlobalAuthority, String> authNames) {
		final List<Granted<GlobalAuthorityDTO>> authorities = new ArrayList<Granted<GlobalAuthorityDTO>>(GlobalRole
			.values().length);
		for (GlobalAuthority ga : GlobalAuthority.values()) {
			authorities.add(Granted.of(sa != null && sa.contains(ga), new GlobalAuthorityDTO(ga, authNames)));
		}
		return authorities;
	}

	/** Serial UID. */
	private static final long serialVersionUID = 1591425780854399004L;
	/** Roles. */
	private final List<Granted<GlobalRoleDTO>> roles;
	/** Authorities. */
	private final List<Granted<GlobalAuthorityDTO>> authorities;

	/**
	 * Constructor based on a previous user.
	 * @param dto Source DTO.
	 * @param names Roles display names.
	 * @param authNames Authorities display names.
	 */
	UserTemplateDTO(UserDTO dto, Map<GlobalRole, String> names, Map<GlobalAuthority, String> authNames) {
		super(dto);
		this.roles = roles(dto.getRoles(), names, authNames);
		this.authorities = authorities(dto.getAuthorities(), authNames);
	}

	/**
	 * Empty template constructor.
	 * @param names Roles display names.
	 * @param authNames Authorities display names.
	 */
	public UserTemplateDTO(Map<GlobalRole, String> names, Map<GlobalAuthority, String> authNames) {
		setActive(true);
		this.roles = roles(null, names, authNames);
		this.authorities = authorities(null, authNames);
	}

	/**
	 * Returns the roles.
	 * @return The roles.
	 */
	public List<Granted<GlobalRoleDTO>> getRoles() {
		return roles;
	}

	/**
	 * Returns the authorities.
	 * @return The authorities.
	 */
	public List<Granted<GlobalAuthorityDTO>> getAuthorities() {
		return authorities;
	}

	public UserDTO toDTO() {
		final UserDTO dto = new UserDTO(this);
		final Set<GlobalRole> sr = new HashSet<GlobalRole>();
		for (Granted<GlobalRoleDTO> gr : roles) {
			if (gr.isGranted()) {
				sr.add(gr.get().getRole());
			}
		}
		dto.setRoles(sr);
		final Set<GlobalAuthority> sa = new HashSet<GlobalAuthority>();
		for (Granted<GlobalAuthorityDTO> ga : authorities) {
			if (ga.isGranted()) {
				sa.add(ga.get().getAuthority());
			}
		}
		dto.setAuthorities(sa);
		return dto;
	}
}
