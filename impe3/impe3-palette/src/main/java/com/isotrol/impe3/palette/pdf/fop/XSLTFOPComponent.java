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


import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.pdf.PDFRenderer;


/**
 * XSLT-based XSL-FO Component.
 * @author Andres Rodriguez
 */
public class XSLTFOPComponent implements Component {
	/** FOP Service. */
	private FopService fopService;
	/** Module Configuration. */
	private XSLTFOPModuleConfig config;
	/** Component Configuration. */
	private XSLTFOPComponentConfig componentConfig;
	/** Source. */
	private Source source;
	/** Stylesheet. */
	private FileId stylesheet;
	/** File loader. */
	private FileLoader fileLoader;

	/**
	 * Public constructor.
	 */
	public XSLTFOPComponent() {
	}

	public void setFopService(FopService fopService) {
		this.fopService = fopService;
	}

	public void setConfig(XSLTFOPModuleConfig config) {
		this.config = config;
	}

	public void setFileLoader(FileLoader fileLoader) {
		this.fileLoader = fileLoader;
	}

	@Inject
	public void setComponentConfig(XSLTFOPComponentConfig componentConfig) {
		this.componentConfig = componentConfig;
	}

	@Inject
	public void setSource(Source source) {
		this.source = source;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		stylesheet = null;
		if (componentConfig != null) {
			stylesheet = componentConfig.xsltFile();
		}
		if (stylesheet == null && config != null) {
			stylesheet = config.xsltFile();
		}
		return ComponentResponse.OK;
	}

	@Renderer
	public final PDFRenderer getRenderer(final RenderContext context) {
		return new FopRenderer(context, fopService, config, componentConfig) {
			@Override
			protected Transformer getTransformer() throws TransformerException {
				if (stylesheet == null) {
					return null;
				}
				final Source xslt = new StreamSource(fileLoader.load(stylesheet).getData());
				return fopService.newTransformer(xslt);
			}

			@Override
			protected Source getSource() {
				if (stylesheet == null) {
					return null;
				}
				return source;
			}
		};
	}
}
