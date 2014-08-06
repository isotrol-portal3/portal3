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

import com.isotrol.impe3.web20.model.GroupEntity;
import com.isotrol.impe3.web20.model.RelationshipEntity;
import com.isotrol.impe3.web20.model.ResourceEntity;
import com.isotrol.impe3.web20.server.GroupKey;
import com.isotrol.impe3.web20.server.Relationship;


/**
 * Relationship component.
 * @author Andres Rodriguez.
 */
@Component
public final class RelationshipComponent extends AbstractMapComponent<Relationship> {
	/** Resource component. */
	private final ResourceComponent resourceComponent;
	/** Group component. */
	private final GroupComponent groupComponent;

	/** Default constructor. */
	@Autowired
	public RelationshipComponent(ResourceComponent resourceComponent, GroupComponent groupComponent) {
		this.resourceComponent = checkNotNull(resourceComponent);
		this.groupComponent = checkNotNull(groupComponent);
	}

	@Override
	Long compute(Relationship key) {
		RelationshipEntity entity = getDao().getRelationship(key);
		if (entity == null) {
			entity = new RelationshipEntity();
			entity.setResource(findById(ResourceEntity.class, key.getResourceId()));
			final Long gid = key.getGroup();
			if (gid != null) {
				entity.setGroup(findById(GroupEntity.class, gid));
			}
			getDao().save(entity);
			flush();
		}
		return entity.getId();
	}

	/**
	 * Returns the numeric id of a relationship, creating it if needed.
	 * @param resourceId Long resource Id.
	 * @param group Group.
	 * @return The numeric id of the relationship.
	 */
	long get(long resourceId, GroupKey group) {
		final Long gid = group != null ? groupComponent.get(group) : null;
		return get(new Relationship(resourceId, gid));
	}

	/**
	 * Returns the numeric id of a relationship, creating it if needed.
	 * @param resourceId Long resource Id.
	 * @param communityId Community Id.
	 * @param aggregation Aggregation.
	 * @return The numeric id of the relationship.
	 */
	long get(long resourceId, UUID communityId, String aggregation) {
		final Long gid = groupComponent.get(communityId, aggregation);
		return get(new Relationship(resourceId, gid));
	}

	/**
	 * Returns the numeric id of a relationship, creating it if needed.
	 * @param resource Resource.
	 * @param communityId Community Id.
	 * @param aggregation Aggregation.
	 * @return The numeric id of the relationship.
	 */
	long get(String resource, UUID communityId, String aggregation) {
		return get(resourceComponent.get(resource), communityId, aggregation);
	}

}
