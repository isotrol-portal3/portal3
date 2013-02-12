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

package com.isotrol.impe3.pms.api.esvc.nr;

import java.io.Serializable;

import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;

/**
 * Information about number of content types found (dto)
 * @author Emilio Escobar Reyero
 */
public class ContentTypeCountDTO implements Serializable {
	/** Serial UID */
	private static final long serialVersionUID = -677642256826134628L;
	/** Content Type information. If unkown type, only set ID */
	private ContentTypeSelDTO contentType;
	/** Content Type nodes count. Value >= 0*/
	private int count;
	
	public ContentTypeSelDTO getContentType() {
		return contentType;
	}
	public void setContentType(ContentTypeSelDTO contentType) {
		this.contentType = contentType;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
