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

package com.isotrol.impe3.pms.core.obj;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;


/**
 * Maps and sets builder utils for domain objects.
 * @author Andres Rodriguez
 */
public final class Builders {
	/** Not instantiable. */
	private Builders() {
		throw new AssertionError();
	}

	static <T> ImmutableSet.Builder<T> add(ImmutableSet.Builder<T> builder, T element) {
		if (builder == null) {
			builder = ImmutableSet.builder();
		}
		return builder.add(element);
	}

	static <T> ImmutableSet<T> build(ImmutableSet.Builder<T> builder) {
		if (builder == null) {
			return ImmutableSet.of();
		}
		return builder.build();
	}

	static <K, V> ImmutableMap.Builder<K, V> put(ImmutableMap.Builder<K, V> builder, K key, V value) {
		if (builder == null) {
			builder = ImmutableMap.builder();
		}
		return builder.put(key, value);
	}

	static <K, V> ImmutableMap<K, V> build(ImmutableMap.Builder<K, V> builder) {
		if (builder == null) {
			return ImmutableMap.of();
		}
		return builder.build();
	}

}
