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


/**
 * DTO for ordering representation.
 * @author Andres Rodriguez
 */
public class OrderDTO extends OfField {
	/** Serial UID. */
	private static final long serialVersionUID = -1416403107451739788L;

	/** Whether the ordering is ascending. */
	private boolean ascending = true;

	/**
	 * Constructor.
	 * @param name Field name.
	 * @param size Page size.
	 */
	public OrderDTO(String name, boolean ascending) {
		super(name);
		this.ascending = ascending;
	}

	/**
	 * Default constructor.
	 */
	public OrderDTO() {
	}

	/**
	 * Returns whether the ordering is ascending.
	 * @return True if the ordering is ascending.
	 */
	public boolean getAscending() {
		return ascending;
	}

	/**
	 * Sets whether the ordering is ascending.
	 * @param ascending True if the ordering is ascending.
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}
