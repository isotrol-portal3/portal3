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

package com.isotrol.impe3.pms.gui.client.data.impl;


import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.smap.CategoryMappingDTO;


/**
 * Model for category mappings
 * @author Manuel Ruiz
 * 
 */
public class CategoryMappingModelData extends AbstractMappingModelData<CategoryMappingDTO> {

	/** descriptor for category mapping property category */
	public static final String PROPERTY_CATEGORY = "category";
	/** descriptor for category mapping property display path */
	public static final String PROPERTY_DISPLAY_PATH = "category";
	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(AbstractMappingModelData.PROPERTIES, PROPERTY_CATEGORY, PROPERTY_DISPLAY_PATH);

	/**
	 * Default constructor
	 */
	public CategoryMappingModelData(CategoryMappingDTO dto) {
		super(dto);
	}

	/**
	 * Default constructor
	 */
	public CategoryMappingModelData() {
		this(new CategoryMappingDTO());
	}

	@Override
	protected Object doGet(String property) {
		if (PROPERTY_CATEGORY.equals(property)) {
			CategoryMappingDTO dto = getDTO();
			if (dto.getCategory() == null) {
				return null;
			}
			return dto.getCategory().getName();
		} else if (PROPERTY_DISPLAY_PATH.equals(property)) {
			CategoryMappingDTO dto = getDTO();
			if (dto.getDisplayPath() == null) {
				return "";
			}
			return dto.getDisplayPath();
		}
		return super.doGet(property);
	}

	@Override
	protected Object doSet(String property, Object value) {
		Object old = null;
		if (PROPERTY_CATEGORY.equals(property)) {
			CategoryMappingDTO dto = getDTO();
			old = dto.getCategory();
			dto.setCategory((CategorySelDTO) value);
		} else {
			old = super.doSet(property, value);
		}
		return old;
	}

	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
