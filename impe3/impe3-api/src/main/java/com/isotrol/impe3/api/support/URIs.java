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

package com.isotrol.impe3.api.support;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import javax.ws.rs.core.UriBuilder;

import com.google.common.collect.Multimap;


/**
 * Utility methods for URIs.
 * @author Andres Rodriguez.
 */
public final class URIs {
	/** HTTP scheme. */
	public static final String HTTP_SCHEME = "http";
	/** Example authority. */
	public static final String EXAMPLE_AUTH = "example.com";
	/** Example URI. */
	public static final URI EXAMPLE_URI = http(EXAMPLE_AUTH, "");

	private URIs() {
		throw new AssertionError();
	}

	/**
	 * Creates a path URI.
	 * @param path Path.
	 * @return
	 */
	public static URI path(String path) {
		try {
			return new URI(null, null, path, null);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Creates and HTTP URI.
	 * @param host Host.
	 * @param path Path.
	 * @param fragment Fragment.
	 * @return
	 */
	public static URI http(String host, String path, String fragment) {
		try {
			return new URI(HTTP_SCHEME, host, path, fragment);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Creates and HTTP URI.
	 * @param host Host.
	 * @param path Path.
	 * @return
	 */
	public static URI http(String host, String path) {
		return http(host, path, null);
	}

	public static UriBuilder queryParameters(UriBuilder b, Multimap<String, ?> parameters) {
		if (parameters != null) {
			for (Entry<String, ?> entry : parameters.entries()) {
				b.queryParam(entry.getKey(), entry.getValue());
			}
		}
		return b;
	}
}
