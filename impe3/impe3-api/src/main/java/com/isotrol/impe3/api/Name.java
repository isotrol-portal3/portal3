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

package com.isotrol.impe3.api;


import com.google.common.base.Preconditions;


/**
 * Value representing a combination of a display name and path segment.
 * @author Andres Rodriguez
 */
public final class Name {
	/** Display name. */
	private final String displayName;
	/** Path segment. */
	private final String path;

	/**
	 * Returns a new content type with specified id and localized name.
	 * @param displayName Display name.
	 * @param path Path segment.
	 * @return The requested content type.
	 */
	public static Name of(final String displayName, final String path) {
		Preconditions.checkNotNull(displayName);
		return new Name(displayName, path);
	}

	private Name(final String displayName, final String path) {
		this.displayName = displayName;
		this.path = path;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getPath() {
		return path;
	}
}
