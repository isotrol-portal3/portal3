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

package com.isotrol.impe3.pms.gui.api.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;

/**
 * GWT-RPC service for Categories. It is analog to {@link com.isotrol.impe3.pms.api.CategoryService}, but uses
 * GWT-compatible types.
 * 
 * @author Andrei Cojocaru
 * 
 */
public interface ICategoriesServiceAsync extends RemoteService {

	/**
	 * Returns all registered categories.
	 * @param callback callback for processing the retrieved categories
	 */
	void getCategories(AsyncCallback<CategoryTreeDTO> callback);
	
	/**
	 * Gets the detail of a category.
	 * @param id Category ID
	 * @param callback callback which processes the requested detail.
	 */
	void get(String id, AsyncCallback<CategoryDTO> callback);
	
	/**
	 * Creates a category.<br/>
	 * @param category Category to save.
	 * @param parentId Parent category ID.
	 * @param order Order.
	 * @param callback Callback for processing the saved category.
	 */
	void create(CategoryDTO category, String parentId, int order, AsyncCallback<CategoryDTO> callback);
	
	/**
	 * Updates a category.<br/>
	 * @param model Category to update.
	 * @param callback Callback for processing the updated category.
	 */
	void update(CategoryDTO model,AsyncCallback<CategoryDTO>callback);
	
	/**
	 * Moves a category.<br/>
	 * @param categoryId Category.
	 * @param parentId Parent category. If {@code null} the parent is unchanged.
	 * @param order Order.
	 * @param callback Callback for processing the updated category tree.
	 */
	void move(String categoryId, String parentId, int order,AsyncCallback<CategoryTreeDTO>callback);
	
	/**
	 * @param id ID of the category.
	 * @param callback Callback for processing the resulting category tree.
	 */
	void delete(String id, AsyncCallback<CategoryTreeDTO> callback);
	
	/**
	 * Export a category branch.
	 * @param id Branch root id.
	 * @param exportRoot Whether to include the root category.
	 * @param allLevels Whether to include all descendant levels.
	 * @param callback URL to download the exported file.
	 */
	void exportBranch(String id, boolean exportRoot, boolean allLevels, AsyncCallback<String> callback);

	/**
	 * Import content types.
	 * @param id Destination category.
	 * @param fileId Uploaded file id.
	 * @param sameLevel Whether to import in the same level.
	 * @param overwrite Whether to overwrite existing types.
	 * @param callback
	 */
	void importCategories(String id, String fileId, boolean sameLevel, boolean overwrite, AsyncCallback<Void> callback);

}
