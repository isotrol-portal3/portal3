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

package com.isotrol.impe3.pms.api.type;


import java.util.List;
import java.util.Set;

import com.isotrol.impe3.pms.api.AuthorizationException;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.InvalidImportFileException;
import com.isotrol.impe3.pms.api.PMSException;


/**
 * Module registry service.
 * @author Andres Rodriguez.
 */
public interface ContentTypesService {
	/**
	 * Returns all registered content types.
	 * @return All registered content types.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 */
	List<ContentTypeSelDTO> getContentTypes() throws PMSException;

	/**
	 * Gets the detail of a content type.
	 * @param id ID of the content type.
	 * @return The requested detail.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws EntityNotFoundException if the content type is not found.
	 */
	ContentTypeDTO get(String id) throws PMSException;

	/**
	 * Saves a content type. If the ID is null the operation is considered an insertion. Otherwise, it is considered an
	 * update.
	 * @param dto Object to save.
	 * @return The saved object.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws EntityNotFoundException if the content type is not found.
	 */
	ContentTypeDTO save(ContentTypeDTO dto) throws PMSException;

	/**
	 * Deletes a content type.
	 * @param id Id of the content type.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws ContentTypeNotFoundException if the content type is not found.
	 * @throws ContentTypeInUseException if the content type is in use and cannot be deleted.
	 */
	void delete(String id) throws PMSException;
	
	/**
	 * Export all content types.
	 * @return URL to download the exported file.
	 */
	String exportAll() throws PMSException;

	/**
	 * Export some content types.
	 * @param ids Set containing the ids of the content types to export.
	 * @return URL to download the exported file.
	 */
	String exportSome(Set<String> ids) throws PMSException;
	
	/**
	 * Import content types.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing types.
	 * @throws InvalidImportFileException if unable to parse the uploaded file.
	 */
	void importTypes(String fileId, boolean overwrite) throws PMSException;

}
