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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CounterEventDTO;
import com.isotrol.impe3.web20.api.CounterFilterDTO;
import com.isotrol.impe3.web20.api.CountersService;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;
import com.isotrol.impe3.web20.server.CounterManager;
import com.isotrol.impe3.web20.server.CounterMap;
import com.isotrol.impe3.web20.server.CounterMap.Key;
import com.isotrol.impe3.web20.server.TimeMapConfig;


/**
 * Implementation of CountersService.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero.
 */
@Service("countersService")
public final class CountersServiceImpl extends AbstractWeb20Service implements CountersService {
	private final CounterManager counterManager;
	/** Scheduler. */
	private final SchedulerComponent scheduler;
	/** Counter map. */
	private final TimeMap<CounterMap> maps;

	/** Constructor. */
	@Autowired
	public CountersServiceImpl(CounterManager counterManager, SchedulerComponent scheduler, TimeMapConfig config) {
		this.counterManager = counterManager;
		this.scheduler = scheduler;
		this.maps = TimeMap.create(config, scheduler, counterManager);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CountersService#register(java.lang.String,
	 * com.isotrol.impe3.web20.api.CounterEventDTO, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void register(String serviceId, final CounterEventDTO event, boolean async) throws ServiceException {
		if (!async) {
			counterManager.register(event);
		} else {
			final Runnable task = new Runnable() {
				public void run() {
					counterManager.register(event);
				}
			};
			scheduler.execute(task);
		}
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CountersService#getGreatestHits(java.lang.String,
	 * com.isotrol.impe3.web20.api.CounterFilterDTO, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceCounterDTO> getGreatestHits(String serviceId, CounterFilterDTO filter, int max)
		throws ServiceException {
		final Key key = counterManager.getKey(filter);
		if (key == null || max <= 0) {
			return Lists.newArrayListWithCapacity(0);
		}
		return counterManager.getResources(maps.get(filter).get(key, max));
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CountersService#getResourceHits(java.lang.String,
	 * com.isotrol.impe3.web20.api.CounterFilterDTO, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public long getResourceHits(String serviceId, CounterFilterDTO filter, String resource) throws ServiceException {
		if (resource == null) {
			return 0L;
		}
		final Key key = counterManager.getKey(filter);
		if (key == null) {
			return 0L;
		}
		return maps.get(filter).getResource(key, counterManager.getResource(resource));
	}

}
