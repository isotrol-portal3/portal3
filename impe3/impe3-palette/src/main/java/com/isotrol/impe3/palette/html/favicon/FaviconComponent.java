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

package com.isotrol.impe3.palette.html.favicon;


import static com.isotrol.impe3.api.component.html.HTMLConstants.HREF;
import static com.isotrol.impe3.api.component.html.HTMLConstants.REL;
import static com.isotrol.impe3.api.component.html.HTMLConstants.TYPE;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.EditModeComponent;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLFragments;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;


/**
 * HTML Favicon Component.
 * @author Andres Rodriguez
 */
public class FaviconComponent implements EditModeComponent {
	/** File loader. */
	private FileLoader fileLoader;
	/** URI Generator. */
	private URIGenerator uriGenerator;
	/** Module Configuration. */
	private FaviconConfig config;
	/** Relationship. */
	private String rel = null;
	/** Image type. */
	private MediaType type = null;
	/** Image URI. */
	private URI uri;

	/**
	 * Public constructor.
	 */
	public FaviconComponent() {
	}

	/* Spring injection. */

	public void setConfig(FaviconConfig config) {
		this.config = config;
	}

	public void setFileLoader(FileLoader fileLoader) {
		this.fileLoader = fileLoader;
	}

	public void setUriGenerator(URIGenerator uriGenerator) {
		this.uriGenerator = uriGenerator;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		edit();
		return ComponentResponse.OK;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
		if (config != null) {
			rel = config.rel();
			if (rel == null) {
				rel = "icon";
			}
			FileData file = fileLoader.load(config.icon());
			type = file.getMediaType();
			uri = uriGenerator.getURI(config.icon());
		}

	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getHeader() {
				if (rel == null || type == null || uri == null) {
					return HTMLFragments.empty();
				}
				return HTML.create(context).link().set(REL, rel).set(TYPE, type.toString())
					.set(HREF, uri.toASCIIString());
			}
		};
	}
}
