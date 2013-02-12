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

package com.isotrol.impe3.web.pms.rest;


import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.core.OfflineEngine;
import com.isotrol.impe3.pms.core.PMSContext;
import com.isotrol.impe3.pms.core.engine.AbstractResource;
import com.sun.jersey.spi.resource.PerRequest;


/**
 * Preview resource implementation.
 */
@Path("preview/{portalId}/{pageId}")
@PerRequest
@Component
@Scope("prototype")
public class PreviewResource extends AbstractResource {
	/** PMS Context. */
	@Autowired
	private PMSContext context;

	public PreviewResource() {
	}

	@GET
	public Response getPreview(@PathParam("portalId")
	String portalId, @PathParam("pageId")
	String pageId) {
		final UUID portal = toPortalUUID(portalId);
		final UUID page = toPageUUID(pageId);
		final OfflineEngine engine = getOfflineEngine();
		return engine.getPreview(portal, page, context.getLocale());
	}
}
