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


/**
 * Utility class to create sitemap renderers.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public final class SitemapRenderers {
	/** Not instantiable. */
	private SitemapRenderers() {
		throw new AssertionError();
	}

	/**
	 * Creates a renderer for a sitemap.
	 * @param sitemap Sitemap to use.
	 * @return The requested renderer.
	 */
	public static SitemapRenderer of(Sitemap sitemap) {
		return new BasicSitemapRenderer(checkNotNull(sitemap));
	}

	/**
	 * Creates a renderer for an index sitemap.
	 * @param entries Index entries.
	 * @return The requested renderer.
	 */
	public static SitemapRenderer index(Iterable<IndexEntry> entries) {
		return of(Sitemap.index(entries));
	}

	/**
	 * Creates a renderer for an URL set sitemap.
	 * @param entries URL set entries.
	 * @return The requested renderer.
	 */
	public static SitemapRenderer set(Iterable<URLEntry> entries) {
		return of(Sitemap.set(entries));
	}

	private static final class BasicSitemapRenderer implements SitemapRenderer {
		private final Sitemap sitemap;

		BasicSitemapRenderer(Sitemap sitemap) {
			this.sitemap = sitemap;
		}

		public Sitemap getSitemap() {
			return sitemap;
		}
	}
}
