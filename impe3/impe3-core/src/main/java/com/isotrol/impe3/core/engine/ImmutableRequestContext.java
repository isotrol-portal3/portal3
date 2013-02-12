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

package com.isotrol.impe3.core.engine;


import java.util.Locale;

import javax.ws.rs.core.Request;

import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.Headers;
import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.RequestContext;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.SessionParams;


/**
 * Immutable implementation of RequestContext. This is a non-final class. Subclasses must guarantee immutability.
 * @author Andres Rodriguez
 */
class ImmutableRequestContext implements RequestContext {
	private final ImmutableClientRequestContext clientContext;
	private final ImmutableHttpRequestContext httpContext;

	ImmutableRequestContext(ClientRequestContext clientContext, HttpRequestContext httpContext) {
		this.clientContext = RequestContexts.copyOf(clientContext);
		this.httpContext = RequestContexts.copyOf(httpContext);
	}

	ImmutableRequestContext(RequestContext context) {
		if (context instanceof ImmutableRequestContext) {
			ImmutableRequestContext irc = (ImmutableRequestContext) context;
			this.clientContext = irc.clientContext;
			this.httpContext = irc.httpContext;
		} else {
			this.clientContext = RequestContexts.copyOf((ClientRequestContext) context);
			this.httpContext = RequestContexts.copyOf((HttpRequestContext) context);
		}
	}

	public final Device getDevice() {
		return clientContext.getDevice();
	}

	public final DeviceCapabilities getDeviceCapabilities() {
		return clientContext.getDeviceCapabilities();
	}

	public final Locale getLocale() {
		return clientContext.getLocale();
	}

	public final Request getJAXRSRequest() {
		return httpContext.getJAXRSRequest();
	}

	public final boolean isSecure() {
		return httpContext.isSecure();
	}

	public final Headers getHeaders() {
		return httpContext.getHeaders();
	}

	public final Cookies getCookies() {
		return httpContext.getCookies();
	}

	public final RequestParams getRequestParams() {
		return httpContext.getRequestParams();
	}

	public final SessionParams getSessionParams() {
		return httpContext.getSessionParams();
	}

}
