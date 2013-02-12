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
import com.google.gwt.user.client.rpc.RemoteService;
import com.isotrol.impe3.pms.api.smap.SourceMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingSelDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingTemplateDTO;


/**
 * Users asynchronous service
 * @author Manuel Ruiz
 * 
 */
public interface ISourceMappingsServiceAsync extends RemoteService {
	/**
	 * Returns all registered source mappings.
	 * @param callback All registered source mappings.
	 */
	void getSourceMappings(AsyncCallback<List<SourceMappingSelDTO>> callback);

	/**
	 * Returns a template for a new source mapping.
	 * @param callback A template for a new source mapping.
	 */
	void newTemplate(AsyncCallback<SourceMappingTemplateDTO> callback);

	/**
	 * Gets the detail of a source mapping.
	 * @param id ID of the source mapping.
	 * @param callback The requested detail.
	 */
	void get(String id, AsyncCallback<SourceMappingTemplateDTO> callback);

	/**
	 * Saves a source mapping. If the ID is null the operation is considered an insertion. Otherwise, it is considered
	 * an update.
	 * @param dto Object to save.
	 * @param callback The saved object.
	 */
	void save(SourceMappingDTO dto, AsyncCallback<SourceMappingTemplateDTO> callback);

	/**
	 * Deletes a source mapping.
	 * @param callback id Id of the source mapping.
	 */
	void delete(String id, AsyncCallback<Void> callback);
	
	/**
	 * Export all mappings.
	 * @param callback URL to download the exported file.
	 */
	void exportAll(AsyncCallback<String> callabck);
	
	/**
	 * Export some mappings.
	 * @param ids Set containing the ids of the mappings to export.
	 * @param callback URL to download the exported file.
	 */
	void exportSome(Set<String> ids, AsyncCallback<String> callback);
	
	/**
	 * Import mapping definitions.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing mappings.
	 * @param callback
	 */
	void importMappings(String fileId, boolean overwrite, AsyncCallback<Void> callback);
	
	void exportSets(String id, AsyncCallback<String> callback);
	void exportCategories(String id, AsyncCallback<String> callback);
	void exportContentTypes(String id, AsyncCallback<String> callback);

	void importSets(String mappingId, String fileId, AsyncCallback<Void> callback);
	void importCategories(String mappingId, String fileId, AsyncCallback<Void> callback);
	void importContentTypes(String mappingId, String fileId, AsyncCallback<Void> callback);
}
