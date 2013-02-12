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

package com.isotrol.impe3.pms.gui.api.service.external;


import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceDTO;
import com.isotrol.impe3.web20.api.CommentCountFilterDTO;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentFilterDTO;
import com.isotrol.impe3.web20.api.CommentRateDTO;
import com.isotrol.impe3.web20.api.ResourceByCommunityCounterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;


/**
 * Comments external asynchronous service
 * @author Manuel Ruiz
 * 
 */
public interface ICommentsExternalServiceAsync {

	/**
	 * Returns the identification data of the service.
	 * @param id External Service Id.
	 * @param callback The identification data of the service.
	 */
	void getService(String id, AsyncCallback<ExternalServiceDTO> callback);

	/**
	 * 
	 * @param serviceId
	 * @param filter
	 * @param callback
	 */
	void getResourceComments(String serviceId, CommentFilterDTO filter, AsyncCallback<PageDTO<CommentDTO>> callback);

	/**
	 * Returns the most commented resources.
	 * @param serviceId Service Id.
	 * @param filter Search filter.
	 * @return callback The most commented resources.
	 */
	void getMostCommented(String serviceId, CommentCountFilterDTO filter,
		AsyncCallback<List<ResourceCounterDTO>> callback);

	/**
	 * 
	 * @param serviceId
	 * @param comment
	 * @param callback
	 */
	void comment(String serviceId, CommentDTO comment, AsyncCallback<Long> callback);

	/**
	 * 
	 * @param serviceId
	 * @param commentId
	 * @param moderator
	 * @param moderation
	 * @param callback
	 */
	void moderate(String serviceId, Long commentId, String moderator, boolean moderation, AsyncCallback<Void> callback);

	/**
	 * 
	 * @param serviceId
	 * @param commentId
	 * @param memberId
	 * @param origin
	 * @param value
	 * @param allowOwn
	 * @param maxRates
	 * @param callback
	 */
	void rate(String serviceId, Long commentId, String memberId, String origin, double value, boolean allowOwn,
		int maxRates, AsyncCallback<CommentRateDTO> callback);

	/**
	 * 
	 * @param serviceId
	 * @param id
	 * @param callback
	 */
	void getById(String serviceId, Long id, AsyncCallback<CommentDTO> callback);

	/**
	 * 
	 * @param serviceId
	 * @param comment
	 * @param callback
	 */
	void update(String serviceId, CommentDTO comment, AsyncCallback<Void> callback);

	/**
	 * Mark as deleted.
	 * @param serviceId
	 * @param id
	 * @param callback
	 */
	void delete(String serviceId, Long id, AsyncCallback<Void> callback);

	/**
	 * Returns resources with unmoderatared comments (RWUC).
	 * @param serviceId Service Id.
	 * @param max Maximum number of results.
	 * @param callback A list with at most max resources with unmoderatared comments.
	 */
	void getRWUC(String serviceId, int max, AsyncCallback<List<ResourceByCommunityCounterDTO>> callback);

	/**
	 * Returns resources with unmoderated comments (RWUC) in a community.
	 * @param serviceId Service Id.
	 * @param communityId Community Id.
	 * @param max Maximum number of results.
	 * @param callback The resources with unmoderatared comments in a the specified community.
	 */
	void getRWUCInCommunity(String serviceId, String communityId, int max,
		AsyncCallback<List<ResourceCounterDTO>> callback);
	
	void getNumberOfComments(String serviceId, List<String> resources, String communityId, AsyncCallback<Map<String, Integer>> callback);
}
