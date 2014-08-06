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

import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CounterEventDTO;
import com.isotrol.impe3.web20.api.CounterFilterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;
import com.isotrol.impe3.web20.model.CounterBreakdown;
import com.isotrol.impe3.web20.model.CounterDailyBreakdown;
import com.isotrol.impe3.web20.model.CounterEntity;
import com.isotrol.impe3.web20.model.CounterEventEntity;
import com.isotrol.impe3.web20.model.CounterHourlyBreakdown;
import com.isotrol.impe3.web20.model.CounterMonthlyBreakdown;
import com.isotrol.impe3.web20.model.CounterPK;
import com.isotrol.impe3.web20.model.CounterYearlyBreakdown;
import com.isotrol.impe3.web20.model.SourceEntity;
import com.isotrol.impe3.web20.server.CounterKey;
import com.isotrol.impe3.web20.server.CounterManager;
import com.isotrol.impe3.web20.server.CounterMap;
import com.isotrol.impe3.web20.server.CounterMap.Entry;
import com.isotrol.impe3.web20.server.CounterMap.Key;


/**
 * Implementation of CounterManager.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero.
 */
@Service("counterManager")
public final class CounterManagerImpl extends AbstractEventManager implements CounterManager {
	private CounterTypeComponent counterTypeComponent;
	private CounterComponent counterComponent;
	private boolean breakdown = true;

	private final Function<Entry, ResourceCounterDTO> transformer = new Function<Entry, ResourceCounterDTO>() {
		public ResourceCounterDTO apply(Entry from) {
			return new ResourceCounterDTO(getResourceById(from.getResourceId()), from.getCount());
		}
	};

	/** Constructor. */
	public CounterManagerImpl() {
	}

	@Autowired
	public void setCounterTypeComponent(CounterTypeComponent counterTypeComponent) {
		this.counterTypeComponent = counterTypeComponent;
	}

	@Autowired
	public void setCounterComponent(CounterComponent counterComponent) {
		this.counterComponent = counterComponent;
	}

	/**
	 * @see com.isotrol.impe3.web20.server.CounterManager#setBreakdown(boolean)
	 */
	public void setBreakdown(boolean breakdown) {
		this.breakdown = breakdown;
	}

	/**
	 * @see com.isotrol.impe3.web20.server.CounterManager#register(com.isotrol.impe3.web20.api.CounterEventDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void register(CounterEventDTO event) {
		// 1 - Check arguments
		if (!hasResource(event)) {
			return;
		}
		final String type = event.getCounterType();
		if (type == null) {
			return;
		}
		// 2 - Check community
		getCommunity(event);
		// 3 - Check source
		final SourceEntity source = breakdown ? getSource(event) : null;
		// 4 - Get Counter type
		final long typeId = counterTypeComponent.get(type);
		// 5 - Load the counters and increment them globally
		final Function<Long, CounterEntity> loader = new Function<Long, CounterEntity>() {
			public CounterEntity apply(Long from) {
				final long id = counterComponent.get(new CounterKey(typeId, from));
				getDao().incrementCounter(id);
				return findById(CounterEntity.class, id);
			}
		};
		final Set<CounterEntity> counters = newHashSet(transform(getRelationships(event), loader));
		// 6 - Create event
		final CounterEventEntity entity = new CounterEventEntity();
		entity.setSource(source);
		final long timestamp = getEventTimestamp(event);
		entity.setTimestamp(timestamp);
		entity.getCounters().addAll(counters);
		getDao().save(entity);
		// 7 - Create breakdown if needed
		if (breakdown) {
			// Base calendar
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(timestamp);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			final long hourly = calendar.getTimeInMillis();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			final long daily = calendar.getTimeInMillis();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			final long monthly = calendar.getTimeInMillis();
			calendar.set(Calendar.MONTH, Calendar.JANUARY);
			final long yearly = calendar.getTimeInMillis();
			for (CounterEntity counter : counters) {
				saveCounterBreakdown(counter, hourly, daily, monthly, yearly);
			}
		}
	}

	private void saveCounterBreakdown(CounterEntity counter, long hourly, long daily, long monthly, long yearly) {
		CounterPK key = new CounterPK(counter, hourly);
		if (getDao().incrementHourlyCounter(key) == 0) {
			if (!tryInsert(new CounterHourlyBreakdown(key))) {
				getDao().incrementHourlyCounter(key);
			}
		}
		key = new CounterPK(counter, daily);
		if (getDao().incrementDailyCounter(key) == 0) {
			if (!tryInsert(new CounterDailyBreakdown(key))) {
				getDao().incrementDailyCounter(key);
			}
		}
		key = new CounterPK(counter, monthly);
		if (getDao().incrementMonthlyCounter(key) == 0) {
			if (!tryInsert(new CounterMonthlyBreakdown(key))) {
				getDao().incrementMonthlyCounter(key);
			}
		}
		key = new CounterPK(counter, yearly);
		if (getDao().incrementYearlyCounter(key) == 0) {
			if (!tryInsert(new CounterYearlyBreakdown(key))) {
				getDao().incrementYearlyCounter(key);
			}
		}
	}

	private boolean tryInsert(CounterBreakdown b) {
		try {
			getDao().save(b);
			flush();
			return true;
		} catch (JDBCException e) {
			// Already inserted, try again.
			return false;
		}
	}

	/**
	 * @see com.isotrol.impe3.web20.server.CounterManager#getKey(com.isotrol.impe3.web20.api.CounterFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Key getKey(CounterFilterDTO filter) throws ServiceException {
		if (filter == null || filter.getCounterType() == null) {
			return null;
		}
		final UUID communityId = getOptionalCommunityUUID(filter.getCommunityId());
		final long typeId = counterTypeComponent.get(filter.getCounterType());
		final Long groupId = getGroup(communityId, filter.getAggregation());
		return new Key(typeId, groupId);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.CounterManager#getResources(java.lang.Iterable)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<ResourceCounterDTO> getResources(Iterable<Entry> entries) {
		return newArrayList(transform(entries, transformer));
	}

	/**
	 * @see com.isotrol.impe3.web20.server.WithTimeMapManager#loadTimeMap(java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public CounterMap loadTimeMap(Long interval) {
		final CounterMap.Builder builder = CounterMap.builder();
		List<Object[]> records = interval == null ? getDao().getAllCounters() : getDao().getAllCountersFromTimestamp(
			Calendar.getInstance().getTimeInMillis()-interval*1000);
		for (Object[] counter : records) {
			builder.add(counter);
		}
		return builder.get();
	}
	
	public CounterMap createEmptyTimeMap() {
		return CounterMap.builder().get();
	}

}
