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


import java.util.Map;


/**
 * DTO for portal device selection.
 * @author Andres Rodriguez
 */
public class PortalDevicesDTO extends AbstractPortalDevicesDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 2554707669503734710L;
	/** Default device id. */
	private String defaultId;
	/** Id to uses map. */
	private Map<String, DiPDTO> uses;

	/** Default constructor. */
	public PortalDevicesDTO() {
	}

	/**
	 * Returns the default device id.
	 * @return The default device id.
	 */
	public String getDefaultId() {
		return defaultId;
	}

	/**
	 * Sets the default device id.
	 * @param defaultId The default device id.
	 */
	public void setDefaultId(String defaultId) {
		this.defaultId = defaultId;
	}

	/**
	 * Returns the device uses.
	 * @return The device uses.
	 */
	public Map<String, DiPDTO> getUses() {
		return uses;
	}

	/**
	 * Sets the device uses.
	 * @param uses The device uses.
	 */
	public void setUses(Map<String, DiPDTO> uses) {
		this.uses = uses;
	}
}
