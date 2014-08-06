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

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.web20.model.AggregationEntity;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.GroupEntity;
import com.isotrol.impe3.web20.server.GroupKey;


/**
 * Group component.
 * @author Andres Rodriguez.
 */
@Component
public final class GroupComponent extends AbstractMapComponent<GroupKey> {
	private final AggregationComponent aggregationComponent;

	/** Default constructor. */
	@Autowired
	public GroupComponent(AggregationComponent aggregationComponent) {
		this.aggregationComponent = checkNotNull(aggregationComponent);
	}

	/**
	 * @see com.isotrol.impe3.web20.impl.AbstractMapComponent#compute(java.lang.Object)
	 */
	@Override
	Long compute(GroupKey key) {
		GroupEntity entity = getDao().getGroup(key);
		if (entity == null) {
			entity = new GroupEntity();
			entity.setCommunity(findById(CommunityEntity.class, key.getCommunity()));
			final Long a = key.getAggregation();
			if (a != null) {
				entity.setAggregation(findById(AggregationEntity.class, a));
			}
			getDao().save(entity);
			flush();
		}
		return entity.getId();
	}

	/**
	 * Returns the numeric id of a group, creating it if needed.
	 * @param communityId Community Id.
	 * @param aggregation Aggregation.
	 * @return The numeric id of the group or {@code null} if the global group.
	 */
	Long get(UUID communityId, String aggregation) {
		if (communityId == null) {
			return null;
		}
		Long aid = aggregation != null ? aggregationComponent.get(aggregation) : null;
		return get(new GroupKey(communityId, aid));
	}

}
