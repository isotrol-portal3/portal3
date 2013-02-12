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

import java.net.URI;

import com.isotrol.impe3.api.PageURI;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.core.support.AbstractURIGenerator;


/**
 * Abstract URI generator implementation.
 * @author Andres Rodriguez.
 */
public final class EngineURIGenerator extends AbstractURIGenerator {
	private final PortalRouter portalRouter;

	/**
	 * Default constructor.
	 * @param portal Currently running portal.
	 * @param base Base URI.
	 * @param absBase Absolute base URI.
	 * @param portalRouter Portal router.
	 */
	public EngineURIGenerator(final Portal portal, final URI base, final URI absBase, final PortalRouter portalRouter) {
		super(portal, base, absBase);
		this.portalRouter = checkNotNull(portalRouter);
	}

	protected final PageURI getRouteSegments(Route route) {
		return portalRouter.getRoute(route);
	}
}
