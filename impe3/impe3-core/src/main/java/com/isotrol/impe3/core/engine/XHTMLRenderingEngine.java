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


import javax.ws.rs.core.MediaType;

import com.google.common.base.Supplier;
import com.isotrol.impe3.api.component.html.Tag;
import com.isotrol.impe3.core.RenderingEngine;


/**
 * HTML Rendering engine. This class is NOT THREAD-SAFE.
 * @author Andres Rodriguez
 */
final class XHTMLRenderingEngine extends AbstractHTMLRenderingEngine {
	private static final MediaType MEDIA_TYPE = MediaType.valueOf("text/html; charset=UTF-8");
	private static final String DOCTYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";

	static final Supplier<RenderingEngine<?>> SUPPLIER = new Supplier<RenderingEngine<?>>() {
		public RenderingEngine<?> get() {
			return new XHTMLRenderingEngine();
		}
	};

	XHTMLRenderingEngine() {
	}

	@Override
	void decorateHTML(Tag html) {
		html.set("xmlns", "http://www.w3.org/1999/xhtml");
	}

	@Override
	String getDocType() {
		return DOCTYPE;
	}

	@Override
	MediaType getMediaType() {
		return MEDIA_TYPE;
	}

}
