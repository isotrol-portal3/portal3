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

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * Mapped superclass for publishable entities definitions.
 * @author Andres Rodriguez
 * @param <D> Definition type.
 * @param <T> Publishable Entity type.
 */
@MappedSuperclass
public abstract class Definition<D extends Definition<D, T, P>, T extends PublishableEntity<T, D, P>, P extends PublishedEntity>
	extends WithUpdatedEntity {
	/** Whether the definition has ever been published. */
	@Column(name = "EVER_PUBLISHED", nullable = true)
	private Boolean everPublished = false;

	/** Default constructor. */
	public Definition() {
	}

	/**
	 * Returns whether the definition has ever been published.
	 * @return True the definition has ever been published.
	 */
	public Boolean getEverPublished() {
		return everPublished;
	}

	/**
	 * Sets whether the definition has ever been published.
	 * @param everPublished True the definition has ever been published.
	 */
	public void setEverPublished(Boolean everPublished) {
		this.everPublished = everPublished;
	}

	/**
	 * Returns the publishable entity.
	 * @return The publishable entity.
	 */
	public abstract T getEntity();

	/**
	 * Sets the publishable entity.
	 * @param entity The publishable entity.
	 */
	public abstract void setEntity(T entity);

	/** Returns the editions of a definition. */
	public abstract Set<P> getEditions();
}
