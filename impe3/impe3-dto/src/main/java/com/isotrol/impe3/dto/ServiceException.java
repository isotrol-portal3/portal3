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

package com.isotrol.impe3.dto;


/**
 * Base class for Service Exceptions.
 * @author Andres Rodriguez
 */
public class ServiceException extends Exception {
	/** Serial UID. */
	private static final long serialVersionUID = -6094765392508298652L;

	/** HTTP status. */
	private final int status;

	/**
	 * Constructor.
	 * @param status HTTP status to use for an eventual response.
	 */
	public ServiceException(int status) {
		this.status = status;
		// TODO: check range.
	}

	/** Default constructor. */
	public ServiceException() {
		this(500);
	}

	/**
	 * Returns the HTTP status to use for an eventual response.
	 * @return The proposed HTTP status.
	 */
	public final int getStatus() {
		return status;
	}

}
