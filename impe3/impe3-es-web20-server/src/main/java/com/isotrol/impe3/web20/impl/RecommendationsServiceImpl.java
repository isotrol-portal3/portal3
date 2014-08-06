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
package com.isotrol.impe3.web20.impl;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.dto.NotFoundException;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilterDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;
import com.isotrol.impe3.web20.api.MemberNotFoundException;
import com.isotrol.impe3.web20.api.RecommendationDTO;
import com.isotrol.impe3.web20.api.RecommendationFilterDTO;
import com.isotrol.impe3.web20.api.RecommendationsService;
import com.isotrol.impe3.web20.mappers.RecommendationDTOMapper;
import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.RecommendationEntity;
import com.isotrol.impe3.web20.model.RelationshipEntity;
import com.isotrol.impe3.web20.server.CommunityManager;

@Service("recommendationsService")
public class RecommendationsServiceImpl extends AbstractWeb20Service implements RecommendationsService {

	/** Relationships */
	@Autowired
	private RelationshipComponent relationshipComponent;
	/** Group component. */
	@Autowired
	private GroupComponent groupComponent;
	
	/**
	 * @see com.isotrol.impe3.web20.api.RecommendationsService#getRecommendedResources(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<RecommendationDTO> getRecommendedResources(String serviceId, String member, String community, PageFilterDTO pagination)
		throws ServiceException {
		
		if (member == null) {
			throw new NotFoundException();
		}
		
		if (pagination == null) {
			pagination = new PageFilterDTO();
		}
		
		final RecommendationFilterDTO filter = new RecommendationFilterDTO();
		
		filter.setOrderings(pagination.getOrderings());
		filter.setPagination(pagination.getPagination());
		
		filter.setRecommended(member);
		
		if (community == null) {
			filter.setCommunity(CommunityManager.GLOBAL_STR_ID);
		} else {
			filter.setCommunity(community);
		}

		UUID communityId = null;

		try {
			communityId = UUID.fromString(filter.getCommunity());
		} catch (IllegalArgumentException e) {
			throw new CommunityNotFoundException(filter.getCommunity());
		}

		final Long group = groupComponent.get(communityId, null);
		if (group == null) {
			throw new CommunityNotFoundException(filter.getCommunity());
		}
		
		filter.setGroup(group);
		
		return getDao().findRecommendations(filter, RecommendationDTOMapper.INSTANCE);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RecommendationsService#recommendResource(java.lang.String, com.isotrol.impe3.web20.api.RecommendationDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Long recommendResource(String serviceId, RecommendationDTO recommendation) throws ServiceException {
		
		final RecommendationEntity entity = new RecommendationEntity();
		fill(entity, recommendation);
		getDao().save(entity);

		return entity.getId();
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RecommendationsService#removeRecommendation(java.lang.String, java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void removeRecommendation(String serviceId, Long recommendation) throws ServiceException {
		final RecommendationEntity entity = load(recommendation);
		getDao().delete(entity);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RecommendationsService#search(java.lang.String, com.isotrol.impe3.web20.api.RecommendationFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<RecommendationDTO> search(String serviceId, RecommendationFilterDTO filter) throws ServiceException {
		if (filter == null) {
			filter = new RecommendationFilterDTO();
		}
	
		if (filter.getCommunity() == null) {
			filter.setCommunity(CommunityManager.GLOBAL_STR_ID);
		}

		UUID communityId = null;

		try {
			communityId = UUID.fromString(filter.getCommunity());
		} catch (IllegalArgumentException e) {
			throw new CommunityNotFoundException(filter.getCommunity());
		}

		final Long group = groupComponent.get(communityId, null);
		if (group == null) {
			throw new CommunityNotFoundException(filter.getCommunity());
		}
		
		filter.setGroup(group);
		
		return getDao().findRecommendations(filter, RecommendationDTOMapper.INSTANCE);
	}
	
	private void fill(RecommendationEntity entity, RecommendationDTO dto) throws ServiceException {
		final Calendar date = Calendar.getInstance();
		date.setTime(dto.getDate());
		entity.setDate(date);
		
		entity.setDescription(dto.getDescription());
		
		UUID communityId = null;

		if (dto.getCommunity() == null) {
			communityId = CommunityManager.GLOBAL_ID;
		} else {
			try {
				communityId = UUID.fromString(dto.getCommunity());
			} catch (IllegalArgumentException e) {
				throw new CommunityNotFoundException(dto.getCommunity());
			}
		}
		
		final Long relationId = relationshipComponent.get(dto.getResource(), communityId, null);
		final RelationshipEntity relationship = getDao().findById(RelationshipEntity.class, relationId, false);
		entity.setRelationship(relationship);
		
		final MemberEntity recommender = loadMember(dto.getRecommender());
		final MemberEntity recommended = loadMember(dto.getRecommended());
		
		entity.setRecommender(recommender);
		entity.setRecommended(recommended);
		
	}

	private RecommendationEntity load(Long id) throws ServiceException {
		if (id == null) {
			throw new NotFoundException();
		}
		
		final RecommendationEntity entity = getDao().findById(RecommendationEntity.class, id, false);
		
		if (entity == null) {
			throw new NotFoundException();
		}

		return entity;
	}
	
	private MemberEntity loadMember(String id) throws MemberNotFoundException {
		if (id == null) {
			throw new MemberNotFoundException(id);
		}
		final UUID uuid;
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new MemberNotFoundException(id);
		}
		final MemberEntity entity = getDao().findById(MemberEntity.class, uuid, false);
		if (entity == null || entity.isDeleted()) {
			throw new MemberNotFoundException(id);
		}

		return entity;
	}
	
	public void setRelationshipComponent(RelationshipComponent relationshipComponent) {
		this.relationshipComponent = relationshipComponent;
	}
	
	public void setGroupComponent(GroupComponent groupComponent) {
		this.groupComponent = groupComponent;
	}
}
