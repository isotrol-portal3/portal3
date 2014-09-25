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
 * Class for transformed internal service exceptions.
 * @author Andres Rodriguez
 */
public class InternalServiceException extends ServiceException {
	/** Serial UID. */
	private static final long serialVersionUID = 6819421092927301427L;
	/** Internal error code. */
	private final String errorCode;

	/**
	 * Constructor.
	 * @param errorCode Error code.
	 */
	public InternalServiceException(Object errorCode) {
		super(500);
		if (errorCode == null) {
			this.errorCode = null;
		} else {
			this.errorCode = errorCode.toString();
		}
	}

	/** Default constructor. */
	public InternalServiceException() {
		this(null);
	}

	/**
	 * Returns the internal error code.
	 * @return The internal error code.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	@Override
	public String getMessage() {
		if (errorCode == null) {
			return "Internal service error";
		}
		return "Internal service error with code [" + errorCode + ']';
	}

}
