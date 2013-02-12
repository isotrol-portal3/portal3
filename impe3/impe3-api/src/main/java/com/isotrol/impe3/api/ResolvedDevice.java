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


import static com.google.common.base.Preconditions.checkNotNull;

import javax.ws.rs.core.Response;


/**
 * Device resolution result.
 * @author Andres Rodriguez.
 */
public final class ResolvedDevice extends Resolved {
	private final Device device;
	private final DeviceCapabilities capabilities;

	/**
	 * Constructor for an interrupting resolution.
	 * @param response Response.
	 */
	public ResolvedDevice(Response response) {
		super(response);
		this.device = null;
		this.capabilities = null;
	}

	public ResolvedDevice(PathSegments path, Device device, DeviceCapabilities capabilities, LocalParams parameters) {
		super(path, parameters);
		this.device = checkNotNull(device);
		this.capabilities = checkNotNull(capabilities);
	}

	public Device getDevice() {
		normal();
		return device;
	}

	public DeviceCapabilities getCapabilities() {
		normal();
		return capabilities;
	}
}
