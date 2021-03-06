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

package com.isotrol.impe3.mappings;


import java.io.Serializable;
import java.util.UUID;


/**
 * DTO for a mapping item.
 * @author Andres Rodriguez
 */
public class MappingDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -7609290215951904308L;
	/** Mapped element. */
	private String id;
	/** Mapping. */
	private String mapping;

	/** Default constructor. */
	public MappingDTO() {
	}

	/**
	 * Returns the mapped element.
	 * @return The mapped element.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the mapped element.
	 * @param id The mapped element.
	 */
	public void setId(String id) {
		this.id = isUUID(id) ? id.toString().toLowerCase() : id;
	}

	/**
	 * Returns the mapping.
	 * @return The mapping.
	 */
	public String getMapping() {
		return mapping;
	}

	/**
	 * Sets the mapping.
	 * @param mapping The mapping.
	 */
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	
	private boolean isUUID(String uuid) {
		try {
			UUID.fromString(uuid);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
