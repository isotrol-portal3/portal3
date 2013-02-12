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

package com.isotrol.impe3.pms.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;
import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.LocaleRouter;
import com.isotrol.impe3.api.PageResolver;
import com.isotrol.impe3.api.PageURI;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.core.engine.AbstractPortalRouter;


/**
 * PMS Portal router.
 * @author Andres Rodriguez.
 */
final class PMSPortalRouter extends AbstractPortalRouter {
	final Supplier<PathSegments> base;

	PMSPortalRouter(Portal portal, DeviceRouter deviceRouter, LocaleRouter localeRouter, PageResolver resolver,
		Supplier<PathSegments> base) {
		super(portal, deviceRouter, localeRouter, resolver);
		this.base = checkNotNull(base);
	}

	@Override
	protected PageURI getPortalRoute(Route route) {
		return new PageURI(base.get(), null);
	}
}
