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

package com.isotrol.impe3.web.rest;


import java.util.Map.Entry;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.core.PageResponse;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.core.impl.CookiesFactory;
import com.isotrol.impe3.core.impl.HeadersFactory;
import com.isotrol.impe3.core.impl.RequestParamsFactory;
import com.isotrol.impe3.core.impl.SessionParamsFactory;
import com.isotrol.impe3.pms.core.PortalManager;
import com.isotrol.impe3.pms.core.engine.AbstractResource;
import com.sun.jersey.spi.resource.PerRequest;


/**
 * External resources implementation.
 */
@Path("/{path:.*}")
@PerRequest
@Component
@Scope("prototype")
public class ExternalResource extends AbstractResource {
	@Context
	private HttpHeaders headers;
	@Context
	private UriInfo uri;
	@Context
	private SecurityContext securityContext;
	@Context
	private Request request;

	public ExternalResource() {
	}

	@GET
	public Response getPage(@PathParam("path") @Encoded String path) {
		final PathSegments segments = PathSegments.of(path, true);
		final HttpSession session = getSession();
		final HttpRequestContext requestContext = RequestContexts.http(request, securityContext.isSecure(), HeadersFactory.of(headers),
			CookiesFactory.of(headers), RequestParamsFactory.of(uri), SessionParamsFactory.of(session));
		try {
			final PageResponse response = getEngine().process(segments, headers, requestContext);
			for (Entry<String, Object> sp : response.getSession().entrySet()) {
				final String key = sp.getKey();
				final Object value = sp.getValue();
				if (value == null) {
					session.removeAttribute(key);
				} else {
					session.setAttribute(key, value);
				}
			}
			return response.getResponse();
		} catch (RuntimeException e) {
			if (uri != null && uri.getQueryParameters() != null
				&& uri.getQueryParameters().containsKey(PortalManager.MASK_ERROR)) {
				return Response.ok().type(MediaType.TEXT_HTML).entity("<html></html>").build();
			}
			throw e;
		}
	}

}
