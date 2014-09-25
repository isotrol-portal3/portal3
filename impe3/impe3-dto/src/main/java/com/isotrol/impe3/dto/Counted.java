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

package com.isotrol.impe3.dto;

import java.io.Serializable;


/**
 * Generic counted DTO.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class Counted<T> implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -6556208620048093792L;
	/** Item. */
	private T item;
	/** Count value. */
	private long count;

	/**
	 * Constructor.
	 * @param item Counted item.
	 * @param count Count value.
	 */
	public Counted(T item, long count) {
		this.item = item;
		this.count = count;
	}

	/** Constructor. */
	public Counted() {
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

	/**
	 * Returns the count value.
	 * @return The count value.
	 */
	public long getCount() {
		return count;
	}

	/**
	 * Sets the count value.
	 * @param count The count value.
	 */
	public void setCount(long count) {
		this.count = count;
	}
}
