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
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.Device;


/**
 * A reference to an external script file.
 * @author Andres Rodriguez
 */
public final class Script implements HTMLFragment {
	public static final String JS_TYPE = "text/javascript";

	private final Tag tag;

	public Script(Device device, String type, URI uri) {
		this.tag = HTML.create(Preconditions.checkNotNull(device)).script(type, uri);
	}

	public Script(Device device, URI uri) {
		this(device, HTMLConstants.MIME_JS, uri);
	}

	public Script(ClientRequestContext context, String type, URI uri) {
		this.tag = HTML.create(context.getDevice()).script(type, uri);
	}

	public Script(ClientRequestContext context, URI uri) {
		this(context.getDevice(), HTMLConstants.MIME_JS, uri);
	}

	public void writeTo(OutputStream output, Charset charset) throws IOException {
		tag.writeTo(output, charset);
	}
}
