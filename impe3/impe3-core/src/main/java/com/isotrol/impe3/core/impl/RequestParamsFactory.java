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


import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.ws.rs.core.UriInfo;

import net.sf.derquinsej.CaseIgnoringString;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.RequestParams;


/**
 * Factory class for different HTTP request parameters implementations.
 * @author Andres Rodriguez
 */
public final class RequestParamsFactory {
	/** Not instantiable. */
	private RequestParamsFactory() {
		throw new AssertionError();
	}

	private static final Empty EMPTY = new Empty();

	/**
	 * Returns an empty collection of cookies.
	 */
	public static RequestParams of() {
		return EMPTY;
	}

	/**
	 * Adds the parameters from a servlet request.
	 * @param request Servlet request.
	 * @return The request parameters.
	 */
	public static RequestParams of(ServletRequest request) {
		Preconditions.checkNotNull(request, "The request cannot be null.");
		final ImmutableMultimap.Builder<CaseIgnoringString, String> builder = ImmutableMultimap.builder();
		@SuppressWarnings("unchecked")
		final Enumeration<String> names = request.getParameterNames();
		if (names != null) {
			while (names.hasMoreElements()) {
				final String parameter = names.nextElement();
				final String[] values = request.getParameterValues(parameter);
				if (values != null && values.length > 0) {
					builder.putAll(CaseIgnoringString.valueOf(parameter), Arrays.asList(values));
				}
			}
		}
		return new Immutable(builder.build());
	}

	/**
	 * Returns the collection of request parameters from a JAX-RS headers object.
	 * @param info URI information.
	 * @return The request query parameters.
	 */
	public static RequestParams of(UriInfo info) {
		Preconditions.checkNotNull(info, "The URI object cannot be null.");
		final ImmutableMultimap.Builder<CaseIgnoringString, String> builder = ImmutableMultimap.builder();
		for (Entry<String, List<String>> entry : info.getQueryParameters().entrySet()) {
			List<String> values = entry.getValue();
			if (values != null) {
				builder.putAll(CaseIgnoringString.valueOf(entry.getKey()), values);
			}
		}
		return new Immutable(builder.build());
	}

	/**
	 * Returns the collection of request parameters from a multimap object.
	 * @param map Multimap.
	 * @return The request query parameters.
	 */
	public static RequestParams of(Multimap<String, String> map) {
		if (map == null || map.isEmpty()) {
			return EMPTY;
		}
		final ImmutableMultimap.Builder<CaseIgnoringString, String> builder = ImmutableMultimap.builder();
		for (String key : map.keySet()) {
			final CaseIgnoringString cis = CaseIgnoringString.valueOf(key);
			builder.putAll(cis, map.get(key));
		}
		return new Immutable(builder.build());
	}

	/**
	 * Empty implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Empty extends EmptyMultiStringParameters implements RequestParams {
		private Empty() {
		}

		public RequestParams subset(Set<String> included) {
			return this;
		}
	}

	/**
	 * Immutable HTTP request params implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Immutable extends AbstractMultiStringParameters.AbstractImmutable implements
		RequestParams {
		private Immutable(Multimap<CaseIgnoringString, String> parameters) {
			super(parameters);
		}

		public RequestParams subset(Set<String> included) {
			return new Immutable(submap(included));
		}
	}

}
