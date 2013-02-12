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

package com.isotrol.impe3.pms.api.portal;


import java.util.List;
import java.util.Set;

import com.isotrol.impe3.pms.api.AbstractWithStateAndId;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * Portal definition.
 * @author Andres Rodriguez
 */
public class PortalIATemplateDTO extends AbstractWithStateAndId {
	/** Serial UID. */
	private static final long serialVersionUID = 8607109527490530073L;
	/** Category mode. */
	private PortalCategoryMode mode;
	/** Root category. */
	private CategorySelDTO root;
	/** Categories. */
	private List<CategorySelDTO> categories;
	/** Possible categories. */
	private CategoryTreeDTO categoryTree;
	/** Inherited content types. */
	private Set<ContentTypeSelDTO> inheritedContentTypes;
	/** Content types. */
	private List<ContentTypeInPortalDTO> contentTypes;

	/** Default constructor. */
	public PortalIATemplateDTO() {
	}

	/**
	 * Returns the category mode.
	 * @return The category mode.
	 */
	public PortalCategoryMode getMode() {
		return mode;
	}

	/**
	 * Sets the category mode.
	 * @param name The category mode.
	 */
	public void setMode(PortalCategoryMode mode) {
		this.mode = mode;
	}

	/**
	 * Returns the root category.
	 * @return The root category.
	 */
	public CategorySelDTO getRoot() {
		return root;
	}

	/**
	 * Sets the root category.
	 * @param name The root category.
	 */
	public void setRoot(CategorySelDTO root) {
		this.root = root;
	}

	/**
	 * Returns the first level of categories.
	 * @return The first level of categories.
	 */
	public List<CategorySelDTO> getCategories() {
		return categories;
	}

	/**
	 * Sets the first level of categories.
	 * @param categories The first level of categories.
	 */
	public void setCategories(List<CategorySelDTO> categories) {
		this.categories = categories;
	}

	/**
	 * Returns the possible categories.
	 * @return The possible of categories.
	 */
	public CategoryTreeDTO getCategoryTree() {
		return categoryTree;
	}

	/**
	 * Sets the possible of categories.
	 * @param tree The possible of categories.
	 */
	public void setCategoryTree(CategoryTreeDTO tree) {
		this.categoryTree = tree;
	}

	/**
	 * Returns the inherited content types.
	 * @return The inherited content types.
	 */
	public Set<ContentTypeSelDTO> getInheritedContentTypes() {
		return inheritedContentTypes;
	}

	/**
	 * Sets the inherited content types.
	 * @param contentTypes The inherited content types.
	 */
	public void setInheritedContentTypes(Set<ContentTypeSelDTO> inheritedContentTypes) {
		this.inheritedContentTypes = inheritedContentTypes;
	}

	/**
	 * Returns the content types.
	 * @return The content types.
	 */
	public List<ContentTypeInPortalDTO> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Sets the content types.
	 * @param contentTypes The content types.
	 */
	public void setContentTypes(List<ContentTypeInPortalDTO> contentTypes) {
		this.contentTypes = contentTypes;
	}
}
