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

package com.isotrol.impe3.pms.api.page;


import java.io.Serializable;


/**
 * DTO for portal pages locator.
 * @author Andres Rodriguez
 */
public class PortalPagesLoc implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 5603369372611283799L;
	/** Portal Id. */
	private String portalId;
	/** Device Id. */
	private String deviceId;

	/** Default constructor. */
	public PortalPagesLoc() {
	}
	
	/**
	 * Copy constructor.
	 * @param loc Source DTO.
	 */
	public PortalPagesLoc(PortalPagesLoc loc) {
		this.portalId = loc.portalId;
		this.deviceId = loc.deviceId;
	}
	
	/**
	 * Constructor.
	 * @param portalId Portal Id.
	 * @param deviceId Device Id.
	 */
	public PortalPagesLoc(String portalId, String deviceId) {
		this.portalId = portalId;
		this.deviceId = deviceId;
	}

	/**
	 * Returns the portal id.
	 * @return The portal id.
	 */
	public String getPortalId() {
		return portalId;
	}

	/**
	 * Sets the portal id.
	 * @param portalId The portal id.
	 */
	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}

	/**
	 * Returns the device id.
	 * @return The device id.
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 * @param portalId The device id.
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
