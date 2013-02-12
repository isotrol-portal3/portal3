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

package com.isotrol.impe3.api;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.net.URI.create;

import java.net.URI;


/**
 * Value representing a routing base.
 * @author Andres Rodriguez
 */
public abstract class RoutingBase {
	private static final String BASE = "The base URI must be provided";
	private static final String ABS = "The absolute URI must be provided";
	private static final String NO_ABS = "No absolute URI provided";

	/**
	 * Creates a routing base with an absolute uri.
	 * @param uri Absolute URI.
	 * @return The requested base.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if the URI is not absolute.
	 */
	public static RoutingBase of(URI uri) {
		checkNotNull(uri, ABS);
		checkArgument(uri.isAbsolute(), "The provided URI [%s] is not absolute", uri.toASCIIString());
		return new Single(uri);
	}

	/**
	 * Creates a routing base with an absolute uri.
	 * @param uri Absolute URI.
	 * @return The requested base.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if the URI is not absolute.
	 * @throws IllegalArgumentException if the argument is not a valid URI.
	 */
	public static RoutingBase of(String uri) {
		return of(create(checkNotNull(uri, ABS)));
	}

	/**
	 * Creates a routing base from a maybe non-absolute uri and an absolute one. If the fisrt argument is an abolute URI
	 * the second is ignored.
	 * @param base Base URI.
	 * @param absolute Absolute URI.
	 * @return The requested base.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if no absolute URI is provided.
	 */
	public static RoutingBase of(URI base, URI absolute) {
		checkNotNull(base, BASE);
		if (base.isAbsolute()) {
			return new Single(base);
		}
		checkArgument(absolute != null && absolute.isAbsolute(), NO_ABS);
		return new Double(base, absolute);
	}

	/**
	 * Creates a routing base from a maybe non-absolute uri and an absolute one. If the fisrt argument is an abolute URI
	 * the second is ignored.
	 * @param base Base URI.
	 * @param absolute Absolute URI.
	 * @return The requested base.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if no absolute URI is provided.
	 * @throws IllegalArgumentException if any of the argument is not a valid URI.
	 */
	public static RoutingBase of(String base, String absolute) {
		checkNotNull(base, BASE);
		final URI b = create(base);
		if (b.isAbsolute()) {
			return new Single(b);
		}
		checkArgument(absolute != null, NO_ABS);
		final URI abs = create(absolute);
		checkArgument(abs.isAbsolute(), NO_ABS);
		return new Double(b, abs);
	}

	/** Constructor. */
	private RoutingBase() {
	}

	/**
	 * Returns the base URI.
	 * @return The base URI.
	 */
	public abstract URI getBase();

	/**
	 * Returns the absolute base URI.
	 * @return The absolute base URI.
	 */
	public abstract URI getAbsoluteBase();

	private static final class Single extends RoutingBase {
		private final URI uri;

		private Single(URI uri) {
			this.uri = uri;
		}

		@Override
		public URI getBase() {
			return uri;
		}

		@Override
		public URI getAbsoluteBase() {
			return uri;
		}
	}

	private static final class Double extends RoutingBase {
		private final URI base;
		private final URI absolute;

		private Double(URI base, URI absolute) {
			this.base = base;
			this.absolute = absolute;
		}

		@Override
		public URI getBase() {
			return base;
		}

		@Override
		public URI getAbsoluteBase() {
			return absolute;
		}
	}
}
