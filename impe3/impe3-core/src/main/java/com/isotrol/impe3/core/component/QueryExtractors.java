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
import com.isotrol.impe3.api.component.ExtractDynQuery;
import com.isotrol.impe3.api.component.ExtractQuery;


/**
 * Collection of query parameters extractors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public final class QueryExtractors<T extends Component> extends ParameterExtractors<T> {
	private final ImmutableList<QueryExtractor> extractors;

	static <T extends Component> QueryExtractors<T> of(Class<T> type, Iterable<Method> methods) {
		return new QueryExtractors<T>(type, methods);
	}

	private QueryExtractors(final Class<T> type, Iterable<Method> methods) {
		super(type, methods, ExtractDynQuery.class);
		final ImmutableList.Builder<QueryExtractor> builder = ImmutableList.builder();
		for (Method m : filter(methods, ExtractQuery.class)) {
			builder.add(new QueryExtractor(m));
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
	 * @return Destination multimap.
	 */
	public final Multimap<String, String> extract(Object target) {
		final Multimap<String, String> map = LinkedListMultimap.create();
		for (QueryExtractor e : extractors) {
			e.extract(target, map);
		}
		extractTo(target, map);
		return map;
	}

	/**
	 * A query parameters extractor.
	 * @author Andres Rodriguez
	 */
	private class QueryExtractor extends Extractor {
		/** Parameter name. */
		private final String name;

		QueryExtractor(final Method method) {
			super(method);
			this.name = method.getAnnotation(ExtractQuery.class).value();
		}

		/**
		 * Extracts the value.
		 * @param target Target object.
		 * @param target Target map.
		 * @return Extracted value.
		 */
		public final void extract(Object target, Multimap<String, String> map) {
			put(name, invoke(target), map);
		}

	}
}
