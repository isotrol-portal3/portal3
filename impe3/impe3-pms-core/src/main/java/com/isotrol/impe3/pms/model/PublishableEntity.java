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


import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * Mapped superclass class for publishable entities.
 * @author Andres Rodriguez
 * @param <D> Definition type.
 */
@MappedSuperclass
public abstract class PublishableEntity<E extends PublishableEntity<E, D, P>, D extends Definition<D, E, P>, P extends PublishedEntity>
	extends OfEnvironment {
	/** Whether the entity is deleted. */
	@Column(name = "DELETED", nullable = false)
	private boolean deleted = false;

	/** Whether the entity has ever been published. */
	@Column(name = "EVER_PUBLISHED", nullable = true)
	private Boolean everPublished = false;

	/** Default constructor. */
	public PublishableEntity() {
	}

	/** Returns the entity name. */
	public abstract String getEntityName();

	/**
	 * Returns whether the entity is deleted.
	 * @return True the entity is deleted.
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets whether the entity is deleted.
	 * @param deleted True the entity is deleted.
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Returns whether the entity has ever been published.
	 * @return True the entity has ever been published.
	 */
	public Boolean getEverPublished() {
		return everPublished;
	}

	/**
	 * Sets whether the entity has ever been published.
	 * @param everPublished True the entity has ever been published.
	 */
	public void setEverPublished(boolean everPublished) {
		this.everPublished = everPublished;
	}

	/**
	 * Returns the current definition.
	 * @return The current definition.
	 */
	public abstract D getCurrent();

	/**
	 * Sets the current definition.
	 * @param current The current definition.
	 */
	public abstract void setCurrent(D current);

	/**
	 * Returns the current definition id.
	 * @return The current definition id.
	 */
	public UUID getCurrentId() {
		final Entity current = getCurrent();
		if (current != null) {
			return current.getId();
		}
		return null;
	}

	/**
	 * Returns the definitions of the current entity.
	 * @return The set of definitions.
	 */
	public abstract Set<D> getDefinitions();

}
