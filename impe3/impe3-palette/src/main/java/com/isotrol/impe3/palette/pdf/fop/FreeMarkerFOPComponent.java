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

package com.isotrol.impe3.palette.pdf.fop;


import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;

import com.google.common.io.ByteStreams;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.component.pdf.PDFRenderer;
import com.isotrol.impe3.freemarker.FreeMarkerService;
import com.isotrol.impe3.palette.freemarker.FreeMarkerComponentSupport;
import com.isotrol.impe3.palette.freemarker.FreeMarkerComponentSupport.Output;


/**
 * FreeMarker-based XSL-FO Component.
 * @author Andres Rodriguez
 */
public class FreeMarkerFOPComponent implements Component {
	/** FOP Service. */
	private FopService fopService;
	/** Freemarker service. */
	private FreeMarkerService freeMarkerService;
	/** Module Configuration. */
	private FreeMarkerFOPModuleConfig config;
	/** Component Configuration. */
	private FreeMarkerFOPComponentConfig componentConfig;
	/** Template key. */
	private TemplateKey templateKey = null;
	/** Template model. */
	private TemplateModel templateModel = null;

	/**
	 * Public constructor.
	 */
	public FreeMarkerFOPComponent() {
	}

	public void setFopService(FopService fopService) {
		this.fopService = fopService;
	}

	public void setFreeMarkerService(FreeMarkerService freeMarkerService) {
		this.freeMarkerService = freeMarkerService;
	}

	public void setConfig(FreeMarkerFOPModuleConfig config) {
		this.config = config;
	}

	@Inject
	public void setComponentConfig(FreeMarkerFOPComponentConfig componentConfig) {
		this.componentConfig = componentConfig;
	}

	@Inject
	public void setTemplateKey(TemplateKey templateKey) {
		this.templateKey = templateKey;
	}

	@Inject
	public void setTemplateModel(TemplateModel templateModel) {
		this.templateModel = templateModel;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	@Renderer
	public final PDFRenderer getRenderer(final RenderContext context) {
		return new FopRenderer(context, fopService, config, componentConfig) {
			@Override
			protected Transformer getTransformer() {
				return null;
			}

			@Override
			protected Source getSource() {
				final FreeMarkerComponentSupport support = new FreeMarkerComponentSupport(freeMarkerService, config,
					componentConfig, templateKey, templateModel, context);
				final Output output = support.process();
				if (context.getRequestParams().contains("showXSLFO")) {
					final StreamingOutput so = new StreamingOutput() {
						public void write(OutputStream os) throws IOException, WebApplicationException {
							ByteStreams.copy(output.getData(), os);
						}
					};
					final Response r = Response.ok(so).type(MediaType.TEXT_PLAIN_TYPE).build();
					throw new WebApplicationException(r);
				}
				return new StreamSource(output.getData());
			}
		};
	}
}
