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

package com.isotrol.impe3.pms.core.obj;


import com.isotrol.impe3.pms.model.AbstractRoutableDfn;


/**
 * Abstract superclass for routable publishable domain objects.
 * @author Andres Rodriguez
 */
public abstract class AbstractRoutableObject extends AbstractPublishableObject {
	/** Whether the element is routable. */
	private final boolean routable;

	/**
	 * Constructor.
	 * @param dfn Definition.
	 * @param function State function to apply.
	 */
	public AbstractRoutableObject(AbstractRoutableDfn<?, ?, ?> dfn) {
		super(dfn);
		this.routable = dfn.isRoutable();
	}

	/**
	 * Returns whether the element is routable.
	 * @return True if the element is routable.
	 */
	public final boolean isRoutable() {
		return routable;
	}
}
