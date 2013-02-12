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

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.DeviceType;

public class DefaultDeviceCapabilities extends ForwardingMap<String, String>  implements DeviceCapabilities {

	private final Map<String, String> caps;
	private final boolean xhtml;
	private final boolean html;
	private final int width;
	
	
	public DefaultDeviceCapabilities(Device device) {
		Preconditions.checkNotNull(device);
		
		this.xhtml = DeviceType.XHTML.equals(device.getType());
		this.html = DeviceType.HTML.equals(device.getType());
		this.width = device.getWidth() == null ? 0 : device.getWidth();
		this.caps = ImmutableMap.<String, String> of();
	}
	
	
	@Override
	protected Map<String, String> delegate() {
		return caps;
	}
	
	public boolean isFlashSupported() {
		return false;
	}
	public boolean isHTML4Supported() {
		return html;
	}
	public boolean isXHTMLSupported() {
		return xhtml;
	}
	public int getWidth() {
		return width;
	}
	
	
}
