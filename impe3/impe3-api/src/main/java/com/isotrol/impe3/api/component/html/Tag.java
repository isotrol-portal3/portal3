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
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.web.util.HtmlUtils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.component.RenderContext;


/**
 * HTML Tag fragment. NOT THREAD-SAFE.
 * @author Andres Rodriguez
 */
public final class Tag extends HTMLBuilder<Tag> {
	private static final Set<String> ALWAYS_CLOSING_TAG = ImmutableSet.of("script");
	
	private final boolean xhtml;
	private final String name;
	private final boolean keepOpen;
	private Map<String, String> attributes;

	public static Tag create(Device device, String name, boolean keepOpen) {
		return HTML.create(device).tag(name, keepOpen);
	}

	public static Tag create(Device device, String name) {
		return create(device, name, false);
	}

	public static Tag create(RenderContext context, String name, boolean keepOpen) {
		return HTML.create(context).tag(name, keepOpen);
	}

	public static Tag create(RenderContext context, String name) {
		return create(context, name, false);
	}

	Tag(HTMLBuilder<?> parent, String name, boolean keepOpen) {
		this.xhtml = checkNotNull(parent).isXHTML();
		this.name = checkNotNull(name);
		this.keepOpen = keepOpen && !xhtml;
	}

	@Override
	boolean isXHTML() {
		return xhtml;
	}

	public Tag set(String attribute, String value) {
		if (attribute != null && value != null) {
			if (attributes == null) {
				attributes = Maps.newLinkedHashMap();
			}
			attributes.put(attribute, HtmlUtils.htmlEscape(value));
		}
		return this;
	}

	public void writeTo(OutputStream output, Charset charset) throws IOException {
		// Build
		final HTMLFragment start;
		final HTMLFragment end;
		StringBuilder b = new StringBuilder().append('<').append(name);
		if (attributes != null) {
			for (Entry<String, String> att : attributes.entrySet()) {
				b.append(' ').append(att.getKey()).append("=\"").append(att.getValue()).append('"');
			}
		}
		if (xhtml && !hasContent() && !ALWAYS_CLOSING_TAG.contains(name.toLowerCase())) {
			start = HTMLFragments.of(b.append(" />\n").toString());
			end = null;
		} else {
			start = HTMLFragments.of(b.append(">\n").toString());
			if (!keepOpen) {
				end = HTMLFragments.of("\n</" + name + ">\n");
			} else {
				end = HTMLFragments.of("\n");
			}
		}
		// Write
		start.writeTo(output, charset);
		super.writeTo(output, charset);
		if (end != null) {
			end.writeTo(output, charset);
		}
	}
}
