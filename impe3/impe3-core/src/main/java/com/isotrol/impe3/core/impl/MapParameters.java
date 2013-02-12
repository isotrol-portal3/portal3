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

package com.isotrol.impe3.core.impl;


import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Maps.filterValues;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;


/**
 * Skeletal implementation for map-based parameters.
 * @author Andres Rodriguez
 * @param <V> Value type.
 */
abstract class MapParameters<V> extends AbstractParameters<V> {
	MapParameters() {
	}

	abstract Map<String, V> map();

	final Set<String> getNamesSubset(Set<String> included) {
		return intersect(getNames(), included);
	}

	public Set<String> getNames() {
		return map().keySet();
	}

	public V get(String name) {
		return map().get(name);
	}

	@Override
	public boolean contains(String name) {
		return map().containsKey(name);
	}

	/**
	 * Skeletal Immutable implementation.
	 * @author Andres Rodriguez
	 */
	abstract static class AbstractImmutable<V> extends MapParameters<V> {
		private final ImmutableMap<String, V> map;

		AbstractImmutable(Map<String, V> map) {
			if (map instanceof ImmutableMap) {
				this.map = ImmutableMap.copyOf(map);
			} else {
				this.map = ImmutableMap.copyOf(filterValues(map, notNull()));
			}
		}

		@Override
		Map<String, V> map() {
			return map;
		}

		final ImmutableMap<String, V> submap(Set<String> included) {
			final Set<String> intersection = getNamesSubset(included);
			if (intersection.isEmpty()) {
				return ImmutableMap.of();
			}
			final ImmutableMap.Builder<String, V> builder = ImmutableMap.builder();
			for (String name : intersection) {
				builder.put(name, map.get(name));
			}
			return builder.build();
		}

	}

}
