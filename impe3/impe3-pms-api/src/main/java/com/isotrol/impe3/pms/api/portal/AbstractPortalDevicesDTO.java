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

package com.isotrol.impe3.pms.api.portal;


import com.isotrol.impe3.pms.api.AbstractWithId;


/**
 * Abstract DTO for portal device selection.
 * @author Andres Rodriguez
 */
public abstract class AbstractPortalDevicesDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = -6813828893748639635L;
	/** Whether the selection is inherited. */
	private boolean inherited;

	/** Default constructor. */
	public AbstractPortalDevicesDTO() {
	}

	/**
	 * Returns whether the selection is inherited.
	 * @return True if the selection is inherited.
	 */
	public boolean isInherited() {
		return inherited;
	}

	/**
	 * Sets whether the selection is inherited.
	 * @param inherited True if the selection is inherited.
	 */
	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}
}
