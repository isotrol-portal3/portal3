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

package com.isotrol.impe3.pms.api.connector;


import com.isotrol.impe3.pms.api.EntityInUseException;


/**
 * Exception thrown when a connector cannot be deleted because it is being used.
 * @author Andres Rodriguez
 */
public class ConnectorInUseException extends EntityInUseException {
	/** Serial UID. */
	private static final long serialVersionUID = 9131959902684115966L;

	/**
	 * Default constructor.
	 * @param id Entity ID.
	 */
	public ConnectorInUseException(String id) {
		super(id);
	}

	/**
	 * Default constructor.
	 */
	public ConnectorInUseException() {
	}

}
