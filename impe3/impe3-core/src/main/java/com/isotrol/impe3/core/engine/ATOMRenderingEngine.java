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
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.CIPResult;
import com.isotrol.impe3.core.Frame;
import com.isotrol.impe3.core.LayoutResponse;
import com.isotrol.impe3.core.PageResult.Ok;
import com.isotrol.impe3.core.RenderingEngine;
import com.isotrol.impe3.core.engine.PageInstance;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.api.component.atom.ATOMFactory;
import com.isotrol.impe3.api.component.atom.ATOMRenderer;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * ATOM Rendering engine. This class is NOT THREAD-SAFE.
 * @author Emilio Escobar Reyero
 */
public class ATOMRenderingEngine implements RenderingEngine<ATOMRenderer> {
	private static final MediaType MEDIA_TYPE = MediaType.APPLICATION_ATOM_XML_TYPE;

	static final Supplier<RenderingEngine<?>> SUPPLIER = new Supplier<RenderingEngine<?>>() {
		public RenderingEngine<?> get() {
			return new ATOMRenderingEngine();
		}
	};

	/**
	 * Empty response.
	 * @see com.isotrol.impe3.core.RenderingEngine#getLayout(com.isotrol.impe3.core.engine.PageInstance, com.isotrol.impe3.core.PageResult.Ok)
	 */
	public LayoutResponse getLayout(PageInstance pi, Ok result) {
		return LayoutResponse.builder().get();
	}

	/**
	 * @see com.isotrol.impe3.core.RenderingEngine#render(com.isotrol.impe3.core.engine.PageInstance, com.isotrol.impe3.core.PageResult.Ok, javax.ws.rs.core.Response.ResponseBuilder)
	 */
	public Response render(final PageInstance pi, final Ok result,
			final ResponseBuilder builder) {
		final Charset charset = Charset.forName("UTF-8");
		final Map<UUID, ATOMRenderer> renderers = result.getRenderers(
				ATOMRenderer.class, rc(result, pi.getLayout()));
		final StreamingOutput output = new StreamingOutput() {
			public void write(OutputStream output) throws IOException,
					WebApplicationException {

				SyndFeed feed = ATOMFactory.createFeed(result.getContext()
						.getDevice());
				final List<SyndEntry> entries = Lists.newArrayList();

				for (ATOMRenderer renderer : renderers.values()) {
					final SyndFeed modified = renderer.getFeed(feed);
					if (modified != null) {
						feed = modified;
					}
					final Iterable<SyndEntry> list = renderer.getEntries();
					if (list != null) {
						for (SyndEntry entry : list) {
							entries.add(entry);
						}
					}
				}

				if (!entries.isEmpty()) {
					feed.setEntries(entries);
				}

				final SyndFeedOutput out = new SyndFeedOutput();
				try {
					out.output(feed, new OutputStreamWriter(output, charset));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (FeedException e) {
					e.printStackTrace();
				}
			}
		};
		return builder.entity(output).type(MEDIA_TYPE).build();
	}

	private Function<CIP, RenderContext> rc(final Ok result, final List<Frame> layout) {
		Map<CIP, RenderContext> map = Maps.newHashMap();
		for (CIPResult cr : result.getCips().values()) {
			map.put(cr.getCip(), RequestContexts.render(cr.getContext(), null, result.getQuery()));
		}
		return Functions.forMap(map);
	}

}
