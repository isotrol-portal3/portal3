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

package com.isotrol.impe3.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.isotrol.impe3.api.Principal;
import com.isotrol.impe3.api.PrincipalContext;


/**
 * Session-based principal context.
 * @author Andres Rodriguez
 */
public class SessionPrincipalContext implements PrincipalContext {
	private static final String SUFFIX = "__impe3pc__";
	private final HttpSession session;
	private final UUID portalId;
	private final String attribute;

	public SessionPrincipalContext(final HttpSession session, UUID portalId) {
		this.session = checkNotNull(session);
		this.portalId = checkNotNull(portalId);
		this.attribute = portalId.toString().toLowerCase() + SUFFIX;
	}

	public Principal getPrincipal() {
		final Object o = session.getAttribute(attribute);
		if (o instanceof Principal) {
			return (Principal) o;
		}
		return null;
	}

	public void login(Principal principal) {
		session.setAttribute(attribute, checkNotNull(principal));
	}

	public void logout() {
		session.removeAttribute(attribute);
	}

	public UUID getPortalId() {
		return portalId;
	}
}
