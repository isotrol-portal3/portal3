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


import java.io.Serializable;

import com.isotrol.impe3.api.DeviceNameUse;


/**
 * Update DTO for device selection in a portal.
 * @author Andres Rodriguez
 */
public class DiPDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -4058258915912610104L;
	/** Device name. */
	private String name;
	/** Device name use. */
	private DeviceNameUse use;

	/** Default constructor. */
	public DiPDTO() {
	}

	/**
	 * Constructor.
	 * @param name Device name.
	 * @param use Device name use.
	 */
	public DiPDTO(String name, DeviceNameUse use) {
		this.name = name;
		this.use = use;
	}

	/**
	 * Returns the device name.
	 * @return The device name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the device name.
	 * @param name The device name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the device name use.
	 * @return The device name use.
	 */
	public DeviceNameUse getUse() {
		if (use == null) {
			return DeviceNameUse.NONE;
		}
		return use;
	}

	/**
	 * Sets the device name use.
	 * @param use The device name use.
	 */
	public void setUse(DeviceNameUse use) {
		this.use = use;
	}
}
