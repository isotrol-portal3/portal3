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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ExtractDynLocal;
import com.isotrol.impe3.api.component.ExtractLocal;


/**
 * Collection of local extractors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public final class LocalExtractors<T extends Component> extends ParameterExtractors<T> {
	private final ImmutableList<LocalExtractor> extractors;

	static <T extends Component> LocalExtractors<T> of(Class<T> type, Iterable<Method> methods) {
		return new LocalExtractors<T>(type, methods);
	}

	private LocalExtractors(final Class<T> type, Iterable<Method> methods) {
		super(type, methods, ExtractDynLocal.class);
		final ImmutableList.Builder<LocalExtractor> builder = ImmutableList.builder();
		for (Method m : filter(methods, ExtractLocal.class)) {
			builder.add(new LocalExtractor(m));
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
	 * @return Application map.
	 */
	public final Map<String, Object> extract(Object target) {
		if (isEmpty()) {
			return ImmutableMap.of();
		}
		final Map<String, Object> map = Maps.newHashMap();
		for (LocalExtractor e : extractors) {
			e.extract(target, map);
		}
		extractTo(target, map);
		return map;
	}

	/**
	 * A local extractor.
	 * @author Andres Rodriguez
	 */
	private class LocalExtractor extends Extractor {
		/** Parameter name. */
		private final String name;

		LocalExtractor(final Method method) {
			super(method);
			this.name = method.getAnnotation(ExtractLocal.class).value();
		}

		/**
		 * Extracts the value.
		 * @param target Target object.
		 * @param target Target map.
		 * @return Extracted value.
		 */
		public final void extract(Object target, Map<String, Object> map) {
			map.put(name, invoke(target));
		}
	}
}
