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


import java.util.List;


/**
 * DTO for source mapping detail.
 * @author Andres Rodriguez
 */
public class SourceMappingDTO extends SourceMappingSelDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 5268887274019954885L;
	/** Set mappings. */
	private List<SetMappingDTO> sets;
	private List<ContentTypeMappingDTO> contentTypes;
	private List<CategoryMappingDTO> categories;

	/** Default constructor. */
	public SourceMappingDTO() {
	}

	/**
	 * Returns the set mappings.
	 * @return The set mappings.
	 */
	public List<SetMappingDTO> getSets() {
		return sets;
	}

	/**
	 * Sets the set mappings.
	 * @param sets The set mappings.
	 */
	public void setSets(List<SetMappingDTO> sets) {
		this.sets = sets;
	}

	/**
	 * Returns the content type mappings.
	 * @return The content type mappings.
	 */
	public List<ContentTypeMappingDTO> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Sets the content type mappings.
	 * @param contentTypes The content type mappings.
	 */
	public void setContentTypes(List<ContentTypeMappingDTO> contentTypes) {
		this.contentTypes = contentTypes;
	}

	/**
	 * Returns the category mappings.
	 * @return The category mappings.
	 */
	public List<CategoryMappingDTO> getCategories() {
		return categories;
	}

	/**
	 * Sets the category mappings.
	 * @param categories The category mappings.
	 */
	public void setCategories(List<CategoryMappingDTO> categories) {
		this.categories = categories;
	}
}
