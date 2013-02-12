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


import org.springframework.util.StringUtils;

import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Principal;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;


/**
 * Login actions exporter component.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public class IsLoggedComponent implements Component {
	private String redirect;
	private String role;
	private PrincipalContext principalContext;
	private Route route;

	public IsLoggedComponent() {
	}

	public void setRedirect(String redirect) {
		this.redirect = StringUtils.hasText(redirect) ? redirect : null;
	}

	public void setRole(String role) {
		this.role = StringUtils.hasText(role) ? role : null;
	}

	@Inject
	public void setConfig(IsLoggedConfig config) {
		if (config != null) {
			setRedirect(config.redirect());
			setRole(config.role());
		}
	}

	@Inject
	public void setPrincipalContext(PrincipalContext principalContext) {
		this.principalContext = principalContext;
	}

	@Inject
	public void setRoute(Route route) {
		this.route = route;
	}

	private ComponentResponse redirect() {
		final PageKey pk = redirect != null ? PageKey.special(redirect) : PageKey.main();
		return ComponentResponse.internal(route.toPage(pk));
	}

	public ComponentResponse execute() {
		if (isOk()) {
			return ComponentResponse.OK;
		}
		return redirect();
	}

	private boolean isOk() {
		if (principalContext == null) {
			return false;
		}
		final Principal p = principalContext.getPrincipal();
		if (p == null) {
			return false;
		}
		if (role != null) {
			return p.getRoles().contains(role);
		}
		return true;
	}
}
