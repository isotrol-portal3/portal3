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


import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.EventDTO;
import com.isotrol.impe3.web20.api.EventFilterDTO;
import com.isotrol.impe3.web20.api.RatedException;
import com.isotrol.impe3.web20.api.RatingEventDTO;
import com.isotrol.impe3.web20.api.ResourceRatingDTO;
import com.isotrol.impe3.web20.model.RatingEntity;
import com.isotrol.impe3.web20.model.RatingEventEntity;
import com.isotrol.impe3.web20.model.SourceEntity;
import com.isotrol.impe3.web20.server.RatingFilterDTO;
import com.isotrol.impe3.web20.server.RatingManager;
import com.isotrol.impe3.web20.server.RatingMap;
import com.isotrol.impe3.web20.server.RatingMap.Entry;


/**
 * Implementation of RatingManager.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero.
 */
@Service("ratingManager")
public final class RatingManagerImpl extends AbstractEventManager implements RatingManager {
	/** Rating component. */
	private RatingComponent ratingComponent;

	private final Function<Entry, ResourceRatingDTO> transformer = new Function<Entry, ResourceRatingDTO>() {
		public ResourceRatingDTO apply(Entry from) {
			ResourceRatingDTO dto = new ResourceRatingDTO(getResourceById(from.getResourceId()), from.getCount());
			dto.setMin(from.getMin());
			dto.setMax(from.getMax());
			dto.setMean(from.getMean());
			return dto;
		}
	};

	/** Constructor. */
	public RatingManagerImpl() {
	}

	@Autowired
	public void setRatingComponent(RatingComponent ratingComponent) {
		this.ratingComponent = ratingComponent;
	}

	@Transactional(rollbackFor = Throwable.class)
	public boolean isRated(EventDTO event) throws ServiceException {
		// 1 - Check arguments
		if (!hasResource(event)) {
			throw new ServiceException();
		}
		// 2 - Check community
		getCommunity(event);
		// 3 - Check source
		final SourceEntity source = getSource(event);
		if (source == null) {
			throw new ServiceException();
		}
		// 4 - Load the ratings
		final Function<Long, RatingEntity> loader = new Function<Long, RatingEntity>() {
			public RatingEntity apply(Long from) {
				final long id = ratingComponent.get(from);
				final RatingEntity entity = findById(RatingEntity.class, id);
				return entity;
			}
		};
		final Set<RatingEntity> ratings = newHashSet(transform(getRelationships(event), loader));
		final RatingFilterDTO filter = new RatingFilterDTO();
		filter.setSource(source.getId());
		for (RatingEntity rating : ratings) {
			filter.setRating(rating.getId());
			final List<RatingEventEntity> events = getDao().findRatingEvents(filter);
			if (events != null && !events.isEmpty()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see com.isotrol.impe3.web20.server.RatingManager#register(com.isotrol.impe3.web20.api.RatingEventDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void register(final RatingEventDTO event, boolean allowMore) throws ServiceException {
		// 1 - Check arguments
		if (!hasResource(event)) {
			return;
		}
		// 2 - Check community
		getCommunity(event);
		// 3 - Check source
		final SourceEntity source = getSource(event);
		// 4 - Load the ratings and increment them globally

		final Set<Long> relationships = getRelationships(event);
		final Set<RatingEntity> ratings = Sets.newHashSet();
		final RatingFilterDTO filter = new RatingFilterDTO();
		filter.setSource(source.getId());
		for (Long from : relationships) {
			final long id = ratingComponent.get(from);
			final RatingEntity entity = getDao().findById(RatingEntity.class, id, true);

			// check
			if (!allowMore) {
				filter.setRating(entity.getId());
				final List<RatingEventEntity> events = getDao().findRatingEvents(filter);
				if (events != null && !events.isEmpty()) {
					throw new RatedException();
				}
			}
			ratings.add(entity);
		}
		for (RatingEntity entity : ratings) {
			entity.add(event.getValue());
		}
		// 5 - Create event.
		final RatingEventEntity entity = new RatingEventEntity();
		entity.setSource(source);
		final long timestamp = getEventTimestamp(event);
		entity.setTimestamp(timestamp);
		entity.getRatings().addAll(ratings);
		entity.setValue(event.getValue());
		getDao().save(entity);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.RatingManager#getGroupId(com.isotrol.impe3.web20.api.EventFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Long getGroupId(EventFilterDTO filter) throws ServiceException {
		if (filter == null) {
			return null;
		}
		final UUID communityId = getOptionalCommunityUUID(filter.getCommunityId());
		return getGroup(communityId, filter.getAggregation());
	}

	/**
	 * @see com.isotrol.impe3.web20.server.RatingManager#getResources(java.lang.Iterable)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceRatingDTO> getResources(Iterable<Entry> entries) {
		return newArrayList(transform(entries, transformer));
	}

	/**
	 * @see com.isotrol.impe3.web20.server.RatingManager#getResource(com.isotrol.impe3.web20.server.RatingMap,
	 * java.lang.Long, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ResourceRatingDTO getResource(RatingMap map, Long groupId, String resource) {
		return transformer.apply(map.getResource(groupId, getResource(resource)));
	}

	/**
	 * @see com.isotrol.impe3.web20.server.RatingManager#getResource(com.isotrol.impe3.web20.server.RatingMap,
	 * com.isotrol.impe3.web20.api.EventFilterDTO, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ResourceRatingDTO getResource(RatingMap map, EventFilterDTO filter, String resource) throws ServiceException {
		return getResource(map, getGroupId(filter), resource);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.WithTimeMapManager#loadTimeMap(java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public RatingMap loadTimeMap(Long interval) {
		final RatingMap.Builder builder = RatingMap.builder();
		List<Object[]> records = interval == null ? getDao().getAllRatings() : getDao().getAllRatingsFromTimestamp(
			Calendar.getInstance().getTimeInMillis() - interval*1000);
		for (Object[] counter : records) {
			builder.add(counter);
		}
		return builder.get();
	}

	/**
	 * @see com.isotrol.impe3.web20.server.WithTimeMapManager#createEmptyTimeMap()
	 */
	public RatingMap createEmptyTimeMap() {
		return RatingMap.builder().get();
	}
}
