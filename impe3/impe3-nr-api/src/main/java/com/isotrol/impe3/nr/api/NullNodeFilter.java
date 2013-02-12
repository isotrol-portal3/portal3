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


/**
 * Null node filter.
 * @author Andres Rodriguez
 */
final class NullNodeFilter extends NodeFilter {
	/** Serial UID. */
	private static final long serialVersionUID = -3021704739441879378L;

	NullNodeFilter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.nr.api.NodeFilter#isEmpty()
	 */
	public boolean isEmpty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.nr.api.NodeFilter#isNull()
	 */
	public boolean isNull() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.nr.api.NodeFilter#isDue()
	 */
	public boolean isDue() {
		return false;
	}

	@Override
	public Map<String, FilterType> sets() {
		return null;
	}

	@Override
	public Map<Optional<UUID>, FilterType> categories() {
		return null;
	}

	@Override
	public Map<UUID, FilterType> contentTypes() {
		return null;
	}

	@Override
	public Map<Locale, FilterType> locales() {
		return null;
	}

	@Override
	public Map<String, FilterType> tags() {
		return null;
	}

	@Override
	public Map<NodeKey, FilterType> keys() {
		return null;
	}

	@Override
	public int hashCode() {
		return 839887;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj; // singleton
	}

	// // Serialization ////

	private Object readResolve() {
		return nullFilter();
	}
}
