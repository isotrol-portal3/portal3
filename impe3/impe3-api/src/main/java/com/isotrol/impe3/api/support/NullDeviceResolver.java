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


import com.isotrol.impe3.api.DeviceResolutionParams;
import com.isotrol.impe3.api.DeviceResolver;
import com.isotrol.impe3.api.ResolvedDevice;


/**
 * A device resolver that always return null.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class NullDeviceResolver implements DeviceResolver {
	private static final NullDeviceResolver INSTANCE = new NullDeviceResolver();

	/**
	 * Returns the instance.
	 * @return The requested device resolver.
	 */
	public static DeviceResolver get() {
		return INSTANCE;
	}

	/** Constructor. */
	private NullDeviceResolver() {
	}

	/**
	 * @see com.isotrol.impe3.api.DeviceResolver#resolveDevice(com.isotrol.impe3.api.DeviceResolutionParams)
	 */
	public ResolvedDevice resolveDevice(DeviceResolutionParams params) {
		return null;
	}

}
