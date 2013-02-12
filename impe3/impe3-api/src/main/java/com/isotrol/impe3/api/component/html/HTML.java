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

package com.isotrol.impe3.api.component.html;


import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceType;


/**
 * Class for programmatic HTML fragment building.
 * @author Andres Rodriguez
 */
public final class HTML extends HTMLBuilder<HTML> implements HTMLFragment {
	/** Whether XHTML is used. */
	private final boolean xhtml;

	public static HTML create(Device device) {
		return new HTML(device);
	}

	public static HTML create(ClientRequestContext context) {
		return new HTML(context.getDevice());
	}

	private HTML(Device device) {
		this.xhtml = device != null && DeviceType.XHTML == device.getType();
	}

	@Override
	boolean isXHTML() {
		return xhtml;
	}

}
