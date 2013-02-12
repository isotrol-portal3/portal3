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


import java.util.Map;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.DeviceType;


/**
 * Default device capabilities.
 * @author Andres Rodriguez
 */
public abstract class DefaultDeviceCapabilities extends ForwardingMap<String, String> implements DeviceCapabilities {
	private static final DeviceCapabilities HTML = new HTML();

	public static DeviceCapabilities get(DeviceType deviceType) {
		return HTML;
	}

	public static DeviceCapabilities get(Device device) {
		return get(device.getType());
	}

	/** Constructor. */
	private DefaultDeviceCapabilities() {
	}

	@Override
	protected Map<String, String> delegate() {
		return ImmutableMap.of();
	}

	private static final class HTML extends DefaultDeviceCapabilities {
		private HTML() {
		}

		public int getWidth() {
			return 1024;
		}

		public boolean isHTML4Supported() {
			return true;
		}

		public boolean isXHTMLSupported() {
			return true;
		}

		public boolean isFlashSupported() {
			return true;
		}
	}
}
