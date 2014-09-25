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


import java.io.Serializable;


/**
 * Selection DTO representing portal users.
 * @author Andres Rodriguez
 */
public class PortalUserSelDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 8558618133399623780L;
	/** User Id. */
	private String id;
	/** Username. */
	private String username;
	/** Display name. */
	private String displayName;
	/** Email address. */
	private String email;
	/** True if the user is active. */
	private boolean active = true;

	/** Default constructor. */
	public PortalUserSelDTO() {
	}

	/**
	 * Returns the user id.
	 * @return The user id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the user id.
	 * @param id The user id.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the username.
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * @param username The username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the display name.
	 * @return The display name.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 * @param displayName The display name.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Returns the email address.
	 * @return The email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address.
	 * @param email The email address.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns whether the user is active.
	 * @return True the user is active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets whether the user is active.
	 * @param active True the user is active.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}
