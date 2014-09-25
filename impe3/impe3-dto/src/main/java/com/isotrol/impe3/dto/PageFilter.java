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

import java.util.List;


/**
 * Generic DTO for page filters.
 * @author Andres Rodriguez
 * @author Emilio Escobar reyero
 * @param <F> Filter type.
 */
public class PageFilter<F> extends PageFilterDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -2596567943367749961L;
	
	/** Filter. */
	private F filter;

	/**
	 * Default constructor.
	 */
	public PageFilter() {
	}

	/**
	 * Returns the filter.
	 * @return The filter.
	 */
	public F getFilter() {
		return filter;
	}

	/**
	 * Sets the filter.
	 * @param filter The filter.
	 */
	public void setFilter(F filter) {
		this.filter = filter;
	}
	
	/**
	 * Creates a new PageFilter.
	 * @param <F> Filter type.
	 * @param filter The filter.
	 * @return The page filter.
	 */
	public static <F>PageFilter<F> of(F filter) {
		PageFilter<F> p = new PageFilter<F>();
		p.setFilter(filter);
		return p;
	}
	
	/**
	 * Creates a new PageFilter.
	 * @param <F> Filter type.
	 * @param filter The filter.
	 * @param pagination The pagination.
	 * @return The page filter.
	 */
	public static <F>PageFilter<F> of(F filter, PaginationDTO pagination) {
		PageFilter<F> p = new PageFilter<F>();
		p.setFilter(filter);
		p.setPagination(pagination);
		return p;
	}
	
	/**
	 * Creates a new PageFilter.
	 * @param <F> Filter type.
	 * @param filter The filter.
	 * @param pagination The pagination.
	 * @param orderings The order fields.
	 * @return The page filter.
	 */
	public static <F>PageFilter<F> of(F filter, PaginationDTO pagination, List<OrderDTO> orderings) {
		PageFilter<F> p = new PageFilter<F>();
		p.setFilter(filter);
		p.setPagination(pagination);
		p.setOrderings(orderings);
		return p;
	}

}
