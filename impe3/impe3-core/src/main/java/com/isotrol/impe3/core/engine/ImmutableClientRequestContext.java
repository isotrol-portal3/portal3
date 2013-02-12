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

package com.isotrol.impe3.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;

import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceCapabilities;


/**
 * Immutable implementation of ClientRequestContext.
 * @author Andres Rodriguez
 */
final class ImmutableClientRequestContext implements ClientRequestContext {
	/** Device. */
	private final Device device;
	/** Device capabilities. */
	private final DeviceCapabilities deviceCapabilities;
	/** Locale. */
	private final Locale locale;

	/**
	 * Constructor.
	 * @param device Device.
	 * @param deviceCapabilities Device capabilities.
	 * @param locale Locale.
	 */
	ImmutableClientRequestContext(Device device, DeviceCapabilities deviceCapabilities, Locale locale) {
		this.device = checkNotNull(device);
		this.deviceCapabilities = checkNotNull(deviceCapabilities);
		this.locale = checkNotNull(locale);
	}

	/**
	 * @see com.isotrol.impe3.api.WithDeviceProperty#getDevice()
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * @see com.isotrol.impe3.api.ClientRequestContext#getDeviceCapabilities()
	 */
	public DeviceCapabilities getDeviceCapabilities() {
		return deviceCapabilities;
	}

	/**
	 * @see com.isotrol.impe3.api.WithLocaleProperty#getLocale()
	 */
	public Locale getLocale() {
		return locale;
	}
}
