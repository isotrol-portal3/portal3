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

package com.isotrol.impe3.pms.model;


import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import com.google.common.base.Objects;


/**
 * Abstract superclass for model entities.
 * @author Andres Rodriguez
 */
@MappedSuperclass
public abstract class Entity {
	/** Entity ID. */
	@Id
	@Type(type = "impeId")
	@Column(name = "ID", length = Lengths.UUID, nullable = false)
	private UUID id;

	/** Default constructor. */
	public Entity() {
	}

	/**
	 * Constructor.
	 * @param id Entity ID.
	 */
	public Entity(UUID id) {
		this.id = id;
	}

	/**
	 * @return The id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Sets the id
	 * @param id the id to set.
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return (id == null) ? 0 : id.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof Entity) {
			final Entity e = (Entity) obj;
			return Objects.equal(getId(), e.getId());
		}
		return false;
	}

}
