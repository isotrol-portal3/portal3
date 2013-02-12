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


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Objects;


/**
 * Device in portal value.
 * @author Andres Rodriguez
 */
public final class DeviceInPortal implements WithDeviceProperty {
	public static final Function<DeviceInPortal, String> NAME = new Function<DeviceInPortal, String>() {
		public String apply(DeviceInPortal from) {
			return from.getName();
		}
	};
	public static final Function<DeviceInPortal, DeviceNameUse> USE = new Function<DeviceInPortal, DeviceNameUse>() {
		public DeviceNameUse apply(DeviceInPortal from) {
			return from.getUse();
		}
	};

	/** Device. */
	private final Device device;
	/** Device name. */
	private final String name;
	/** Device name use. */
	private final DeviceNameUse use;

	private DeviceInPortal(final Device device, final String name, final DeviceNameUse use) {
		this.device = device;
		this.name = name;
		this.use = use;
	}

	/**
	 * Creates a device in portal key.
	 * @param name Device name.
	 * @param use Device name use.
	 * @return The requested key.
	 */
	public static DeviceInPortal of(final Device device, final String name, final DeviceNameUse use) {
		return new DeviceInPortal(checkNotNull(device, "Device"), checkNotNull(name, "Device name"), checkNotNull(use,
			"Device name use"));
	}

	/**
	 * Creates a device in portal key.
	 * @param device Device.
	 * @param use Device name use.
	 * @return The requested key.
	 */
	public static DeviceInPortal of(final Device device, final DeviceNameUse use) {
		return of(device, device.getName(), use);
	}

	/**
	 * Creates a device in portal key with no use.
	 * @param device Device.
	 * @return The requested key.
	 */
	public static DeviceInPortal of(final Device device) {
		return of(device, DeviceNameUse.NONE);
	}

	/**
	 * Returns the device.
	 * @return The device.
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * Returns the device name.
	 * @return The device name.
	 */
	public String getName() {
		return name;
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

	private PathSegments segment() {
		return PathSegments.of(false, name);
	}

	/**
	 * Returns the default transformer.
	 * @return The default transformer.
	 */
	public Function<PathSegments, PathSegments> getTransformer() {
		switch (use) {
			case NONE:
				return Functions.identity();
			case FIRST_SEGMENT:
				return PathSegmentsTransformers.insert(segment());
			case LAST_SEGMENT:
				return PathSegmentsTransformers.append(segment());
			case EXTENSION:
				return PathSegmentsTransformers.extension(name);
			default:
				throw new AssertionError();
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(device, name, use);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DeviceInPortal) {
			final DeviceInPortal key = (DeviceInPortal) obj;
			return equal(device, key.device) && equal(name, key.name) && equal(use, key.use);
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("DeviceInPortal[%s/%s/%s]", device, name, use);
	}
}
