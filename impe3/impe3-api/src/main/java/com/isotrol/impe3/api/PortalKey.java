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


import java.util.UUID;

import com.google.common.base.Preconditions;


/**
 * Set of values that (each on its own) uniquely identify a portal.
 * @author Andres Rodriguez
 */
public final class PortalKey extends AbstractIdentifiable {
	/** Path of the portal. */
	private final String path;
	
	private PortalKey(UUID id, String path) {
		super(id);
		Preconditions.checkNotNull(path, "The portal path must be provided.");
		this.path = path;
	}
	
	/**
	 * Returns a new portal key for the specified id and path.
	 * @param id Id of the portal.
	 * @param path Path of the portal.
	 * @return The requested key.
	 */
	public static PortalKey of(UUID id, String path) {
		return new PortalKey(id, path);
	}
	
	public String getPath() {
		return path;
	}
}
