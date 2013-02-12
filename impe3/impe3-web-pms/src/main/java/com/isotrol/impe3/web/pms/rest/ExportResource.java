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


import static com.isotrol.impe3.core.WebExceptions.found;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.ExportJobManager;
import com.isotrol.impe3.pms.core.ExportResult;
import com.isotrol.impe3.pms.core.engine.AbstractResource;
import com.sun.jersey.spi.resource.PerRequest;


/**
 * PMS Export resource implementation.
 */
@Path("export/{jobId}")
@PerRequest
@Component
@Scope("prototype")
public class ExportResource extends AbstractResource {
	@Autowired
	private ExportJobManager exportJobManager;

	public ExportResource() {
	}

	@GET
	public Response export(@PathParam("jobId")
	String jobId) {
		try {
			ExportResult result = exportJobManager.export(jobId);
			found(result != null, String.format("Export Job [%s] not found", jobId));
			return result.getResponse();
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (PMSException e) {
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}

}
