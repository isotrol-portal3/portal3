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
 * DTO for page selection.
 * @author Andres Rodriguez
 */
public class PageSelDTO extends PageLoc {
	/** Serial UID. */
	private static final long serialVersionUID = -6625632495380289040L;
	/** Page class. */
	private PageClass pageClass;
	/** Name or tag. */
	private String name;
	/** Description. */
	private String description;

	/** Default constructor. */
	public PageSelDTO() {
	}

	/**
	 * Copy constructor.
	 * @param dto Source dto.
	 */
	public PageSelDTO(PageSelDTO dto) {
		super(dto);
		this.pageClass = dto.pageClass;
		this.name = dto.name;
		this.description = dto.description;
	}

	/**
	 * Returns the page class.
	 * @return The page class.
	 */
	public PageClass getPageClass() {
		return pageClass;
	}

	/**
	 * Sets the page class.
	 * @param pageClass The page class.
	 */
	public void setPageClass(PageClass pageClass) {
		this.pageClass = pageClass;
	}

	/**
	 * Returns the name or tag.
	 * @return The name or tag.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name or tag.
	 * @param name The name or tag.
	 */
	public void setName(String name) {
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
