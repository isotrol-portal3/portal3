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
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import net.sf.derquinsej.CaseIgnoringString;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.Headers;


/**
 * Factory class for different HTTP headers implementations.
 * @author Andres Rodriguez
 */
public final class HeadersFactory {
	/** Not instantiable. */
	private HeadersFactory() {
		throw new AssertionError();
	}

	private static final Empty EMPTY = new Empty();

	/**
	 * Returns an empty collection of cookies.
	 */
	public static Headers of() {
		return EMPTY;
	}

	/**
	 * Adds the parameters from a servlet request.
	 * @param request Servlet request.
	 * @return The request parameters.
	 */
	public static Headers of(HttpServletRequest request) {
		Preconditions.checkNotNull(request, "The request cannot be null.");
		final ImmutableMultimap.Builder<CaseIgnoringString, String> builder = ImmutableMultimap.builder();
		@SuppressWarnings("unchecked")
		final Enumeration<String> names = request.getHeaderNames();
		if (names != null) {
			while (names.hasMoreElements()) {
				final String header = names.nextElement();
				@SuppressWarnings("unchecked")
				final Enumeration<String> values = request.getHeaders(header);
				if (values != null && values.hasMoreElements()) {
					final CaseIgnoringString cih = CaseIgnoringString.valueOf(header);
					while (values.hasMoreElements()) {
						builder.put(cih, values.nextElement());
					}
				}
			}
		}
		return new Immutable(builder.build());
	}

	/**
	 * Adds the parameters from a JAX-RS headers object.
	 * @param headers HTTP Headers.
	 * @return The request headers.
	 */
	public static Headers of(HttpHeaders headers) {
		Preconditions.checkNotNull(headers, "The request headers object cannot be null.");
		final ImmutableMultimap.Builder<CaseIgnoringString, String> builder = ImmutableMultimap.builder();
		for (Entry<String, List<String>> entry : headers.getRequestHeaders().entrySet()) {
			List<String> values = entry.getValue();
			if (values != null) {
				builder.putAll(CaseIgnoringString.valueOf(entry.getKey()), values);
			}
		}
		return new Immutable(builder.build());
	}

	/**
	 * Empty implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Empty extends EmptyMultiStringParameters implements Headers {
		private Empty() {
		}

		public Headers subset(Set<String> included) {
			return this;
		}
	}

	/**
	 * Immutable HTTP request params implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Immutable extends AbstractMultiStringParameters.AbstractImmutable implements Headers {
		private Immutable(Multimap<CaseIgnoringString, String> headers) {
			super(headers);
		}

		public Headers subset(Set<String> included) {
			return new Immutable(submap(included));
		}
	}

}
