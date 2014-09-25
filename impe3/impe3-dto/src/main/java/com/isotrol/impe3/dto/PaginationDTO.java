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


import static java.lang.Math.max;

import java.io.Serializable;


/**
 * DTO for pagination position.
 * @author Andres Rodriguez
 */
public class PaginationDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -5397297876987392274L;

	/** Default page size. */
	public static final int SIZE = 10;

	/** First record. */
	private int first;
	/** Page size. */
	private int size;

	/**
	 * Constructor. If any of the arguments is < 0 it will be set to zero.
	 * @param first First record (0-based).
	 * @param size Page size.
	 */
	public PaginationDTO(int first, int size) {
		this.first = max(0, first);
		this.size = max(0, size);
	}

	/**
	 * Default constructor.
	 */
	public PaginationDTO() {
		this(0, SIZE);
	}

	/**
	 * Returns the first record index (0-based).
	 * @return The first record index (0-based).
	 */
	public int getFirst() {
		return first;
	}

	/**
	 * Sets the first record index (0-based).
	 * @param first The first record index (0-based). If less than zero it will be set to zero.
	 */
	public void setFirst(int first) {
		this.first = max(0, first);
	}

	/**
	 * Returns the page size.
	 * @return The page size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the page size.
	 * @param size The page size. If less than zero it will be set to zero.
	 */
	public void setSize(int size) {
		this.size = max(0, size);
	}
}
