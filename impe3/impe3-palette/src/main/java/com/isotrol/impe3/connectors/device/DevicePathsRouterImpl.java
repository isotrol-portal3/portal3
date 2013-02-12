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

package com.isotrol.impe3.connectors.device;


import com.google.common.base.Function;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceResolutionParams;
import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.DevicesInPortal;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.ResolvedDevice;
import com.isotrol.impe3.api.support.DefaultDeviceURIGenerator;


/**
 * Device router that uses device names as path segments.
 * @author Andres Rodriguez
 */
public class DevicePathsRouterImpl implements DeviceRouter {
	private DevicePathsRouterConfig config;

	public DevicePathsRouterImpl() {
	}

	public void setConfig(DevicePathsRouterConfig config) {
		this.config = config;
	}

	/**
	 * @see com.isotrol.impe3.api.DeviceURIGenerator#getTransformer(com.isotrol.impe3.api.Portal,
	 * com.isotrol.impe3.api.Device)
	 */
	public Function<PathSegments, PathSegments> getTransformer(Portal portal, Device device) {
		return DefaultDeviceURIGenerator.get().getTransformer(portal, device);
	}

	public ResolvedDevice resolveDevice(DeviceResolutionParams params) {
		final Portal portal = params.getPortal();
		final PathSegments path = params.getPath();
		DevicesInPortal dips = portal.getDevices();
		if (config != null && !config.includeDefault()) {
			dips = dips.excludeDevice(portal.getDevice());
		}
		final Device d = portal.getDevices().findDeviceByFirstSegment(path);
		if (d != null) {
			return new ResolvedDevice(path.consume(), d, new DefaultDeviceCapabilities(d), params.getParameters());
		}
		return null;
	}
}
