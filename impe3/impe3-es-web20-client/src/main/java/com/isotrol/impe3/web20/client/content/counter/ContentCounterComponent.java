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
package com.isotrol.impe3.web20.client.content.counter;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLFragments;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;


/**
 * Content counter component.
 * @author Andres Rodriguez
 */
public class ContentCounterComponent implements Component {
	private ContentCounterConfig config;
	/** Content key. */
	private ContentKey contentKey;

	/** Default constructor. */
	public ContentCounterComponent() {
	}

	public void setConfig(ContentCounterConfig config) {
		this.config = config;
	}

	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	/**
	 * Generates and IMG URI for the action.
	 * @param context Render context.
	 * @return URI to use.
	 */
	String getURI(final RenderContext context) {
		if (contentKey == null) {
			return null;
		}
		final String idr = contentKey.getContentType().getStringId() + ':' + contentKey.getContentId();
		final String ct = config.counterType();
		Multimap<String, Object> parameters = ArrayListMultimap.create();
		parameters.put("idr", idr);
		parameters.put("ct", ct);
		return context.getAbsoluteActionURI("countAction", parameters).toASCIIString();
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getFooter() {
				String src = getURI(context);
				if (src == null) {
					return HTMLFragments.empty();
				}
				return HTML.create(context).img(src, "");
			}
		};
	}

	@Inject
	public void setContentKey(ContentKey contentKey) {
		this.contentKey = contentKey;
	}
}
