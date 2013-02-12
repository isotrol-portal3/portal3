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

package com.isotrol.impe3.palette.html;


import net.sf.derquinsej.io.Streams;

import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.EditModeComponent;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLFragments;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;


/**
 * No visual HTML Component.
 * @author Manuel Ruiz
 */
public class NoVisualHTMLComponent implements EditModeComponent {
	/** Module configuration. */
	private HTMLModuleConfig config;
	/** Component configuration. */
	private NoVisualHTMLConfig htmlConfig;
	/** HTML to output. */
	private String html = null;
	/** Encoding. */
	private String encoding = "UTF-8";
	/** File loader. */
	private FileLoader fileLoader;

	/**
	 * Public constructor.
	 */
	public NoVisualHTMLComponent() {
	}

	public void setConfig(HTMLModuleConfig config) {
		this.config = config;
	}

	public void setFileLoader(FileLoader fileLoader) {
		this.fileLoader = fileLoader;
	}

	@Inject
	public void setHtmlConfig(NoVisualHTMLConfig htmlConfig) {
		this.htmlConfig = htmlConfig;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
		if (htmlConfig == null) {
			return;
		}
		// Encoding
		if (config != null && config.encoding() != null) {
			encoding = config.encoding();
		}
		// HTML
		try {
			final FileData data;
			final FileId file = htmlConfig.file();
			if (file != null) {
				data = fileLoader.load(file);
			} else if (htmlConfig.path() != null && config.bundle() != null) {
				data = fileLoader.loadFromBundle(config.bundle(), htmlConfig.path());
			} else {
				return;
			}
			html = new String(Streams.consume(data.getData(), false), encoding);
		} catch (Exception e) {
			html = null;
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
			public HTMLFragment getHeader() {
				if (html == null || htmlConfig.htmlPlace().equals(HTMLPlace.FOOTER)) {
					return null;
				}
				return HTMLFragments.of(html);
			}
			
			@Override
			public HTMLFragment getFooter() {
				if (html == null || htmlConfig.htmlPlace().equals(HTMLPlace.HEADER)) {
					return null;
				}
				return HTMLFragments.of(html);
			}
		};
	}
}
