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
package com.isotrol.impe3.oi.api;


/**
 * DTO for a used class.
 * @author Andres Rodriguez
 */
public class UsedClassDTO extends AbstractClassDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -4753638194781482773L;
	
	/** Class use count. */
	private long count;

	/**
	 * Constructor.
	 * @param name Class name.
	 * @param count Class use count.
	 */
	public UsedClassDTO(String name, long count) {
		super(name);
		this.count = count;
	}

	/** Constructor. */
	public UsedClassDTO() {
	}

	/**
	 * Returns the class use count value.
	 * @return The class use count value.
	 */
	public long getCount() {
		return count;
	}

	/**
	 * Sets the class use count value.
	 * @param count The class use count value.
	 */
	public void setCount(long count) {
		this.count = count;
	}
}
