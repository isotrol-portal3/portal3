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

package com.isotrol.impe3.content.api;


import com.isotrol.impe3.dto.AbstractStringId;


/**
 * DTO for a content reference.
 * @author Andres Rodriguez Chamorro
 */
public final class ContentRefDTO extends AbstractStringId {
	/** Serial UID. */
	private static final long serialVersionUID = -8701197300658502819L;
	/** Content type. */
	private ContentTypeRefDTO contentType;

	/** Default constructor. */
	public ContentRefDTO() {
	}

	/**
	 * Returns the node key.
	 * @return The node key.
	 */
	public String getNodeKey() {
		final String id = getId();
		final String tid = (contentType != null) ? contentType.getId() : null;
		if (tid != null && id != null) {
			return tid + ':' + id;
		} else if (id != null) {
			return id;
		}
		return tid;
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public ContentTypeRefDTO getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * @param type The content type.
	 */
	public void setContentType(ContentTypeRefDTO type) {
		this.contentType = type;
	}
}
