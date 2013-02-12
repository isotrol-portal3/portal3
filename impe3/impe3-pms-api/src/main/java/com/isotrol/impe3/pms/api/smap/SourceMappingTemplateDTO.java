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


import java.io.Serializable;
import java.util.List;

import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * DTO for source mapping template.
 * @author Andres Rodriguez
 */
public class SourceMappingTemplateDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -8404310269251750171L;
	private SourceMappingDTO mapping;
	private List<ContentTypeSelDTO> contentTypes;
	private CategoryTreeDTO categories;

	/** Default constructor. */
	public SourceMappingTemplateDTO() {
	}

	/**
	 * Returns the source mapping.
	 * @return The source mapping.
	 */
	public SourceMappingDTO getMapping() {
		return mapping;
	}

	/**
	 * Sets the source mapping.
	 * @param mapping The source mapping.
	 */
	public void setMapping(SourceMappingDTO mapping) {
		this.mapping = mapping;
	}

	/**
	 * Returns the possible content types.
	 * @return The possible content types.
	 */
	public List<ContentTypeSelDTO> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Sets the possible content types.
	 * @param contentTypes The possible content types.
	 */
	public void setContentTypes(List<ContentTypeSelDTO> contentTypes) {
		this.contentTypes = contentTypes;
	}

	/**
	 * Returns the possible categories.
	 * @return The possible categories.
	 */
	public CategoryTreeDTO getCategories() {
		return categories;
	}

	/**
	 * Sets the possible categories.
	 * @param categories The possible categories.
	 */
	public void setCategories(CategoryTreeDTO categories) {
		this.categories = categories;
	}
}
