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
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;


/**
 * GWT-RPC service for Connectors. It is analog to {@link com.isotrol.impe3.pms.api.connector.ConnectorsService}, but
 * uses GWT-compatible types.
 * 
 * @author Manuel Ruiz
 * 
 */
public interface IConnectorsServiceAsync extends RemoteService {

	/**
	 * Returns all registered connector modules.
	 * @param callback
	 */
	void getConnectorModules(AsyncCallback<List<ConnectorModuleDTO>> callback);

	/**
	 * Returns all registered connector instances.
	 * @param callback
	 */
	void getConnectors(AsyncCallback<List<ModuleInstanceSelDTO>> callback);
	
	/**
	 * Returns registered connector instances with a certain correctness status.
	 * @param correctness Correctness status.
	 * @param callback The requested connector instances.
	 */
	void getConnectorsByCorrectness(Correctness correctness, AsyncCallback<List<ModuleInstanceSelDTO>> callback);

	/**
	 * Creates a template for a new connector.
	 * @param key Module key (interface class name).
	 * @param callback
	 */
	void newTemplate(String key, AsyncCallback<ModuleInstanceTemplateDTO> callback);

	/**
	 * Returns a template to modify an existing connector.
	 * @param id Connector id.
	 * @param callback
	 */
	void get(String id, AsyncCallback<ModuleInstanceTemplateDTO> callback);

	/**
	 * Saves a connector. If the ID is null the operation is considered an insertion. Otherwise, it is considered an
	 * update.
	 * @param dto Object to save.
	 * @param callback
	 */
	void save(ModuleInstanceDTO dto, AsyncCallback<ModuleInstanceTemplateDTO> callback);

	/**
	 * Deletes a connector.
	 * @param id Id of the connector.
	 */
	void delete(String id, AsyncCallback<Void> callback);
	
	/**
	 * Export all connectors.
	 * @param callback URL to download the exported file.
	 */
	void exportAll(AsyncCallback<String> callback);

	/**
	 * Export some connectors.
	 * @param ids Set containing the ids of the connectors to export.
	 * @param callback URL to download the exported file.
	 */
	void exportSome(Set<String> ids, AsyncCallback<String> callback);
	
	/**
	 * Import connectors definitions.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing connectors.
	 * @param callback
	 */
	void importConnectors(String fileId, boolean overwrite, AsyncCallback<Void> callback);
}
