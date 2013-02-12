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


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import javax.ws.rs.core.Response;


/**
 * Resolved element inside a portal.
 * @author Andres Rodriguez.
 */
public abstract class Resolved {
	/** Response to interrupt processing. */
	private final Response response;
	/** Remaining path segments. */
	private final PathSegments path;
	/** Local parameters. */
	private final LocalParams parameters;

	/**
	 * Constructor for an interrupting resolution.
	 * @param response Response.
	 */
	Resolved(Response response) {
		this.response = checkNotNull(response);
		this.path = null;
		this.parameters = null;
	}

	/**
	 * Constructor for a normal resolution.
	 * @param path Remaining path segments (not {@code null}).
	 * @param parameters Local parameters (not {@code null}).
	 */
	Resolved(PathSegments path, LocalParams parameters) {
		this.response = null;
		this.path = checkNotNull(path);
		this.parameters = checkNotNull(parameters);
	}

	final void normal() {
		checkState(isNormal());
	}

	private void interrupting() {
		checkState(!isNormal());
	}

	public final boolean isNormal() {
		return response == null;
	}

	/**
	 * Returns the response.
	 * @return The response.
	 * @throws IllegalStateException for a normal resolution.
	 */
	public final Response getResponse() {
		interrupting();
		return response;
	}

	/**
	 * Returns the remaining path segments.
	 * @return The remaining path segments.
	 * @throws IllegalStateException for an interrupting resolution.
	 */
	public final PathSegments getPath() {
		normal();
		return path;
	}

	/**
	 * Returs the local parameters collection.
	 * @return The local parameters collection.
	 * @throws IllegalStateException for an interrupting resolution.
	 */
	public final LocalParams getParameters() {
		normal();
		return parameters;
	}

}
