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

package com.isotrol.impe3.web20.api;


import java.util.List;
import java.util.Map;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;


/**
 * Comments service.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public interface CommentsService extends Web20Service {

	/**
	 * 
	 * @param serviceId
	 * @param filter
	 * @return
	 * @throws ServiceException
	 */
	PageDTO<CommentDTO> getResourceComments(String serviceId, CommentFilterDTO filter) throws ServiceException;

	/**
	 * Returns the most commented resources.
	 * @param serviceId Service Id.
	 * @param filter Search filter.
	 * @return The most commented resources.
	 * @throws ServiceException
	 */
	List<ResourceCounterDTO> getMostCommented(String serviceId, CommentCountFilterDTO filter) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param comment
	 * @return
	 * @throws ServiceException
	 */
	Long comment(String serviceId, CommentDTO comment) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param commentId
	 * @param moderator
	 * @param moderation
	 * @throws ServiceException
	 */
	void moderate(String serviceId, Long commentId, String moderator, boolean moderation) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param commentId
	 * @param memberId optional.
	 * @param origin
	 * @param value
	 * @param allowOwn
	 * @param maxRates
	 * @return 
	 * @throws ServiceException
	 */
	CommentRateDTO rate(String serviceId, Long commentId, String memberId, String origin, double value, boolean allowOwn, int maxRates) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	CommentDTO getById(String serviceId, Long id) throws ServiceException;

	/**
	 * 
	 * @param serviceId
	 * @param comment
	 * @throws ServiceException
	 */
	void update(String serviceId, CommentDTO comment) throws ServiceException;

	/**
	 * Mark as deleted.
	 * @param serviceId
	 * @param id
	 * @throws ServiceException
	 */
	void delete(String serviceId, Long id) throws ServiceException;

	/**
	 * Returns resources with unmoderatared comments (RWUC).
	 * @param serviceId Service Id.
	 * @param max Maximum number of results.
	 * @return A list with at most max resources with unmoderatared comments.
	 * @throws ServiceException
	 */
	List<ResourceByCommunityCounterDTO> getRWUC(String serviceId, int max) throws ServiceException;

	/**
	 * Returns resources with unmoderated comments (RWUC) in a community.
	 * @param serviceId Service Id.
	 * @param communityId Community Id.
	 * @param max Maximum number of results.
	 * @return The resources with unmoderatared comments in a the specified community..
	 * @throws ServiceException
	 */
	List<ResourceCounterDTO> getRWUCInCommunity(String serviceId, String communityId, int max) throws ServiceException;

	/**
	 * Returns map with resourceKey and number of comments in a community
	 * @param serviceId Service Id.
	 * @param resources Resource keys list.
	 * @param communityId Community Id. May be null.
	 * @return The map.
	 * @throws ServiceException
	 */
	Map<String, Integer> getNumberOfComments(String serviceId, List<String> resources, String communityId) throws ServiceException;
}
