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

package com.isotrol.impe3.test;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import net.sf.derquinsej.io.Streams;

import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.html.CSS;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLFragments;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.Script;


/**
 * HTML Template for Component rendering tests.
 * @author Andres Rodriguez
 */
final class HTMLTemplate {
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String IMPE3_HEADER = "<!-- IMPE3 Header -->";
	private static final String IMPE3_BODY = "<!-- IMPE3 Body -->";
	private static final String IMPE3_FOOTER = "<!-- IMPE3 Footer -->";
	private static final Charset UTF8 = Charset.forName(DEFAULT_ENCODING);

	private HTMLFragment fragment1;
	private HTMLFragment fragment2;
	private HTMLFragment fragment3;
	private HTMLFragment fragment4;

	private static String[] extract(String text, String mark) {
		final String[] split = text.split(mark);
		if (split.length != 2) {
			throw new IllegalStateException();
		}
		return split;
	}

	/** Default Constructor. */
	HTMLTemplate() {
		try {
			final InputStream is = getClass().getResourceAsStream("html-template.html");
			final byte[] data = Streams.consume(is, true);
			final String template = new String(data, DEFAULT_ENCODING);
			final String[] s1 = extract(template, IMPE3_HEADER);
			fragment1 = HTMLFragments.of(s1[0]);
			final String[] s2 = extract(s1[1], IMPE3_BODY);
			fragment2 = HTMLFragments.of(s2[0]);
			final String[] s3 = extract(s2[1], IMPE3_FOOTER);
			fragment3 = HTMLFragments.of(s3[0]);
			fragment4 = HTMLFragments.of(s3[1]);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	void render(HTMLRenderer renderer, OutputStream out, RenderContext rc) {
		renderFragment(fragment1, out);
		final Iterable<CSS> styles = renderer.getCSS();
		if (styles != null) {
			for (CSS css : styles) {
				renderFragment(css, out);
			}
		}
		final Iterable<Script> scripts = renderer.getHeaderScripts();
		if (scripts != null) {
			for (Script script : scripts) {
				renderFragment(script, out);
			}
		}
		renderFragment(renderer.getHeader(), out);
		renderFragment(fragment2, out);
		renderFragment(renderer.getBody(), out);
		renderFragment(fragment3, out);
		renderFragment(renderer.getFooter(), out);
		renderFragment(fragment4, out);
	}

	private static void renderFragment(HTMLFragment f, OutputStream out) {
		if (f != null) {
			try {
				f.writeTo(System.out, UTF8);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}

}
