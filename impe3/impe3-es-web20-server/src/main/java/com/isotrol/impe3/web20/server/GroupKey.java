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
package com.isotrol.impe3.web20.server;


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.google.common.base.Objects;


/**
 * Value that represents a group key.
 * @author Andres Rodriguez
 */
public final class GroupKey {
	/** Community. */
	private final UUID community;
	/** Aggregation, optional. */
	private final Long aggregation;
	/** Hash code. */
	private final int hash;

	public GroupKey(UUID community, Long aggregation) {
		this.community = checkNotNull(community);
		this.aggregation = aggregation;
		this.hash = Objects.hashCode(community, aggregation);
	}

	/**
	 * Returns the community.
	 * @return The community.
	 */
	public UUID getCommunity() {
		return community;
	}

	/**
	 * Returns the counter aggregation.
	 * @return The counter aggregation.
	 */
	public Long getAggregation() {
		return aggregation;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof GroupKey) {
			final GroupKey k = (GroupKey) obj;
			return hash == k.hash && community.equals(k.community) && equal(aggregation, k.aggregation);
		}
		return false;
	}
}
