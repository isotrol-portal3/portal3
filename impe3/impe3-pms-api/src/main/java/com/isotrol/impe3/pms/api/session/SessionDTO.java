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

package com.isotrol.impe3.pms.api.session;


import com.isotrol.impe3.pms.api.AbstractWithId;
import com.isotrol.impe3.pms.api.user.AuthorizationDTO;


/**
 * DTO for session information. The Id is the current user Id
 * @author Andres Rodriguez
 */
public final class SessionDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = -1586569892794662830L;
	/** Current user display name. */
	private String name;
	/** Current authorization. */
	private AuthorizationDTO authorization;

	/** Default constructor. */
	public SessionDTO() {
	}

	/**
	 * Returns the current user display name.
	 * @return The current user display name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the current user display name.
	 * @param name The current user display name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the current authorization.
	 * @return The current authorization.
	 */
	public AuthorizationDTO getAuthorization() {
		return authorization;
	}

	/**
	 * Sets the current authorization.
	 * @param name The current authorization.
	 */
	public void setAuthorization(AuthorizationDTO authorization) {
		this.authorization = authorization;
	}
}
