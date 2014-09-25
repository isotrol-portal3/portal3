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
 * DTO for a counter event.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class CounterEventDTO extends EventDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -4822217422826333831L;

	/** Counter type. */
	private String counterType;
	
	/** Constructor. */
	public CounterEventDTO() {
	}
	
	/**
	 * Returns the counter type.
	 * @return The counter type.
	 */
	public String getCounterType() {
		return counterType;
	}
	
	/**
	 * Sets the counter type.
	 * @param counterType The counter type.
	 */
	public void setCounterType(String counterType) {
		this.counterType = counterType;
	}
}
