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
 * Abstract class for DTOs that contain an ID.
 * @author Andres Rodriguez
 */
public abstract class AbstractWithId implements WithId, Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -8363697110236950425L;
	/** Object ID. */
	private String id;

	/** Default constructor. */
	public AbstractWithId() {
	}

	/**
	 * Constructor.
	 * @param id Id.
	 */
	public AbstractWithId(String id) {
		this.id = id;
	}

	/**
	 * Copy constructor.
	 * @param dto Source DTO.
	 */
	public AbstractWithId(WithId dto) {
		this.id = (dto == null) ? null : dto.getId();
	}

	/**
	 * Returns the object ID.
	 * @return The object ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the object ID.
	 * @param id The object ID.
	 */
	public void setId(String id) {
		this.id = id;
	}
}
