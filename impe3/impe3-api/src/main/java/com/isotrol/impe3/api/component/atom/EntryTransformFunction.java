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

package com.isotrol.impe3.api.component.atom;


import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import com.google.common.base.Function;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.content.Content;
import com.sun.syndication.feed.synd.SyndEntry;


/**
 * Basic content to entry transformation function.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public class EntryTransformFunction implements Function<Content, SyndEntry> {
	/** Render context. */
	private final RenderContext context;
	/** Configuration. */
	private final EntryTransformConfig config;

	/**
	 * Constructor.
	 * @param context Render context.
	 * @param config Configuration.
	 */
	public EntryTransformFunction(RenderContext context, EntryTransformConfig config) {
		this.context = checkNotNull(context);
		this.config = config;
	}

	public SyndEntry apply(Content c) {
		final ContentKey key = c.getContentKey();
		NavigationKey nk = null;
		if (config != null && config.keepNavigation()) {
			nk = context.getNavigationKey();
			if (nk != null) {
				nk = nk.withoutContentType();
			}
		}
		final PageKey pageKey = PageKey.content(nk, key);
		Route route = context.getRoute().toPage(pageKey);
		if (config != null && config.toDefaultDevice()) {
			route = route.toDevice(context.getPortal().getDevice());
		}
		final URI link = context.getAbsoluteURI(route);
		return ATOMFactory.entry(c, link, config);
	}

}
