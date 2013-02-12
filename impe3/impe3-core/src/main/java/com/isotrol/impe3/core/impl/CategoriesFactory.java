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

package com.isotrol.impe3.core.impl;


import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.PathSegments;


/**
 * Factory class for different categories collection implementations.
 * @author Andres Rodriguez
 */
public final class CategoriesFactory {
	/** Not instantiable. */
	private CategoriesFactory() {
		throw new AssertionError();
	}

	private static final Empty EMPTY = new Empty();

	/**
	 * Returns an empty collection.
	 */
	public static Categories of() {
		return EMPTY;
	}

	/**
	 * Returns a collection of categories containing the provided hierarchy.
	 * @param categories Hierarchy of categories.
	 * @return The requested collection.
	 * @throws NullPointerException if the argument is null.
	 */
	public static Categories of(ImmutableHierarchy<UUID, Category> categories) {
		Preconditions.checkNotNull(categories);
		return new Immutable(categories);
	}

	/**
	 * Abstract collection.
	 * @author Andres Rodriguez
	 */
	private abstract static class Abstract extends AbstractIdentifiableHierarchy<Category> implements Categories {
		private Abstract() {
		}

		/**
		 * @see com.isotrol.impe3.api.Categories#getByPath(java.lang.String, boolean, boolean)
		 */
		public Category getByPath(String path, boolean encoded, boolean includeRoot) {
			return getByPath(null, path, encoded, includeRoot);
		}
	}

	/**
	 * Empty collection.
	 * @author Andres Rodriguez
	 */
	private static final class Empty extends Abstract {
		private Empty() {
		}

		@Override
		protected Hierarchy<UUID, Category> delegate() {
			return ImmutableHierarchy.of();
		}

		/**
		 * @see com.isotrol.impe3.api.Categories#getRoot()
		 */
		public Category getRoot() {
			return null;
		}

		/**
		 * @see com.isotrol.impe3.api.Categories#getByPath(java.util.Locale, java.lang.String, boolean, boolean)
		 */
		public Category getByPath(Locale locale, String path, boolean encoded, boolean includeRoot) {
			return null;
		}

		/**
		 * @see com.isotrol.impe3.api.Categories#getByPath(java.lang.String, boolean, boolean)
		 */
		public Category getByPath(String path, boolean encoded, boolean includeRoot) {
			return null;
		}

	}

	/**
	 * Immutable collection.
	 * @author Andres Rodriguez
	 */
	private static final class Immutable extends Abstract {
		private ImmutableHierarchy<UUID, Category> hierarchy;
		private final LoadingCache<Key, ImmutableMap<PathSegments, Category>> byPath;

		private Immutable(ImmutableHierarchy<UUID, Category> hierarchy) {
			this.hierarchy = hierarchy;
			final CacheLoader<Key, ImmutableMap<PathSegments, Category>> loader = new CacheLoader<Key, ImmutableMap<PathSegments, Category>>() {
				public ImmutableMap<PathSegments, Category> load(Key key) {
					if (isEmpty()) {
						return ImmutableMap.of();
					}
					final Map<PathSegments, Category> map = Maps.newHashMap();
					if (key.includeRoot()) {
						add(map, getRoot(), key.getLocale(), PathSegments.of());
					} else {
						add(map, getChildren(getRoot().getId()), key.getLocale(), PathSegments.of());
					}
					return ImmutableMap.copyOf(map);
				}
			};
			this.byPath = CacheBuilder.newBuilder().softValues().build(loader);
		}

		@Override
		protected Hierarchy<UUID, Category> delegate() {
			return hierarchy;
		}

		/**
		 * @see com.isotrol.impe3.api.Categories#getRoot()
		 */
		public Category getRoot() {
			return isEmpty() ? null : getFirstLevel().get(0);
		}

		private Map<PathSegments, Category> add(Map<PathSegments, Category> map, Category c, Locale locale,
			PathSegments base) {
			final String path;
			try {
				path = (locale == null) ? c.getDefaultName().getPath() : c.getName().get(locale).getPath();
			} catch (RuntimeException e) {
				return map;
			}
			if (path == null) {
				return map;
			}
			final PathSegments s = base.add(PathSegments.of(false, path));
			map.put(s, c);
			return add(map, hierarchy.getChildren(c.getId()), locale, s);
		}

		private Map<PathSegments, Category> add(Map<PathSegments, Category> map, Iterable<Category> children,
			Locale locale, PathSegments base) {
			for (Category child : children) {
				add(map, child, locale, base);
			}
			return map;
		}

		public Category getByPath(Locale locale, String path, boolean encoded, boolean includeRoot) {
			final Key key = new Key(locale, includeRoot);
			final PathSegments segments = PathSegments.of(path, encoded);
			return byPath.getUnchecked(key).get(segments);
		}

	}

	/**
	 * Key for segment maps.
	 * @author Andres Rodriguez
	 */
	private static final class Key {
		private final Locale locale;
		private final boolean root;
		private final int hash;

		Key(final Locale locale, final boolean root) {
			this.locale = locale;
			this.root = root;
			this.hash = Objects.hashCode(locale, root);
		}

		Locale getLocale() {
			return locale;
		}

		public boolean includeRoot() {
			return root;
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Key) {
				final Key k = (Key) obj;
				return (root == k.root && hash == k.hash && Objects.equal(locale, k.locale));
			}
			return false;
		}
	}

}
