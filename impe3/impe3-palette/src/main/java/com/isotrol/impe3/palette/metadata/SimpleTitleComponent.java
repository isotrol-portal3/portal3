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

package com.isotrol.impe3.palette.metadata;


import org.springframework.util.StringUtils;

import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.EditModeComponent;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.atom.ATOMRenderer;
import com.isotrol.impe3.api.component.atom.SkeletalATOMRenderer;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.sun.syndication.feed.synd.SyndFeed;


/**
 * Simple Title Component.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public class SimpleTitleComponent implements EditModeComponent {
	/** Module configuration. */
	private SimpleTitleConfig moduleConfig;
	/** Component configuration. */
	private SimpleTitleConfig config;
	/** Resolved title. */
	private String title = null;

	/**
	 * Public constructor.
	 */
	public SimpleTitleComponent() {
	}

	public void setModuleConfig(SimpleTitleConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}

	@Inject
	public void setConfig(SimpleTitleConfig config) {
		this.config = config;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
		if (config != null && StringUtils.hasText(config.title())) {
			title = config.title();
		} else if (moduleConfig != null && StringUtils.hasText(moduleConfig.title())) {
			title = moduleConfig.title();
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
				if (title == null) {
					return null;
				}
				return HTML.create(context).title(title);
			}
		};
	}
	
	@Renderer
	public ATOMRenderer atom(final RenderContext context) {
		return new SkeletalATOMRenderer() {
			@Override
			public SyndFeed getFeed(SyndFeed feed) {
				if (title == null) {
					return null;
				}
				feed.setTitle(title);
				return feed;
			}
		};
	}
}
