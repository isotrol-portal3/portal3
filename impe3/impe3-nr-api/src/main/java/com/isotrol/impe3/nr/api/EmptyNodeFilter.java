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

package com.isotrol.impe3.nr.api;


import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;


/**
 * Empty node filter.
 * @author Andres Rodriguez
 */
final class EmptyNodeFilter extends NodeFilter {
	/** Serial UID. */
	private static final long serialVersionUID = 2437418370770692433L;

	EmptyNodeFilter() {
	}

	public boolean isEmpty() {
		return true;
	}

	public boolean isNull() {
		return false;
	}

	@Override
	public boolean isDue() {
		return false;
	}

	@Override
	public Map<String, FilterType> sets() {
		return ImmutableMap.of();
	}

	@Override
	public Map<Optional<UUID>, FilterType> categories() {
		return ImmutableMap.of();
	}

	@Override
	public Map<UUID, FilterType> contentTypes() {
		return ImmutableMap.of();
	}

	@Override
	public Map<Locale, FilterType> locales() {
		return ImmutableMap.of();
	}

	@Override
	public Map<String, FilterType> tags() {
		return ImmutableMap.of();
	}

	@Override
	public Map<NodeKey, FilterType> keys() {
		return ImmutableMap.of();
	}

	@Override
	public int hashCode() {
		return 660617;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof EmptyNodeFilter;
	}

	// // Serialization // //

	private Object readResolve() {
		return emptyFilter();
	}

}
