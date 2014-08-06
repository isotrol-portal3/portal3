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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.MapMaker;
import com.isotrol.impe3.web20.api.EventFilterDTO;
import com.isotrol.impe3.web20.server.TimeMapConfig;
import com.isotrol.impe3.web20.server.WithTimeMapManager;


/**
 * Time-based map caches.
 * @author Andres Rodriguez
 * @param <M> Map type.
 */
final class TimeMap<M> implements Function<Long, M> {
	private final ConcurrentMap<Long, M> maps;
	private final WithTimeMapManager<M> manager;
	private final ImmutableSortedSet<Long> intervals;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	static <M> TimeMap<M> create(TimeMapConfig config, SchedulerComponent scheduler, WithTimeMapManager<M> manager) {
		return new TimeMap<M>(config, scheduler, manager);
	}

	private TimeMap(TimeMapConfig config, SchedulerComponent scheduler, WithTimeMapManager<M> manager) {
		this.maps = new MapMaker().makeMap();
		this.manager = checkNotNull(manager);
		ImmutableSortedSet.Builder<Long> ib = ImmutableSortedSet.naturalOrder();
		if (config.isGlobal()) {
			ib.add(Long.MAX_VALUE);
			this.maps.put(Long.MAX_VALUE, manager.createEmptyTimeMap());
			scheduler.scheduleWithFixedDelay(new Task(null), 0L, config.getDelay(), TimeUnit.SECONDS);
		}
		for (Long seconds : config.getIntervals()) {
			ib.add(seconds);
			this.maps.put(seconds, manager.createEmptyTimeMap());
			scheduler.scheduleWithFixedDelay(new Task(seconds), 0L, config.getDelay(), TimeUnit.SECONDS);
		}
		this.intervals = ib.build();
	}

	public M apply(Long from) {
		if (from == null) {
			from = Long.MAX_VALUE;
		}
		return maps.get(intervals.tailSet(from).first());
	}

	public M get(EventFilterDTO dto) {
		if (dto == null) {
			return apply(null);
		}
		return apply(dto.getTime());
	}

	private final class Task implements Runnable {
		private final Long seconds;

		Task(Long seconds) {
			this.seconds = seconds;
		}

		public void run() {
			try {
				M map = manager.loadTimeMap(seconds);
				if (map != null) {
					long key = seconds != null ? seconds.longValue() : Long.MAX_VALUE;
					maps.put(key, map);
				}
			} catch (Throwable t) {
				logger.error("Error during time map task execution", t);
			}
		}
	}

}
