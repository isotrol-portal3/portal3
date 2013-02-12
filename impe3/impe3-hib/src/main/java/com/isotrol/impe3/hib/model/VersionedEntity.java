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

package com.isotrol.impe3.hib.model;


import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;


/**
 * Abstract superclass for model entities with a version field.
 * @author Andres Rodriguez
 */
@MappedSuperclass
public abstract class VersionedEntity extends Entity implements WithVersion {
	/** Version number. */
	@Version
	@Column(name = "VERSION")
	private int version = 0;

	/** Default constructor. */
	public VersionedEntity() {
		super();
	}

	/**
	 * Constructor.
	 * @param id Entity ID.
	 */
	public VersionedEntity(UUID id) {
		this(id, 0);
	}
	
	/**
	 * Constructor.
	 * @param id Entity ID.
	 * @param version Version number.
	 */
	public VersionedEntity(UUID id, int version) {
		super(id);
		this.version = version;
	}

	/**
	 * Returns the version number.
	 * @return The version number.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version number.
	 * @param version The version number.
	 */
	public void setVersion(int version) {
		this.version = version;
	}
}
