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

package com.isotrol.impe3.pms.api.category;


import com.isotrol.impe3.pms.api.InvalidImportFileException;
import com.isotrol.impe3.pms.api.PMSException;


/**
 * Categories service.
 * @author Andres Rodriguez.
 */
public interface CategoriesService {
	/**
	 * Returns all registered categories.
	 * @return All registered categories.
	 */
	CategoryTreeDTO getCategories() throws PMSException;

	/**
	 * Gets the detail of a category.
	 * @param id ID of the category.
	 * @return The requested detail.
	 * @throws PMSException If an error occurs.
	 */
	CategoryDTO get(String id) throws PMSException;

	/**
	 * Creates a category.
	 * @param dto Category to save.
	 * @param parent Parent category id. If {@code null} the root category will be used.
	 * @param order Order.
	 * @return The saved category.
	 * @throws PMSException If an error occurs.
	 */
	CategoryDTO create(CategoryDTO dto, String parentId, int order) throws PMSException;

	/**
	 * Updates a category.
	 * @param dto Category to update.
	 * @return The updated category.
	 * @throws PMSException If an error occurs.
	 */
	CategoryDTO update(CategoryDTO dto) throws PMSException;

	/**
	 * Moves a category.
	 * @param categoryId Category.
	 * @param parentId Parent category. If {@code null} the parent is unchanged.
	 * @param order Order.
	 * @return The updated category tree.
	 * @throws PMSException If an error occurs.
	 */
	CategoryTreeDTO move(String categoryId, String parentId, int order) throws PMSException;

	/**
	 * Deletes a category.
	 * @param id Id of the category.
	 * @return The resulting category tree.
	 * @throws CategoryNotFoundException if the category is not found.
	 * @throws CategoryInUseException if the category is in use and cannot be deleted.
	 */
	CategoryTreeDTO delete(String id) throws PMSException;

	/**
	 * Export a category branch.
	 * @param id Branch root id.
	 * @param exportRoot Whether to include the root category.
	 * @param allLevels Whether to include all descendant levels.
	 * @return URL to download the exported file.
	 */
	String exportBranch(String id, boolean exportRoot, boolean allLevels) throws PMSException;

	/**
	 * Import categories.
	 * @param id Destination category.
	 * @param fileId Uploaded file id.
	 * @param sameLevel Whether to import in the same level.
	 * @param overwrite Whether to overwrite existing types.
	 * @throws InvalidImportFileException if unable to parse the uploaded file.
	 */
	void importCategories(String id, String fileId, boolean sameLevel, boolean overwrite) throws PMSException;

}
