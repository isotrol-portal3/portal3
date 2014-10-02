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

package com.isotrol.impe3.connectors.locale;


import static com.google.common.base.MoreObjects.firstNonNull;

import java.util.Locale;

import com.isotrol.impe3.api.LocaleResolutionParams;
import com.isotrol.impe3.api.LocaleResolver;
import com.isotrol.impe3.api.ResolvedLocale;


/**
 * Fallback locale resolver based on a portal default.
 * @author Andres Rodriguez Chamorro
 */
public class PortalDefaultLocaleResolver implements LocaleResolver {
	private static final Locale SPANISH = new Locale("es", "ES");

	public PortalDefaultLocaleResolver() {
	}

	/**
	 * @see com.isotrol.impe3.api.LocaleResolver#resolveLocale(com.isotrol.impe3.api.LocaleResolutionParams)
	 */
	public ResolvedLocale resolveLocale(LocaleResolutionParams params) {
		Locale locale = params.getPortal().getDefaultLocale();
		return new ResolvedLocale(params.getPath(), firstNonNull(locale, SPANISH), params.getParameters());
	}

}
