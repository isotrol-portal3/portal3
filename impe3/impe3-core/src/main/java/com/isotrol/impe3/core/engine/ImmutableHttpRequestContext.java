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


import static com.google.common.base.Preconditions.checkNotNull;

import javax.ws.rs.core.Request;

import net.sf.derquinsej.Proxies;

import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.api.Headers;
import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.SessionParams;
import com.isotrol.impe3.core.impl.CookiesFactory;
import com.isotrol.impe3.core.impl.HeadersFactory;
import com.isotrol.impe3.core.impl.RequestParamsFactory;
import com.isotrol.impe3.core.impl.SessionParamsFactory;


/**
 * Immutable implementation of HttpRequestContext.
 * @author Andres Rodriguez
 */
final class ImmutableHttpRequestContext implements HttpRequestContext {
	private final Request request;
	private final boolean secure;
	private final Headers headers;
	private final Cookies cookies;
	private final RequestParams requestParams;
	private final SessionParams sessionParams;

	static final ImmutableHttpRequestContext EMPTY = new ImmutableHttpRequestContext();

	private ImmutableHttpRequestContext() {
		this.request = Proxies.alwaysNull(Request.class);
		this.secure = false;
		this.headers = HeadersFactory.of();
		this.cookies = CookiesFactory.of();
		this.requestParams = RequestParamsFactory.of();
		this.sessionParams = SessionParamsFactory.of();
	}

	ImmutableHttpRequestContext(Request request, boolean secure, Headers headers, Cookies cookies,
		RequestParams requestParams, SessionParams sessionParams) {
		this.request = checkNotNull(request);
		this.secure = secure;
		this.headers = checkNotNull(headers);
		this.cookies = checkNotNull(cookies);
		this.requestParams = checkNotNull(requestParams);
		this.sessionParams = checkNotNull(sessionParams);
	}

	public Request getJAXRSRequest() {
		return request;
	}

	public boolean isSecure() {
		return secure;
	}

	public Headers getHeaders() {
		return headers;
	}

	public Cookies getCookies() {
		return cookies;
	}

	public RequestParams getRequestParams() {
		return requestParams;
	}

	public SessionParams getSessionParams() {
		return sessionParams;
	}
}
