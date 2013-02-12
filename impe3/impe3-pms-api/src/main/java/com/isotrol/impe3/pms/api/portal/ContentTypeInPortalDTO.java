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

package com.isotrol.impe3.pms.api.portal;

import java.io.Serializable;

import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * DTO that represents a content type in a portal template.
 * @author Andres Rodriguez
 */
public class ContentTypeInPortalDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -1346486479401843955L;
	/** Whether the content type is included. */
	private boolean included;
	/** Content type. */
	private ContentTypeSelDTO contentType;

	/** Default constructor. */
	public ContentTypeInPortalDTO() {
	}

	/**
	 * Returns whether the content type is included.
	 * @return True if the content type is included.
	 */
	public boolean isIncluded() {
		return included;
	}

	/**
	 * Sets whether the content type is included.
	 * @param name True if the content type is included.
	 */
	public void setIncluded(boolean included) {
		this.included = included;
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public ContentTypeSelDTO getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * @param contentType The content type.
	 */
	public void setContentType(ContentTypeSelDTO contentType) {
		this.contentType = contentType;
	}
}
