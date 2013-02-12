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


import com.isotrol.impe3.pms.api.device.DeviceSelDTO;
import com.isotrol.impe3.pms.api.portal.AbstractDeviceInPortalDTO;


/**
 * DTO for device selection in pages.
 * @author Andres Rodriguez
 */
public class PageDeviceDTO extends AbstractDeviceInPortalDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 1028698288142231586L;

	/** Default constructor. */
	public PageDeviceDTO() {
	}

	/**
	 * Returns the display name.
	 * @return The display name.
	 */
	public String getDisplayName() {
		String name = getName();
		DeviceSelDTO d = getDevice();
		if (d == null) {
			return name;
		}

		if (name == null) {
			name = d.getName();
		}

		String deviceName = d.getName();
		if (deviceName == null || deviceName.equals(name)) {
			return name;
		}

		return deviceName + " (" + name + ")";
	}

	/**
	 * Returns whether the device supports layout.
	 * @return True if the device supports layout.
	 */
	public boolean isLayout() {
		DeviceSelDTO d = getDevice();
		if (d != null) {
			return d.isLayout();
		}
		return false;
	}

}
