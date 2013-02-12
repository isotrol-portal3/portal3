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

package com.isotrol.impe3.pms.api.smap;


/**
 * DTO for a node set mapping.
 * @author Andres Rodriguez
 */
public class SetMappingDTO extends AbstractMappingDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 6362864664401752963L;
	/** Set name. */
	private String set;

	/** Default constructor. */
	public SetMappingDTO() {
	}

	/**
	 * Returns the set name.
	 * @return The set name.
	 */
	public String getSet() {
		return set;
	}

	/**
	 * Sets the set name.
	 * @param set The set name.
	 */
	public void setSet(String set) {
		this.set = set;
	}
}
