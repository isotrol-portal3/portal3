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

package com.isotrol.impe3.api;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;


/**
 * Value describing an output device.
 * @author Andres Rodriguez
 */
public final class Device extends AbstractIdentifiable {
	public static final Function<Device, String> NAME = new Function<Device, String>() {
		public String apply(Device from) {
			return from.getName();
		}
	};

	public static final Function<Device, DeviceType> TYPE = new Function<Device, DeviceType>() {
		public DeviceType apply(Device from) {
			return from.getType();
		}
	};

	/** Device type. */
	private final DeviceType type;
	/** Device name. */
	private final String name;
	/** Device description. */
	private final String description;
	/** Layout width. */
	private final Integer width;
	/** User agent. */
	private final UserAgentPattern userAgent;
	/** Properties. */
	private final ImmutableMap<String, String> properties;

	public Device(UUID id, DeviceType type, String name, String description, Integer width, UserAgentPattern userAgent,
		Map<String, String> properties) {
		super(id);
		this.type = checkNotNull(type);
		this.name = checkNotNull(name);
		this.description = description;
		if (type.isLayout()) {
			this.width = width != null ? width : type.getWidth();
		} else {
			this.width = null;
		}
		if (userAgent == null) {
			this.userAgent = UserAgentPattern.none();
		} else {
			this.userAgent = userAgent;
		}
		if (properties == null) {
			this.properties = ImmutableMap.of();
		} else {
			this.properties = ImmutableMap.copyOf(properties);
		}
	}

	/**
	 * Returns the device type.
	 * @return The device type.
	 */
	public DeviceType getType() {
		return type;
	}

	/**
	 * Returns the device name.
	 * @return The device name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the device description.
	 * @return The device description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns whether the device supports layout.
	 * @return True if the device supports layout.
	 */
	public boolean isLayout() {
		return type.isLayout();
	}

	/**
	 * Returns the device width.
	 * @return The device width.
	 */
	public Integer getWidth() {
		return width;
	}

	public ImmutableMap<String, String> getProperties() {
		return properties;
	}

	/**
	 * Checks whether the devices matches an user agent.
	 * @param ua User agent string to match.
	 * @return True if there is a match.
	 */
	public boolean matchesUA(String ua) {
		if (ua == null || ua.length() == 0) {
			return false;
		}
		return userAgent.apply(ua);
	}

	@Override
	public String toString() {
		return String.format("Device[%s:%s]", getStringId(), name);
	}
}
