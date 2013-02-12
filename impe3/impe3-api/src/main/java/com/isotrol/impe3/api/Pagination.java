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

package com.isotrol.impe3.api;


import static com.google.common.base.Objects.equal;

import com.google.common.base.Preconditions;


/**
 * Value representing a pagination position.
 * @author Andres Rodriguez
 */
public final class Pagination implements Link {
	public static final String PAGE = "page";
	public static final int SIZE = 10;

	private static String param(String provided, String defaultValue) {
		if (provided == null || provided.length() == 0) {
			return defaultValue;
		}
		return provided;
	}

	private static Integer npages(Integer records, Integer size) {
		if (records == null || size == null) {
			return null;
		}
		int n = records / size;
		if (records % size > 0) {
			n++;
		}
		return n;
	}

	/** Page parameter name. */
	private final String parameter;
	/** Page number (1-based). */
	private final int page;
	/** Page size. */
	private final Integer size;
	/** Total number of records. */
	private final Integer records;
	/** Total number of pages. */
	private final Integer pages;

	/**
	 * Returns a new pagination.
	 * @param parameter Page parameter name.
	 * @param page Page number (1-based).
	 * @param size Page size.
	 * @return The requested content type.
	 */
	public Pagination(final String parameter, final int page, final Integer size, final Integer records) {
		this.parameter = param(parameter, PAGE);
		this.page = (page < 1) ? 1 : page;
		this.size = (size == null || size.intValue() < 1) ? null : size;
		if (records != null && records >= 0) {
			this.records = records;
		} else {
			this.records = null;
		}
		this.pages = npages(this.records, this.size);
	}

	/**
	 * Returns a new pagination.
	 * @param size Page size.
	 * @return The requested content type.
	 */
	public Pagination(final Integer size, final Integer records) {
		this(PAGE, 1, size, records);
	}

	/**
	 * Returns a new default pagination.
	 */
	public Pagination() {
		this(null, null);
	}

	public String getParameter() {
		return parameter;
	}

	public int getPage() {
		return page;
	}

	public Integer getSize() {
		return size;
	}

	public Integer getRecords() {
		return records;
	}

	public Integer getPages() {
		return pages;
	}

	/**
	 * Returns the first record index (0-based).
	 * @return The first record index (0-based).
	 */
	public int getFirstRecord() {
		return size == null ? 0 : (page - 1) * size;
	}

	/**
	 * Sets the first record index (0-based).
	 * @param first The first record index (0-based).
	 * @return The new pagination.
	 * @throws IllegalStateException if the size is not set.
	 */
	public Pagination setFirstRecord(int first) {
		Preconditions.checkState(size != null);
		if (first < 0) {
			first = 0;
		}
		return setPage(first / size);
	}

	public Pagination setPage(int page) {
		return this.page == page ? this : new Pagination(parameter, page, size, records);
	}

	public Pagination setSize(int size) {
		return equal(this.size, Integer.valueOf(size)) ? this : new Pagination(parameter, page, size, records);
	}

	public Pagination setRecords(Integer records) {
		return equal(this.records, records) ? this : new Pagination(parameter, page, size, records);
	}
}
