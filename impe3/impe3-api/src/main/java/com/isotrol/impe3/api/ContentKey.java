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
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.isotrol.impe3.nr.api.NodeKey;


/**
 * Value representing a content key.
 * @author Andres Rodriguez
 */
public final class ContentKey implements Link {
	private final ContentType contentType;
	private final String contentId;

	private ContentKey(final ContentType contentType, final String contentId) {
		this.contentType = contentType;
		this.contentId = contentId;
	}

	/**
	 * Creates a content key.
	 * @param contentType Content type.
	 * @param contentId Content id.
	 * @return The requested content key.
	 */
	public static ContentKey of(final ContentType contentType, final String contentId) {
		return new ContentKey(checkNotNull(contentType), checkNotNull(contentId));
	}

	public String getContentId() {
		return contentId;
	}

	public ContentType getContentType() {
		return contentType;
	}

	/** Returns the node key. */
	public NodeKey getNodeKey() {
		return NodeKey.of(contentType.getId(), contentId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(contentType, contentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ContentKey) {
			final ContentKey key = (ContentKey) obj;
			return equal(contentType, key.contentType) && equal(contentId, key.contentId);
		}
		return false;
	}
}
