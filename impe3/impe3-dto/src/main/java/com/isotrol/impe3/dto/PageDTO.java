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
import java.util.ArrayList;
import java.util.List;


/**
 * DTO for a page of results.
 * @author Andres Rodriguez
 * @param <E> Page element type.
 */
public class PageDTO<E> implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -3237574770757915409L;

	private static Integer clean(Integer i) {
		if (i != null && i.intValue() < 0) {
			return null;
		}
		return i;
	}

	/** First record. */
	private int first = 0;
	/** Requested size (if known). */
	private Integer size = null;
	/** Total number of records (if known). */
	private Integer total = null;
	/** Elements. */
	private List<E> elements = null;

	/**
	 * Default constructor.
	 */
	public PageDTO() {
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
	 * Returns the requested page size (if known).
	 * @return The requested page size.
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * Sets the requested page size.
	 * @param size The page size. If less than zero it will be set to {@code null}.
	 */
	public void setSize(Integer size) {
		this.size = clean(size);
	}

	/**
	 * Returns the total number of records (if known).
	 * @return The total number of records.
	 */
	public Integer getTotal() {
		return total;
	}

	/**
	 * Sets the total number of records.
	 * @param total The total number of records. If less than zero it will be set to {@code null}.
	 */
	public void setTotal(Integer total) {
		this.total = clean(total);
	}

	/**
	 * Ensure the elements list is not {@code null}.
	 * @return The current not-{@code null} value.
	 */
	private List<E> elements() {
		if (elements == null) {
			elements = new ArrayList<E>();
		}
		return elements;
	}

	/**
	 * Returns the elements.
	 * @return The elements (never {@code null}).
	 */
	public List<E> getElements() {
		return elements();
	}

	/**
	 * Sets the elements.
	 * @param elements The elements.
	 */
	public void setElements(List<E> elements) {
		this.elements = elements;
	}

	/**
	 * Estimates page size.
	 * @return The estimated value.
	 */
	private int size() {
		if (size != null) {
			return size;
		}
		return elements().size();
	}

	/**
	 * Returns the best approximation to the first pagination.
	 * @return The first pagination.
	 */
	public PaginationDTO getFirstPage() {
		return new PaginationDTO(0, size());
	}

	/**
	 * Returns the best approximation to the current pagination.
	 * @return The current pagination.
	 */
	public PaginationDTO getCurrentPage() {
		return new PaginationDTO(first, size());
	}
}
