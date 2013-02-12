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


import com.isotrol.impe3.pms.api.AbstractWithStateAndId;


/**
 * DTO for portal parent setting.
 * @author Andres Rodriguez
 */
public class PortalParentDTO extends AbstractWithStateAndId {
	/** Serial UID. */
	private static final long serialVersionUID = 1435281801058737450L;
	/** Current parent. */
	private PortalSelDTO parent;
	/** Possible portals. */
	private PortalTreeDTO portalTree;

	/** Default constructor. */
	public PortalParentDTO() {
	}

	/**
	 * Returns the parent portal.
	 * @return The parent portal.
	 */
	public PortalSelDTO getParent() {
		return parent;
	}

	/**
	 * Sets the parent portal.
	 * @param parent The parent portal.
	 */
	public void setParent(PortalSelDTO parent) {
		this.parent = parent;
	}

	/**
	 * Returns the possible portals.
	 * @return The possible portalss.
	 */
	public PortalTreeDTO getPortalTree() {
		return portalTree;
	}

	/**
	 * Sets the possible portals.
	 * @param portalTree The possible portals.
	 */
	public void setPortalTree(PortalTreeDTO portalTree) {
		this.portalTree = portalTree;
	}

}
