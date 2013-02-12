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


import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceResolutionParams;
import com.isotrol.impe3.api.DeviceResolver;
import com.isotrol.impe3.api.DevicesInPortal;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.ResolvedDevice;


/**
 * Device resolver based on device names uses. It checks first on first prefix, after that on last segment, and third on
 * file extension. If no suitable device is found, {@code null} is returned.
 * @author Andres Rodriguez
 */
public class DeviceNameUseDeviceResolver implements DeviceResolver {
	private static final DeviceNameUseDeviceResolver INSTANCE = new DeviceNameUseDeviceResolver();

	/**
	 * Returns the instance.
	 * @return The requested device resolver.
	 */
	public static DeviceResolver get() {
		return INSTANCE;
	}

	/** Constructor. */
	private DeviceNameUseDeviceResolver() {
	}

	/**
	 * @see com.isotrol.impe3.api.DeviceResolver#resolveDevice(com.isotrol.impe3.api.DeviceResolutionParams)
	 */
	public ResolvedDevice resolveDevice(DeviceResolutionParams params) {
		final DevicesInPortal dips = params.getPortal().getDevices();
		final PathSegments path = params.getPath();
		Device d = dips.findDeviceByFirstSegment(path);
		if (d != null) {
			return params.resolve(d, path.consume());
		}
		d = dips.findDeviceByLastSegment(path);
		if (d != null) {
			return params.resolve(d, path.consumeLast());
		}
		d = dips.findDeviceByLastSegmentExtension(path);
		if (d != null) {
			return params.resolve(d, path.removeExtension());
		}
		return null;
	}

}
