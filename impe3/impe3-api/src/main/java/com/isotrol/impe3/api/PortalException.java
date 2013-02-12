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

package com.isotrol.impe3.api;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;


/**
 * Base class for Port@l exceptions.
 * @author Andres Rodriguez.
 */
public class PortalException extends WebApplicationException {
	/** Serial UID. */
	private static final long serialVersionUID = -4474869675545972870L;

	private static Response response(ResponseBuilder b, String message) {
		if (message != null && message.length() > 0) {
			b.type(MediaType.TEXT_PLAIN_TYPE).entity(message);
		}
		return b.build();
	}

	/**
	 * Default constructor.
	 */
	public PortalException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message Message to return to the HTTP client.
	 */
	public PortalException(String message) {
		this(500, message);
	}

	/**
	 * Constructor.
	 * @param status The HTTP status code that will be returned to the client.
	 */
	public PortalException(int status) {
		super(status);
	}

	/**
	 * Constructor.
	 * @param status The HTTP status code that will be returned to the client.
	 */
	public PortalException(Status status) {
		super(status);
	}

	/**
	 * Constructor.
	 * @param status The HTTP status code that will be returned to the client.
	 * @param message Message to return to the HTTP client.
	 */
	public PortalException(int status, String message) {
		super(response(Response.status(status), message));
	}

	/**
	 * Constructor.
	 * @param status The HTTP status code that will be returned to the client.
	 * @param message Message to return to the HTTP client.
	 */
	public PortalException(Status status, String message) {
		super(response(Response.status(status), message));
	}

	/**
	 * Constructor.
	 * @param cause The underlying cause of the exception.
	 * @param status The HTTP status code that will be returned to the client.
	 */
	public PortalException(Throwable cause, int status) {
		super(cause, status);
	}

	/**
	 * Constructor.
	 * @param cause The underlying cause of the exception.
	 * @param status The HTTP status code that will be returned to the client.
	 */
	public PortalException(Throwable cause, Status status) {
		super(cause, status);
	}

	/**
	 * Constructor.
	 * @param cause The underlying cause of the exception.
	 */
	public PortalException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param cause The underlying cause of the exception.
	 * @param status The HTTP status code that will be returned to the client.
	 * @param message Message to return to the HTTP client.
	 */
	public PortalException(Throwable cause, int status, String message) {
		super(cause, response(Response.status(status), message));
	}

	/**
	 * Constructor.
	 * @param cause The underlying cause of the exception.
	 * @param status The HTTP status code that will be returned to the client.
	 * @param message Message to return to the HTTP client.
	 */
	public PortalException(Throwable cause, Status status, String message) {
		super(cause, response(Response.status(status), message));
	}

	/**
	 * Constructor.
	 * @param cause The underlying cause of the exception.
	 * @param message Message to return to the HTTP client.
	 */
	public PortalException(Throwable cause, String message) {
		this(cause, 500, message);
	}

}
