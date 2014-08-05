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


import static com.isotrol.impe3.api.ContentType.IS_NAVIGABLE;
import static com.isotrol.impe3.api.RoutableNamedIdentifiable.IS_ROUTABLE;

import java.util.Locale;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.core.Loggers;


/**
 * ContentType routing map.
 * @author Andres Rodriguez
 */
final class ContentTypeRoutingMap {
	private static final Segmenter SEGMENTER = new Segmenter() {
		public String get(Locale locale, ContentType contentType) {
			return contentType.getName().get(locale).getPath();
		}
	};

	static ContentTypeRoutingMap content(final ContentTypes contentTypes) {
		return new ContentTypeRoutingMap(contentTypes, IS_ROUTABLE, SEGMENTER);
	}

	static ContentTypeRoutingMap navigation(final ContentTypes contentTypes) {
		return new ContentTypeRoutingMap(contentTypes, IS_NAVIGABLE, SEGMENTER);
	}

	private final LoadingCache<Locale, ImmutableMap<String, ContentType>> cache;

	private ContentTypeRoutingMap(final ContentTypes contentTypes, final Predicate<? super ContentType> predicate,
		final Segmenter segmenter) {
		final Iterable<ContentType> types = Iterables.filter(contentTypes.values(), predicate);
		final CacheLoader<Locale, ImmutableMap<String, ContentType>> creation = new CacheLoader<Locale, ImmutableMap<String, ContentType>>() {
			public ImmutableMap<String, ContentType> load(Locale from) {
				try {
					// No builder to avoid errors because of duplicates.
					final Map<String, ContentType> map = Maps.newHashMap();
					for (ContentType c : types) {
						try {
							final String segment = segmenter.get(from, c);
							if (segment != null) {
								map.put(segment, c);
							}
						} catch (RuntimeException e) {
							Loggers.core().error("Unable to compute CTRMap segment for content type [{}]: {}",
								new Object[] {c.getId(), e.getMessage()});
						}
					}
					return ImmutableMap.copyOf(map);
				} catch (RuntimeException e) {
					Loggers.core().error("Unable to compute CTRMap value map for locale [{}]: {}",
						new Object[] {from, e.getMessage()});
					return ImmutableMap.of();
				}
			}
		};
		this.cache = CacheBuilder.newBuilder().build(creation);
	}

	ContentType get(Locale locale, String segment) {
		if (locale == null || segment == null) {
			return null;
		}
		return cache.getUnchecked(locale).get(segment);
	}

	interface Segmenter {
		String get(Locale locale, ContentType contentType);
	}
}
