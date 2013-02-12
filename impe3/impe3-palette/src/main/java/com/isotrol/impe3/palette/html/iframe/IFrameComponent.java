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

package com.isotrol.impe3.palette.html.iframe;


import javax.ws.rs.core.UriBuilder;

import org.springframework.util.StringUtils;

import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.VisualComponent;
import com.isotrol.impe3.api.component.html.HTMLConstants;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.api.component.html.Tag;


/**
 * HTML IFRAME Component.
 * @author Andres Rodriguez
 */
public class IFrameComponent implements VisualComponent {
	/** Component Configuration. */
	private IFrameConfig config;
	/** Query parameters. */
	private RequestParams query;

	/**
	 * Public constructor.
	 */
	public IFrameComponent() {
	}

	@Inject
	public void setConfig(IFrameConfig config) {
		this.config = config;
	}

	@Inject
	public void setQuery(RequestParams query) {
		this.query = query;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
	}

	final String getURI() {
		String u = config.uri();
		final UriBuilder b;
		try {
			b = UriBuilder.fromUri(u);
		} catch (IllegalArgumentException e) {
			return null;
		}
		if (query != null) {
			addQuery(b, config.qp1());
			addQuery(b, config.qp2());
			addQuery(b, config.qp3());
			addQuery(b, config.qp4());
			addQuery(b, config.qp5());
		}
		return b.build().toASCIIString();
	}

	private void addQuery(UriBuilder b, String qp) {
		if (StringUtils.hasText(qp) && query.contains(qp)) {
			for (String v : query.get(qp)) {
				b.queryParam(qp, v);
			}
		}
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				Tag tag = Tag.create(context, HTMLConstants.IFRAME);
				if (config != null) {
					tag.set(HTMLConstants.SRC, getURI());
					tag.set(HTMLConstants.WIDTH, config.width());
					tag.set(HTMLConstants.HEIGHT, config.height());
					final Boolean border = config.frameborder();
					if (border != null) {
						tag.set(HTMLConstants.FRAMEBORDER, border.booleanValue() ? "1" : "0");
					}
					final Boolean scroll = config.scrolling();
					if (scroll != null) {
						tag.set(HTMLConstants.SCROLLING, scroll.booleanValue() ? HTMLConstants.YES : HTMLConstants.NO);
					}
				}
				return tag;
			}
		};
	}
}
