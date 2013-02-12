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


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.firstNonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.google.common.base.Objects;
import com.isotrol.impe3.api.DeviceNameUse;


/**
 * Value that represents a device used in a portal.
 * @author Andres Rodriguez
 */
@Embeddable
public class PortalDeviceValue implements Cloneable {
	/** Device name. */
	@Column(name = "PRDV_NAME", nullable = false, length = Lengths.NAME)
	private String name;
	/** Whether the device is the default device. */
	@Column(name = "PRDV_DEFAULT", nullable = false)
	private boolean defaultDevice;
	/** Device name use. */
	@Column(name = "PRDV_USE", nullable = true, length = Lengths.NAME)
	@Enumerated(EnumType.STRING)
	private DeviceNameUse use;

	/** Default constructor. */
	public PortalDeviceValue() {
	}

	/**
	 * Constructor.
	 * @param name The device name.
	 * @param defaultDevice True if the device is the default device.
	 * @param use The device name use.
	 */
	public PortalDeviceValue(String name, boolean defaultDevice, DeviceNameUse use) {
		this.name = name;
		this.defaultDevice = defaultDevice;
		this.use = firstNonNull(use, DeviceNameUse.NONE);
	}

	/**
	 * Constructor.
	 * @param name The device name.
	 * @param defaultDevice True if the device is the default device.
	 */
	public PortalDeviceValue(String name, boolean defaultDevice) {
		this.name = name;
		this.defaultDevice = defaultDevice;
	}

	/**
	 * Clones the value.
	 */
	public PortalDeviceValue clone() {
		return new PortalDeviceValue(name, defaultDevice, use);
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

	@Override
	public int hashCode() {
		return Objects.hashCode(name, defaultDevice);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PortalDeviceValue) {
			final PortalDeviceValue v = (PortalDeviceValue) obj;
			return defaultDevice == v.defaultDevice && equal(name, v.name);
		}
		return false;
	}
}
