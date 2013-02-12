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


import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.UUID;

import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceType;


/**
 * Base class for HTML tests.
 * @author Andres Rodriguez
 */
abstract class HTMLTest {
	static Device device(DeviceType type) {
		return new Device(UUID.randomUUID(), type, "Test Device", "", 980, null, null);
	}

	static void out(HTMLFragment f) throws IOException {
		f.writeTo(System.out, Charset.forName("UTF-8"));
	}
	
	static final URI TEST_URI = URI.create("http://www.google.com"); 
}
