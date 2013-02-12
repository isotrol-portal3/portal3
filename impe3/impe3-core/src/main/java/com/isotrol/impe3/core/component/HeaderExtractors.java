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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ExtractDynHeader;
import com.isotrol.impe3.api.component.ExtractHeader;


/**
 * Collection of header extractors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public final class HeaderExtractors<T extends Component> extends ParameterExtractors<T> {
	private final ImmutableList<HeaderExtractor> extractors;

	static <T extends Component> HeaderExtractors<T> of(Class<T> type, Iterable<Method> methods) {
		return new HeaderExtractors<T>(type, methods);
	}

	private HeaderExtractors(final Class<T> type, Iterable<Method> methods) {
		super(type, methods, ExtractDynHeader.class);
		final ImmutableList.Builder<HeaderExtractor> builder = ImmutableList.builder();
		for (Method m : filter(methods, ExtractHeader.class)) {
			builder.add(new HeaderExtractor(m));
		}
		this.extractors = builder.build();
	}

	@Override
	public boolean isEmpty() {
		return extractors.isEmpty() && super.isEmpty();
	}

	/**
	 * Performs an extraction.
	 * @param target Target object.
	 * @param map Destination multimap.
	 */
	public final Multimap<String, String> extract(Object target) {
		final Multimap<String, String> map = LinkedListMultimap.create();
		for (HeaderExtractor e : extractors) {
			e.extract(target, map);
		}
		extractTo(target, map);
		return map;
	}

	/**
	 * A header extractor.
	 * @author Andres Rodriguez
	 */
	private class HeaderExtractor extends Extractor {
		/** Parameter name. */
		private final String name;

		HeaderExtractor(final Method method) {
			super(method);
			this.name = method.getAnnotation(ExtractHeader.class).value();
		}

		/**
		 * Extracts the value.
		 * @param target Target object.
		 * @param target Target map.
		 * @return Extracted value.
		 */
		public final void extract(Object target, Multimap<String, String> map) {
			put(invoke(target), map);
		}

		private void put(Object value, Multimap<String, String> map) {
			if (value == null) {
				return;
			}
			if (value instanceof Iterable) {
				for (Object o : (Iterable<?>) value) {
					put(o, map);
				}
			} else {
				map.put(name, value.toString());
			}
		}
	}
}
