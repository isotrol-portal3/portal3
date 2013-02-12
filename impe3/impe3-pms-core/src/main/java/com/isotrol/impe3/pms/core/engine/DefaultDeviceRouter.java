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

package com.isotrol.impe3.pms.core.engine;


import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceResolutionParams;
import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.ResolvedDevice;
import com.isotrol.impe3.api.support.DefaultDeviceURIGenerator;
import com.isotrol.impe3.api.support.DeviceNameUseDeviceResolver;


/**
 * Default device router.
 * @author Andres Rodriguez.
 */
@Component
public class DefaultDeviceRouter implements DeviceRouter {

	/**
	 * Constructor.
	 */
	public DefaultDeviceRouter() {
	}

	/**
	 * @see com.isotrol.impe3.api.DeviceResolver#resolveDevice(com.isotrol.impe3.api.DeviceResolutionParams)
	 */
	public ResolvedDevice resolveDevice(DeviceResolutionParams params) {
		ResolvedDevice rd = DeviceNameUseDeviceResolver.get().resolveDevice(params);
		if (rd != null) {
			return rd;
		}
		return params.resolve(params.getPortal().getDevice());
	}

	/**
	 * @see com.isotrol.impe3.api.DeviceURIGenerator#getTransformer(com.isotrol.impe3.api.Portal,
	 * com.isotrol.impe3.api.Device)
	 */
	public Function<PathSegments, PathSegments> getTransformer(Portal portal, Device device) {
		return DefaultDeviceURIGenerator.get().getTransformer(portal, device);
	}
}
