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
import java.util.ArrayList;
import java.util.List;


/**
 * Base DTO for page filters.
 * @author Andres Rodriguez
 */
public class PageFilterDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 8388178928786087992L;

	/** Pagination. */
	private PaginationDTO pagination;
	/** Orderings. */
	private List<OrderDTO> orderings;

	/**
	 * Default constructor.
	 */
	public PageFilterDTO() {
	}

	/**
	 * Returns the pagination.
	 * @return The pagination (never {@code null}).
	 */
	public PaginationDTO getPagination() {
		if (pagination == null) {
			pagination = new PaginationDTO();
		}
		return pagination;
	}

	/**
	 * Sets the pagination.
	 * @param pagination The pagination.
	 */
	public void setPagination(PaginationDTO pagination) {
		this.pagination = pagination;
	}

	/**
	 * Ensure the orderings list is not {@code null}.
	 * @return The current not-{@code null} value.
	 */
	private List<OrderDTO> orderings() {
		if (orderings == null) {
			orderings = new ArrayList<OrderDTO>();
		}
		return orderings;
	}

	/**
	 * Returns the orderings.
	 * @return The orderings (never {@code null}).
	 */
	public List<OrderDTO> getOrderings() {
		return orderings();
	}

	/**
	 * Sets the orderings.
	 * @param orderings The orderings.
	 */
	public void setOrderings(List<OrderDTO> orderings) {
		this.orderings = orderings;
	}

	/**
	 * Adds an ordering.
	 * @param order Ordering to add. If {@code null} it will be ignored.
	 */
	public void addOrdering(OrderDTO order) {
		if (!OrderDTO.isNull(order)) {
			orderings().add(order);
		}
	}
}
