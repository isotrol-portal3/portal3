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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.api.service.external;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.esvc.nr.NodeDTO;
import com.isotrol.impe3.pms.api.esvc.nr.NodeRepositoryDTO;
import com.isotrol.impe3.pms.api.esvc.nr.NodesFilterDTO;
import com.isotrol.impe3.pms.api.esvc.nr.ResultDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;

/**
 * Async service for {@link INodeRepositoryExternalService}.
 * Synchronous operations are specified in interface 
 * {@link com.isotrol.impe3.pms.api.esvc.nr.NodeRepositoryExternalService NodeRepositoryExternalService}.
 * 
 * @author Andrei Cojocaru
 *
 */
public interface INodeRepositoryExternalServiceAsync extends IExternalServiceAsync {
	/**
	 * Returns node repository information by ID.<br/>
	 * @param repositoryId node repository id
	 * @param callback callback to process the repository summary info
	 */
	void getSummary(String repositoryId, AsyncCallback<NodeRepositoryDTO> callback);
	
	/**
	 * Returns the list of nodes of current repository filtered by the passed filter.<br/>
	 * @param repositoryId repository id
	 * @param filter query filter
	 * @param callback callback to process the list of nodes with extra information
	 */
	void  getNodes(String repositoryId, NodesFilterDTO filter, AsyncCallback<ResultDTO<NodeDTO>> callback);
	
	/**
	 * Returns a list of Content Types for filters.<br/>
	 * @param repositoryId repository id (module)
	 * @param callback callback to process the Content Types list
	 */
	void getContentTypes(String repositoryId, AsyncCallback<List<ContentTypeSelDTO>> callback);
	
}
