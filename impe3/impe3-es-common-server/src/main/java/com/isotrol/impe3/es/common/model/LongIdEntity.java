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

package com.isotrol.impe3.es.common.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract superclass for model entities.
 * @author Andres Rodriguez
 */
@MappedSuperclass
public abstract class LongIdEntity {
	/** Entity ID. */
	/*
	@Id
	@Column(name = "ID", nullable = false)
	@GenericGenerator(name = "gen", strategy = "increment")
	@GeneratedValue(generator = "gen")
	// @Generated(GenerationTime.ALWAYS)
	*/
	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Default constructor. */
	public LongIdEntity() {
	}

	/**
	 * Constructor.
	 * @param id Entity ID.
	 */
	public LongIdEntity(Long id) {
		this.id = id;
	}

	/**
	 * Returns the id.
	 * @return The id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id
	 * @param id the id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (id == null) ? 0 : id.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LongIdEntity) {
			final LongIdEntity e = (LongIdEntity) obj;
			return id != null && getEntityType().equals(e.getEntityType()) && id.equals(e.id);
		}
		return false;
	}

	protected Class<? extends LongIdEntity> getEntityType() {
		return getClass();
	}
}
