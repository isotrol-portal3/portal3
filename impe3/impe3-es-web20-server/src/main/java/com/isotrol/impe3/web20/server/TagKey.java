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
 * Value that represents a tag key.
 * @author Andres Rodriguez
 */
public final class TagKey {
	/** Tag set. */
	private final long set;
	/** Tag name. */
	private final long name;
	/** Hash code. */
	private final int hash;

	public TagKey(long set, long name) {
		this.set = set;
		this.name = name;
		this.hash = Objects.hashCode(set, name);
	}

	/**
	 * Returns the tag set.
	 * @return The tag set.
	 */
	public long getSet() {
		return set;
	}

	/**
	 * Returns the tag name.
	 * @return The tag name.
	 */
	public long getName() {
		return name;
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
		if (obj instanceof TagKey) {
			final TagKey k = (TagKey) obj;
			return hash == k.hash && set == k.set && name == k.name;
		}
		return false;
	}
}
