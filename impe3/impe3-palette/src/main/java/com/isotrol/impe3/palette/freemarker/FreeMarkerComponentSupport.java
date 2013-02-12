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

package com.isotrol.impe3.palette.freemarker;


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableListMultimap;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.freemarker.FreeMarkerService;


/**
 * Support class for freemarker components.
 * @author Andres Rodriguez
 */
public class FreeMarkerComponentSupport {
	/** Freemarker service. */
	private final FreeMarkerService freeMarkerService;
	/** Module Configuration. */
	private final ApplyModuleConfig config;
	/** Component Configuration. */
	private final ApplyConfig componentConfig;
	/** Template key. */
	private final TemplateKey templateKey;
	/** Template model. */
	private final TemplateModel templateModel;
	/** Render context. */
	private final RenderContext context;

	/**
	 * Public constructor.
	 */
	public FreeMarkerComponentSupport(FreeMarkerService freeMarkerService, ApplyModuleConfig config,
		ApplyConfig componentConfig, TemplateKey templateKey, TemplateModel templateModel, RenderContext context) {
		this.freeMarkerService = checkNotNull(freeMarkerService);
		this.config = config;
		this.componentConfig = componentConfig;
		this.templateKey = templateKey;
		this.templateModel = templateModel;
		this.context = checkNotNull(context);
	}

	public Output process() {
		// Template
		final String template = getTemplate();
		if (template == null) {
			// Nothing to do
			return new Output(true, null);
		}
		final RenderContext rc = RequestContexts.render(context, null, ImmutableListMultimap.<String, String> of());
		// Template model
		final TemplateModel model;
		if (templateModel != null) {
			model = templateModel;
			freeMarkerService.decorateRenderModel(rc, model);
		} else {
			model = freeMarkerService.createModel(rc);
		}
		// Create output stream
		final ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
		final OutputStreamWriter osw = new OutputStreamWriter(os, getCharset());
		// Process
		try {
			freeMarkerService.process(template, rc, null, model, osw, null, false);
			osw.flush();
			osw.close();
			return new Output(true, os.toByteArray());
		} catch (Exception e) {
			return new Output(false, null);
		}
	}

	private String getTemplate() {
		String template = null;
		if (templateKey != null) {
			template = templateKey.toString();
		}
		if (template == null) {
			if (componentConfig != null && componentConfig.templateFile() != null) {
				template = componentConfig.templateFile();
			} else if (config != null && config.templateFile() != null) {
				template = config.templateFile();
			}
		}
		return template;
	}

	private Charset getCharset() {
		if (config == null) {
			return Charsets.UTF_8;
		}
		final String name = config.encoding();
		if (name == null) {
			return Charsets.UTF_8;
		}
		try {
			return Charset.forName(name);
		} catch (RuntimeException e) {
			return Charsets.UTF_8;
		}
	}

	public static final class Output {
		private final boolean ok;
		private final byte[] data;

		Output(boolean ok, byte[] data) {
			this.ok = ok;
			this.data = data != null ? data : new byte[0];
		}

		public boolean isOk() {
			return ok;
		}

		public int getSize() {
			return data.length;
		}

		public InputStream getData() {
			return new ByteArrayInputStream(data);
		}
	}

}
