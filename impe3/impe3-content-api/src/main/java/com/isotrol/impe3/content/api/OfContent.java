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


/**
 * Generic content composition DTO.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class OfContent<T> implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -4931981650879553797L;
	/** Content. */
	private ContentDTO content;
	/** Item. */
	private T item;

	/**
	 * Constructor.
	 * @param content Content.
	 * @param item Related item.
	 */
	public OfContent(ContentDTO content, T item) {
		this.item = item;
		this.content = content;
	}

	/** Constructor. */
	public OfContent() {
	}

	/**
	 * Returns the content.
	 * @return The content.
	 */
	public ContentDTO getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 * @param content The content.
	 */
	public void setContent(ContentDTO content) {
		this.content = content;
	}

	/**
	 * Returns the item.
	 * @return The item.
	 */
	public T getItem() {
		return item;
	}

	/**
	 * Sets the item.
	 * @param item The item.
	 */
	public void setItem(T item) {
		this.item = item;
	}
}
