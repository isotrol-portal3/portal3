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


import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import net.sf.derquinsej.uuid.UUIDGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.api.ActionNotFoundPortalException;
import com.isotrol.impe3.api.PortalException;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.PageResponse;


/**
 * Exception mapper for JAX-RS.
 * @author Andres Rodriguez.
 */
@Component
@Provider
public final class ImpeExceptionMapper implements ExceptionMapper<Exception> {
	/** UUID generator for exception identification. */
	@Autowired
	private UUIDGenerator uuidGenerator;

	public ImpeExceptionMapper() {
	}

	public Response toResponse(Exception exception) {
		// Generate exception id
		final UUID id = uuidGenerator.get();
		// Check whether is an action.
		final ActionRequest r = ActionRequest.consume();
		if (r == null) {
			return mapNormal(id, exception, "JAX-RS resource");
		}
		if (exception instanceof ActionNotFoundPortalException) {
			return ((ActionNotFoundPortalException) exception).getResponse();
		}
		// Log exception
		Loggers.core().error(String.format("Exception [%s] during action [%s] processing", id, r.getAction()), exception);
		try {
			PageResponse response = r.getEngine().processActionException(r.getPortalId(), r.getDeviceId(),
				r.getLocale(), r.getHeaders(), r.getRequest(), r.getQuery(), exception);
			for (Entry<String, Object> sp : response.getSession().entrySet()) {
				final String key = sp.getKey();
				final Object value = sp.getValue();
				if (value == null) {
					r.getSession().removeAttribute(key);
				} else {
					r.getSession().setAttribute(key, value);
				}
			}
			return response.getResponse();
		} catch (Exception e2) {
			return mapNormal(id, e2, "action exception");
		}
	}

	private Response mapNormal(UUID id, Exception exception, String when) {
		if (exception instanceof PortalException && exception.getCause() != null) {
			final PortalException pe = (PortalException) exception;
			final Response r = pe.getResponse();
			Loggers.core().error(String.format("Port@l Exception [%s] during %s processing", id, when), exception);
			final ResponseBuilder b = Response.fromResponse(r);
			b.entity(String.format("Port@l Exception [%s]: %s", id, r.getEntity()));
			return b.build();
		}
		if (exception instanceof WebApplicationException) {
			return ((WebApplicationException) exception).getResponse();
		}
		Loggers.core().error(String.format("Exception [%s] during %s processing", id, when), exception);
		return Response.serverError().type(MediaType.TEXT_PLAIN_TYPE)
			.entity(String.format("Exception [%s]. See error log for details", id)).build();
	}
}
