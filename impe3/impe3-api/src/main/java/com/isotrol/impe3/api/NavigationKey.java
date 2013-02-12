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

package com.isotrol.impe3.api;


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


/**
 * Value representing a navigation key.
 * @author Andres Rodriguez
 */
public final class NavigationKey implements Link {
	private final Category category;
	private final String tag;
	private final ContentType contentType;

	private NavigationKey(final Category category, final String tag, final ContentType contentType) {
		checkArgument(category != null || tag != null || contentType != null);
		checkArgument(category == null || tag == null);
		this.category = category;
		this.tag = tag;
		this.contentType = contentType;
	}

	/**
	 * Returns true for category-based navigation.
	 */
	public boolean isCategory() {
		return category != null;
	}

	/**
	 * Returns true for tag-based navigation.
	 */
	public boolean isTag() {
		return tag != null;
	}

	/**
	 * Returns true for content type-based navigation.
	 */
	public boolean isContentType() {
		return contentType != null;
	}

	/**
	 * Returns the category.
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Returns the tag.
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Returns the content type.
	 */
	public ContentType getContentType() {
		return contentType;
	}

	public NavigationKey withContentType(ContentType contentType) {
		Preconditions.checkNotNull(contentType);
		return new NavigationKey(category, tag, contentType);
	}

	public NavigationKey withoutContentType() {
		if (!isContentType()) {
			return this;
		}
		if (category == null && tag == null) {
			return null;
		}
		return new NavigationKey(category, tag, null);
	}

	/**
	 * Returns a new category-based navigation key.
	 * @param category Category.
	 * @param contentType Content type.
	 * @return The requested key.
	 */
	public static NavigationKey category(final Category category, final ContentType contentType) {
		return new NavigationKey(checkNotNull(category), null, contentType);
	}

	/**
	 * Returns a new category-based navigation key.
	 * @param category Category.
	 * @return The requested key.
	 */
	public static NavigationKey category(final Category category) {
		return category(category, null);
	}

	/**
	 * Returns a new tag-based navigation key.
	 * @param tag Tag.
	 * @param contentType Content type.
	 * @return The requested key.
	 */
	public static NavigationKey tag(final String tag, final ContentType contentType) {
		return new NavigationKey(null, checkNotNull(tag), contentType);
	}

	/**
	 * Returns a new tag-based navigation key.
	 * @param tag Tag.
	 * @param contentType Content type.
	 * @return The requested key.
	 */
	public static NavigationKey tag(final String tag) {
		return tag(tag, null);
	}

	/**
	 * Returns a new content type-based navigation key.
	 * @param contentType Content type.
	 * @return The requested key.
	 */
	public static NavigationKey contentType(final ContentType contentType) {
		return new NavigationKey(null, null, contentType);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(category, tag, contentType);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NavigationKey) {
			final NavigationKey key = (NavigationKey) obj;
			return equal(category, key.category) && equal(tag, key.tag) && equal(contentType, key.contentType);
		}
		return false;
	}
}
