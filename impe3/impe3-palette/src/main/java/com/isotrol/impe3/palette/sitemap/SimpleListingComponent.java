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

package com.isotrol.impe3.palette.sitemap;


import java.net.URI;
import java.util.List;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.component.sitemap.SitemapRenderer;
import com.isotrol.impe3.api.component.sitemap.SitemapRenderers;
import com.isotrol.impe3.api.component.sitemap.URLEntry;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.Listing;


/**
 * Listing Page Component.
 * @author Emilio Escobar Reyero.
 * @author Andres Rodriguez
 */
@RequiresLink(Listing.class)
public class SimpleListingComponent implements Component {

	/** Listing page. */
	private Listing<Content> page = null;

	@Inject
	public void setPage(Listing<Content> page) {
		this.page = page;
	}

	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	@Renderer
	public SitemapRenderer sitemap(final RenderContext context) {
		final List<URLEntry> entries = Lists.newLinkedList();
		if (page != null) {
			Route r = context.getRoute();
			r = r.toDevice(context.getPortal().getDevice());
			for (Content c : page) {
				PageKey pk = PageKey.content(c.getContentKey());
				URI u = context.getAbsoluteURI(r.toPage(pk));
				entries.add(URLEntry.of(u));
			}
		}
		return SitemapRenderers.set(entries);
	}

}
