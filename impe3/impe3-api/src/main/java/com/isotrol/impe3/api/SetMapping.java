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

package com.isotrol.impe3.api;


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;


/**
 * Value that represents a set mapping.
 * @author Andres Rodriguez
 */
public final class SetMapping {
	/** Set name. */
	private final String set;
	/** Mapping. */
	private final String mapping;

	/**
	 * Constructor.
	 * @param set Set name.
	 * @param mapping Mapping.
	 */
	public SetMapping(String set, String mapping) {
		this.set = checkNotNull(set, "The set name must be provided");
		this.mapping = mapping;
	}

	/**
	 * Returns the set name.
	 * @return The set name.
	 */
	public String getSet() {
		return set;
	}

	/**
	 * Returns the mapping.
	 * @return The mapping.
	 */
	public String getMapping() {
		return mapping;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return Objects.hashCode(set, mapping);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof SetMapping) {
			final SetMapping m = (SetMapping) obj;
			return Objects.equal(set, m.set) && Objects.equal(mapping, m.mapping);
		}
		return false;
	}

}
