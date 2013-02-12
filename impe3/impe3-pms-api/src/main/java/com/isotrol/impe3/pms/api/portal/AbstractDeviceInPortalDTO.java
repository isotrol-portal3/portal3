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

import com.isotrol.impe3.pms.api.device.DeviceSelDTO;


/**
 * Abstract DTO for device selection in a portal.
 * @author Andres Rodriguez
 */
public abstract class AbstractDeviceInPortalDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 2464610174727412977L;
	/** Device. */
	private DeviceSelDTO device;
	/** Device name in portal. */
	private String name;
	/** Whether the device is the default device. */
	private boolean defaultDevice;

	/** Default constructor. */
	public AbstractDeviceInPortalDTO() {
	}

	/**
	 * Returns the device id.
	 * @return The device id.
	 */
	public String getDeviceId() {
		return device != null ? device.getId() : null;
	}

	/**
	 * Returns the device.
	 * @return The device.
	 */
	public DeviceSelDTO getDevice() {
		return device;
	}

	/**
	 * Sets the device.
	 * @param device The device.
	 */
	public void setDevice(DeviceSelDTO device) {
		this.device = device;
	}

	/**
	 * Returns the device name in the portal.
	 * @return The device name in the portal.
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
	 * Returns whether the device is the default device.
	 * @return True if the device is the default device.
	 */
	public boolean isDefaultDevice() {
		return defaultDevice;
	}

	/**
	 * Sets whether the device is the default device.
	 * @param defaultDevice True if the device is the default device.
	 */
	public void setDefaultDevice(boolean defaultDevice) {
		this.defaultDevice = defaultDevice;
	}
}
