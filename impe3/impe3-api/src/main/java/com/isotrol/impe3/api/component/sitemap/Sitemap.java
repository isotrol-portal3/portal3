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

package com.isotrol.impe3.api.component.sitemap;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;


/**
 * Object representing a sitemap.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public abstract class Sitemap {
	private static <T> ImmutableList<T> create(Iterable<T> entries) {
		if (entries == null) {
			return ImmutableList.of();
		} else {
			return ImmutableList.copyOf(Iterables.filter(entries, Predicates.notNull()));
		}
	}

	public static IndexSitemap index(Iterable<IndexEntry> entries) {
		return new IndexSitemap(create(entries));
	}

	public static URLSetSitemap set(Iterable<URLEntry> entries) {
		return new URLSetSitemap(create(entries));
	}

	private Sitemap() {
	}

	/**
	 * Returns wether the sitemap is an index sitemap.
	 */
	public abstract boolean isIndex();

	/**
	 * Return the sitemap as an index sitemap.
	 * @throws ClassCastException if the sitemap is not an index sitemap.
	 */
	public abstract IndexSitemap asIndex();

	/**
	 * Return the sitemap as an URL set sitemap.
	 * @throws ClassCastException if the sitemap is not an URL set sitemap.
	 */
	public abstract URLSetSitemap asSet();

	/**
	 * Appends a sitemap to the current one.
	 * @param other The sitemap to append. If {@code null} or of a different kind of the current no operation is
	 * performed.
	 * @return The resulting sitemap.
	 */
	public abstract Sitemap append(Sitemap other);

	public static final class IndexSitemap extends Sitemap {
		private final ImmutableList<IndexEntry> entries;

		private IndexSitemap(ImmutableList<IndexEntry> entries) {
			this.entries = checkNotNull(entries);
		}

		@Override
		public boolean isIndex() {
			return true;
		}

		@Override
		public IndexSitemap asIndex() {
			return this;
		}

		@Override
		public URLSetSitemap asSet() {
			throw new ClassCastException("Not an URL set sitemap.");
		}

		@Override
		public IndexSitemap append(Sitemap other) {
			if (other == null) {
				return this;
			}
			if (!other.isIndex()) {
				return this;
			}
			List<IndexEntry> list = other.asIndex().entries;
			if (list.isEmpty()) {
				return this;
			}
			return index(Iterables.concat(entries, list));
		}

		/**
		 * Return the entries for a index sitemap.
		 */
		public Iterable<IndexEntry> getIndexEntries() {
			return entries;
		}

	}

	public static final class URLSetSitemap extends Sitemap {
		private final ImmutableList<URLEntry> entries;

		private URLSetSitemap(ImmutableList<URLEntry> entries) {
			this.entries = checkNotNull(entries);
		}

		@Override
		public boolean isIndex() {
			return false;
		}

		@Override
		public IndexSitemap asIndex() {
			throw new ClassCastException("Not an index sitemap.");
		}

		@Override
		public URLSetSitemap asSet() {
			return this;
		}

		@Override
		public URLSetSitemap append(Sitemap other) {
			if (other == null) {
				return this;
			}
			if (other.isIndex()) {
				return this;
			}
			List<URLEntry> list = other.asSet().entries;
			if (list.isEmpty()) {
				return this;
			}
			return set(Iterables.concat(entries, list));
		}

		/**
		 * Return the URL entries for a URL set sitemap.
		 */
		public Iterable<URLEntry> getURLEntries() {
			return entries;
		}

	}
}
