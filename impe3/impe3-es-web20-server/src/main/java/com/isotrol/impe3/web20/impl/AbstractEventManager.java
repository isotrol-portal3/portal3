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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.isotrol.impe3.web20.api.EventDTO;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.SourceEntity;
import com.isotrol.impe3.web20.server.CommunityManager;
import com.isotrol.impe3.web20.server.Relationship;
import com.isotrol.impe3.web20.server.ResourceManager;
import com.isotrol.impe3.web20.server.SourceKey;


/**
 * Abstract class for event managers.
 * @author Andres Rodriguez.
 */
public abstract class AbstractEventManager extends AbstractResourceManager implements ResourceManager {
	@Autowired
	private GroupComponent groupComponent;
	private SourceComponent sourceComponent;
	/** Relationship component. */
	private RelationshipComponent relationshipComponent;

	/** Constructor. */
	public AbstractEventManager() {
	}

	@Autowired
	public void setSourceComponent(SourceComponent sourceComponent) {
		this.sourceComponent = sourceComponent;
	}

	@Autowired
	public void setRelationshipComponent(RelationshipComponent relationshipComponent) {
		this.relationshipComponent = relationshipComponent;
	}

	/**
	 * Checks whether and event has a resource.
	 * @param event Event to check.
	 * @return True if the event has a resource.
	 */
	protected final boolean hasResource(EventDTO event) {
		return event != null && event.getResource() != null;
	}

	/**
	 * Generates a source key from an event.
	 * @param event Event.
	 * @return The requested key.
	 * @throws IllegalArgumentException if no suitable key can be generated.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	protected final SourceEntity getSource(EventDTO event) {
		checkNotNull(event);
		final SourceKey key = SourceKey.source(event.getSource());
		final UUID memberId = key.getMember();
		if (memberId != null) {
			MemberEntity m = findById(MemberEntity.class, memberId);
			checkArgument(m != null);
		}
		return findById(SourceEntity.class, sourceComponent.get(key));
	}

	/**
	 * Finds a community id from an event.
	 * @param event Event.
	 * @return The community key referenced in the event.
	 * @throws IllegalArgumentException if the reference community does not exist.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	protected final UUID getCommunity(EventDTO event) {
		checkNotNull(event);
		final String sid = event.getCommunityId();
		if (sid == null) {
			return CommunityManager.GLOBAL_ID;
		}
		final UUID id = UUID.fromString(sid);
		CommunityEntity c = findById(CommunityEntity.class, id);
		checkArgument(c != null);
		return id;
	}

	/**
	 * Returns the event timestamp.
	 * @param event Event.
	 * @return Event timestamp.
	 */
	protected final long getEventTimestamp(EventDTO event) {
		checkNotNull(event);
		final Date d = event.getDate();
		final long current = System.currentTimeMillis();
		if (d != null && d.getTime() <= current) {
			return d.getTime();
		}
		return current;
	}

	private Iterable<String> getAggregations(EventDTO event) {
		Set<String> set = event.getAggregations();
		if (set == null) {
			return ImmutableSet.of();
		}
		return Iterables.filter(set, Predicates.notNull());
	}

	/**
	 * Returns the numeric id of a group.
	 * @param communityId Community Id.
	 * @param aggregation Aggregation.
	 * @return The numeric id of the group or {@code null} if the global group.
	 */
	protected final Long getGroup(UUID communityId, String aggregation) {
		return groupComponent.get(communityId, aggregation);
	}

	/**
	 * Gets the groups referenced from an event.
	 * @param event Event.
	 * @return The groups.
	 * @throws IllegalArgumentException if the reference community does not exist.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	protected final Set<Long> getGroups(EventDTO event) {
		checkNotNull(event);
		final UUID communityId = getCommunity(event);
		final Set<Long> set = Sets.newHashSet();
		set.add(null);
		if (communityId != null) {
			set.add(getGroup(communityId, null));
			for (String a : getAggregations(event)) {
				set.add(getGroup(communityId, a));
			}
		}
		return set;
	}

	/**
	 * Gets the relationships referenced from an event.
	 * @param event Event.
	 * @return The relationships.
	 * @throws IllegalArgumentException if the reference community does not exist.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	protected final Set<Long> getRelationships(EventDTO event) {
		checkArgument(hasResource(event));
		final long resourceId = getResource(event.getResource());
		final Function<Long, Long> f = new Function<Long, Long>() {
			public Long apply(Long from) {
				return relationshipComponent.get(new Relationship(resourceId, from));
			}
		};
		return newHashSet(Iterables.transform(getGroups(event), f));
	}
}
