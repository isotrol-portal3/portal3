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


import com.google.common.base.Objects;


/**
 * Relationship value.
 * @author Andres Rodriguez
 */
public final class Relationship {
	/** Resource Id. */
	private final long resourceId;
	/** Group. */
	private final Long group;
	/** Hash code. */
	private final int hash;

	/**
	 * Constructor.
	 * @param resourceId Resource Id.
	 * @param group Group Id.
	 */
	public Relationship(long resourceId, Long group) {
		this.resourceId = resourceId;
		this.group = group;
		this.hash = Objects.hashCode(resourceId, group);
	}

	/**
	 * Returns the resource id.
	 * @return The resource id.
	 */
	public long getResourceId() {
		return resourceId;
	}

	/**
	 * Returns the group.
	 * @return The group.
	 */
	public Long getGroup() {
		return group;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Relationship) {
			Relationship r = (Relationship) obj;
			return hash == r.hash && resourceId == r.resourceId && Objects.equal(group, r.group);
		}
		return false;
	}
}
