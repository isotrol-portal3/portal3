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

package com.isotrol.impe3.palette.redirect;


import java.net.URI;

import javax.annotation.Resource;
import javax.ws.rs.core.UriBuilder;

import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.EditModeComponent;
import com.isotrol.impe3.api.component.Inject;


/**
 * Abstract redirection component.
 * @author Andres Rodriguez
 */
public abstract class RedirectComponent implements EditModeComponent {
	/** URI Generator. */
	@Resource
	private URIGenerator uriGenerator;
	/** Categories. */
	@Resource
	private Categories categories;
	/** Content types. */
	@Resource
	private ContentTypes contentTypes;
	/** Route. */
	private Route route;

	/**
	 * Constructor.
	 */
	public RedirectComponent() {
	}

	@Inject
	public final void setRoute(Route route) {
		this.route = route;
	}

	final Route getRoute() {
		return route;
	}

	final URIGenerator getUriGenerator() {
		return uriGenerator;
	}

	final Categories getCategories() {
		return categories;
	}

	final ContentTypes getContentTypes() {
		return contentTypes;
	}

	public final void edit() {
	}

	final URI replaceQuery(URI uri, String query) {
		if (query == null || query.length() == 0) {
			return uri;
		}
		try {
			return UriBuilder.fromUri(uri).replaceQuery(query).build();
		} catch (Exception e) {
			return uri;
		}
	}
}
