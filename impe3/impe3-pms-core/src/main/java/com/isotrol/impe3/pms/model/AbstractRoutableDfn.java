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


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * Mapped superclass for routable entity definitions.
 * @author Andres Rodriguez
 * @param <D> Definition type.
 * @param <T> Publishable Entity type.
 */
@MappedSuperclass
public abstract class AbstractRoutableDfn<D extends Definition<D, T, P>, T extends PublishableEntity<T, D, P>, P extends PublishedEntity>
	extends Definition<D, T, P> implements WithName {
	/** Default name. */
	private NameValue name;
	/** If the element is routable. */
	@Column(name = "ROUTABLE", nullable = false)
	private boolean routable = true;

	/** Default constructor. */
	public AbstractRoutableDfn() {
	}

	/**
	 * Returns the default name.
	 * @return The default name.
	 */
	public NameValue getName() {
		return name;
	}

	/**
	 * Sets the default name.
	 * @param name The default name.
	 */
	public void setName(NameValue name) {
		this.name = name;
	}

	/**
	 * Returns whether the element is routable.
	 * @return True if the element is routable.
	 */
	public boolean isRoutable() {
		return routable;
	}

	/**
	 * Sets whether the element is routable.
	 * @param routable True if the element is routable.
	 */
	public void setRoutable(boolean routable) {
		this.routable = routable;
	}
}
