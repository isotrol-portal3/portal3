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

package com.isotrol.impe3.core.support;


import java.util.UUID;

import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.Identifiable;


/**
 * Utility methods for identifiable maps.
 * @author Andres Rodriguez
 */
public final class IdentifiableMaps {
	private IdentifiableMaps() {
		throw new AssertionError();
	}

	/**
	 * Creates a builder for a new hierarchy of identifiable objects.
	 * @param <V> Value type.
	 * @return A new builder.
	 */
	public static <V extends Identifiable> ImmutableMap<UUID, V> immutableOf(Iterable<? extends V> values) {
		final ImmutableMap.Builder<UUID, V> builder = ImmutableMap.builder();
		for (V value : values) {
			builder.put(value.getId(), value);
		}
		return builder.build();
	}
}
