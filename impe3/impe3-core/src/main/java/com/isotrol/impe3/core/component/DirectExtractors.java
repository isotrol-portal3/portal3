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

package com.isotrol.impe3.core.component;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.Extract;


/**
 * Collection of direct injectors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public final class DirectExtractors<T extends Component> extends Extractors<T> {
	private final ImmutableMap<Class<?>, DirectExtractor> extractors;

	static <T extends Component> DirectExtractors<T> of(Class<T> type, Iterable<Method> methods) {
		return new DirectExtractors<T>(type, filter(methods, Extract.class));
	}

	private DirectExtractors(final Class<T> type, Iterable<Method> methods) {
		super(type);
		final Map<Class<?>, DirectExtractor> builder = Maps.newHashMap();
		for (Method m : methods) {
			final DirectExtractor i = new DirectExtractor(m);
			final Class<?> extractedType = i.getParameterType();
			if (builder.containsKey(extractedType)) {
				throw new DuplicateExtractorException(type, extractedType);
			}
			builder.put(i.getReturnType(), i);
		}
		this.extractors = ImmutableMap.copyOf(builder);
	}

	@Override
	public boolean isEmpty() {
		return extractors.isEmpty();
	}

	/**
	 * Checks if the collection contains an extractor for the provided type.
	 * @param type type to check.
	 * @return true if there is an extractor for the provided type.
	 */
	public boolean contains(Class<?> type) {
		return extractors.containsKey(type);
	}

	/**
	 * Returns the extracted types.
	 * @return The extracted types.
	 */
	public Set<Class<?>> typeSet() {
		return extractors.keySet();
	}

	/**
	 * Performs an extraction.
	 * @param target Target object.
	 * @param valueType Value type.
	 * @param <V> Value type.
	 */
	public final <V> V extract(Object target, Class<V> valueType) {
		final DirectExtractor e = extractors.get(valueType);
		Preconditions.checkArgument(e != null);
		return valueType.cast(e.extract(target));
	}

	/**
	 * A direct extractor.
	 * @author Andres Rodriguez
	 */
	private class DirectExtractor extends Extractor {
		DirectExtractor(final Method method) {
			super(method);
			if (!Components.IS_EXTRACTABLE.apply(getReturnType())) {
				throw new IllegalExtractionMethodException(getType(), method);
			}
		}

		/**
		 * Extracts the value.
		 * @param target Target object.
		 * @return Extracted value.
		 */
		public final Object extract(Object target) {
			return invoke(target);
		}
	}
}
