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


import com.isotrol.impe3.pms.api.AbstractWithId;


/**
 * Abstract class for values representing a component in a page.
 * @author Andres Rodriguez
 */
public abstract class AbstractComponentInPageDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = 986919931498366337L;
	/** Instance name. */
	private String name;

	/** Default constructor. */
	public AbstractComponentInPageDTO() {
	}

	/**
	 * Returns the instance name.
	 * @return The instance name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the instance name.
	 * @param name The instance name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public abstract boolean isSpace();
}
