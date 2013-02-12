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


import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;


/**
 * Redirection to category component.
 * @author Andres Rodriguez
 */
public class ToSpecialComponent extends RedirectComponent {
	/** Destination page. */
	private String destination = null;
	/** Redirection type. */
	private Boolean type = false;
	/** Query string. */
	private String query = null;

	/**
	 * Constructor.
	 */
	public ToSpecialComponent() {
	}

	@Inject
	public void setConfig(ToSpecialConfig config) {
		if (config != null) {
			this.destination = config.destination();
			this.type = config.type();
			this.query = config.query();
		}
	}

	/** Component execution. */
	public ComponentResponse execute() {
		if (destination == null) {
			// Nothing to do
			return ComponentResponse.OK;
		}
		final PageKey pk = PageKey.special(destination);
		final Route r = getRoute().toPage(pk);
		// External
		if (type != null && type.booleanValue()) {
			return ComponentResponse.seeOther(replaceQuery(getUriGenerator().getAbsoluteURI(r), query));
		} else {
			return ComponentResponse.internal(r);
		}
	}
}
