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

package com.isotrol.impe3.web20.api;


/**
 * DTO for a rating event.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class RatingEventDTO extends EventDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -4670789989283476215L;

	/** Rating value. */
	private int value;

	/** Constructor. */
	public RatingEventDTO() {
	}

	/**
	 * Returns the rating value.
	 * @return The rating value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the rating value.
	 * @param value The rating value.
	 */
	public void setValue(int value) {
		this.value = value;
	}
}
