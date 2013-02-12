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

package com.isotrol.impe3.pms.api.device;


import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.pms.api.AbstractWithId;


/**
 * DTO for device selection.
 * @author Andres Rodriguez
 */
public class DeviceSelDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = 3060221809026145610L;
	/** Device name. */
	private String name;
	/** Layout width. */
	private Integer width;
	/** Device type. */
	private DeviceType type;

	/** Default constructor. */
	public DeviceSelDTO() {
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
	 * Returns the layout width.
	 * @return The layout width.
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * Sets the layout width.
	 * @param width The layout width.
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * Returns the device type.
	 * @return The device type.
	 */
	public DeviceType getType() {
		return type;
	}

	/**
	 * Sets the device type.
	 * @param name The device type.
	 */
	public void setType(DeviceType type) {
		this.type = type;
	}
	
	/**
	 * Returns whether the device supports layout.
	 * @return True if the device supports layout.
	 */
	public boolean isLayout() {
		return type != null && type.isLayout();
	}
}
