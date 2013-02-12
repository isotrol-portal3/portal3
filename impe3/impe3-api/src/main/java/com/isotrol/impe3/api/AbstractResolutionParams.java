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

import javax.ws.rs.core.HttpHeaders;


/**
 * Parameters for device, locale and page resolution inside a portal.
 * @author Andres Rodriguez.
 */
public abstract class AbstractResolutionParams {
	/** JAX-RS Headers. */
	private final HttpHeaders headers;
	/** HTTP request context. */
	private final HttpRequestContext request;
	/** Portal. */
	private final Portal portal;
	/** Full path (from portal base). */
	private final PathSegments fullPath;
	/** Remaining decoded segment paths. */
	private final PathSegments path;
	/** Current local parameters. */
	private final LocalParams parameters;

	/**
	 * Constructor.
	 * @param headers JAX-RS Headers.
	 * @param request HTTP request context.
	 * @param portal Portal.
	 * @param fullPath Full path (from portal base).
	 * @param path Remaining decoded segment paths.
	 * @param parameters Current local parameters.
	 */
	AbstractResolutionParams(HttpHeaders headers, HttpRequestContext request, Portal portal, PathSegments fullPath,
		PathSegments path, LocalParams parameters) {
		this.headers = checkNotNull(headers);
		this.request = checkNotNull(request);
		this.portal = checkNotNull(portal);
		this.fullPath = checkNotNull(fullPath);
		this.path = checkNotNull(path);
		this.parameters = checkNotNull(parameters);
	}

	/**
	 * Copy constructor.
	 * @param params Base parameters.
	 * @param resolved Previous resolution.
	 * @throws IllegalArgumentException if the resolution is interrupting.
	 */
	AbstractResolutionParams(AbstractResolutionParams params, Resolved resolved) {
		this.headers = params.headers;
		this.request = params.request;
		this.portal = params.portal;
		this.fullPath = params.fullPath;
		checkArgument(checkNotNull(resolved).isNormal());
		this.path = resolved.getPath();
		this.parameters = resolved.getParameters();
	}

	/**
	 * Returns the JAX-RS Headers.
	 * @return The JAX-RS Headers.
	 */
	public final HttpHeaders getHeaders() {
		return headers;
	}
	
	/**
	 * Returns the portal.
	 * @return The portal.
	 */
	public final HttpRequestContext getRequest() {
		return request;
	}

	/**
	 * Returns the portal.
	 * @return The portal.
	 */
	public final Portal getPortal() {
		return portal;
	}

	/**
	 * Returns the full path (from portal base).
	 * @return The full path (from portal base).
	 */
	public final PathSegments getFullPath() {
		return fullPath;
	}

	/**
	 * Returns the remaining path segments.
	 * @return The remaining path segments.
	 */
	public final PathSegments getPath() {
		return path;
	}

	/**
	 * Returs the local parameters collection.
	 * @return The local parameters collection.
	 */
	public final LocalParams getParameters() {
		return parameters;
	}

}
