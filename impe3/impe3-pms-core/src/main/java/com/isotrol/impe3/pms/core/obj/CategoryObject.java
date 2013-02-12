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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.pbuf.category.CategoryProtos.CategoryPB;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.model.CategoryDfn;


/**
 * Category type domain object.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class CategoryObject extends AbstractIAEObject {
	public static Function<CategoryObject, CategorySelDTO> MAP2SEL = new Function<CategoryObject, CategorySelDTO>() {
		public CategorySelDTO apply(CategoryObject from) {
			return from.toSelDTO();
		}
	};

	public static Function<CategoryObject, CategoryDTO> MAP2DTO = new Function<CategoryObject, CategoryDTO>() {
		public CategoryDTO apply(CategoryObject from) {
			return from.toDTO();
		}
	};

	static final Function<CategoryObject, Category> MAP2API = new Function<CategoryObject, Category>() {
		public Category apply(CategoryObject from) {
			return set(Category.builder(), from).setVisible(from.isVisible()).get();
		}
	};

	static final Function<CategoryObject, CategoryPB> map2pb(final CategoriesObject categories, final int levels) {
		return new Function<CategoryObject, CategoryPB>() {
			public CategoryPB apply(CategoryObject from) {
				return from.toPB(categories, levels);
			}
		};
	}

	/** Whether the category is visible. */
	private final boolean visible;

	/**
	 * Constructor.
	 * @param dfn Definition.
	 */
	CategoryObject(CategoryDfn dfn) {
		super(dfn);
		this.visible = dfn.isVisible();
	}

	/**
	 * Returns whether the content type is visible.
	 * @return True if the content type is visible.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Transforms the object to a selection DTO.
	 * @return The selection DTO.
	 */
	public CategorySelDTO toSelDTO() {
		final CategorySelDTO dto = new CategorySelDTO();
		dto.setId(getStringId());
		dto.setName(getName().get().getDisplayName());
		dto.setState(getState());
		dto.setVisible(isVisible());
		dto.setRoutable(isRoutable());
		return dto;
	}

	/**
	 * Transforms the object to a management DTO.
	 * @return The management DTO.
	 */
	public CategoryDTO toDTO() {
		final CategoryDTO dto = new CategoryDTO();
		dto.setId(getStringId());
		dto.setState(getState());
		dto.setRoutable(isRoutable());
		dto.setVisible(isVisible());
		fillName(dto);
		return dto;
	}

	/**
	 * Transforms the object to a protocol buffer message.
	 * @param categories Category hierarchy.
	 * @param parentId Parent to use.
	 * @param levels Levels to include (including this).
	 * @return The PB message.
	 */
	final CategoryPB toPB(CategoriesObject categories, int levels) {
		checkArgument(levels > 0);
		CategoryPB.Builder b = CategoryPB.newBuilder();
		b.setId(getStringId());
		b.setDefaultName(MessageMappers.name().apply(getDefaultName()));
		b.addAllLocalizedNames(MessageMappers.localizedName(getLocalizedNames()));
		b.setVisible(isVisible());
		b.setRoutable(isRoutable());
		if (levels > 1) {
			b.addAllChildren(transform(categories.getChildren(getId()), map2pb(categories, levels - 1)));
		}
		return b.build();
	}
}
