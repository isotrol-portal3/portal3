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

package com.isotrol.impe3.api.content;


import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.nr.api.NRBooleanQuery;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.Schema;


/**
 * Content specific queries.
 * @author Andres Rodriguez
 */
public final class ContentQueries extends NodeQueries {
	/** Not instantiable. */
	private ContentQueries() {
		throw new AssertionError();
	}

	/**
	 * Category query.
	 * @param category Category to filter.
	 * @return The requested query or {@code null} if the argument is {@code null}.
	 */
	public static NodeQuery category(Category category) {
		if (category == null) {
			return null;
		}
		return term(Schema.CATEGORY, category.getId());
	}

	/**
	 * Content type query.
	 * @param contentType Content type to filter.
	 * @return The requested query or {@code null} if the argument is {@code null}.
	 */
	public static NodeQuery contentType(ContentType contentType) {
		if (contentType == null) {
			return null;
		}
		return term(Schema.TYPE, contentType.getId());
	}

	/**
	 * Tag query.
	 * @param tag Tag to filter.
	 * @return The requested query or {@code null} if the argument is {@code null}.
	 */
	public static NodeQuery tag(String tag) {
		if (tag == null) {
			return null;
		}
		return term(Schema.TAG, tag);
	}

	/**
	 * Navigation key query.
	 * @param key Navigation key to filter.
	 * @return The requested query or {@code null} if the argument is {@code null}.
	 */
	public static NodeQuery navigationKey(NavigationKey key) {
		if (key == null) {
			return null;
		}
		return all(category(key.getCategory()), tag(key.getTag()), contentType(key.getContentType()));
	}

	/**
	 * Content key query.
	 * @param key Content key to filter.
	 * @return The requested query or {@code null} if the argument is {@code null}.
	 */
	public static NodeQuery contentKey(ContentKey key) {
		if (key == null) {
			return null;
		}
		final NRBooleanQuery q = NodeQueries.bool();
		q.must(contentType(key.getContentType()));
		q.must(NodeQueries.term(Schema.ID, key.getContentId()));
		return q;
	}
}
