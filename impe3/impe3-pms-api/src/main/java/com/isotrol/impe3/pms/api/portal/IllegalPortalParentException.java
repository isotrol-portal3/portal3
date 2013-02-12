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

package com.isotrol.impe3.pms.api.portal;


import com.isotrol.impe3.pms.api.EntityException;


/**
 * Exception thrown when a requested portal parent is not allowed.
 * @author Andres Rodriguez
 */
public class IllegalPortalParentException extends EntityException {
	/** Serial UID. */
	private static final long serialVersionUID = 3936080157442059957L;
	/** Requested parent id. */
	private final String parentId;

	/**
	 * Default constructor.
	 * @param id Entity ID.
	 */
	public IllegalPortalParentException(String id, String parentId) {
		super(id);
		this.parentId = parentId;
	}

	/**
	 * Default constructor.
	 */
	public IllegalPortalParentException() {
		this(null, null);
	}

	/**
	 * Returns the requested parent id.
	 * @return The requested parent id.
	 */
	public String getParentId() {
		return parentId;
	}

}
