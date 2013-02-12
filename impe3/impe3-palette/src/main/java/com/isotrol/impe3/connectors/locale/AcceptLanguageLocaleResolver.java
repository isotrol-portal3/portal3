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

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.core.HttpHeaders;

import com.google.common.collect.Sets;
import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.LocaleResolutionParams;
import com.isotrol.impe3.api.LocaleResolver;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.ResolvedLocale;

/**
 * Accept-Language header locale resolver.
 * 
 * @author Emilio Escobar Reyero
 */
public class AcceptLanguageLocaleResolver implements LocaleResolver {

	// Accept-Language
	// en-ca,en;q=0.8,en-us;q=0.6,de-de;q=0.4,de;q=0.2

	public ResolvedLocale resolveLocale(LocaleResolutionParams params) {
		return resolve(params.getPortal(), params.getPath(),
				params.getHeaders(), params.getRequest(),
				params.getParameters());
	}

	private ResolvedLocale resolve(Portal portal, PathSegments path,
			HttpHeaders headers, HttpRequestContext request,
			LocalParams parameters) {

		final Locale locale = headers.getLanguage();
		final List<Locale> acceptables = headers.getAcceptableLanguages();

		if (locale == null && (acceptables == null || acceptables.isEmpty())) {
			return null;
		}

		final Set<Locale> locales = portalSupportedLocales(
				portal.getDefaultLocale(), portal.getLocales());

		if (locales.isEmpty()) {
			return null;
		}

		if (locale != null && locales.contains(locale)) {
			return new ResolvedLocale(path, locale, parameters);
		}

		if (acceptables != null) {
			for (Locale l : acceptables) {
				if (locales.contains(l)) {
					return new ResolvedLocale(path, l, parameters);
				}
			}
		}

		return null;
	}

	private Set<Locale> portalSupportedLocales(Locale d, Set<Locale> s) {
		final Set<Locale> locales = Sets.newHashSet();
		if (d != null) {
			locales.add(d);
		}
		if (s != null) {
			locales.addAll(s);
		}
		return locales;
	}

}
