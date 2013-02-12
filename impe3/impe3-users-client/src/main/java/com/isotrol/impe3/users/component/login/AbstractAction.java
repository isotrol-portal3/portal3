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

package com.isotrol.impe3.users.component.login;


import javax.ws.rs.core.Response;

import org.springframework.util.StringUtils;

import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.Inject;


/**
 * Base class for login and logout actions.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public abstract class AbstractAction {
	private URIGenerator uriGenerator;
	private Route route;
	private LoginConfig config;
	private PrincipalContext principalContext;

	public AbstractAction() {
	}

	public void setUriGenerator(URIGenerator uriGenerator) {
		this.uriGenerator = uriGenerator;
	}

	public void setConfig(LoginConfig config) {
		this.config = config;
	}

	@Inject
	public void setRoute(Route route) {
		this.route = route;
	}
	
	@Inject
	public void setPrincipalContext(PrincipalContext principalContext) {
		this.principalContext = principalContext;
	}
	
	final PrincipalContext getPrincipalContext() {
		return principalContext;
	}

	protected final PageKey getReturnPage() {
		if (route != null) {
			final PageKey pk = route.getPage();
			if (pk != null) {
				return pk;
			}
		}
		return PageKey.main();
	}
	
	Route getRoute(PageKey pk) {
		if (route == null) {
			return Route.of(false, pk, null, null);
		} else {
			return route.toPage(pk);
		}
	}

	protected Response toPage(PageKey pk) {
		if (pk == null) {
			pk = PageKey.main();
		}
		final Route r = getRoute(pk);
		return Response.seeOther(uriGenerator.getAbsoluteURI(r)).build();
	}

	protected final Response getResponse(boolean ok) {
		final boolean hasConfig = config != null;
		final PageKey pk;
		if (hasConfig) {
			String name = ok ? config.ok() : config.nok();
			pk = StringUtils.hasText(name) ? PageKey.special(name) : null;
		} else {
			pk = null;
		}
		return toPage(pk);
	}
}
