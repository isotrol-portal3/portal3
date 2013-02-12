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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.google.common.base.Function;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.CategoryMapping;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.pms.api.smap.CategoryMappingDTO;
import com.isotrol.impe3.pms.model.CategoryMappingValue;


/**
 * Value that represents a category mapping.
 * @author Andres Rodriguez
 */
public final class CategoryMappingObject extends AbstractMappingObject<CategoryMappingDTO, CategoryMapping> {
	static final Function<CategoryMappingValue, CategoryMappingObject> OF = new Function<CategoryMappingValue, CategoryMappingObject>() {
		public CategoryMappingObject apply(CategoryMappingValue from) {
			return new CategoryMappingObject(from);
		}
	};

	/**
	 * Constructor.
	 * @param mapping Mapping.
	 */
	CategoryMappingObject(CategoryMappingValue mapping) {
		super(checkNotNull(mapping, "The category mapping must be provided"));
	}

	CategoryMappingDTO toDTO(Context1 context) {
		final CategoriesObject categories = context.getCategories();
		final UUID id = getId();
		CategoryObject cg = categories.get(id);
		if (cg == null) {
			return null;
		}
		CategoryMappingDTO dto = new CategoryMappingDTO();
		dto.setCategory(cg.toSelDTO());
		dto.setDisplayPath(categories.getDisplayPath(id));
		dto.setMapping(getMapping());
		return dto;
	}

	CategoryMapping toMapping(BaseModel model) {
		Category cg = model.getCategories().get(getId());
		if (cg == null) {
			return null;
		}
		return new CategoryMapping(cg, getMapping());
	}
}
