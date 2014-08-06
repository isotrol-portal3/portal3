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
package com.isotrol.impe3.web20.client.connector;

import java.util.List;
import java.util.Map;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommentCountFilterDTO;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentFilterDTO;
import com.isotrol.impe3.web20.api.CommentRateDTO;
import com.isotrol.impe3.web20.api.CommentsService;
import com.isotrol.impe3.web20.api.ResourceByCommunityCounterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;

/**
 * 
 * @author Emilio Escobar Reyero
 */
public class CommentsRemoteServiceImpl extends WithHessian<CommentsService> implements CommentsService  {

	@SuppressWarnings("unchecked")
	@Override
	protected Class serviceClass() {
		return CommentsService.class;
	}
	
	@Override
	protected String serviceUrl() {
		return server() + "/comments";
	}
	
	public List<ResourceCounterDTO> getMostCommented(String serviceId, CommentCountFilterDTO filter)
		throws ServiceException {
		return delegate().getMostCommented(serviceId, filter);
	}

	public Long comment(String serviceId, CommentDTO comment) throws ServiceException {
		return delegate().comment(serviceId, comment);
	}
	
	public void delete(String serviceId, Long id) throws ServiceException {
		delegate().delete(serviceId, id);
	}
	
	public CommentDTO getById(String serviceId, Long id) throws ServiceException {
		return delegate().getById(serviceId, id);
	}
	
	public PageDTO<CommentDTO> getResourceComments(String serviceId, CommentFilterDTO filter) throws ServiceException {
		return delegate().getResourceComments(serviceId, filter);
	}
	
	public Map<String, Integer> getNumberOfComments(String serviceId, List<String> resources, String communityId)
		throws ServiceException {
		return delegate().getNumberOfComments(serviceId, resources, communityId);
	}
	
	public void moderate(String serviceId, Long commentId, String moderator, boolean moderation)
		throws ServiceException {
		delegate().moderate(serviceId, commentId, moderator, moderation);
	}
	
	public CommentRateDTO rate(String serviceId, Long commentId, String memberId, String origin, double value, boolean allowOwn, int maxRates) throws ServiceException {
		return delegate().rate(serviceId, commentId, memberId, origin, value, allowOwn, maxRates);
	}
	
	public void update(String serviceId, CommentDTO comment) throws ServiceException {
		delegate().update(serviceId, comment);
	}
	
	public List<ResourceByCommunityCounterDTO> getRWUC(String serviceId, int max) throws ServiceException {
		return delegate().getRWUC(serviceId, max);
	}
	
	public List<ResourceCounterDTO> getRWUCInCommunity(String serviceId, String communityId, int max)
		throws ServiceException {
		return delegate().getRWUCInCommunity(serviceId, communityId, max);
	}
	
	
}
