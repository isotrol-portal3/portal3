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


import javax.ws.rs.core.HttpHeaders;

import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.DeviceCapabilitiesProvider;


/**
 * Device capabilities provider returning {@code null}.
 * @author Andres Rodriguez
 */
public final class NullDeviceCapabilitiesProvider implements DeviceCapabilitiesProvider {
	private static final NullDeviceCapabilitiesProvider INSTANCE = new NullDeviceCapabilitiesProvider();

	public static DeviceCapabilitiesProvider get() {
		return INSTANCE;
	}

	/** Constructor. */
	private NullDeviceCapabilitiesProvider() {
	}

	/**
	 * @see com.isotrol.impe3.api.DeviceCapabilitiesProvider#getDeviceCapabilities(javax.ws.rs.core.HttpHeaders)
	 */
	public DeviceCapabilities getDeviceCapabilities(HttpHeaders headers) {
		return null;
	}
}
