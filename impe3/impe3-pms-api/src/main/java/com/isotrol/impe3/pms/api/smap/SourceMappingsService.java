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

package com.isotrol.impe3.pms.api.smap;


import java.util.List;
import java.util.Set;

import com.isotrol.impe3.pms.api.InvalidImportFileException;
import com.isotrol.impe3.pms.api.PMSException;


/**
 * Source mappings service.
 * @author Andres Rodriguez.
 */
public interface SourceMappingsService {
	/**
	 * Returns all registered source mappings.
	 * @return All registered source mappings.
	 */
	List<SourceMappingSelDTO> getSourceMappings() throws PMSException;

	/**
	 * Returns a template for a new source mapping.
	 * @return A template for a new source mapping.
	 */
	SourceMappingTemplateDTO newTemplate() throws PMSException;

	/**
	 * Gets the detail of a source mapping.
	 * @param id ID of the source mapping.
	 * @return The requested detail.
	 * @throws SourceMappingNotFoundException if the source mapping is not found.
	 * @throws PMSException If an error occurs.
	 */
	SourceMappingTemplateDTO get(String id) throws PMSException;

	/**
	 * Saves a source mapping. If the ID is null the operation is considered an insertion. Otherwise, it is considered
	 * an update.
	 * @param dto Object to save.
	 * @return The saved object.
	 * @throws SourceMappingNotFoundException if the source mapping is not found.
	 * @throws PMSException If an error occurs.
	 */
	SourceMappingTemplateDTO save(SourceMappingDTO dto) throws PMSException;

	/**
	 * Deletes a source mapping.
	 * @param id Id of the source mapping.
	 * @throws SourceMappingNotFoundException if the source mapping is not found.
	 * @throws PMSException If an error occurs.
	 */
	void delete(String id) throws PMSException;
	
	/**
	 * Export all mappings.
	 * @return URL to download the exported file.
	 */
	String exportAll() throws PMSException;
	
	/**
	 * Export some mappings.
	 * @param ids Set containing the ids of the mappings to export.
	 * @return URL to download the exported file.
	 */
	String exportSome(Set<String> ids) throws PMSException;
	
	/**
	 * Import mapping definitions.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing mappings.
	 * @throws InvalidImportFileException if unable to parse the uploaded file.
	 */
	void importMappings(String fileId, boolean overwrite) throws PMSException;
	
	String exportSets(String id) throws PMSException;
	String exportCategories(String id) throws PMSException;
	String exportContentTypes(String id) throws PMSException;

	void importSets(String mappingId, String fileId) throws PMSException;
	void importCategories(String mappingId, String fileId) throws PMSException;
	void importContentTypes(String mappingId, String fileId) throws PMSException;

	
}
