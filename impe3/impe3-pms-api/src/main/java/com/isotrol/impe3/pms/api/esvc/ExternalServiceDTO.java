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

package com.isotrol.impe3.pms.api.esvc;


import com.isotrol.impe3.pms.api.AbstractWithId;
import com.isotrol.impe3.pms.api.Described;


/**
 * DTO for a external service.
 * @author Andres Rodriguez
 */
public final class ExternalServiceDTO extends AbstractWithId implements Described {
	/** Serial UID. */
	private static final long serialVersionUID = -4306146702180202897L;
	/** Service name. */
	private String name;
	/** Service description. */
	private String description;

	/** Default constructor. */
	public ExternalServiceDTO() {
	}

	/**
	 * Returns the service name.
	 * @return The service name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the service name.
	 * @param name The service name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the service description.
	 * @return The service description.
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * Sets the service description.
	 * @param description The service description.
	 */
	public final void setDescription(String description) {
		this.description = description;
	}

}
