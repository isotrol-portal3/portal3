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
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.component.BinaryRenderer;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.excel.ExcelRenderer;
import com.isotrol.impe3.api.component.pdf.PDFRenderer;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.CIPResult;
import com.isotrol.impe3.core.LayoutResponse;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.PageResult.Ok;
import com.isotrol.impe3.core.RenderingEngine;


/**
 * Binary Rendering engine. This class is NOT THREAD-SAFE.
 * @author Andres Rodriguez
 * @param <R> Renderer type.
 */
public final class BinaryRenderingEngine<R extends BinaryRenderer> implements RenderingEngine<R> {
	/** Renderer type. */
	private final Class<R> rendererType;
	/** Logger. */
	private final Logger logger = Loggers.core();
	
	static final Supplier<RenderingEngine<?>> EXCEL_SUPPLIER = new Supplier<RenderingEngine<?>>() {
		public RenderingEngine<?> get() {
			return new BinaryRenderingEngine<ExcelRenderer>(ExcelRenderer.class);
		}
	};

	static final Supplier<RenderingEngine<?>> PDF_SUPPLIER = new Supplier<RenderingEngine<?>>() {
		public RenderingEngine<?> get() {
			return new BinaryRenderingEngine<PDFRenderer>(PDFRenderer.class);
		}
	};
	
	BinaryRenderingEngine(Class<R> rendererType) {
		this.rendererType = rendererType;
	}

	private Function<CIP, RenderContext> rc(final Ok result) {
		Map<CIP, RenderContext> map = Maps.newHashMap();
		for (CIPResult cr : result.getCips().values()) {
			map.put(cr.getCip(), RequestContexts.render(cr.getContext(), null, result.getQuery()));
		}
		return Functions.forMap(map);
	}

	public LayoutResponse getLayout(PageInstance pi, Ok result) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.isotrol.impe3.core.RenderingEngine#render(com.isotrol.impe3.core.PageResult.Ok, java.util.List,
	 * javax.ws.rs.core.Response.ResponseBuilder)
	 */
	public Response render(final PageInstance pi, final Ok result, final ResponseBuilder builder) {
		// 1 - Obtain renderer.
		final Map<UUID, R> renderers = result.getRenderers(rendererType, rc(result));
		if (renderers == null || renderers.isEmpty()) {
			logger.warn("No binary renderer!!!");
			return builder.build(); // TODO
		}
		final R renderer = renderers.values().iterator().next();
		// 2 - Prepare response
		renderer.prepare();
		builder.type(renderer.getMediaType());
		final Integer length = renderer.getLength();
		if (length != null && length >= 0) {
			builder.header(HttpHeaders.CONTENT_LENGTH, length);
		}
		final String file = renderer.getFileName();
		if (StringUtils.hasText(file)) {
			builder.header("Content-Disposition", "attachment; filename=" + file);
		}
		// 3 - Prepare response writing
		final StreamingOutput output = new StreamingOutput() {
			public void write(OutputStream output) throws IOException, WebApplicationException {
				renderer.write(output);
			}
		};
		// 4 - Done
		return builder.entity(output).build();
	}
}
