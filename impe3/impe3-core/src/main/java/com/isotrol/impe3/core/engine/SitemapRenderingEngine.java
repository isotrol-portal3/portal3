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


import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.sitemap.IndexEntry;
import com.isotrol.impe3.api.component.sitemap.Sitemap;
import com.isotrol.impe3.api.component.sitemap.SitemapRenderer;
import com.isotrol.impe3.api.component.sitemap.URLEntry;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.CIPResult;
import com.isotrol.impe3.core.Frame;
import com.isotrol.impe3.core.LayoutResponse;
import com.isotrol.impe3.core.PageResult.Ok;
import com.isotrol.impe3.core.RenderingEngine;


/**
 * Sitemap rendering engine.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class SitemapRenderingEngine implements RenderingEngine<SitemapRenderer> {
	private static final MediaType MEDIA_TYPE = MediaType.APPLICATION_XML_TYPE;
	/** Sitemap namespace. */
	private static final Namespace NS = Namespace.getNamespace("http://www.sitemaps.org/schemas/sitemap/0.9");
	/** ISO 8601 date format string. */
	private static final String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
	/** Formatter instance. */
	private static final ThreadLocal<SimpleDateFormat> ISO8601 = new ThreadLocal<SimpleDateFormat>();

	/** Creates a new element in the sitemap namespace. */
	private static Element element(String name) {
		return new Element(name, NS);
	}

	/** Creates a new element with content in the sitemap namespace. */
	private static Element element(String name, String content) {
		Element e = element(name);
		if (content != null) {
			e.addContent(content);
		}
		return e;
	}

	/** Creates a new element with content in the sitemap namespace. */
	private static Element element(String name, URI content) {
		return element(name, content != null ? content.toASCIIString() : null);
	}

	/** Adds a new element with a date content in the sitemap namespace. */
	private static Element element(Element parent, String name, Long date) {
		if (date != null) {
			Date d = new Date(date);
			parent.addContent(element(name, format(d)));
		}
		return parent;
	}

	/** Formats a date into a ISO8601 string. */
	private static String format(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat f = ISO8601.get();
		if (f == null) {
			f = new SimpleDateFormat(ISO8601_PATTERN);
			ISO8601.set(f);
		}
		return f.format(date);
	}

	static final Supplier<RenderingEngine<?>> SUPPLIER = new Supplier<RenderingEngine<?>>() {
		public RenderingEngine<?> get() {
			return new SitemapRenderingEngine();
		}
	};

	public LayoutResponse getLayout(PageInstance pi, Ok result) {
		return LayoutResponse.builder().get();
	}

	public Response render(final PageInstance pi, final Ok result, final ResponseBuilder builder) {
		final Map<UUID, SitemapRenderer> renderers = result.getRenderers(SitemapRenderer.class,
			rc(result, pi.getLayout()));
		Sitemap sitemap = null;
		for (SitemapRenderer r : renderers.values()) {
			Sitemap s = r.getSitemap();
			if (s != null) {
				if (sitemap == null) {
					sitemap = s;
				} else {
					sitemap = sitemap.append(s);
				}
			}
		}
		if (sitemap == null) {
			sitemap = Sitemap.set(ImmutableList.<URLEntry> of());
		}
		return builder.entity(new XMLOutput(sitemap)).type(MEDIA_TYPE).build();
	}

	private Function<CIP, RenderContext> rc(final Ok result, final List<Frame> layout) {
		Map<CIP, RenderContext> map = Maps.newHashMap();
		for (CIPResult cr : result.getCips().values()) {
			map.put(cr.getCip(), RequestContexts.render(cr.getContext(), null, result.getQuery()));
		}
		return Functions.forMap(map);
	}

	private static final class XMLOutput implements StreamingOutput {
		private final Sitemap sitemap;

		XMLOutput(Sitemap sitemap) {
			if (sitemap == null) {
				this.sitemap = Sitemap.set(ImmutableList.<URLEntry> of());
			} else {
				this.sitemap = sitemap;
			}
		}

		public void write(OutputStream output) throws IOException, WebApplicationException {
			final String rootName;
			final Iterable<Element> children;
			if (sitemap.isIndex()) {
				rootName = "sitemapindex";
				children = Iterables.transform(sitemap.asIndex().getIndexEntries(), IndexEntryXML.INSTANCE);
			} else {
				rootName = "urlset";
				children = Iterables.transform(sitemap.asSet().getURLEntries(), URLEntryXML.INSTANCE);
			}
			final Document doc = new Document();
			final Element root = element(rootName);
			doc.setRootElement(root);
			for (Element e : children) {
				root.addContent(e);
			}
			final XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding(Charsets.UTF_8.name()));
			out.output(doc, output);
			ISO8601.set(null);
		}
	}

	private enum IndexEntryXML implements Function<IndexEntry, Element> {
		INSTANCE;

		public Element apply(IndexEntry input) {
			Element entry = element("sitemap").addContent(element("loc", input.getUri()));
			return element(entry, "lastModified", input.getLastModified());
		}
	}

	private enum URLEntryXML implements Function<URLEntry, Element> {
		INSTANCE;

		public Element apply(URLEntry input) {
			Element entry = element("url").addContent(element("loc", input.getUri()));
			element(entry, "lastModified", input.getLastModified());
			if (input.getChangefreq() != null) {
				entry.addContent(element("changefreq", input.getChangefreq().getValue()));
			}
			if (input.getPriority() != null) {
				entry.addContent(element("priority", input.getPriority().toString()));
			}
			return entry;
		}
	}

}
