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


import static com.isotrol.impe3.api.RoutableNamedIdentifiable.IS_ROUTABLE;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.core.Loggers;


/**
 * Category routing map.
 * @author Andres Rodriguez
 */
final class CategoryRoutingMap {
	private final ConcurrentMap<CRKey, ImmutableMap<String, Category>> map;

	CategoryRoutingMap(final Categories categories) {
		final Function<CRKey, ImmutableMap<String, Category>> computer = new Function<CRKey, ImmutableMap<String, Category>>() {
			public ImmutableMap<String, Category> apply(CRKey from) {
				try {
					final List<Category> children = categories.getChildren(from.getId());
					if (children == null || children.isEmpty()) {
						return ImmutableMap.of();
					}
					// No builder to avoid errors because of duplicates.
					final Map<String, Category> map = Maps.newHashMap();
					final Locale locale = from.getLocale();
					for (Category c : Iterables.filter(children, IS_ROUTABLE)) {
						try {
							final String segment = c.getName().get(locale).getPath();
							if (segment != null) {
								map.put(segment, c);
							}
						} catch (RuntimeException e) {
							Loggers.core().error("Unable to compute CRMap segment for category [{}]: {}",
								new Object[] {c.getId(), e.getMessage()});
						}
					}
					return ImmutableMap.copyOf(map);
				} catch (RuntimeException e) {
					Loggers.core().error("Unable to compute CRMap value map for category [{}]: {}",
						new Object[] {from.getId(), e.getMessage()});
					return ImmutableMap.of();
				}
			}
		};
		this.map = new MapMaker().makeComputingMap(computer);
	}

	Category get(Category current, Locale locale, String segment) {
		if (current == null || locale == null || segment == null) {
			return null;
		}
		return map.get(new CRKey(locale, current)).get(segment);
	}
}
