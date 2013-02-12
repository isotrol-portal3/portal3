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


/**
 * Abstract DTO for page modification.
 * @author Andres Rodriguez
 */
public abstract class AbstractPageDTO extends PageSelDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 4666531076202948730L;
	/** Whether the page is an umbrella page. */
	private boolean umbrella = false;

	/** Default constructor. */
	public AbstractPageDTO() {
	}

	/**
	 * Copy constructor.
	 * @param dto Source dto.
	 */
	public AbstractPageDTO(AbstractPageDTO dto) {
		super(dto);
		this.umbrella = dto.umbrella;
	}

	/**
	 * Returns whether the page is an umbrella page.
	 * @return True if the page is an umbrella page.
	 */
	public boolean isUmbrella() {
		return umbrella;
	}

	/**
	 * Sets whether the page is an umbrella page.
	 * @param umbrella True if the page is an umbrella page.
	 */
	public void setUmbrella(boolean umbrella) {
		this.umbrella = umbrella;
	}
}
