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


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;

import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.Device;


/**
 * A reference to a CSS stylesheet.
 * @author Andres Rodriguez
 */
public final class CSS implements HTMLFragment {
	private static final String STYLESHEET = "stylesheet";
	private static final String CSS = "text/css";

	private final Device device;
	private final URI uri;
	private final String media;

	public CSS(Device device, URI uri, String media) {
		this.device = checkNotNull(device);
		this.uri = checkNotNull(uri);
		this.media = media;
	}

	public CSS(ClientRequestContext context, URI uri, String media) {
		this(context.getDevice(), uri, media);
	}

	public URI getUri() {
		return uri;
	}
	
	public String getMedia() {
		return media;
	}

	public void writeTo(OutputStream output, Charset charset) throws IOException {
		final Tag link = HTML.create(device).link().set(HTMLConstants.REL, STYLESHEET).set(HTMLConstants.TYPE, CSS)
			.set(HTMLConstants.HREF, uri.toASCIIString());
		if (media != null && media.length() > 0) {
			link.set(HTMLConstants.MEDIA, media);
		}
		link.writeTo(output, charset);
	}
}
