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
 * Base class for DTOs with a string id and version.
 * @author Andres Rodriguez
 */
public class AbstractVersionedStringId extends AbstractStringId implements WithVersion {
	/** Serial UID. */
	private static final long serialVersionUID = -8080046984847519350L;

	/** Version. */
	private int version;

	/**
	 * Constructor.
	 * @param id Id.
	 * @param version Version.
	 */
	public AbstractVersionedStringId(String id, int version) {
		super(id);
		this.version = version;
	}

	/**
	 * Default constructor.
	 */
	public AbstractVersionedStringId() {
	}
	
	/**
	 * @see com.isotrol.impe3.dto.WithVersion#getVersion()
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * @see com.isotrol.impe3.dto.WithVersion#setVersion(int)
	 */
	public void setVersion(int version) {
		this.version = version;
	}
}
