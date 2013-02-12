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

package com.isotrol.impe3.core;


import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.PathSegments;


/**
 * Interface for a running engine. Implementations of this interface may be created per request.
 * @author Andres
 * 
 */
public interface Engine {
	/**
	 * Process a request.
	 * @param path List of decoded path segments.
	 * @param headers JAX-RS Headers.
	 * @param request Impe request.
	 * @return Page response.
	 */
	PageResponse process(PathSegments path, HttpHeaders headers, HttpRequestContext request);

	/**
	 * Process an action exception page.
	 * @param portalId Portal Id.
	 * @param deviceId Device Id.
	 * @param locale Locale.
	 * @param headers JAX-RS Headers.
	 * @param request Impe request.
	 * @param query Query parameters.
	 * @param exception Thrown exception.
	 * @return Page response.
	 */
	PageResponse processActionException(UUID portalId, UUID deviceId, Locale locale, HttpHeaders headers,
		HttpRequestContext request, MultivaluedMap<String, String> query, Exception exception);

	/**
	 * Returns an action subresource.
	 * @param portalId Portal Id.
	 * @param deviceId Device Id.
	 * @param locale Locale.
	 * @param cipId Component in page id.
	 * @param name Action name.
	 * @param headers JAX-RS Headers.
	 * @param request Impe request.
	 * @param query Query parameters.
	 * @return The requested action sub-resource.
	 */
	Object getAction(UUID portalId, UUID deviceId, Locale locale, UUID cipId, String name, HttpHeaders headers,
		HttpRequestContext request, MultivaluedMap<String, String> query);
}
