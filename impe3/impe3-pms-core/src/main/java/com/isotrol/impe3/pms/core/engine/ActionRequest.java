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

package com.isotrol.impe3.pms.core.engine;


import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.core.Engine;


/**
 * Action request data for exception processing.
 * @author Andres Rodriguez.
 */
public final class ActionRequest {
	private static final ThreadLocal<ActionRequest> CURRENT = new ThreadLocal<ActionRequest>();

	private final Engine engine;
	private final UUID portalId;
	private final UUID deviceId;
	private final Locale locale;
	private final String action;
	private final HttpHeaders headers;
	private final HttpRequestContext request;
	private final MultivaluedMap<String, String> query;
	private final HttpSession session;

	static ActionRequest consume() {
		ActionRequest r = CURRENT.get();
		if (r != null) {
			CURRENT.set(null);
		}
		return r;
	}

	static void set(ActionRequest request) {
		CURRENT.set(request);
	}
	
	public static void clear() {
		CURRENT.set(null);
	}

	ActionRequest(Engine engine, UUID portalId, UUID deviceId, Locale locale, String action, HttpHeaders headers,
		HttpRequestContext request, MultivaluedMap<String, String> query, HttpSession session) {
		this.engine = engine;
		this.portalId = portalId;
		this.deviceId = deviceId;
		this.locale = locale;
		this.action = action;
		this.headers = headers;
		this.request = request;
		this.query = query;
		this.session = session;
	}

	Engine getEngine() {
		return engine;
	}

	UUID getPortalId() {
		return portalId;
	}

	UUID getDeviceId() {
		return deviceId;
	}

	Locale getLocale() {
		return locale;
	}
	
	String getAction() {
		return action;
	}

	HttpHeaders getHeaders() {
		return headers;
	}

	HttpRequestContext getRequest() {
		return request;
	}

	MultivaluedMap<String, String> getQuery() {
		return query;
	}

	HttpSession getSession() {
		return session;
	}

}
