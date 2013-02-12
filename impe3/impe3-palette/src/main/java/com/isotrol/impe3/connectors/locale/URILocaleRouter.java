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


import java.util.Locale;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.LocaleResolutionParams;
import com.isotrol.impe3.api.LocaleResolver;
import com.isotrol.impe3.api.LocaleRouter;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.ResolvedLocale;
import com.isotrol.impe3.api.support.DefaultLocaleURIGenerator;


/**
 * URI Locale Router Connector.
 * @author Andres Rodriguez Chamorro
 */
public class URILocaleRouter implements LocaleRouter {
	/** Configuration. */
	private final boolean useDefaultLocale;
	/** Fallback resolver. */
	@SuppressWarnings("unused")
	private LocaleResolver fallback;
	/** Locale cache. */
	private final LoadingCache<Portal, ImmutableMap<String, Locale>> cache;

	public URILocaleRouter(URILocaleRouterConfig config, LocaleResolver fallback) {
		if (config != null) {
			this.useDefaultLocale = config.useDefaultLocale();
		} else {
			this.useDefaultLocale = true;
		}
		this.fallback = fallback;
		this.cache = CacheBuilder.newBuilder().weakKeys().build(new Loader());
	}

	/**
	 * @see com.isotrol.impe3.api.LocaleResolver#resolveLocale(com.isotrol.impe3.api.LocaleResolutionParams)
	 */
	public ResolvedLocale resolveLocale(LocaleResolutionParams params) {
		Locale locale = null;
		PathSegments p = params.getPath();
		if (p.isEmpty()) {
			// TODO use fallback
			return new ResolvedLocale(params.getPath(), params.getPortal().getDefaultLocale(), params.getParameters());
		}
		String segment = p.get(0);
		locale = cache.getUnchecked(params.getPortal()).get(segment);
		if (locale == null) {
			// TODO use fallback
			return new ResolvedLocale(params.getPath(), params.getPortal().getDefaultLocale(), params.getParameters());
		}
		return new ResolvedLocale(p.consume(), locale, params.getParameters());
	}

	/**
	 * @see com.isotrol.impe3.api.LocaleURIGenerator#getTransformer(com.isotrol.impe3.api.Portal, java.util.Locale)
	 */
	public Function<PathSegments, PathSegments> getTransformer(Portal portal, Locale locale) {
		if (useDefaultLocale && Objects.equal(portal.getDefaultLocale(), locale)) {
			return DefaultLocaleURIGenerator.get().getTransformer(portal, locale);
		}
		final ImmutableList<String> segments = ImmutableList.of(locale.toString());
		return new Function<PathSegments, PathSegments>() {
			public PathSegments apply(PathSegments input) {
				return PathSegments.of(false, Iterables.concat(segments, input));
			}
		};
	}
	
	private static final class Loader extends CacheLoader<Portal, ImmutableMap<String, Locale>> {
		@Override
		public ImmutableMap<String, Locale> load(Portal key) throws Exception {
			return Maps.uniqueIndex(key.getLocales(), Functions.toStringFunction());
		}
	}
}
