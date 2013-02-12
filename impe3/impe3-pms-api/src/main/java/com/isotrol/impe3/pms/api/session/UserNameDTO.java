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

package com.isotrol.impe3.pms.api.session;


import java.io.Serializable;


/**
 * DTO for current user name.
 * @author Andres Rodriguez
 */
public class UserNameDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -5892530879372436285L;
	/** Login name. */
	private String name;
	/** Display name. */
	private String displayName;

	/** Default constructor. */
	public UserNameDTO() {
	}

	/**
	 * Returns the login name.
	 * @return The login name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the login name.
	 * @param name The login name.
	 */
	public void setName(String name) {
		this.name = name;
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
}
