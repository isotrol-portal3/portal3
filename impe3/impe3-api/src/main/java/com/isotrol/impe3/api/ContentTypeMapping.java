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


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;


/**
 * Value that represents a content type mapping.
 * @author Andres Rodriguez
 */
public final class ContentTypeMapping {
	/** Content type. */
	private final ContentType contentType;
	/** Mapping. */
	private final String mapping;

	/**
	 * Constructor.
	 * @param contentType Content type.
	 * @param mapping Mapping.
	 */
	public ContentTypeMapping(ContentType contentType, String mapping) {
		this.contentType = checkNotNull(contentType, "The content type must be provided");
		this.mapping = mapping;
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Returns the mapping.
	 * @return The mapping.
	 */
	public String getMapping() {
		return mapping;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return Objects.hashCode(contentType, mapping);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof ContentTypeMapping) {
			final ContentTypeMapping m = (ContentTypeMapping) obj;
			return Objects.equal(contentType, m.contentType) && Objects.equal(mapping, m.mapping);
		}
		return false;
	}

}
