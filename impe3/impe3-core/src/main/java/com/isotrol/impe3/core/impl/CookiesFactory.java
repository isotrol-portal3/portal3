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


import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.core.impl.MapParameters.AbstractImmutable;


/**
 * Factory class for different Cookies collection implementations.
 * @author Andres Rodriguez
 */
public final class CookiesFactory {
	/** Not instantiable. */
	private CookiesFactory() {
		throw new AssertionError();
	}

	private static final Empty EMPTY = new Empty();

	/**
	 * Returns an empty collection of cookies.
	 */
	public static Cookies of() {
		return EMPTY;
	}

	/**
	 * Returns a coolection of cookies containing the provided cookies.
	 * @param cookies Cookies to add to the collection.
	 * @return The requested collection.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if there are more than one cookie with the same name.
	 */
	public static Cookies of(Iterable<Cookie> cookies) {
		Preconditions.checkNotNull(cookies);
		final ImmutableMap.Builder<String, Cookie> builder = ImmutableMap.builder();
		for (final Cookie cookie : cookies) {
			builder.put(cookie.getName(), cookie);
		}
		return new Immutable(builder.build());
	}

	private static final Function<javax.servlet.http.Cookie, Cookie> COOKIE_IN = new Function<javax.servlet.http.Cookie, Cookie>() {
		public Cookie apply(javax.servlet.http.Cookie from) {
			return new Cookie(from.getName(), from.getValue(), from.getPath(), from.getDomain(), from.getVersion());
		};
	};

	/**
	 * Returns a coolection of cookies from a HTTP Servlet HTTP request.
	 * @param request HTTP Servlet request..
	 * @return The requested collection.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if there are more than one cookie with the same name.
	 */
	public static Cookies of(HttpServletRequest request) {
		Preconditions.checkNotNull(request);
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return EMPTY;
		}
		return of(transform(asList(cookies), COOKIE_IN));
	}

	/**
	 * Returns a collection of cookies from a JAX-RS headers object.
	 * @param headers HTTP Headers.
	 * @return The request cookies.
	 */
	public static Cookies of(HttpHeaders headers) {
		Preconditions.checkNotNull(headers, "The request headers object cannot be null.");
		final Map<String, Cookie> cookies = headers.getCookies();
		if (cookies == null || cookies.isEmpty()) {
			return EMPTY;
		}
		return new Immutable(cookies);
	}

	/**
	 * Empty HTTP cookies collection implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Empty extends EmptyParameters<Cookie> implements Cookies {
		private Empty() {
		}

		public Cookies subset(Set<String> included) {
			return this;
		}
	}

	/**
	 * Immutable HTTP cookies collection implementation.
	 * @author Andres Rodriguez
	 */
	private static final class Immutable extends AbstractImmutable<Cookie> implements Cookies {

		private Immutable(Map<String, Cookie> cookies) {
			super(cookies);
		}

		public Cookies subset(Set<String> included) {
			return new Immutable(submap(included));
		}
	}
}
