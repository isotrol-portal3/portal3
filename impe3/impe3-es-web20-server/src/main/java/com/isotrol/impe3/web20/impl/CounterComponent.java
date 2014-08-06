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


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.web20.model.CounterEntity;
import com.isotrol.impe3.web20.model.CounterTypeEntity;
import com.isotrol.impe3.web20.model.RelationshipEntity;
import com.isotrol.impe3.web20.server.CounterKey;


/**
 * Counter component.
 * @author Andres Rodriguez.
 */
@Component
public final class CounterComponent extends AbstractMapComponent<CounterKey> {
	/** Relationship component. */
	private final RelationshipComponent relationshipComponent;

	/** Default constructor. */
	@Autowired
	public CounterComponent(RelationshipComponent relationshipComponent) {
		this.relationshipComponent = relationshipComponent;
	}

	/**
	 * @see com.isotrol.impe3.web20.impl.AbstractMapComponent#compute(java.lang.Object)
	 */
	@Override
	Long compute(CounterKey key) {
		CounterEntity entity = getDao().getCounter(key);
		if (entity == null) {
			entity = new CounterEntity();
			entity.setCounterType(findById(CounterTypeEntity.class, key.getCounterType()));
			entity.setRelationship(findById(RelationshipEntity.class, key.getRelationship()));
			getDao().save(entity);
			flush();
		}
		return entity.getId();
	}

	/**
	 * Returns the numeric id of a counter, creating it if needed.
	 * @param counterTypeId Counter type Id.
	 * @param resourceId Long resource Id.
	 * @param communityId Community Id.
	 * @param aggregation Aggregation.
	 * @return The numeric id of the relationship.
	 */
	long get(long counterTypeId, long resourceId, UUID communityId, String aggregation) {
		final long rid = relationshipComponent.get(resourceId, communityId, aggregation);
		return get(new CounterKey(counterTypeId, rid));
	}
}
