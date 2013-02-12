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

import javax.ws.rs.core.HttpHeaders;

import com.isotrol.impe3.api.support.DefaultDeviceCapabilities;


/**
 * Parameters for device resolution inside a portal.
 * @author Andres Rodriguez.
 */
public final class DeviceResolutionParams extends AbstractResolutionParams {
	/** Devices. */
	private final Devices devices;
	/** Already known capabilities. */
	private final DeviceCapabilities capabilities;

	/**
	 * Constructor.
	 * @param headers JAX-RS Headers.
	 * @param request HTTP request context.
	 * @param portal Portal.
	 * @param fullPath Full path (from portal base).
	 * @param path Remaining decoded segment paths.
	 * @param parameters Current local parameters.
	 * @param devices Devices.
	 * @param capabilities Already known capabilities.
	 */
	public DeviceResolutionParams(HttpHeaders headers, HttpRequestContext request, Portal portal,
		PathSegments fullPath, PathSegments path, LocalParams parameters, Devices devices,
		DeviceCapabilities capabilities) {
		super(headers, request, portal, fullPath, path, parameters);
		this.devices = checkNotNull(devices);
		this.capabilities = capabilities;
	}

	/**
	 * Returns the devices.
	 * @return The devices.
	 */
	public Devices getDevices() {
		return devices;
	}

	/**
	 * Returns the known capabilities.
	 * @return The known capabilities.
	 */
	public DeviceCapabilities getCapabilities() {
		return capabilities;
	}

	private DeviceCapabilities resolveCapabilities(Device d, DeviceCapabilities dc) {
		if (dc != null) {
			return dc;
		}
		if (capabilities != null) {
			return capabilities;
		}
		return DefaultDeviceCapabilities.get(d);
	}

	private DeviceCapabilities resolveCapabilities(Device d) {
		return resolveCapabilities(d, null);
	}

	public ResolvedDevice resolve(Device device, PathSegments path, LocalParams parameters,
		DeviceCapabilities capabilities) {
		return new ResolvedDevice(path, device, resolveCapabilities(device, capabilities), parameters);
	}

	public ResolvedDevice resolve(Device device, PathSegments path, LocalParams parameters) {
		return resolve(device, path, parameters, resolveCapabilities(device));
	}

	public ResolvedDevice resolve(Device device, PathSegments path) {
		return resolve(device, path, getParameters(), resolveCapabilities(device));
	}

	public ResolvedDevice resolve(Device device) {
		return resolve(device, getPath(), getParameters(), resolveCapabilities(device));
	}
}
