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


import static com.isotrol.impe3.api.component.html.HTMLConstants.A;
import static com.isotrol.impe3.api.component.html.HTMLConstants.BODY;
import static com.isotrol.impe3.api.component.html.HTMLConstants.CLASSID;
import static com.isotrol.impe3.api.component.html.HTMLConstants.CODEBASE;
import static com.isotrol.impe3.api.component.html.HTMLConstants.DIV;
import static com.isotrol.impe3.api.component.html.HTMLConstants.EMBED;
import static com.isotrol.impe3.api.component.html.HTMLConstants.HEAD;
import static com.isotrol.impe3.api.component.html.HTMLConstants.HEIGHT;
import static com.isotrol.impe3.api.component.html.HTMLConstants.HREF;
import static com.isotrol.impe3.api.component.html.HTMLConstants.HTML;
import static com.isotrol.impe3.api.component.html.HTMLConstants.IMG;
import static com.isotrol.impe3.api.component.html.HTMLConstants.LI;
import static com.isotrol.impe3.api.component.html.HTMLConstants.LINK;
import static com.isotrol.impe3.api.component.html.HTMLConstants.META;
import static com.isotrol.impe3.api.component.html.HTMLConstants.MIME_JS;
import static com.isotrol.impe3.api.component.html.HTMLConstants.NAME;
import static com.isotrol.impe3.api.component.html.HTMLConstants.NOEMBED;
import static com.isotrol.impe3.api.component.html.HTMLConstants.NOSCRIPT;
import static com.isotrol.impe3.api.component.html.HTMLConstants.OBJECT;
import static com.isotrol.impe3.api.component.html.HTMLConstants.P;
import static com.isotrol.impe3.api.component.html.HTMLConstants.PARAM;
import static com.isotrol.impe3.api.component.html.HTMLConstants.PLUGINSPAGE;
import static com.isotrol.impe3.api.component.html.HTMLConstants.PRE;
import static com.isotrol.impe3.api.component.html.HTMLConstants.QUALITY;
import static com.isotrol.impe3.api.component.html.HTMLConstants.SCRIPT;
import static com.isotrol.impe3.api.component.html.HTMLConstants.SRC;
import static com.isotrol.impe3.api.component.html.HTMLConstants.TITLE;
import static com.isotrol.impe3.api.component.html.HTMLConstants.TYPE;
import static com.isotrol.impe3.api.component.html.HTMLConstants.UL;
import static com.isotrol.impe3.api.component.html.HTMLConstants.VALUE;
import static com.isotrol.impe3.api.component.html.HTMLConstants.WIDTH;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import net.sf.derquinsej.This;
import net.sf.derquinsej.io.Streams;

import com.google.common.collect.Lists;


/**
 * Abstract class for programmatic HTML fragment building. This class and its subclasses are mutable and not
 * THREAD-SAFE.
 * @author Andres Rodriguez
 */
public abstract class HTMLBuilder<T extends HTMLBuilder<T>> extends This<T> implements HTMLFragment {
	/** Content. */
	private List<HTMLFragment> content = null;

	/** Default constructor. */
	HTMLBuilder() {
	}

	private List<HTMLFragment> content() {
		if (content == null) {
			content = Lists.newLinkedList();
		}
		return content;
	}

	final boolean hasContent() {
		return content != null && !content.isEmpty();
	}

	abstract boolean isXHTML();

	//
	// PUBLIC API
	//

	public final T add(HTMLFragment f) {
		if (f != null) {
			content().add(f);
		}
		return thisValue();
	}

	/**
	 * Adds a throwable's stack trace in a pre tag.
	 * @param t Throwable to write.
	 * @return This object.
	 */
	public final T stackTrace(final Throwable t) {
		if (t != null) {
			pre().add(HTMLFragments.stackTrace(t));
		}
		return thisValue();
	}

	public final T add(Object fragment) {
		HTMLFragment f = HTMLFragments.object(fragment);
		if (f != HTMLFragments.empty()) {
			return add(f);
		}
		return thisValue();
	}

	/**
	 * Adds a HTML fragment that contains the exception in a pre in case of an error.
	 * @param fragment Object to add.
	 * @return The requested fragment.
	 */
	public final T safeAdd(Object fragment) {
		final HTMLFragment f = HTMLFragments.object(fragment);
		if (f == HTMLFragments.empty()) {
			return thisValue();
		}
		final HTMLFragment wrapped = new HTMLFragment() {
			public void writeTo(OutputStream output, Charset charset) throws IOException {
				final byte[] data;
				try {
					final ByteArrayOutputStream bos = new ByteArrayOutputStream();
					f.writeTo(bos, charset);
					data = bos.toByteArray();
				} catch (IOException e) {
					stackTrace(e).writeTo(output, charset);
					return;
				} catch (RuntimeException e) {
					stackTrace(e).writeTo(output, charset);
					return;
				}
				Streams.consume(new ByteArrayInputStream(data), output, true);
			}
		};
		return add(wrapped);
	}

	public final T add(Object... fragments) {
		if (fragments != null) {
			for (Object f : fragments) {
				add(f);
			}
		}
		return thisValue();
	}

	public final T add(Iterable<?> fragments) {
		if (fragments != null) {
			for (Object f : fragments) {
				add(f);
			}
		}
		return thisValue();
	}

	public final Tag tag(String name, boolean keepOpen) {
		Tag tag = new Tag(this, name, keepOpen);
		add(tag);
		return tag;
	}

	public final Tag tag(String name) {
		return tag(name, false);
	}

	/**
	 * Creates a new html tag.
	 * @return The created tag.
	 */
	public final Tag html() {
		return tag(HTML);
	}

	/**
	 * Creates a new head tag.
	 * @return fluid builder
	 */
	public final Tag head() {
		return tag(HEAD);
	}

	/**
	 * Creates a new body tag builder
	 * @return fluid builder
	 */
	public final Tag body() {
		return tag(BODY);
	}

	/**
	 * Creates a new div tag builder
	 * @return fluid builder
	 */
	public final Tag div() {
		return tag(DIV);
	}

	/**
	 * Creates a new pre tag builder
	 * @return fluid builder
	 */
	public final Tag pre() {
		return tag(PRE);
	}

	/**
	 * Creates a new p tag builder
	 * @return fluid builder
	 */
	public final Tag p() {
		return tag(P);
	}

	/**
	 * Creates a new p tag builder with content
	 * @param s p content string
	 * @return fluid builder
	 */
	public final Tag p(String s) {
		return p().add(s);
	}

	public final Tag meta() {
		return tag(META, true);
	}

	public final Tag link() {
		return tag(LINK, true);
	}

	/**
	 * Creates a new ul tag builder
	 * @return fluid builder
	 */
	public final Tag ul() {
		return tag(UL);
	}

	/**
	 * Creates a new li tag builder
	 * @return fluid builder
	 */
	public final Tag li() {
		return tag(LI);
	}

	public final Tag li(String s) {
		return li().add(s);
	}

	/**
	 * Returns an IMG tag.
	 * @param src SRC attribute.
	 * @param alt Short description. Used for both the ALT and TITLE attributes.
	 * @return The requested tag.
	 */
	public final Tag img(String src, String alt) {
		return tag(IMG, true).set(SRC, src).set("alt", alt).set("title", alt);
	}

	public final Tag a(String href) {
		return tag(A).set(HREF, href);
	}

	public final Tag title() {
		return tag(TITLE);
	}

	public final Tag title(String s) {
		return title().add(s);
	}

	public final Tag object(String height, String width, String codebase, String classid) {
		return tag(OBJECT).set(HEIGHT, height).set(WIDTH, width).set(CODEBASE, codebase).set(CLASSID, classid);
	}

	public final Tag param(String name, String value) {
		return tag(PARAM).set(NAME, name).set(VALUE, value);
	}

	public final Tag embed(String height, String width, String type, String pluginspage, String quality, String src) {
		return tag(EMBED).set(HEIGHT, height).set(WIDTH, width).set(TYPE, type).set(PLUGINSPAGE, pluginspage)
			.set(QUALITY, quality).set(SRC, src);
	}

	public final Tag noscript() {
		return tag(NOSCRIPT);
	}

	public final Tag noembed() {
		return tag(NOEMBED);
	}

	public final Tag script() {
		return tag(SCRIPT);
	}

	public final Tag script(String type) {
		return script().set(TYPE, type);
	}

	public final Tag script(String type, URI uri) {
		return script(type).set(SRC, uri.toASCIIString());
	}
	
	public final Tag javascript() {
		return script().set(TYPE, MIME_JS);
	}
	
	public final Tag javascript(URI uri) {
		return javascript().set(SRC, uri.toASCIIString());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.api.component.html.HTMLFragment#writeTo(java.io.OutputStream, java.nio.charset.Charset)
	 */
	public void writeTo(OutputStream output, Charset charset) throws IOException {
		if (hasContent()) {
			for (HTMLFragment f : content) {
				f.writeTo(output, charset);
			}
		}
	}

}
