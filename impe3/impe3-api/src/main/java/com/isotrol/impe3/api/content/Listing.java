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

package com.isotrol.impe3.api.content;


import java.util.List;

import com.isotrol.impe3.api.Link;
import com.isotrol.impe3.api.Pagination;


/**
 * Listing page.
 * @author Andres Rodriguez
 * @param <E> Page element type.
 */
public interface Listing<E> extends List<E>, Link {
	/**
	 * Returns the total results (if known).
	 * @return The total results (if known).
	 */
	Integer getTotal();

	/**
	 * Returns the pagination.
	 * @return The pagination.
	 */
	Pagination getPagination();
}
