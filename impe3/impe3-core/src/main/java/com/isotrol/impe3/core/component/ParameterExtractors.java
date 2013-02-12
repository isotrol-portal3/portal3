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


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ExtractedParameter;


/**
 * Collection of parameters extractors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public abstract class ParameterExtractors<T extends Component> extends Extractors<T> {
	private final ImmutableList<DynamicExtractor> extractors;

	ParameterExtractors(final Class<T> type, Iterable<Method> methods, final Class<? extends Annotation> annotation) {
		super(type);
		final ImmutableList.Builder<DynamicExtractor> builder = ImmutableList.builder();
		for (Method m : filter(methods, annotation)) {
			builder.add(new DynamicExtractor(m));
		}
		this.extractors = builder.build();
	}

	@Override
	public boolean isEmpty() {
		return extractors.isEmpty();
	}

	final void put(String name, Object value, Multimap<String, String> map) {
		if (name == null || value == null) {
			return;
		}
		if (value instanceof Iterable) {
			for (Object o : (Iterable<?>) value) {
				put(name, o, map);
			}
		} else {
			map.put(name, value.toString());
		}
	}

	/**
	 * Performs an extraction.
	 * @param target Target object.
	 * @param Application map.
	 */
	final void extractTo(Object target, Map<String, Object> map) {
		for (DynamicExtractor e : extractors) {
			final ExtractedParameter<?> p = e.extract(target);
			if (p != null) {
				String name = p.getName();
				if (name != null) {
					map.put(p.getName(), p.getValue());
				}
			}
		}
	}

	/**
	 * Performs an extraction.
	 * @param target Target object.
	 * @param Application map.
	 */
	final void extractTo(Object target, Multimap<String, String> map) {
		for (DynamicExtractor e : extractors) {
			final ExtractedParameter<?> p = e.extract(target);
			if (p != null) {
				String name = p.getName();
				if (name != null) {
					put(p.getName(), p.getValue(), map);
				}
			}
		}
	}

	/**
	 * A dynamic extractor.
	 * @author Andres Rodriguez
	 */
	private class DynamicExtractor extends Extractor {
		DynamicExtractor(final Method method) {
			super(method);
			if (!ExtractedParameter.class.isAssignableFrom(method.getReturnType())) {
				throw new IllegalExtractionMethodException(getType(), method);
			}
		}

		/**
		 * Extracts the value.
		 * @param target Target object.
		 * @return Extracted parameter.
		 */
		public final ExtractedParameter<?> extract(Object target) {
			return ExtractedParameter.class.cast(invoke(target));
		}
	}
}
