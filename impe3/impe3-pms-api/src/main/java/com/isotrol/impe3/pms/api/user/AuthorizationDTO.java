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


import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PortalAuthority;


/**
 * DTO for user authorization information.
 * @author Andres Rodriguez
 */
public final class AuthorizationDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -9135747565113521966L;
	/** Whether the user is root. */
	private boolean root;
	/** Global Authorities. */
	private Set<GlobalAuthority> authorities;
	/** Portal authorities. */
	private Map<String, Set<PortalAuthority>> portalAuthorities;

	/** Default constructor. */
	public AuthorizationDTO() {
	}

	/**
	 * Returns whether the user is root.
	 * @return True if the user is root.
	 */
	public boolean isRoot() {
		return root;
	}

	/**
	 * Sets whether the user is root.
	 * @param root True if the user is root.
	 */
	public void setRoot(boolean root) {
		this.root = root;
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
	 * Returns the authorities.
	 * @return The authorities.
	 */
	public Map<String, Set<PortalAuthority>> getPortalAuthorities() {
		return portalAuthorities;
	}
	
	/**
	 * Sets the user authorities.
	 * @param authorities The user authorities.
	 */
	public void setPortalAuthorities(Map<String, Set<PortalAuthority>> portalAuthorities) {
		this.portalAuthorities = portalAuthorities;
	}
}
