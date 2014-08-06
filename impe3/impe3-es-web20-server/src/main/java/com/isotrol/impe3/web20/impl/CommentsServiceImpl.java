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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommentCountFilterDTO;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentFilterDTO;
import com.isotrol.impe3.web20.api.CommentNotFoundException;
import com.isotrol.impe3.web20.api.CommentRateDTO;
import com.isotrol.impe3.web20.api.CommentsService;
import com.isotrol.impe3.web20.api.CommunityCounterDTO;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;
import com.isotrol.impe3.web20.api.MaxCommentRatesException;
import com.isotrol.impe3.web20.api.MemberNotFoundException;
import com.isotrol.impe3.web20.api.NotAllowOwnerCommentRateException;
import com.isotrol.impe3.web20.api.ResourceByCommunityCounterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;
import com.isotrol.impe3.web20.mappers.CommentDTOMapper;
import com.isotrol.impe3.web20.model.CommentEntity;
import com.isotrol.impe3.web20.model.CommentModerationEntity;
import com.isotrol.impe3.web20.model.CommentRateEntity;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.RelationshipEntity;
import com.isotrol.impe3.web20.model.SourceEntity;
import com.isotrol.impe3.web20.server.CommentInternalFilterDTO;
import com.isotrol.impe3.web20.server.CommentManager;
import com.isotrol.impe3.web20.server.CommentMap;
import com.isotrol.impe3.web20.server.CommunityManager;
import com.isotrol.impe3.web20.server.SourceKey;
import com.isotrol.impe3.web20.server.TimeMapConfig;


/**
 * Implementation of CommentsService.
 * @author Emilio Escobar Reyero.
 */
@Service("commentsService")
public class CommentsServiceImpl extends AbstractWeb20Service implements CommentsService {
	/** Relationship component. */
	@Autowired
	private RelationshipComponent relationshipComponent;
	/** Source component. */
	@Autowired
	private SourceComponent sourceComponent;
	/** Comment map. */
	private final TimeMap<CommentMap> maps;

	@Autowired
	public CommentsServiceImpl(CommentManager commentManager, SchedulerComponent scheduler, TimeMapConfig config) {
		this.maps = TimeMap.create(config, scheduler, commentManager);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#getMostCommented(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommentCountFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceCounterDTO> getMostCommented(String serviceId, CommentCountFilterDTO filter)
		throws ServiceException {
		if (filter == null) {
			filter = new CommentCountFilterDTO();
		}
		String communityId = filter.getCommunityId();
		UUID community = CommunityManager.GLOBAL_ID;
		if (communityId != null) {
			try {
				community = UUID.fromString(communityId);
			} catch (IllegalArgumentException e) {
				throw new CommunityNotFoundException(communityId);
			}
		}
		return maps.apply(filter.getTime()).get(community, filter.isValid(), filter.isModerated(), filter.getMax());
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#comment(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommentDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Long comment(String serviceId, CommentDTO comment) throws ServiceException {

		final CommentEntity entity = new CommentEntity();
		fill(entity, comment);
		getDao().save(entity);

		return entity.getId();
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#getById(java.lang.String, java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public CommentDTO getById(String serviceId, Long id) throws ServiceException {
		final CommentEntity entity = load(id);

		if (entity != null) {
			return CommentDTOMapper.INSTANCE.apply(entity);
		}

		return null;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#getResourceComments(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommentFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<CommentDTO> getResourceComments(String serviceId, CommentFilterDTO filter) throws ServiceException {
		final CommentInternalFilterDTO ifilter;

		if (filter == null) {
			ifilter = CommentInternalFilterDTO.empty();
		} else if (filter.getResourceKey() != null) {
			final String communityId = filter.getCommunityId();
			final UUID community = getCommunityUUID(communityId);
			final long relationship = relationshipComponent.get(filter.getResourceKey(), community, null);
			ifilter = CommentInternalFilterDTO.of(filter, relationship);
		} else {
			ifilter = CommentInternalFilterDTO.of(filter);
		}

		return getDao().findComments(ifilter, CommentDTOMapper.INSTANCE);
	}

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Integer> getNumberOfComments(String serviceId, List<String> resources, String communityId)
		throws ServiceException {
		if (resources == null || resources.isEmpty()) {
			return Maps.newHashMapWithExpectedSize(0);
		}
		final Map<String, Integer> map = Maps.newHashMapWithExpectedSize(resources.size());

		final UUID community = getCommunityUUID(communityId);

		for (String resourceKey : resources) {
			final long relationship = relationshipComponent.get(resourceKey, community, null);
			final int comments = getDao().numberOfComments(relationship);
			map.put(resourceKey, comments);
		}

		return map;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#moderate(java.lang.String, java.lang.Long, java.lang.String,
	 * boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void moderate(String serviceId, Long commentId, String moderator, boolean moderation)
		throws ServiceException {
		final Calendar date = Calendar.getInstance();
		final CommentEntity entity = load(commentId);

		entity.setValid(moderation);
		entity.setLastModeration(date);

		final CommentModerationEntity audit = new CommentModerationEntity();
		audit.setComment(entity);
		audit.setDate(date);
		audit.setModerated(moderation);
		audit.setModerator(moderator);

		getDao().save(audit);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#delete(java.lang.String, java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, Long id) throws ServiceException {
		final CommentEntity entity = load(id);
		entity.setDeleted(true);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#rate(java.lang.String, com.isotrol.impe3.web20.api.CommentDTO,
	 * double, boolean, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public CommentRateDTO rate(String serviceId, Long commentId, String memberId, String origin, double value,
		boolean allowOwn, int maxRates) throws ServiceException {
		final Calendar date = Calendar.getInstance();
		final CommentEntity entity = load(commentId);

		final SourceKey sourceKey = memberId != null ? SourceKey.member(getMemberUUID(memberId)) : SourceKey
			.origin(origin);
		final long ratingSourceId = sourceComponent.get(sourceKey);

		if (maxRates > 0 && (allowOwn || !sameSource(entity.getSource(), memberId, origin))) {

			final int sourceRates = getDao().countRatesByCommentAndSource(entity.getId(), ratingSourceId);

			if (sourceRates < maxRates) {
				final double actualRate = entity.getRate();
				final int numberOfRates = entity.getNumberOfRates();

				final double rate = actualRate + value;

				entity.setRate(rate);
				entity.setNumberOfRates(numberOfRates + 1);

				final CommentRateEntity rateEntity = new CommentRateEntity();
				rateEntity.setComment(entity);
				rateEntity.setDate(date);
				rateEntity.setRate(rate);

				final SourceEntity ratingSource = findById(SourceEntity.class, ratingSourceId);

				rateEntity.setSource(ratingSource);

				getDao().save(rateEntity);

				final CommentRateDTO dto = new CommentRateDTO();
				dto.setCommentId(commentId);
				dto.setRate(rate);
				dto.setNumberOfRates(numberOfRates + 1);

				return dto;
			} else {
				throw new MaxCommentRatesException();
			}
		} else {
			throw new NotAllowOwnerCommentRateException();
		}
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#update(java.lang.String, com.isotrol.impe3.web20.api.CommentDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void update(String serviceId, CommentDTO comment) throws ServiceException {
		Preconditions.checkNotNull(comment);
		final Long id = comment.getId();
		Preconditions.checkNotNull(id);
		CommentEntity entity = load(id);
		fill(entity, comment);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#getRWUC(java.lang.String, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceByCommunityCounterDTO> getRWUC(String serviceId, int max) throws ServiceException {
		final Map<String, ResourceByCommunityCounterDTO> map = Maps.newHashMap();
		for (Object[] a : getDao().getRWUC(max)) {
			final String res = (String) a[0];
			final ResourceByCommunityCounterDTO rc;
			if (map.containsKey(res)) {
				rc = map.get(res);
			} else {
				rc = new ResourceByCommunityCounterDTO(res);
				map.put(res, rc);
			}
			final CommunityEntity c = (CommunityEntity) a[1];
			final CommunityCounterDTO cc = new CommunityCounterDTO(c.getId().toString(), c.getName(),
				((Number) a[2]).longValue());
			rc.addCommunity(cc);
		}
		return Lists.newArrayList(map.values());
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommentsService#getRWUCInCommunity(java.lang.String, java.lang.String, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceCounterDTO> getRWUCInCommunity(String serviceId, String communityId, int max)
		throws ServiceException {
		// Check community
		final UUID cid = getCommunityUUID(communityId);
		final CommunityEntity e = getDao().findById(CommunityEntity.class, cid, false);
		if (e == null) {
			throw new CommunityNotFoundException(communityId);
		}
		final List<Object[]> result = getDao().getRWUCInCommunity(cid, max);
		final List<ResourceCounterDTO> list = Lists.newArrayListWithCapacity(result.size());
		for (Object[] a : result) {
			list.add(new ResourceCounterDTO((String) a[0], ((Number) a[1]).longValue()));
		}
		return list;
	}

	private UUID getCommunityUUID(String id) throws ServiceException {
		if (id == null) {
			return CommunityManager.GLOBAL_ID;
		}
		try {
			return UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new CommunityNotFoundException(id);
		}
	}

	private UUID getMemberUUID(String id) throws ServiceException {
		Preconditions.checkNotNull(id);
		try {
			return UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new MemberNotFoundException(id);
		}
	}

	private boolean sameSource(SourceEntity entity, String memberId, String origin) {

		if (memberId != null && entity.getMember() != null) {
			return memberId.equals(entity.getMember().getId().toString());
		} else if (origin != null) {
			return origin.equals(entity.getOrigin());
		}

		return false;
	}

	private void fill(CommentEntity entity, CommentDTO dto) throws ServiceException {
		final Calendar date = Calendar.getInstance();
		date.setTime(dto.getDate());
		entity.setDate(date);
		entity.setDescription(dto.getDescription());
		entity.setEmail(dto.getEmail());
		if (dto.getLastModeration() != null) {
			final Calendar last = Calendar.getInstance();
			last.setTime(dto.getLastModeration());
			entity.setLastModeration(last);
		}
		entity.setRate(dto.getRate());
		entity.setNumberOfRates(dto.getNumberOfRates());
		entity.setTitle(dto.getTitle());
		entity.setValid(dto.isValid());

		UUID communityId = null;

		if (dto.getCommunityId() == null) {
			communityId = CommunityManager.GLOBAL_ID;
		} else {
			try {
				communityId = UUID.fromString(dto.getCommunityId());
			} catch (IllegalArgumentException e) {
				throw new CommunityNotFoundException(dto.getCommunityId());
			}
		}

		final Long relationId = relationshipComponent.get(dto.getResourceKey(), communityId, null);
		final RelationshipEntity relationship = getDao().findById(RelationshipEntity.class, relationId, false);

		entity.setRelationship(relationship);

		SourceKey sourceKey = null;

		if (dto.getMemberId() != null) {
			try {
				sourceKey = SourceKey.member(UUID.fromString(dto.getMemberId()));
			} catch (IllegalArgumentException e) {
				throw new MemberNotFoundException(dto.getMemberId());
			}
		} else if (dto.getOrigin() != null) {
			sourceKey = SourceKey.origin(dto.getOrigin());
		}

		final Long sourceId = sourceComponent.compute(sourceKey);
		final SourceEntity source = getDao().findById(SourceEntity.class, sourceId, false);

		entity.setSource(source);
	}

	private CommentEntity load(Long id) throws ServiceException {
		if (id == null) {
			throw new CommentNotFoundException(id);
		}

		final CommentEntity entity = getDao().findById(CommentEntity.class, id, false);
		if (entity == null || entity.isDeleted()) {
			throw new CommentNotFoundException(id);
		}

		return entity;
	}

	public void setRelationshipComponent(RelationshipComponent relationshipComponent) {
		this.relationshipComponent = relationshipComponent;
	}

	public void setSourceComponent(SourceComponent sourceComponent) {
		this.sourceComponent = sourceComponent;
	}
}
