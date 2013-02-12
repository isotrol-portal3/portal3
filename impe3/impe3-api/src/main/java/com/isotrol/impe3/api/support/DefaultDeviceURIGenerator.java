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

package com.isotrol.impe3.api.support;


import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceURIGenerator;
import com.isotrol.impe3.api.DevicesInPortal;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;


/**
 * Default device URI generator.
 * @author Andres Rodriguez
 */
public class DefaultDeviceURIGenerator implements DeviceURIGenerator {
	private static final DefaultDeviceURIGenerator INSTANCE = new DefaultDeviceURIGenerator();

	public static DeviceURIGenerator get() {
		return INSTANCE;
	}

	/** Constructor. */
	private DefaultDeviceURIGenerator() {
	}

	/**
	 * @see com.isotrol.impe3.api.DeviceURIGenerator#getTransformer(com.isotrol.impe3.api.Portal,
	 * com.isotrol.impe3.api.Device)
	 */
	public Function<PathSegments, PathSegments> getTransformer(Portal portal, Device device) {
		if (portal != null && device != null) {
			DevicesInPortal dips = portal.getDevices();
			if (dips.containsDevice(device)) {
				return dips.getByDevice(device).getTransformer();
			}
		}
		return Functions.identity();
	}
}
