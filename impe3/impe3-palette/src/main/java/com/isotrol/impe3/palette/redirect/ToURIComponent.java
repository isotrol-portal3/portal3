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

import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;


/**
 * Redirection to an external URI component.
 * @author Andres Rodriguez
 */
public class ToURIComponent extends RedirectComponent {
	/** Destination URI. */
	private String uri = null;

	/**
	 * Constructor.
	 */
	public ToURIComponent() {
	}

	@Inject
	public void setConfig(ToURIConfig config) {
		if (config != null) {
			this.uri = config.destination();
		}
	}

	/** Component execution. */
	public ComponentResponse execute() {
		URI u;
		final Route route = getRoute();
		try {
			u = new URI(uri);
		} catch (Exception e) {
			if (route != null) {
				u = getUriGenerator().getAbsoluteURI(route.toPage(PageKey.main()));
			} else {
				u = URI.create("/");
			}
		}
		return ComponentResponse.seeOther(u);
	}
}
