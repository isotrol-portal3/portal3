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

package com.isotrol.impe3.pms.core.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.pms.api.esvc.web20.CommentsExternalService;
import com.isotrol.impe3.web20.api.CommentCountFilterDTO;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentFilterDTO;
import com.isotrol.impe3.web20.api.CommentRateDTO;
import com.isotrol.impe3.web20.api.CommentsService;
import com.isotrol.impe3.web20.api.ResourceByCommunityCounterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;


/**
 * Implementation of CommentsExternalService
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
@Service("commentsExternalService")
public class CommentsExternalServiceImpl extends AbstractExternalService<CommentsService> implements
	CommentsExternalService {

	/** Default constructor. */
	public CommentsExternalServiceImpl() {
		super(CommentsService.class);
	}

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Integer> getNumberOfComments(String serviceId, List<String> resources, String communityId)
		throws ServiceException {
		return getExternalService(serviceId).getNumberOfComments(null, resources, communityId);
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<CommentDTO> getResourceComments(String serviceId, CommentFilterDTO filter) throws ServiceException {
		return getExternalService(serviceId).getResourceComments(null, filter);
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceCounterDTO> getMostCommented(String serviceId, CommentCountFilterDTO filter)
		throws ServiceException {
		return getExternalService(serviceId).getMostCommented(null, filter);
	}

	@Transactional(rollbackFor = Throwable.class)
	public Long comment(String serviceId, CommentDTO comment) throws ServiceException {
		return getExternalService(serviceId).comment(null, comment);
	}

	@Transactional(rollbackFor = Throwable.class)
	public void moderate(String serviceId, Long commentId, String moderator, boolean moderation)
		throws ServiceException {
		getExternalService(serviceId).moderate(null, commentId, moderator, moderation);
	}

	@Transactional(rollbackFor = Throwable.class)
	public CommentRateDTO rate(String serviceId, Long commentId, String memberId, String origin, double value, boolean allowOwn, int maxRates) throws ServiceException {
		return getExternalService(serviceId).rate(null, commentId, memberId, origin, value, allowOwn, maxRates);
	}

	@Transactional(rollbackFor = Throwable.class)
	public CommentDTO getById(String serviceId, Long id) throws ServiceException {
		return getExternalService(serviceId).getById(null, id);
	}

	@Transactional(rollbackFor = Throwable.class)
	public void update(String serviceId, CommentDTO comment) throws ServiceException {
		getExternalService(serviceId).update(null, comment);
	}

	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, Long id) throws ServiceException {
		getExternalService(serviceId).delete(null, id);
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceByCommunityCounterDTO> getRWUC(String serviceId, int max) throws ServiceException {
		return getExternalService(serviceId).getRWUC(null, max);
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceCounterDTO> getRWUCInCommunity(String serviceId, String communityId, int max)
		throws ServiceException {
		return getExternalService(serviceId).getRWUCInCommunity(null, communityId, max);
	}

}
