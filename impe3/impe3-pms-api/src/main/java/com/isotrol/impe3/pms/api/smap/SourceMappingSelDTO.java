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

import com.isotrol.impe3.pms.api.AbstractWithId;


/**
 * DTO for source mapping selectors.
 * @author Andres Rodriguez
 */
public class SourceMappingSelDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = -146728189868866185L;
	/** Source mapping unique name. */
	private String name;
	/** Source mapping description. */
	private String description;

	/** Default constructor. */
	public SourceMappingSelDTO() {
	}

	/**
	 * Returns the source mapping name.
	 * @return The source mapping name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the source mapping name.
	 * @param name The source mapping name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the source mapping description.
	 * @return The source mapping description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the source mapping description.
	 * @param description The source mapping description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
