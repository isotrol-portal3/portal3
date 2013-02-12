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

package com.isotrol.impe3.pms.api.connector;


import java.util.List;
import java.util.Set;

import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.InvalidImportFileException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;


/**
 * Connectors service.
 * @author Andres Rodriguez.
 */
public interface ConnectorsService {
	/**
	 * Returns all registered connector modules.
	 * @return All registered connector modules.
	 */
	List<ConnectorModuleDTO> getConnectorModules() throws PMSException;

	/**
	 * Returns all registered connector instances.
	 * @return All registered connector instances.
	 */
	List<ModuleInstanceSelDTO> getConnectors() throws PMSException;

	/**
	 * Returns registered connector instances with a certain correctness status.
	 * @param correctness Correctness status.
	 * @return The requested connector instances.
	 */
	List<ModuleInstanceSelDTO> getConnectorsByCorrectness(Correctness correctness) throws PMSException;
	
	/**
	 * Creates a template for a new connector.
	 * @param key Module key (interface class name).
	 * @return A template.
	 * @throws PMSException If an error occurs.
	 */
	ModuleInstanceTemplateDTO newTemplate(String key) throws PMSException;

	/**
	 * Returns a template to modify an existing connector.
	 * @param id Connector id.
	 * @return A template.
	 * @throws PMSException If an error occurs.
	 */
	ModuleInstanceTemplateDTO get(String id) throws PMSException;

	/**
	 * Saves a connector. If the ID is null the operation is considered an insertion. Otherwise, it is considered an
	 * update.
	 * @param dto Object to save.
	 * @return The saved object.
	 * @throws PMSException If an error occurs.
	 */
	ModuleInstanceTemplateDTO save(ModuleInstanceDTO dto) throws PMSException;

	/**
	 * Deletes a connector.
	 * @param id Id of the connector.
	 * @throws ConnectorNotFoundException if the connector is not found.
	 * @throws ConnectorInUseException if the connector is in use and cannot be deleted.
	 */
	void delete(String id) throws PMSException;
	
	/**
	 * Export all connectors.
	 * @return URL to download the exported file.
	 */
	String exportAll() throws PMSException;

	/**
	 * Export some connectors.
	 * @param ids Set containing the ids of the connectors to export.
	 * @return URL to download the exported file.
	 */
	String exportSome(Set<String> ids) throws PMSException;
	
	/**
	 * Import connectors definitions.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing connectors.
	 * @throws InvalidImportFileException if unable to parse the uploaded file.
	 */
	void importConnectors(String fileId, boolean overwrite) throws PMSException;
}
