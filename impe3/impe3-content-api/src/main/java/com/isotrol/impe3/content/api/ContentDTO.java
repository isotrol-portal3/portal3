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


import java.io.Serializable;
import java.util.Date;


/**
 * DTO for a content resource.
 * @author Andres Rodriguez Chamorro
 */
public class ContentDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -4383906793327546672L;
	/** Content reference. */
	private ContentRefDTO ref;
	/** Content title. */
	private String title;
	/** Content date */
	private Date date;

	/** Default constructor. */
	public ContentDTO() {
	}

	/**
	 * Returns the content id.
	 * @return The content id.
	 */
	public String getId() {
		if (ref != null) {
			return ref.getId();
		}
		return null;
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public ContentTypeRefDTO getContentType() {
		if (ref != null) {
			return ref.getContentType();
		}
		return null;
	}

	/**
	 * Returns the node key.
	 * @return The node key.
	 */
	public String getNodeKey() {
		if (ref != null) {
			return ref.getNodeKey();
		}
		return null;
	}

	/**
	 * Returns the content reference.
	 * @return The content reference.
	 */
	public ContentRefDTO getRef() {
		return ref;
	}

	/**
	 * Sets the content reference.
	 * @param ref The content reference.
	 */
	public void setRef(ContentRefDTO ref) {
		this.ref = ref;
	}

	/**
	 * Returns the content title.
	 * @return The content title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the content title.
	 * @param name The content title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the content date.
	 * @return The content date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the content date.
	 * @param name The content date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}
