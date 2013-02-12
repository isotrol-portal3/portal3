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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import javax.ws.rs.core.Response;

import com.google.common.collect.ImmutableMap;


/**
 * A page response contains the HTTP response and an optional map elements to include in the session.
 * @author Andres Rodriguez
 */
public final class PageResponse {
	/** HTTP response. */
	private final Response response;
	/** Session elements. */
	private final Map<String, Object> session;

	public PageResponse(final Response response, Map<String, Object> session) {
		this.response = checkNotNull(response);
		if (session == null) {
			this.session = ImmutableMap.of();
		} else {
			this.session = session;
		}
	}

	public Response getResponse() {
		return response;
	}

	public Map<String, Object> getSession() {
		return session;
	}
}
