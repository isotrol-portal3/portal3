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


import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableListMultimap;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.freemarker.FreeMarkerService;
import com.isotrol.impe3.palette.Loggers;
import com.isotrol.impe3.palette.freemarker.FreeMarkerComponentSupport.Output;


/**
 * Generate XML with a FreeMarker Template Component.
 * @author Andres Rodriguez
 */
public class XMLComponent implements Component {
	/** Freemarker service. */
	private FreeMarkerService freeMarkerService;
	/** Module Configuration. */
	private XMLModuleConfig config;
	/** Document builder factory. */
	private DocumentBuilderFactory parser;
	/** Component Configuration. */
	private ApplyConfig componentConfig;
	/** Template key. */
	private TemplateKey templateKey = null;
	/** Template model. */
	private TemplateModel templateModel = null;
	/** Component request context. */
	private ComponentRequestContext context;
	/** Output bytes. */
	private Output output;
	/** XML Output. */
	private Source source;

	/**
	 * Public constructor.
	 */
	public XMLComponent() {
	}

	public void setFreeMarkerService(FreeMarkerService freeMarkerService) {
		this.freeMarkerService = freeMarkerService;
	}

	public void setConfig(XMLModuleConfig config) {
		this.config = config;
	}

	public void setParser(DocumentBuilderFactory parser) {
		this.parser = parser;
	}

	@Inject
	public void setComponentConfig(ApplyConfig componentConfig) {
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

	@Inject
	public void setContext(ComponentRequestContext context) {
		this.context = context;
	}

	@Extract
	public Source getSource() {
		return source;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		final RenderContext rc = RequestContexts.render(context, null, ImmutableListMultimap.<String, String> of());
		// Process
		final FreeMarkerComponentSupport support = new FreeMarkerComponentSupport(freeMarkerService, config,
			componentConfig, templateKey, templateModel, rc);
		output = support.process();
		if (output.isOk() && output.getSize() > 0 && parser != null && config != null && config.parse()) {
			source = parse();
		} else {
			source = source();
		}
		return ComponentResponse.OK;
	}

	private Source source() {
		return new StreamSource(output.getData());
	}

	private Source error(Exception e) {
		Loggers.palette().error("Unable to parse XML", e);
		return source();
	}

	private Source parse() {
		try {
			final DocumentBuilder db = parser.newDocumentBuilder();
			final Document doc = db.parse(output.getData());
			return new DOMSource(doc);
		} catch (ParserConfigurationException e) {
			return error(e);
		} catch (IOException e) {
			return error(e);
		} catch (SAXException e) {
			return error(e);
		}
	}

}
