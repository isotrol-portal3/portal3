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

import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentListing;


/**
 * Value for content listings page.
 * @author Andres Rodriguez
 */
public final class ContentListingPage extends DefaultListingPage<Content> implements ContentListing {
	private static final ImmutableList<Content> EMPTY_LIST = ImmutableList.of();
	public static final ContentListingPage EMPTY = new ContentListingPage(0, new Pagination(10, 0), EMPTY_LIST);
	
	public static ContentListingPage empty(Pagination pag) {
		return pag == null ? EMPTY : new ContentListingPage(0, pag, EMPTY_LIST);
	}
	
	/**
	 * Constructor.
	 * @param total Total results (if known).
	 * @param pagination Pagination.
	 * @param elements Page elements.
	 */
	public ContentListingPage(final Integer total, final Pagination pagination, final List<? extends Content> elements) {
		super(total, pagination, elements);
	}
}
