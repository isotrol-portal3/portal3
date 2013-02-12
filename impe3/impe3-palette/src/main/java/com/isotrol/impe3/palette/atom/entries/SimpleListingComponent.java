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

package com.isotrol.impe3.palette.atom.entries;


import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.component.atom.ATOMRenderer;
import com.isotrol.impe3.api.component.atom.EntryTransformFunction;
import com.isotrol.impe3.api.component.atom.SkeletalATOMRenderer;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.Listing;
import com.sun.syndication.feed.synd.SyndEntry;


/**
 * Listing Page Component.
 * @author Emilio Escobar Reyero.
 */
@RequiresLink(Listing.class)
public class SimpleListingComponent implements Component {
	/** Configuration. */
	private final ListingModuleConfig config;

	/**
	 * Constructor.
	 * @param config Module configuration.
	 */
	@Autowired
	public SimpleListingComponent(ListingModuleConfig config) {
		this.config = checkNotNull(config);
	}

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
	public ATOMRenderer atom(final RenderContext context) {
		return new SkeletalATOMRenderer() {
			@Override
			public Iterable<SyndEntry> getEntries() {
				if (page == null) {
					return super.getEntries();
				}
				return Lists.transform(page, new EntryTransformFunction(context, config));
			}
		};
	}

}
