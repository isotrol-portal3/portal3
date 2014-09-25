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
 * Base class for field related DTOs.
 * @author Andres Rodriguez
 */
public abstract class OfField implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 5900042196053711649L;

	/**
	 * Checks whether a dto instance is considered {@code null}.
	 * @param dto DTO to check.
	 * @return True if the argument is {@code null} or the field name is {@code null}.
	 */
	public static boolean isNull(OfField dto) {
		return dto == null || dto.name == null;
	}

	/** Field name. */
	private String name;

	/**
	 * Constructor.
	 * @param name Field name.
	 */
	public OfField(String name) {
		this.name = name;
	}

	/**
	 * Default constructor.
	 */
	public OfField() {
	}

	/**
	 * Returns the field name.
	 * @return The field name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the field name.
	 * @param name The field name.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
