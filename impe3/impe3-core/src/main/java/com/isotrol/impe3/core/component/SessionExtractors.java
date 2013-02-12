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
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ExtractDynSession;
import com.isotrol.impe3.api.component.ExtractSession;


/**
 * Collection of session extractors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public final class SessionExtractors<T extends Component> extends ParameterExtractors<T> {
	private final ImmutableList<SessionExtractor> extractors;

	static <T extends Component> SessionExtractors<T> of(Class<T> type, Iterable<Method> methods) {
		return new SessionExtractors<T>(type, methods);
	}

	private SessionExtractors(final Class<T> type, Iterable<Method> methods) {
		super(type, methods, ExtractDynSession.class);
		final ImmutableList.Builder<SessionExtractor> builder = ImmutableList.builder();
		for (Method m : filter(methods, ExtractSession.class)) {
			builder.add(new SessionExtractor(m));
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
	 * @param map Destination map.
	 */
	public Map<String, Object> extract(Object target) {
		final Map<String, Object> map = Maps.newHashMap();
		for (SessionExtractor e : extractors) {
			e.extract(target, map);
		}
		extractTo(target, map);
		return map;
	}

	/**
	 * A session extractor.
	 * @author Andres Rodriguez
	 */
	private class SessionExtractor extends Extractor {
		/** Parameter name. */
		private final String name;

		SessionExtractor(final Method method) {
			super(method);
			this.name = method.getAnnotation(ExtractSession.class).value();
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
