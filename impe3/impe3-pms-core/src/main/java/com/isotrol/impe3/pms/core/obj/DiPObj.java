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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.isotrol.impe3.api.DeviceNameUse;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.PortalDeviceValue;


/**
 * Device in portal domain object.
 * @author Andres Rodriguez
 */
final class DiPObj {
	public static final Function<DiPObj, String> NAME = new Function<DiPObj, String>() {
		public String apply(DiPObj from) {
			return from.getName();
		}
	};
	public static final Function<DiPObj, UUID> DEVICE_ID = new Function<DiPObj, UUID>() {
		public UUID apply(DiPObj from) {
			return from.getDeviceId();
		}
	};

	/** Device id. */
	private final UUID deviceId;
	/** Device name. */
	private final String name;
	/** Device name use. */
	private final DeviceNameUse use;

	DiPObj(UUID deviceId, String name, DeviceNameUse use) {
		this.deviceId = checkNotNull(deviceId);
		this.name = checkNotNull(name);
		this.use = checkNotNull(use);
	}

	DiPObj(DeviceEntity device, PortalDeviceValue value) {
		this(checkNotNull(device, "Device").getId(), checkNotNull(value, "PortalDeviceValue").getName(), value.getUse());
	}

	DiPObj(Entry<DeviceEntity, PortalDeviceValue> entry) {
		this(entry.getKey(), entry.getValue());
	}

	DiPObj(DeviceEntity entity) {
		this(entity.getId(), entity.getName(), DeviceNameUse.NONE);
	}

	/**
	 * Returns the device id.
	 * @return The device id.
	 */
	UUID getDeviceId() {
		return deviceId;
	}

	/**
	 * Returns the device name.
	 * @return The device name.
	 */
	String getName() {
		return name;
	}

	/**
	 * Returns the device name use.
	 * @return The device name use.
	 */
	DeviceNameUse getUse() {
		if (use == null) {
			return DeviceNameUse.NONE;
		}
		return use;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(deviceId, name, use);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DiPObj) {
			final DiPObj key = (DiPObj) obj;
			return equal(deviceId, key.deviceId) && equal(name, key.name) && equal(use, key.use);
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("DiPObj[%s/%s/%s]", deviceId, name, use);
	}
}
