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

package com.isotrol.impe3.pms.api;


import java.io.Serializable;


/**
 * DTO representing a combination of a display name and path segment.
 * @author Andres Rodriguez
 */
public final class NameDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -1581749805281712962L;
	/** Display name. */
	private String displayName;
	/** Path segment. */
	private String path;

	/** Default constructor. */
	public NameDTO() {
	}

	/**
	 * Constructor.
	 * @param displayName Display name.
	 * @param path Path segment.
	 */
	public NameDTO(String displayName, String path) {
		this.displayName = displayName;
		this.path = path;
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
	 * Returns the path segment.
	 * @return The path segment.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path segment.
	 * @param path The path segment.
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
