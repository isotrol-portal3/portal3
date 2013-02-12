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

package com.isotrol.impe3.support.action;


import java.net.URI;
import java.util.UUID;

import javax.ws.rs.core.Response;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.Inject;


/**
 * An utility base class for actions that use the redirect after post pattern.
 * @author Andres Rodriguez
 */
public abstract class RedirectAction {
	private static boolean hasText(String text) {
		if (text == null || text.length() == 0) {
			return false;
		}
		final int n = text.length();
		if (n == 0) {
			return false;
		}
		for (int i = 0; i < n; i++) {
			if (!Character.isWhitespace(text.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasValue(Object value) {
		if (value instanceof String) {
			return hasText((String) value);
		}
		return value != null;
	}

	/** Component in page Id. */
	private UUID id;
	/** Calling route (if known). */
	private Route route;
	/** Query parameters for success. */
	private Multimap<String, Object> okQP;
	/** Query parameters for errors. */
	private Multimap<String, Object> errorQP;
	/** URI Generator. */
	private URIGenerator uriGenerator;

	public RedirectAction() {
	}

	protected final UUID getId() {
		return id;
	}

	@Inject
	public final void setId(UUID id) {
		this.id = id;
	}

	protected final Route getRoute() {
		return route;
	}

	protected final Route getRoute(PageKey key) {
		if (route == null) {
			return Route.of(false, key, null, null);
		}
		return route.toPage(key);
	}

	@Inject
	public void setRoute(Route route) {
		this.route = route;
	}

	protected final URIGenerator getUriGenerator() {
		return uriGenerator;
	}

	public final void setUriGenerator(URIGenerator uriGenerator) {
		this.uriGenerator = uriGenerator;
	}

	protected final void registerSuccessQP(String name, Object value) {
		if (hasText(name) && hasValue(value)) {
			if (okQP == null) {
				okQP = ArrayListMultimap.create();
			}
			okQP.put(name, value);
		}
	}

	protected final void registerErrorQP(String name, Object value) {
		if (hasText(name) && hasValue(value)) {
			if (errorQP == null) {
				errorQP = ArrayListMultimap.create();
			}
			errorQP.put(name, value);
		}
	}

	protected final void registerQP(String name, Object value) {
		registerSuccessQP(name, value);
		registerErrorQP(name, value);
	}

	protected final URI getURI(PageKey key, Multimap<String, ?> parameters) {
		return uriGenerator.getAbsoluteURI(getRoute(key), parameters);
	}

	protected final Response getResponse(PageKey key, Multimap<String, ?> parameters) {
		return Response.seeOther(getURI(key, parameters)).build();
	}

	protected final Response getBack(Multimap<String, ?> parameters) {
		return Response.seeOther(uriGenerator.getAbsoluteURI(route, parameters)).build();
	}
	
	/**
	 * Performs a redirection to the specified special page with the parameters registered for success.
	 * @param name Destination page. If {@code null} or empty the redirection will be to the main page.
	 * @return The action response.
	 */
	protected final Response ok2Special(String name) {
		return getResponse(hasText(name) ? PageKey.special(name) : PageKey.main(), okQP);
	}

	/**
	 * Performs a redirection to the calling page with the parameters registered for error.
	 * If there is no calling page, the redirection will be performed to the main page with no parameters.
	 * @return The action response.
	 */
	protected final Response errorBack() {
		if (route == null) {
			return getResponse(PageKey.main(), null);
		}
		return getBack(errorQP);
	}

}
