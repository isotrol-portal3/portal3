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


import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;


/**
 * Abstract mapped superclass for portal child entities.
 * @author Andres Rodriguez
 */
@MappedSuperclass
public class OfPortal extends WithCreatedEntity {
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRTL_ID", nullable = false)
	private PortalEntity portal;

	/** Default constructor. */
	public OfPortal() {
	}

	/**
	 * Returns the portal.
	 * @return The portal.
	 */
	public PortalEntity getPortal() {
		return portal;
	}

	/**
	 * Sets the portal.
	 * @param portal The portal.
	 */
	public void setPortal(PortalEntity portal) {
		this.portal = portal;
	}
}
