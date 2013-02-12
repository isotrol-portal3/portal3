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

package com.isotrol.impe3.pms.model;


import javax.persistence.MappedSuperclass;


/**
 * Mapped superclass for named entities.
 * @author Andres Rodriguez
 */
@MappedSuperclass
public abstract class NamedEntity extends VersionedEntity implements WithName {
	/** Name (default if localized). */
	private NameValue name;

	/** Default constructor. */
	public NamedEntity() {
	}

	/**
	 * Returns the name (the default one if localized).
	 * @return The name (the default one if localized).
	 */
	public NameValue getName() {
		return name;
	}

	/**
	 * Sets the name (the default one if localized).
	 * @param name The name (the default one if localized).
	 */
	public void setName(NameValue name) {
		this.name = name;
	}
}
