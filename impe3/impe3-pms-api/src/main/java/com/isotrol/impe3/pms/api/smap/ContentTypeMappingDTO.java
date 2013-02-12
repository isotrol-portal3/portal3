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

package com.isotrol.impe3.pms.api.smap;

import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * DTO for a content type mapping.
 * @author Andres Rodriguez
 */
public class ContentTypeMappingDTO extends AbstractMappingDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 5501471634497484947L;
	/** Content type. */
	private ContentTypeSelDTO contentType;

	/** Default constructor. */
	public ContentTypeMappingDTO() {
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
