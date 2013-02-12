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


import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceInPortal;
import com.isotrol.impe3.api.DeviceResolutionParams;
import com.isotrol.impe3.api.ResolvedDevice;


/**
 * Device resolver based in parsing the user agent.
 * @author Andres Rodriguez
 */
public class DeviceResolverByUA extends AbstractDeviceResolver {
	/**
	 * Constructor.
	 * @param config module configuration.
	 */
	public DeviceResolverByUA(DeviceResolverModuleConfig config) {
		super(config);
	}

	@Override
	ResolvedDevice resolve(DeviceResolutionParams params, Iterable<DeviceInPortal> devices) {
		if (devices == null) {
			return null;
		}
		final List<String> uas = params.getHeaders().getRequestHeader(HttpHeaders.USER_AGENT);
		if (uas == null || uas.isEmpty()) {
			return null;
		}
		final String ua = uas.get(0);
		for (DeviceInPortal dip : devices) {
			final Device device = dip.getDevice();
			if (device.matchesUA(ua)) {
				return params.resolve(device);
			}
		}
		return null;
	}
}
