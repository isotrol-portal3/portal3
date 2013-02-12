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

package com.isotrol.impe3.palette.html.js;


import java.net.URI;

import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.EditModeComponent;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.Script;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;


/**
 * External JS Component.
 * @author Andres Rodriguez
 */
public class JSComponent implements EditModeComponent {
	/** Module Configuration. */
	private final JSModuleConfig moduleConfiguration;
	/** URI Generator. */
	private final URIGenerator uriGenerator;
	/** Component configuration. */
	private JSConfig componentConfiguration;
	/** Resolved URI. */
	private URI uri = null;

	/**
	 * Public constructor.
	 * @param moduleConfiguration Module Configuration.
	 * @param uriGenerator URI Generator.
	 */
	public JSComponent(final JSModuleConfig moduleConfiguration, final URIGenerator uriGenerator) {
		this.moduleConfiguration = moduleConfiguration;
		this.uriGenerator = uriGenerator;
	}

	@Inject
	public void setComponentConfiguration(JSConfig componentConfiguration) {
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
		// Resolve URI
		String path = null;
		FileId bundle = null;
		boolean moduleURI = true;
		if (componentConfiguration != null) {
			uri = toURI(componentConfiguration.headerUri());
			if (uri != null) {
				return;
			}
			path = componentConfiguration.headerPath();
			bundle = componentConfiguration.bundle();
			moduleURI = (path == null && bundle == null);
		}
		if (moduleURI) {
			uri = toURI(moduleConfiguration.headerUri());
			if (uri != null) {
				return;
			}
		}
		if (path == null) {
			path = moduleConfiguration.headerPath();
		}
		if (bundle == null) {
			bundle = moduleConfiguration.bundle();
		}
		if (bundle != null) {
			if (path != null) {
				uri = uriGenerator.getURI(bundle, path);
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
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public Iterable<Script> getHeaderScripts() {
				if (uri == null || !componentConfiguration.useInHeader()) {
					return ImmutableList.of();
				}
				return ImmutableList.of(new Script(context, uri));
			}

			@Override
			public HTMLFragment getFooter() {
				if (uri == null || componentConfiguration.useInHeader()) {
					return null;
				}
				return new Script(context, uri);
			}
		};
	}
}
