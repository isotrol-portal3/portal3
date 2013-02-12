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
 * Node information dto
 * @author Emilio Escobar Reyero
 */
public class NodeDTO implements Serializable {
	/** Serial UID */
	private static final long serialVersionUID = 8485598544133355128L;
	/** Node key */
	private String id;
	/** Node title */
	private String title;
	/** Node description */
	private String description;
	/** Node date */
	private String date;
	/** Node content type */
	private ContentTypeSelDTO contentTypeSelDTO;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public ContentTypeSelDTO getContentTypeSelDTO() {
		return contentTypeSelDTO;
	}
	public void setContentTypeSelDTO(ContentTypeSelDTO contentTypeSelDTO) {
		this.contentTypeSelDTO = contentTypeSelDTO;
	}
	
}