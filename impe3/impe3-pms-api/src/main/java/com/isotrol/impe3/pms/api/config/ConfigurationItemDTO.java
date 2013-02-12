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

package com.isotrol.impe3.pms.api.config;


import java.io.Serializable;


/**
 * DTO for a configuration item.
 * @author Andres Rodriguez
 */
public final class ConfigurationItemDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 6743982168146120087L;
	/** Configuration item key. */
	private String key;
	/** Value. */
	private String currentValue;

	/** Default constructor. */
	public ConfigurationItemDTO() {
	}

	/**
	 * Returns the configuration item key.
	 * @return The configuration item key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the configuration item key.
	 * @param key The configuration item key.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns the current value.
	 * @return The current value.
	 */
	public String getCurrentValue() {
		return currentValue;
	}

	/**
	 * Sets the current value.
	 * @param value The current value.
	 */
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
}
