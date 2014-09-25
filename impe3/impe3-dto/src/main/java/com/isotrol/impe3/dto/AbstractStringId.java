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

package com.isotrol.impe3.dto;


import java.io.Serializable;


/**
 * Base class for DTOs with a string id.
 * @author Andres Rodriguez
 */
public class AbstractStringId implements Serializable, WithStringId {
	/** Serial UID. */
	private static final long serialVersionUID = 437978362356839654L;

	/** Id. */
	private String id;

	/**
	 * Constructor.
	 * @param id Id.
	 */
	public AbstractStringId(String id) {
		this.id = id;
	}

	/**
	 * Default constructor.
	 */
	public AbstractStringId() {
	}

	/**
	 * @see com.isotrol.impe3.dto.WithStringId#getId()
	 */
	public String getId() {
		return id;
	}

	/**
	 * @see com.isotrol.impe3.dto.WithStringId#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}
}
