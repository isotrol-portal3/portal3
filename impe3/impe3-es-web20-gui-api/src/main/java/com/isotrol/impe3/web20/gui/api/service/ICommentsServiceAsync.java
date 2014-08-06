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
package com.isotrol.impe3.web20.gui.api.service;


import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.web20.api.CommentCountFilterDTO;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentFilterDTO;
import com.isotrol.impe3.web20.api.CommentRateDTO;
import com.isotrol.impe3.web20.api.ResourceByCommunityCounterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;


/**
 * @author Manuel Ruiz
 * 
 */
public interface ICommentsServiceAsync {

	/**
	 * Returns the most commented resources.
	 * @param serviceId Service Id.
	 * @param filter Search filter.
	 * @return The most commented resources.
	 */
	void getMostCommented(String serviceId, CommentCountFilterDTO filter, AsyncCallback<List<ResourceCounterDTO>> callback);

	/**
	 * 
	 * @param serviceId
	 * @param filter
	 */
	void getResourceComments(String serviceId, CommentFilterDTO filter, AsyncCallback<PageDTO<CommentDTO>> callback);

	/**
	 * 
	 * @param serviceId
	 * @param comment
	 */
	void comment(String serviceId, CommentDTO comment, AsyncCallback<Long> callback);

	/**
	 * 
	 * @param serviceId
	 * @param commentId
	 * @param moderator
	 * @param moderation
	 */
	void moderate(String serviceId, Long commentId, String moderator, boolean moderation, AsyncCallback<Void> callback);

	/**
	 * @param serviceId
	 * @param commentId
	 * @param memberId
	 * @param origin
	 * @param value
	 * @param allowOwn
	 * @param maxRates
	 * @param callback
	 */
	void rate(String serviceId, Long commentId, String memberId, String origin, double value, boolean allowOwn, int maxRates, AsyncCallback<CommentRateDTO> callback);

	/**
	 * 
	 * @param serviceId
	 * @param id
	 */
	void getById(String serviceId, Long id, AsyncCallback<CommentDTO> callback);

	/**
	 * 
	 * @param serviceId
	 * @param comment
	 */
	void update(String serviceId, CommentDTO comment, AsyncCallback<Void> callback);

	/**
	 * Mark as deleted.
	 * @param serviceId
	 * @param id
	 */
	void delete(String serviceId, Long id, AsyncCallback<Void> callback);
	
	/**
	 * 
	 * @param serviceId
	 * @param max
	 * @return
	 */
	void getRWUC(String serviceId, int max, AsyncCallback<List<ResourceByCommunityCounterDTO>> callback);
	
	/**
	 * 
	 * @param serviceId
	 * @param communityId
	 * @param max
	 * @return
	 */
	void getRWUCInCommunity(String serviceId, String communityId, int max, AsyncCallback<List<ResourceCounterDTO>> callback);
	
	/**
	 * Returns map with resourceKey and number of comments in a community
	 * @param serviceId Service Id.
	 * @param resources Resource keys list.
	 * @param communityId Community Id. May be null.
	 * @param callback The map.
	 */
	void getNumberOfComments(String serviceId, List<String> resources, String communityId, AsyncCallback<Map<String, Integer>> callback);

}
