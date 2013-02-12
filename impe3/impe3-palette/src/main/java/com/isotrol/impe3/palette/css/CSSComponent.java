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

package com.isotrol.impe3.palette.css;


import java.net.URI;

import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.EditModeComponent;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.CSS;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;


/**
 * CSS Component.
 * @author Andres Rodriguez
 */
public class CSSComponent implements EditModeComponent {
	/** Module Configuration. */
	private final CSSConfig moduleConfiguration;
	/** URI Generator. */
	private final URIGenerator uriGenerator;
	/** Component configuration. */
	private CSSConfig componentConfiguration;
	/** Resolved URI. */
	private URI uri = null;
	/** Resolved IE6 URI. */
	private URI uriIE6 = null;
	/** Resolved IE7 URI. */
	private URI uriIE7 = null;
	/** Resolved IE8 URI. */
	private URI uriIE8 = null;
	/** Resolved media. */
	private String media = null;

	/**
	 * Public constructor.
	 * @param moduleConfiguration Module Configuration.
	 * @param uriGenerator URI Generator.
	 */
	public CSSComponent(final CSSConfig moduleConfiguration, final URIGenerator uriGenerator) {
		this.moduleConfiguration = moduleConfiguration;
		this.uriGenerator = uriGenerator;
	}

	@Inject
	public void setComponentConfiguration(CSSConfig componentConfiguration) {
		this.componentConfiguration = componentConfiguration;
	}

	private URI toURI(String uri) {
		if (uri == null) {
			return null;
		}
		try {
			return URI.create(uri);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
		// 1 - Media (the easy part)
		if (componentConfiguration != null) {
			media = componentConfiguration.media();
		}
		if (media == null) {
			media = moduleConfiguration.media();
		}
		// 2 - URI in component configuration.
		String path = null;
		String path6 = null;
		String path7 = null;
		String path8 = null;
		FileId bundle = null;
		boolean moduleURI = true;
		if (componentConfiguration != null) {
			uri = toURI(componentConfiguration.uri());
			if (uri != null) {
				return;
			}
			path = componentConfiguration.path();
			path6 = componentConfiguration.pathIE6();
			path7 = componentConfiguration.pathIE7();
			path8 = componentConfiguration.pathIE8();
			bundle = componentConfiguration.bundle();
			moduleURI = (path == null && path6 == null && path7 == null && path8 == null && bundle == null);
		}
		if (moduleURI) {
			uri = toURI(moduleConfiguration.uri());
			if (uri != null) {
				return;
			}
		}
		if (path == null) {
			path = moduleConfiguration.path();
		}
		if (path6 == null) {
			path6 = moduleConfiguration.pathIE6();
		}
		if (path7 == null) {
			path7 = moduleConfiguration.pathIE7();
		}
		if (path8 == null) {
			path8 = moduleConfiguration.pathIE8();
		}
		if (bundle == null) {
			bundle = moduleConfiguration.bundle();
		}
		if (bundle != null) {
			if (path != null) {
				uri = uriGenerator.getURI(bundle, path);
			}
			if (path6 != null) {
				uriIE6 = uriGenerator.getURI(bundle, path6);
			}
			if (path7 != null) {
				uriIE7 = uriGenerator.getURI(bundle, path7);
			}
			if (path8 != null) {
				uriIE8 = uriGenerator.getURI(bundle, path8);
			}
		}
	}

	/**
	 * Execute component.
	 */
	public ComponentResponse execute() {
		edit();
		return ComponentResponse.OK;
	}

	@Renderer
	public HTMLRenderer html(RenderContext context) {
		return new HTML(context);
	}

	private class HTML extends SkeletalHTMLRenderer {
		private final Device device;

		HTML(RenderContext context) {
			this.device = context.getDevice();
		}

		@Override
		public Iterable<CSS> getCSS() {
			if (uri == null) {
				return ImmutableList.of();
			}
			return ImmutableList.of(new CSS(device, uri, media));
		}

		@Override
		public Iterable<CSS> getIE6CSS() {
			if (uriIE6 == null) {
				return ImmutableList.of();
			}
			return ImmutableList.of(new CSS(device, uriIE6, media));
		}

		@Override
		public Iterable<CSS> getIE7CSS() {
			if (uriIE7 == null) {
				return ImmutableList.of();
			}
			return ImmutableList.of(new CSS(device, uriIE7, media));
		}

		@Override
		public Iterable<CSS> getIE8CSS() {
			if (uriIE8 == null) {
				return ImmutableList.of();
			}
			return ImmutableList.of(new CSS(device, uriIE8, media));
		}

	}
}
