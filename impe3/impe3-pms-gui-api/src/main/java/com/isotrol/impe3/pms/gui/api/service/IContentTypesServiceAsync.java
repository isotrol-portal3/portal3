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

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;

/**
 * GWT-RPC service for content types.<br/> It is analog to {@link com.isotrol.impe3.pms.api.type.ContentTypesService}, 
 * but uses GWT-compatible types.
 * 
 * @author Andrei Cojocaru
 * @see com.isotrol.impe3.pms.api.type.ContentTypesService
 */
public interface IContentTypesServiceAsync {

	/**
	 * Returns all registered content types.
	 * @param callback
	 */
	void getContentTypes(AsyncCallback<List<ContentTypeSelDTO>> callback);

	/**
	 * Gets the detail of a content type.
	 * @param id ID of the content type.
	 * @param callback
	 */
	void get(String id, AsyncCallback<ContentTypeDTO> callback);
	
	/**
	 * Saves a content type. If the ID is null the operation is considered an insertion. 
	 * Otherwise, it is considered an update.
	 * @param model
	 * @param callback
	 */
	void save(ContentTypeDTO model, AsyncCallback<ContentTypeDTO> callback);
	
	/**
	 * Deletes a content type.
	 * @param id Id of the content type.
	 * @param callback
	 */
	void delete(String id, AsyncCallback<Void> callback);
	
	/**
	 * Export all content types.
	 * @param callback URL to download the exported file.
	 */
	void exportAll(AsyncCallback<String> callback);

	/**
	 * Export some content types.
	 * @param ids Set containing the ids of the content types to export.
	 * @param callback URL to download the exported file.
	 */
	void exportSome(Set<String> ids, AsyncCallback<String> callback);
	
	/**
	 * Import content types.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing types.
	 * @param callback
	 */
	void importTypes(String fileId, boolean overwrite, AsyncCallback<Void> callback);

}
