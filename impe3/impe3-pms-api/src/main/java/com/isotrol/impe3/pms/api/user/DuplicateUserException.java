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


import com.isotrol.impe3.pms.api.PMSException;


/**
 * Exception thrown when a user with the same login name is found.
 * @author Andres Rodriguez
 */
public class DuplicateUserException extends PMSException {
	/** Serial UID. */
	private static final long serialVersionUID = -7824173601841236662L;
	/** Duplicate name. */
	private final String name;

	/**
	 * Default constructor.
	 * @param name Duplicate name.
	 */
	public DuplicateUserException(String name) {
		this.name = name;
	}

	/**
	 * Default constructor.
	 */
	public DuplicateUserException() {
		this(null);
	}

	/**
	 * Returns the duplicate name.
	 * @return The duplicate name.
	 */
	public String getName() {
		return name;
	}

}
