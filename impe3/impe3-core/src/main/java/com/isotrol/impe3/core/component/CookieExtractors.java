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
import java.util.List;

import javax.ws.rs.core.NewCookie;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ExtractCookie;


/**
 * Collection of cookie extractors.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public final class CookieExtractors<T extends Component> extends Extractors<T> {
	private final ImmutableSet<CookieExtractor> extractors;

	static <T extends Component> CookieExtractors<T> of(Class<T> type, Iterable<Method> methods) {
		return new CookieExtractors<T>(type, filter(methods, ExtractCookie.class));
	}

	private CookieExtractors(final Class<T> type, Iterable<Method> methods) {
		super(type);
		final ImmutableSet.Builder<CookieExtractor> builder = ImmutableSet.builder();
		for (Method m : methods) {
			final CookieExtractor e = new CookieExtractor(m);
			builder.add(e);
		}
		this.extractors = builder.build();
	}

	@Override
	public boolean isEmpty() {
		return extractors.isEmpty();
	}

	/**
	 * Performs an extraction.
	 * @param target Target object.
	 * @return Actions to register.
	 */
	public final List<NewCookie> extract(Object target) {
		if (isEmpty()) {
			return ImmutableList.of();
		}
		final List<NewCookie> actions = Lists.newArrayListWithCapacity(extractors.size());
		for (CookieExtractor e : extractors) {
			final NewCookie cookie = e.extract(target);
			if (cookie != null) {
				actions.add(cookie);
			}
		}
		return actions;
	}

	/**
	 * A cookie extractor.
	 * @author Andres Rodriguez
	 */
	private class CookieExtractor extends Extractor {
		CookieExtractor(final Method method) {
			super(method);
			if (getReturnType() != NewCookie.class) {
				throw new IllegalExtractionMethodException(getType(), method);
			}
		}

		/**
		 * Extracts the value.
		 * @param target Target object.
		 * @return Extracted value.
		 */
		public final NewCookie extract(Object target) {
			return (NewCookie) invoke(target);
		}
	}
}
