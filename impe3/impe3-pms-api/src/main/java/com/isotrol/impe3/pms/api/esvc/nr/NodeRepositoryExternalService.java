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

package com.isotrol.impe3.pms.api.esvc.nr;


import java.util.List;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.esvc.ExternalService;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * Node repository external service.
 * @author Emilio Escobar.
 */
public interface NodeRepositoryExternalService extends ExternalService {
	
	/**
	 * Recover node repository information by id
	 * @param repositoryId node repository id
	 * @return repository detail
	 * @throws PMSException if the requested service is not found.
	 */
	NodeRepositoryDTO getSummary(String repositoryId) throws PMSException;
	
	/**
	 * Gets list of nodes of current repository filtered by filter
	 * @param repositoryId repository id
	 * @param filter query filter
	 * @return list of nodes with extra information
	 * @throws PMSException
	 */
	ResultDTO<NodeDTO> getNodes(String repositoryId, NodesFilterDTO filter) throws PMSException;
	
	/**
	 * Recover content types for filters.
	 * @param repositoryId repository id (module)
	 * @return content types list
	 * @throws PMSException
	 */
	List<ContentTypeSelDTO> getContentTypes(String repositoryId) throws PMSException;
}
