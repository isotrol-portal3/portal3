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


import static com.google.common.base.Preconditions.checkArgument;
import static com.isotrol.impe3.core.WebExceptions.notFound;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import net.sf.derquinsej.i18n.Locales;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.core.Engine;
import com.isotrol.impe3.core.WebExceptions;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.core.impl.CookiesFactory;
import com.isotrol.impe3.core.impl.HeadersFactory;
import com.isotrol.impe3.core.impl.RequestParamsFactory;
import com.isotrol.impe3.core.impl.SessionParamsFactory;
import com.isotrol.impe3.pms.core.engine.AbstractResource;
import com.sun.jersey.spi.resource.PerRequest;


/**
 * Internal resources implementation.
 */
@Path("-")
@PerRequest
@Component
@Scope("prototype")
public class InternalResource extends AbstractResource {
	/** PMS Context. */
	@Context
	private HttpHeaders headers;
	@Context
	private SecurityContext securityContext;
	@Context
	private UriInfo uri;
	@Context
	private Request request;

	public InternalResource() {
	}

	private Date expires() {
		return new Date(System.currentTimeMillis() + 1L * 360 * 24 * 3600 * 1000);
	}

	private ResponseBuilder getFile(FileData data) {
		final EntityTag tag = new EntityTag(data.getId().toString().toLowerCase());
		final ResponseBuilder builder = Response.ok(data.getData(), data.getMediaType());
		builder.expires(expires());
		builder.tag(tag);
		return builder;
	}

	private Response getDownloadableFile(FileData data) {
		checkArgument(data.isDownloadable());
		return getFile(data).build();
	}

	private ResponseBuilder evaluateFilePreconditions(String id) {
		final EntityTag tag = new EntityTag(id);
		return request.evaluatePreconditions(tag);
	}

	@Path("file/{id}/{name}")
	@GET
	public Response getFile(@PathParam("id")
	String id, @PathParam("name")
	String name) {
		try {
			final UUID uuid = UUID.fromString(id);
			ResponseBuilder b = evaluateFilePreconditions(id);
			if (b != null) {
				return b.build();
			}
			final FileData data = getFileLoader().load(FileId.of(uuid, name));
			return getDownloadableFile(data);
		} catch (IllegalArgumentException e) {
			throw WebExceptions.notFound(String.format("File [%s/%s] not found", id, name));
		}
	}

	@Path("fitem/{id}/{name:.+}")
	@GET
	public Response getFileBundleItem(@PathParam("id")
	String id, @PathParam("name")
	String name) {
		try {
			final UUID uuid = UUID.fromString(id);
			ResponseBuilder b = evaluateFilePreconditions(id);
			if (b != null) {
				return b.build();
			}
			final FileData data = getFileLoader().loadFromBundle(uuid, name);
			return getDownloadableFile(data);
		} catch (IllegalArgumentException e) {
			throw WebExceptions.notFound(String.format("File bundle item [%s/%s] not found", id, name));
		}
	}

	@Path("action/{portalId}/{deviceId}/{locale}/{cipId}/{name}")
	public Object getAction(@PathParam("portalId")
	String portalId, @PathParam("deviceId")
	String deviceId, @PathParam("locale")
	String locale, @PathParam("cipId")
	String cipId, @PathParam("name")
	String name, @Context
	UriInfo uriInfo) {
		final UUID portal = toPortalUUID(portalId);
		final UUID device = toPageUUID(deviceId);
		final Locale loc;
		try {
			loc = Locales.fromString(locale);
		} catch (IllegalArgumentException e) {
			throw notFound("Unable to resolve action locale");
		}
		final UUID cip = toUUID(cipId, "CIP Id");
		final HttpSession session = getSession();
		final HttpRequestContext requestContext = RequestContexts.http(getCSRFToken(), request, securityContext.isSecure(),
			HeadersFactory.of(headers), CookiesFactory.of(headers), RequestParamsFactory.of(uri),
			SessionParamsFactory.of(session));
		final Engine engine = getEngine();
		final String actionLoc = cipId + ":" + name;
		setActionRequest(engine, portal, device, loc, actionLoc, headers, requestContext, uriInfo.getQueryParameters(),
			getSession());
		return engine.getAction(portal, device, loc, cip, name, headers, requestContext, uriInfo.getQueryParameters());
	}
}
