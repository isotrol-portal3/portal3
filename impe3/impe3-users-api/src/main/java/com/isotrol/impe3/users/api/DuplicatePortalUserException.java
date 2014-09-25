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


/**
 * Exception thrown when a portal user with the same username is found.
 * @author Andres Rodriguez
 */
public class DuplicatePortalUserException extends PortalUserException {
	/** Serial UID. */
	private static final long serialVersionUID = -8179897082311602479L;
	/** Username. */
	private final String username;

	/**
	 * Default constructor.
	 * @param id Entity ID.
	 * @param username Duplicate username.
	 */
	public DuplicatePortalUserException(String id, String username) {
		super(id);
		this.username = username;
	}

	/**
	 * Default constructor.
	 * @param username Duplicate username.
	 */
	public DuplicatePortalUserException(String username) {
		this(null, username);
	}

	/**
	 * Default constructor.
	 */
	public DuplicatePortalUserException() {
		this(null);
	}

	/**
	 * Returns the username.
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

}
