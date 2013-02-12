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


import javax.persistence.MappedSuperclass;


/**
 * Mapped superclass class for versioned entities with an update record.
 * @author Andres Rodriguez
 */
@MappedSuperclass
public abstract class WithUpdatedEntity extends VersionedEntity implements WithUpdated {
	/** Update record. */
	private Updated updated;

	/** Default constructor. */
	public WithUpdatedEntity() {
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithUpdated#getUpdated()
	 */
	public Updated getUpdated() {
		return updated;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithUpdated#setUpdated(com.isotrol.impe3.pms.model.Updated)
	 */
	public void setUpdated(Updated updated) {
		this.updated = updated;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithUpdated#setUpdated(com.isotrol.impe3.pms.model.UserEntity)
	 */
	public void setUpdated(UserEntity user) {
		setUpdated(new Updated(user));
	}

}
