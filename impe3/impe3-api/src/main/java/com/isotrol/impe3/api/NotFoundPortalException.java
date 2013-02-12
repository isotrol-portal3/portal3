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


/**
 * Base class for Port@l exceptions with a 404 HTTP status code.
 * @author Andres Rodriguez.
 */
public class NotFoundPortalException extends PortalException {
	/** Serial UID. */
	private static final long serialVersionUID = -7592849358264285609L;

	/**
	 * Default constructor.
	 */
	public NotFoundPortalException() {
		super(404);
	}

	/**
	 * Constructor.
	 * @param cause The underlying cause of the exception.
	 */
	public NotFoundPortalException(Throwable cause) {
		super(cause, 404);
	}

	/**
	 * Constructor.
	 * @param cause The underlying cause of the exception.
	 * @param message Message to return to the HTTP client.
	 */
	public NotFoundPortalException(Throwable cause, String message) {
		super(cause, 404, message);
	}

	/**
	 * Constructor.
	 * @param message Message to return to the HTTP client.
	 */
	public NotFoundPortalException(String message) {
		super(404, message);
	}
}
