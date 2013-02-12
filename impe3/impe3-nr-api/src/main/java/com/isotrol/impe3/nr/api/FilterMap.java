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


import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;


/**
 * Filter map.
 * @author Andres Rodriguez
 * @param <k> Key type.
 */
abstract class FilterMap<K> implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -6602391140305796229L;
	/** Empty filter. */
	private static final EmptyMap<Object> EMPTY = new EmptyMap<Object>();

	/** Returns the empty filter. */
	@SuppressWarnings("unchecked")
	static <K> FilterMap<K> emptyMap() {
		return (FilterMap<K>) EMPTY;
	}

	/** Returns the null filter. */
	static <K> FilterMap<K> nullMap() {
		return new NullMap<K>();
	}

	/** Constructor. */
	FilterMap() {
	}

	/**
	 * Returns the map.
	 * @return The map.
	 */
	abstract Map<K, FilterType> map();

	boolean isEmpty() {
		return false;
	}

	boolean isNull() {
		return false;
	}

	boolean isRegular() {
		return false;
	}

	private final static class NullMap<K> extends FilterMap<K> {
		/** Serial UID. */
		private static final long serialVersionUID = 512414140430000691L;

		/** Constructor. */
		NullMap() {
		}

		boolean isNull() {
			return true;
		}

		@Override
		Map<K, FilterType> map() {
			return null;
		}
	}

	private final static class EmptyMap<K> extends FilterMap<K> {
		/** Serial UID. */
		private static final long serialVersionUID = -7893711182383727515L;

		/** Constructor. */
		EmptyMap() {
		}

		boolean isEmpty() {
			return true;
		}

		@Override
		Map<K, FilterType> map() {
			return ImmutableMap.of();
		}

	}

	@SuppressWarnings({"unused", "serial"})
	private final static class RegularMap<K> extends FilterMap<K> {
		private final Map<K, FilterType> map;

		/** Constructor. */
		RegularMap(Map<K, FilterType> map) {
			this.map = map;
		}

		boolean isRegular() {
			return true;
		}

		@Override
		Map<K, FilterType> map() {
			return Collections.unmodifiableMap(map);
		}

	}
}
