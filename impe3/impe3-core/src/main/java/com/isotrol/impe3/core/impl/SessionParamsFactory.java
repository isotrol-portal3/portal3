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


import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.SessionParams;
import com.isotrol.impe3.core.impl.MapParameters.AbstractImmutable;


/**
 * Factory class for different HTTP session parameters implementations.
 * @author Andres Rodriguez
 */
public final class SessionParamsFactory {
	/** Not instantiable. */
	private SessionParamsFactory() {
		throw new AssertionError();
	}

	private static final Empty EMPTY = new Empty();

	/**
	 * Returns an empty collection of cookies.
	 */
	public static SessionParams of() {
		return EMPTY;
	}

	/**
	 * Returns a collection built from a Servlet HTTP session.
	 * @param session Session.
	 * @return The requested collection.
	 */
	public static SessionParams of(HttpSession session) {
		Preconditions.checkNotNull(session, "The session cannot be null.");
		final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
		@SuppressWarnings("unchecked")
		final Enumeration<String> names = session.getAttributeNames();
		if (names != null) {
			while (names.hasMoreElements()) {
				final String parameter = names.nextElement();
				final Object value = session.getAttribute(parameter);
				if (value != null) {
					builder.put(parameter, value);
				}
			}
		}
		return new Immutable(builder.build());
	}

	/**
	 * Empty HTTP session parameters implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Empty extends EmptyParameters<Object> implements SessionParams {
		private Empty() {
		}

		public <T> Object get(String parameter, Class<T> type) {
			return null;
		}

		public SessionParams subset(Set<String> included) {
			return this;
		}
	}

	/**
	 * Immutable HTTP session params implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Immutable extends AbstractImmutable<Object> implements SessionParams {
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

		public SessionParams subset(Set<String> included) {
			return new Immutable(submap(included));
		}
	}
}
