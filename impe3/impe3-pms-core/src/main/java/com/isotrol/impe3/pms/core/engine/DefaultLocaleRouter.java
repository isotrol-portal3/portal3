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


import java.util.Locale;

import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.isotrol.impe3.api.LocaleResolutionParams;
import com.isotrol.impe3.api.LocaleRouter;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.ResolvedLocale;
import com.isotrol.impe3.api.support.DefaultLocaleURIGenerator;


/**
 * Default device router.
 * @author Andres Rodriguez.
 */
@Component
public class DefaultLocaleRouter implements LocaleRouter {
	private static final Locale SPANISH = new Locale("es", "ES");

	/**
	 * Constructor.
	 */
	public DefaultLocaleRouter() {
	}

	/**
	 * @see com.isotrol.impe3.api.LocaleResolver#resolveLocale(com.isotrol.impe3.api.LocaleResolutionParams)
	 */
	public ResolvedLocale resolveLocale(LocaleResolutionParams params) {
		return new ResolvedLocale(params.getPath(), SPANISH, params.getParameters());
	}

	/**
	 * @see com.isotrol.impe3.api.LocaleURIGenerator#getTransformer(com.isotrol.impe3.api.Portal, java.util.Locale)
	 */
	public Function<PathSegments, PathSegments> getTransformer(Portal portal, Locale locale) {
		return DefaultLocaleURIGenerator.get().getTransformer(portal, locale);
	}
}
