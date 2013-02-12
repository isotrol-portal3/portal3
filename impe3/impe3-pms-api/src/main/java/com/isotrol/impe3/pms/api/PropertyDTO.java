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
 * DTO representing a string-valued property.
 * @author Andres Rodriguez
 */
public final class PropertyDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 2602155361188578867L;

	/** Property name. */
	private String name;
	/** Property value. */
	private String value;

	/** Default constructor. */
	public PropertyDTO() {
	}

	/**
	 * Constructor.
	 * @param key The property name.
	 * @param uri The property value.
	 */
	public PropertyDTO(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the property name.
	 * @return The property name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the property name.
	 * @param name The property name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the property value.
	 * @return The property value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the property value.
	 * @param value The property value.
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
