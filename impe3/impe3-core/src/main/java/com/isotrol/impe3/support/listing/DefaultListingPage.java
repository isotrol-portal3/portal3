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

package com.isotrol.impe3.support.listing;


import java.util.List;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.content.Listing;


/**
 * Default class for content listings page.
 * @author Andres Rodriguez
 * @param <E> Page element type.
 */
public class DefaultListingPage<E> extends ForwardingList<E> implements Listing<E> {
	/** Total results (if known). */
	private final Integer total;
	/** Pagination. */
	private final Pagination pagination;
	/** Page elements. */
	private final List<E> elements;

	/**
	 * Constructor.
	 * @param total Total results (if known).
	 * @param pagination Pagination.
	 * @param elements Page elements.
	 */
	public DefaultListingPage(final Integer total, final Pagination pagination, final List<? extends E> elements) {
		this.total = total;
		if (elements == null) {
			this.elements = ImmutableList.of();
		} else {
			this.elements = ImmutableList.copyOf(elements);

		}
		if (pagination != null) {
			if (total != null) {
				this.pagination = pagination.setRecords(total);
			} else {
				this.pagination = pagination;
			}
		} else {
			this.pagination = new Pagination(this.elements.size(), total);
		}
	}

	@Override
	protected List<E> delegate() {
		return elements;
	}

	/**
	 * Returns the total results (if known).
	 * @return The total results (if known).
	 */
	public Integer getTotal() {
		return total;
	}

	/**
	 * Returns the pagination.
	 * @return The pagination.
	 */
	public Pagination getPagination() {
		return pagination;
	}

}
