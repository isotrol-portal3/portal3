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

package com.isotrol.impe3.pms.api.page;


import com.isotrol.impe3.pms.api.AbstractDescribed;



/**
 * DTO for component palette item.
 * @author Andres Rodriguez
 */
public final class PaletteDTO extends AbstractDescribed {
	/** Serial UID. */
	private static final long serialVersionUID = -916601876814299653L;
	/** Component key. */
	private ComponentKey key;

	/** Default constructor. */
	public PaletteDTO() {
	}

	/**
	 * Returns the component key.
	 * @return The component key.
	 */
	public ComponentKey getKey() {
		return key;
	}

	/**
	 * Sets the component key.
	 * @param key The component key.
	 */
	public void setKey(ComponentKey key) {
		this.key = key;
	}
}
