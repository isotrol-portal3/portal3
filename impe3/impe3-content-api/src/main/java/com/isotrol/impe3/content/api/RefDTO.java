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

package com.isotrol.impe3.content.api;


import com.isotrol.impe3.dto.AbstractStringId;


/**
 * DTO for a reference to a category or content type.
 * @author Andres Rodriguez Chamorro
 */
public class RefDTO extends AbstractStringId {
	/** Serial uid. */
	private static final long serialVersionUID = 8463398881635887029L;
	/** Name. */
	private String name;

	/** Default constructor. */
	public RefDTO() {
	}

	/**
	 * Constructor.
	 * @param id Id.
	 * @param name Name.
	 */
	public RefDTO(String id, String name) {
		super(id);
		this.name = name;
	}

	/**
	 * Returns the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
