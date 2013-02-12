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


import java.util.Date;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.core.WebExceptions;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.PMSContext;
import com.isotrol.impe3.pms.core.engine.AbstractResource;
import com.sun.jersey.spi.resource.PerRequest;


/**
 * Uploaded file resource.
 */
@Path("ufile/{id}")
@PerRequest
@Component
@Scope("prototype")
public class UploadedFileResource extends AbstractResource {
	/** PMS Context. */
	@Autowired
	private PMSContext context;
	@Autowired
	private FileManager fileManager;

	public UploadedFileResource() {
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

	private Response getUploadedFile(FileData data) {
		return getFile(data).header("Content-Disposition", "attachment; filename=" + data.getName()).build();
	}

	@GET
	public Response getUploadedFile(@PathParam("id")
	String id, @PathParam("name")
	String name) {
		if (context == null || context.getUserId() == null) {
			throw WebExceptions.notFound("No current session found");
		}
		try {
			final UUID uuid = UUID.fromString(id);
			final FileData data = fileManager.getFile(uuid);
			return getUploadedFile(data);
		} catch (Exception e) {
			throw WebExceptions.notFound(String.format("Uploaded file [%s/%s] not found", id, name));
		}
	}

}
