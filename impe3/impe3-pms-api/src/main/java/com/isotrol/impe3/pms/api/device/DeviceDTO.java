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


import java.util.List;

import com.isotrol.impe3.pms.api.PropertyDTO;


/**
 * DTO for device management.
 * @author Andres Rodriguez
 */
public class DeviceDTO extends DeviceSelDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 2396135911898296447L;
	/** Whether the device is the default device. */
	private boolean defaultDevice;
	/** Device description. */
	private String description;
	/** User agent. */
	private String userAgent;
	/** Whether the user agent is a regular expression. */
	private boolean userAgentRE;
	/** Properties. */
	private List<PropertyDTO> properties;

	/** Default constructor. */
	public DeviceDTO() {
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
	 * Returns the device description.
	 * @return The device description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the device description.
	 * @param description The device description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the user agent.
	 * @return The user agent.
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * Sets the user agent.
	 * @param userAgent The user agent.
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * Returns whether the user agent is a regular expression.
	 * @return True if the user agent is a regular expression.
	 */
	public boolean isUserAgentRE() {
		return userAgentRE;
	}

	/**
	 * Sets whether the the user agent is a regular expression.
	 * @param userAgentRE True if the the user agent is a regular expression.
	 */
	public void setUserAgentRE(boolean userAgentRE) {
		this.userAgentRE = userAgentRE;
	}

	/**
	 * Returns the device properties.
	 * @return The device properties.
	 */
	public List<PropertyDTO> getProperties() {
		return properties;
	}

	/**
	 * Sets the device properties.
	 * @param properties The device properties.
	 */
	public void setProperties(List<PropertyDTO> properties) {
		this.properties = properties;
	}

}
