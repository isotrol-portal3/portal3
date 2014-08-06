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
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;
import com.isotrol.impe3.web20.api.FavoriteDTO;
import com.isotrol.impe3.web20.api.FavoriteFilterDTO;
import com.isotrol.impe3.web20.api.FavoritesService;
import com.isotrol.impe3.web20.api.MemberNotFoundException;
import com.isotrol.impe3.web20.mappers.FavoriteDTOMapper;
import com.isotrol.impe3.web20.model.FavoriteEntity;
import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.RelationshipEntity;
import com.isotrol.impe3.web20.server.CommunityManager;


/**
 * Favorites service implementation.
 * @author Emilio Escobar Reyero
 */
@Service("favoritesService")
public class FavoritesServiceImpl extends AbstractWeb20Service implements FavoritesService {

	/** Relationship component. */
	@Autowired
	private RelationshipComponent relationshipComponent;
	/** Group component. */
	@Autowired
	private GroupComponent groupComponent;

	/**
	 * @see com.isotrol.impe3.web20.api.FavoritesService#add(java.lang.String, com.isotrol.impe3.web20.api.FavoriteDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Long add(String serviceId, FavoriteDTO dto) throws ServiceException {
		final FavoriteEntity entity = new FavoriteEntity();
		fill(entity, dto);
		getDao().save(entity);

		return entity.getId();
	}

	/**
	 * @see com.isotrol.impe3.web20.api.FavoritesService#remove(java.lang.String, java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void remove(String serviceId, Long id) throws ServiceException {
		final FavoriteEntity entity = load(id);
		getDao().delete(entity);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.FavoritesService#getById(java.lang.String, java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public FavoriteDTO getById(String serviceId, Long id) throws ServiceException {
		FavoriteEntity entity = load(id);
		return FavoriteDTOMapper.INSTANCE.apply(entity);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.FavoritesService#getCommunityFavorites(java.lang.String,
	 * com.isotrol.impe3.web20.api.FavoriteFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<FavoriteDTO> getCommunityFavorites(String serviceId, FavoriteFilterDTO filter)
		throws ServiceException {
		if (filter == null || filter.getMember() == null) {
			throw new NotFoundException();
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
		
		return getDao().findFavorites(filter, FavoriteDTOMapper.INSTANCE);
	}

	private void fill(FavoriteEntity entity, FavoriteDTO dto) throws ServiceException {
		final Calendar date = Calendar.getInstance();
		date.setTime(dto.getDate());
		entity.setDate(date);
		entity.setDescription(dto.getDescription());

		final MemberEntity member = loadMember(dto.getMember());
		entity.setMember(member);

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

		final Long relationshipId = relationshipComponent.get(dto.getResource(), communityId, null);
		final RelationshipEntity relationship = getDao().findById(RelationshipEntity.class, relationshipId, false);

		entity.setRelationship(relationship);

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

	private FavoriteEntity load(Long id) throws ServiceException {
		if (id == null) {
			throw new NotFoundException();
		}

		final FavoriteEntity entity = getDao().findById(FavoriteEntity.class, id, false);
		if (entity == null) {
			throw new NotFoundException();
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
