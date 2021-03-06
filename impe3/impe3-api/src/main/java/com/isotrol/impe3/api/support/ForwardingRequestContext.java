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


import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.Request;

import com.google.common.collect.ForwardingObject;
import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.Headers;
import com.isotrol.impe3.api.RequestContext;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.SessionParams;


/**
 * Forwarding request context.
 * @author Andres Rodriguez.
 */
public abstract class ForwardingRequestContext extends ForwardingObject implements RequestContext {
	/** Default constructor. */
	public ForwardingRequestContext() {
	}

	protected abstract RequestContext delegate();

	@Override
	public UUID getCSRFToken() {
		return delegate().getCSRFToken();
	}
	
	@Override
	public Request getJAXRSRequest() {
		return delegate().getJAXRSRequest();
	}

	@Override
	public boolean isSecure() {
		return delegate().isSecure();
	}

	@Override
	public Headers getHeaders() {
		return delegate().getHeaders();
	}

	@Override
	public Cookies getCookies() {
		return delegate().getCookies();
	}

	@Override
	public RequestParams getRequestParams() {
		return delegate().getRequestParams();
	}

	@Override
	public Device getDevice() {
		return delegate().getDevice();
	}

	@Override
	public DeviceCapabilities getDeviceCapabilities() {
		return delegate().getDeviceCapabilities();
	}

	@Override
	public Locale getLocale() {
		return delegate().getLocale();
	}

	@Override
	public SessionParams getSessionParams() {
		return delegate().getSessionParams();
	}

}
