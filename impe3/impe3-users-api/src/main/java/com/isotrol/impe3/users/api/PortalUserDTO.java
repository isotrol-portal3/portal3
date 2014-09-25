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

package com.isotrol.impe3.users.api;


import java.util.Map;
import java.util.Set;


/**
 * DTO representing a portal user.
 * @author Andres Rodriguez
 */
public class PortalUserDTO extends PortalUserSelDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 1652599214693344955L;
	/** User properties. */
	private Map<String, String> properties;
	/** User roles. */
	private Set<String> roles;

	/** Default constructor. */
	public PortalUserDTO() {
	}

	/**
	 * Returns the user properties.
	 * @return The user properties.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * Sets the user properties.
	 * @param properties The user properties.
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * Returns the user roles.
	 * @return The user roles.
	 */
	public Set<String> getRoles() {
		return roles;
	}

	/**
	 * Sets the user roles.
	 * @param roles The user roles.
	 */
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

}
