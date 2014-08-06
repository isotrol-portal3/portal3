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


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.EventDTO;
import com.isotrol.impe3.web20.api.EventFilterDTO;
import com.isotrol.impe3.web20.api.RatingEventDTO;
import com.isotrol.impe3.web20.api.RatingsService;
import com.isotrol.impe3.web20.api.ResourceRatingDTO;
import com.isotrol.impe3.web20.server.RatingManager;
import com.isotrol.impe3.web20.server.RatingMap;
import com.isotrol.impe3.web20.server.TimeMapConfig;


/**
 * Implementation of RatingsService.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero.
 */
@Service("ratingsService")
public final class RatingsServiceImpl extends AbstractWeb20Service implements RatingsService {
	/** Rating manager. */
	private final RatingManager ratingManager;
	/** Scheduler. */
	private final SchedulerComponent scheduler;
	/** Rating map. */
	private final TimeMap<RatingMap> maps;

	/** Constructor. */
	@Autowired
	public RatingsServiceImpl(RatingManager ratingManager, SchedulerComponent scheduler, TimeMapConfig config) {
		this.ratingManager = ratingManager;
		this.scheduler = scheduler;
		this.maps = TimeMap.create(config, scheduler, ratingManager);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#isRated(java.lang.String, com.isotrol.impe3.web20.api.EventDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public boolean isRated(String serviceId, EventDTO event) throws ServiceException {
		return ratingManager.isRated(event);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#register(java.lang.String,
	 * com.isotrol.impe3.web20.api.RatingEventDTO, boolean, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void register(String serviceId, final RatingEventDTO event, boolean async, final boolean allowMore)
		throws ServiceException {

		if (!async) {
			ratingManager.register(event, allowMore);
		} else {
			final Runnable task = new Runnable() {
				public void run() {
					try {
						ratingManager.register(event, allowMore);
					} catch (ServiceException e) {
						// TODO: log
					}
				}
			};
			scheduler.execute(task);
		}
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#getBestRated(java.lang.String,
	 * com.isotrol.impe3.web20.api.EventFilterDTO, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceRatingDTO> getBestRated(String serviceId, EventFilterDTO filter, int max)
		throws ServiceException {
		if (max <= 0) {
			return Lists.newArrayListWithCapacity(0);
		}
		final Long groupId = ratingManager.getGroupId(filter);
		return ratingManager.getResources(maps.get(filter).get(groupId, max));
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#getResourceRatings(java.lang.String,
	 * com.isotrol.impe3.web20.api.EventFilterDTO, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ResourceRatingDTO getResourceRatings(String serviceId, EventFilterDTO filter, String resource)
		throws ServiceException {
		if (resource == null) {
			return null;
		}
		return ratingManager.getResource(maps.get(filter), filter, resource);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.RatingsService#getRatingsOfResources(java.lang.String, java.util.List,
	 * com.isotrol.impe3.web20.api.EventFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Map<String, ResourceRatingDTO> getRatingsOfResources(String serviceId, List<String> resources,
		EventFilterDTO filter) throws ServiceException {

		if (filter == null || resources == null) {
			return null;
		}

		final Long groupId = ratingManager.getGroupId(filter);

		final Map<String, ResourceRatingDTO> ratings = Maps.newHashMapWithExpectedSize(resources.size());

		for (String resource : resources) {
			final ResourceRatingDTO rating = ratingManager.getResource(maps.get(filter), groupId, resource);
			ratings.put(resource, rating);
		}

		return ratings;
	}

}
