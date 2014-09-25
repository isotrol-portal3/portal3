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
 * Base abstract class for resource-related DTOs.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public abstract class AbstractResourceDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 737318777441433441L;
	/** Resource. */
	private String resource;

	/**
	 * Constructor.
	 * @param resource Resource.
	 */
	public AbstractResourceDTO(String resource) {
		this.resource = resource;
	}

	/** Constructor. */
	public AbstractResourceDTO() {
	}

	/**
	 * Returns the resource.
	 * @return The resource.
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * Sets the resource.
	 * @param resource The resource.
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
}
