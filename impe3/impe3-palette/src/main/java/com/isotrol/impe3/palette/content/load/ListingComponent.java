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

package com.isotrol.impe3.palette.content.load;


import static com.isotrol.impe3.nr.api.NodeQueries.all;
import static com.isotrol.impe3.nr.api.NodeQueries.null2All;
import net.sf.derquinse.lucis.Page;

import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.support.WithPageSize;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.palette.content.AbstractListingComponent;
import com.isotrol.impe3.support.nr.ContentRepository;


/**
 * Reads a page from a ContentRepository.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
@RequiresLink(Pagination.class)
public class ListingComponent extends AbstractListingComponent<Content> {
	/** Content repository. */
	private ContentRepository contentRepository;
	/** Page configuration. */
	private PageConfig config;
	/** Navigation key. */
	private NavigationKey navigationKey;

	/**
	 * Constructor.
	 */
	public ListingComponent() {
	}

	public void setContentRepository(ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}

	@Override
	protected WithPageSize getConfiguration() {
		return config;
	}

	@Inject
	public void setConfig(PageConfig config) {
		this.config = config;
	}

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	/** Component execution. */
	public ComponentResponse execute() {
		// Navigation query
		final NodeQuery nq = contentRepository.navigationKey(navigationKey);
		// Filter query
		final NodeQuery f;
		if (config == null) {
			f = null;
		} else {
			f = all(contentRepository.category(config.category()), contentRepository.tag(config.tag()),
				contentRepository.contentType(config.contentType()));
		}
		// Final query
		final NodeQuery q = null2All(all(nq, f));
		final Pagination p = loadPagination(10);
		final Page<Content> page = contentRepository.getPage(q, null, false, p.getFirstRecord(), p.getSize());
		setPage(page);
		return ComponentResponse.OK;
	}
}
