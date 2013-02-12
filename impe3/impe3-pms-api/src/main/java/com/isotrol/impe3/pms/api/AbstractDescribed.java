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
 * Abstract superclass for described DTOs.
 * @author Andres Rodriguez
 */
public abstract class AbstractDescribed implements Described, Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 7452845354581882148L;
	/** Name. */
	private String name;
	/** Description. */
	private String description;

	/** Default constructor. */
	public AbstractDescribed() {
	}

	/**
	 * Returns the name.
	 * @return The name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description.
	 * @return The description.
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * @param description The description.
	 */
	public final void setDescription(String description) {
		this.description = description;
	}
}
