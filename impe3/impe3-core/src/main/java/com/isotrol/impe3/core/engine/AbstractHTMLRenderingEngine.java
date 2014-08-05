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


import static com.isotrol.impe3.api.component.html.HTMLConstants.CLASS;
import static com.isotrol.impe3.api.component.html.HTMLConstants.CONTENT;
import static com.isotrol.impe3.api.component.html.HTMLConstants.HTTP_EQUIV;
import static com.isotrol.impe3.api.component.html.HTMLConstants.STYLE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.html.CSS;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.Script;
import com.isotrol.impe3.api.component.html.Tag;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.CIPResult;
import com.isotrol.impe3.core.Column;
import com.isotrol.impe3.core.Frame;
import com.isotrol.impe3.core.LayoutResponse;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.PageResult.Ok;
import com.isotrol.impe3.core.RenderingEngine;


/**
 * Abstract HTML Rendering engine. This class is NOT THREAD-SAFE.
 * @author Andres Rodriguez
 */
public abstract class AbstractHTMLRenderingEngine implements RenderingEngine<HTMLRenderer> {
	private static final String MAIN_COLUMN = "impe3_mainColumn";
	private static final String COLUMN = "impe3_column";
	private static final String FRAME = "impe3_frame";

	private static String classAtt(String base, String name) {
		if (name == null) {
			return base;
		}
		return base + ' ' + name;
	}

	final Logger logger = Loggers.core();

	AbstractHTMLRenderingEngine() {
	}

	final HTMLFragment timed(final Ok result, final String name, final UUID cipId, final HTMLFragment f) {
		if (f == null) {
			return null;
		}
		return new HTMLFragment() {
			public void writeTo(OutputStream output, Charset charset) throws IOException {
				final Stopwatch w = Stopwatch.createStarted();
				try {
					f.writeTo(output, charset);
				}
				finally {
					final long t = w.elapsed(TimeUnit.MILLISECONDS);
					if (t > 150) {
						logger.warn(String.format("CIP [%s]-[%s] took [%d] ms to render [%s]", cipId, result.getCips()
							.get(cipId).getCip().getDefinition().getType(), t, name));
					}
				}
			}
		};

	}

	abstract String getDocType();

	abstract MediaType getMediaType();

	abstract void decorateHTML(Tag html);

	private Function<CIP, RenderContext> rc(final Ok result, final List<Frame> layout) {
		Map<CIP, RenderContext> map = Maps.newHashMap();
		for (CIPResult cr : result.getCips().values()) {
			map.put(cr.getCip(), RequestContexts.render(cr.getContext(), null, result.getQuery()));
		}
		return Functions.forMap(map);
	}

	public LayoutResponse getLayout(PageInstance pi, Ok result) {
		final LayoutResponse.Builder builder = LayoutResponse.builder();
		final Map<UUID, HTMLRenderer> renderers = result.getRenderers(HTMLRenderer.class, rc(result, pi.getLayout()));
		for (HTMLRenderer renderer : renderers.values()) {
			Iterable<CSS> csss = renderer.getCSS();
			if (csss != null) {
				for (CSS css : csss) {
					builder.addStyle(css.getUri(), css.getMedia());
				}
			}
			csss = renderer.getIE6CSS();
			if (csss != null) {
				for (CSS css : csss) {
					builder.addIE6Style(css.getUri(), css.getMedia());
				}
			}
			csss = renderer.getIE7CSS();
			if (csss != null) {
				for (CSS css : csss) {
					builder.addIE7Style(css.getUri(), css.getMedia());
				}
			}
			csss = renderer.getIE8CSS();
			if (csss != null) {
				for (CSS css : csss) {
					builder.addIE8Style(css.getUri(), css.getMedia());
				}
			}
		}
		for (UUID cipId : renderers.keySet()) {
			final HTMLRenderer renderer = renderers.get(cipId);
			final HTMLFragment f = renderer.getBody();
			final Stopwatch w = Stopwatch.createStarted();
			try {
				if (f != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					HTML.create((Device) null).safeAdd(f).writeTo(bos, Charset.forName("UTF-8"));
					String value = new String(bos.toByteArray(), "UTF-8");
					if (value != null) {
						builder.putMarkup(cipId, value);
					}
				}
			} catch (Exception e) {
				builder.putMarkup(cipId, "<p>Error: " + e.getMessage() + "</p>");
			}
			finally {
				final long t = w.elapsed(TimeUnit.MILLISECONDS);
				if (t > 150) {
					logger.warn(String.format("CIP [%s]-[%s] took [%d] ms to render in layout mode", cipId, result
						.getCips().get(cipId).getCip().getDefinition().getType(), t));
				}
			}
		}
		return builder.get();
	}

	/**
	 * @see com.isotrol.impe3.core.RenderingEngine#render(com.isotrol.impe3.core.PageResult.Ok, java.util.List,
	 * javax.ws.rs.core.Response.ResponseBuilder)
	 */
	public Response render(final PageInstance pi, final Ok result, final ResponseBuilder builder) {
		final Charset charset = Charset.forName("UTF-8");
		final Map<UUID, HTMLRenderer> renderers = result.getRenderers(HTMLRenderer.class, rc(result, pi.getLayout()));
		final StreamingOutput output = new StreamingOutput() {
			public void write(OutputStream output) throws IOException, WebApplicationException {
				final HTML document = HTML.create(result.getContext().getDevice());
				document.add(getDocType());
				// HTML header
				final Tag html = document.html();
				html.set("lang", result.getContext().getLocale().getLanguage());
				decorateHTML(html);
				// Head
				final Tag head = html.head();
				// In line styles
				final Integer width_int = result.getContext().getDevice().getWidth();
				final String width_str = (width_int != null && width_int > 0) ? width_int.toString() + "px" : "100%";
				StringBuilder sb = new StringBuilder("<style type=\"text/css\">\n");
				sb.append(String.format(
					"\t.impe3_mainColumn { margin: auto; float: none; text-align:left; width: %s; }\n", width_str));
				sb.append("\t.impe3_column { float: left; width: auto; }\n");
				sb.append("\t.impe3_frame { width: 100%; }\n");
				sb.append("</style>\n");
				head.add(sb.toString());
				// Stylesheets
				final List<CSS> ie6css = Lists.newLinkedList();
				final List<CSS> ie7css = Lists.newLinkedList();
				final List<CSS> ie8css = Lists.newLinkedList();
				for (HTMLRenderer renderer : renderers.values()) {
					Iterable<CSS> csss = renderer.getCSS();
					if (csss != null) {
						for (CSS css : csss) {
							head.add(css);
						}
					}
					csss = renderer.getIE6CSS();
					if (csss != null) {
						for (CSS css : csss) {
							ie6css.add(css);
						}
					}
					csss = renderer.getIE7CSS();
					if (csss != null) {
						for (CSS css : csss) {
							ie7css.add(css);
						}
					}
					csss = renderer.getIE8CSS();
					if (csss != null) {
						for (CSS css : csss) {
							ie8css.add(css);
						}
					}
				}
				// Additional CSS for IE6, 7, 8
				cssIE(head, ie6css, 6);
				cssIE(head, ie7css, 7);
				cssIE(head, ie8css, 8);
				// Scripts
				for (HTMLRenderer renderer : renderers.values()) {
					final Iterable<Script> scripts = renderer.getHeaderScripts();
					if (scripts != null) {
						for (Script script : scripts) {
							head.add(script);
						}
					}
				}
				// Meta
				head.meta().set(HTTP_EQUIV, "content-type").set(CONTENT, getMediaType().toString());
				// Headers
				for (Entry<UUID, HTMLRenderer> entry : renderers.entrySet()) {
					head.add(timed(result, "HEAD", entry.getKey(), entry.getValue().getHeader()));
				}
				// Body
				final Tag body = html.body();
				final Tag main = body.div().set(CLASS, MAIN_COLUMN);
				frames(main, pi.getLayout());
				// Footer
				for (Entry<UUID, HTMLRenderer> entry : renderers.entrySet()) {
					body.add(timed(result, "FOOTER", entry.getKey(), entry.getValue().getFooter()));
				}
				// End
				document.writeTo(output, charset);
			}

			private void cssIE(Tag tag, List<CSS> csss, int version) {
				if (csss.isEmpty()) {
					return;
				}
				tag.add(String.format("<!--[if IE %d]>", version));
				for (CSS css : csss) {
					tag.add(css);
				}
				tag.add("<![endif]-->");
			}

			private void frames(Tag tag, Iterable<Frame> frames) throws IOException {
				for (Frame frame : frames) {
					frame(tag, frame);
				}
			}

			private void frame(Tag tag, Frame frame) throws IOException {
				final Tag div = tag.div();
				final String name = frame.getName();
				div.set(CLASS, classAtt(FRAME, name));
				if (frame instanceof Frame.Component) {
					Frame.Component fc = (Frame.Component) frame;
					final HTMLRenderer r = renderers.get(fc.getId());
					if (r != null) {
						div.add(timed(result, "BODY", fc.getId(), r.getBody()));
					} else {
						div.p();
					}
				} else {
					for (Column col : ((Frame.Columns) frame).getColumns()) {
						final Tag divcol = div.div();
						divcol.set(STYLE, String.format("width:%s%%", col.getWidth()));
						divcol.set(CLASS, classAtt(COLUMN, col.getName()));
						frames(divcol, col.getFrames());
					}
					// Closing div
					div.div().set(STYLE, "clear: both;font-size: 0.1em;").add("<!-- Layout -->");
				}
			}
		};
		return builder.entity(output).type(getMediaType()).build();
	}
}
