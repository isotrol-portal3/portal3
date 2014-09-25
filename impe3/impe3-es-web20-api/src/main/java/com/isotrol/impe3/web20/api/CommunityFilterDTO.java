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

import com.isotrol.impe3.dto.StringFilterDTO;


/**
 * DTO for community selection filter.
 * @author Emilio Escobar Reyero
 */
public class CommunityFilterDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 2870905225585100443L;

	/** Community name. */
	private StringFilterDTO name;

	/** Community description. */
	private StringFilterDTO description;

	/** Default constructor. */
	public CommunityFilterDTO() {
	}

	/**
	 * Returns the community name.
	 * @return The community name.
	 */
	public StringFilterDTO getName() {
		return name;
	}

	/**
	 * Sets the community name.
	 * @param name The community name.
	 */
	public void setName(StringFilterDTO name) {
		this.name = name;
	}
	
	/**
	 * Sets the community name.
	 * @param name The community name.
	 * @return The fluid builder.
	 */
	public CommunityFilterDTO putName(StringFilterDTO name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Returns the description.
	 * @return The description.
	 */
	public StringFilterDTO getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * @param description The description.
	 */
	public void setDescription(StringFilterDTO description) {
		this.description = description;
	}
	
	/**
	 * Sets the description.
	 * @param description The description.
	 * @return The fluid builder.
	 */
	public CommunityFilterDTO putDescription(StringFilterDTO description) {
		this.description = description;
		return this;
	}

}
