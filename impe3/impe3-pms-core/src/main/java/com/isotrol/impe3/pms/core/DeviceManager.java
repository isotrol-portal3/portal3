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

package com.isotrol.impe3.pms.core;


import java.util.UUID;

import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.obj.DevicesObject;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Interface for the users registry.
 * @author Andres Rodriguez.
 */
public interface DeviceManager {
	/** Default device. */
	String DEFAULT = "WEB1024";

	/**
	 * Finds a device by name, creating it if it does not exist.
	 * @param user The user creating the device.
	 * @param type Device type.
	 * @param name Device name.
	 * @param description Device description.
	 * @param layout Whether the device has layout.
	 * @param width Layout width.
	 * @return The found or created device.
	 */
	DeviceEntity findOrCreate(UserEntity user, DeviceType type, String name, String description, boolean layout,
		Integer width) throws PMSException;

	/**
	 * Loads the registered devices.
	 * @param envId Environment Id.
	 * @return The collection of registered devices.
	 */
	DevicesObject load(UUID envId);
}
