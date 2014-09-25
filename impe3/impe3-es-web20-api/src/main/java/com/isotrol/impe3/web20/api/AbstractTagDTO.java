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

import java.io.Serializable;

/**
 * Abstract class for tag DTOs.
 * @author Andres Rodriguez
 */
public class AbstractTagDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -4427090074602958840L;
	
	/** Tag name. */
	private String name;

	/**
	 * Constructor.
	 * @param name Tag name.
	 */
	public AbstractTagDTO(String name) {
		this.name = name;
	}

	/** Constructor. */
	public AbstractTagDTO() {
	}
	
	/**
	 * Returns the tag name.
	 * @return The tag name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the tag name.
	 * @param name The tag name.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
