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

package com.isotrol.impe3.api.component;


import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.isotrol.impe3.api.Route;


/**
 * Value representing the result of a component execution.
 * @author Andres Rodriguez
 */
public abstract class ComponentResponse {
	/** The OK response. */
	public static final ComponentResponse OK = new Ok();

	public static Internal internal(Status status, Route route) {
		return new Internal(status, route);
	}

	public static Internal internal(Route route) {
		return internal(Status.OK, route);
	}

	public static External seeOther(URI uri) {
		return new External(Response.seeOther(uri).build());
	}

	public static External temporaryRedirect(URI uri) {
		return new External(Response.temporaryRedirect(uri).build());
	}

	/** Default constructor. */
	private ComponentResponse() {
	}

	/**
	 * Returns the HTTP status.
	 */
	public abstract Status getStatus();

	private static final class Ok extends ComponentResponse {
		private Ok() {
		}

		@Override
		public Status getStatus() {
			return Status.OK;
		}
	}

	/**
	 * A redirection response.
	 * @author Andres Rodriguez
	 */
	public abstract static class Redirection extends ComponentResponse {
		private Redirection() {
		}

	}

	/**
	 * An internal redirection response.
	 * @author Andres Rodriguez
	 */
	public static final class Internal extends Redirection {
		private final Route route;
		private final Status status;

		private Internal(final Status status, final Route route) {
			this.status = checkNotNull(status);
			this.route = checkNotNull(route);
		}

		public Route getRoute() {
			return route;
		}

		@Override
		public final Status getStatus() {
			return status;
		}

	}

	/**
	 * An external redirection response.
	 * @author Andres Rodriguez
	 */
	public static final class External extends Redirection {
		private final Response response;

		private External(final Response response) {
			this.response = checkNotNull(response);
		}

		public final Response getResponse() {
			return response;
		}

		@Override
		public final Status getStatus() {
			return Status.fromStatusCode(response.getStatus());
		}
	}

}
