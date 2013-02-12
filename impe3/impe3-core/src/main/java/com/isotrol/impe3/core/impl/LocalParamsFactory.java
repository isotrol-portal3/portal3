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


import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.core.impl.MapParameters.AbstractImmutable;


/**
 * Factory class for different HTTP session parameters implementations.
 * @author Andres Rodriguez
 */
public final class LocalParamsFactory {
	/** Not instantiable. */
	private LocalParamsFactory() {
		throw new AssertionError();
	}

	private static final Empty EMPTY = new Empty();

	/**
	 * Returns an empty collection of cookies.
	 */
	public static LocalParams of() {
		return EMPTY;
	}

	/**
	 * Returns a collection built from a Servlet HTTP session.
	 * @param map Parameters map.
	 * @return The requested collection.
	 */
	public static LocalParams of(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return EMPTY;
		}
		return new Immutable(map);
	}

	/**
	 * Empty HTTP local parameters implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Empty extends EmptyParameters<Object> implements LocalParams {
		private Empty() {
		}

		public <T> Object get(String parameter, Class<T> type) {
			return null;
		}

		public LocalParams subset(Set<String> included) {
			return this;
		}

		public LocalParams apply(Map<String, Object> changes) {
			return of(changes);
		}
	}

	/**
	 * Immutable HTTP local params implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Immutable extends AbstractImmutable<Object> implements LocalParams {
		private Immutable(Map<String, Object> parameters) {
			super(parameters);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.isotrol.impe3.api.SessionParams#get(java.lang.String, java.lang.Class)
		 */
		public final <T> Object get(String parameter, Class<T> type) {
			Preconditions.checkNotNull(type);
			Object value = get(parameter);
			if (value != null) {
				return type.cast(value);
			}
			return null;
		}

		public LocalParams subset(Set<String> included) {
			return new Immutable(submap(included));
		}

		public LocalParams apply(Map<String, Object> changes) {
			if (changes == null || changes.isEmpty()) {
				return this;
			}
			ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
			for (String key : Sets.union(getNames(), changes.keySet())) {
				if (changes.containsKey(key)) {
					final Object value = changes.get(key);
					if (value != null) {
						builder.put(key, value);
					}
				} else {
					builder.put(key, get(key));
				}
			}
			return new Immutable(builder.build());
		}
	}

}
