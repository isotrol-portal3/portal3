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

package com.isotrol.impe3.palette.content;


import net.sf.derquinse.lucis.Page;

import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.api.support.PaginationSupport;
import com.isotrol.impe3.api.support.WithPageSize;
import com.isotrol.impe3.support.listing.DefaultListingPage;


/**
 * Abstract listing component.
 * @param <T> Listing type.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public abstract class AbstractListingComponent<T> implements Component {
	/** Pagination. */
	private Pagination pagination;
	/** Listing. */
	private Listing<T> listing = null;

	/**
	 * Constructor.
	 */
	public AbstractListingComponent() {
	}

	protected abstract WithPageSize getConfiguration();

	@Inject
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	protected Pagination loadPagination(int defaultSize) {
		this.pagination = PaginationSupport.load(pagination, getConfiguration(), defaultSize);
		return pagination;
	}

	protected void setListing(Listing<T> listing) {
		this.listing = listing;
	}

	protected void setPage(Page<T> page) {
		if (page == null) {
			listing = null;
		} else {
			listing = new DefaultListingPage<T>(page.getTotalHits(), pagination, page.getItems());
		}
	}

	@Extract
	public Pagination getPagination() {
		return listing != null ? listing.getPagination() : pagination;
	}

	@Extract
	public Listing<T> getListing() {
		return listing;
	}
}
