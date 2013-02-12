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

import com.isotrol.impe3.pms.api.category.CategorySelDTO;


/**
 * DTO for a category mapping.
 * @author Andres Rodriguez
 */
public class CategoryMappingDTO extends AbstractMappingDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 4727941337233337658L;
	/** Category. */
	private CategorySelDTO category;
	/** Display path. */
	private String displayPath;

	/** Default constructor. */
	public CategoryMappingDTO() {
	}

	/**
	 * Returns the category.
	 * @return The category.
	 */
	public CategorySelDTO getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category The category.
	 */
	public void setCategory(CategorySelDTO category) {
		this.category = category;
	}
	
	/** Returns the category path, using the display name as segments. */
	public String getDisplayPath() {
		return displayPath;
	}
	
	/** Sets the category path, using the display name as segments. */
	public void setDisplayPath(String displayPath) {
		this.displayPath = displayPath;
	}
}
