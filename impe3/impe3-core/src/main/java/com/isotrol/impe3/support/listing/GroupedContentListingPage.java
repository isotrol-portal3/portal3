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
import com.isotrol.impe3.api.content.GroupItem;


/**
 * Value for grouped content listings page.
 * @author Andres Rodriguez
 */
public final class GroupedContentListingPage extends DefaultListingPage<GroupItem<Content>> {
	public static final List<GroupItem<Content>> EMPTY_LIST = ImmutableList.of();

	/**
	 * Constructor.
	 * @param total Total results (if known).
	 * @param pagination Pagination.
	 * @param elements Page elements.
	 */
	public GroupedContentListingPage(final Integer total, final Pagination pagination,
		final List<GroupItem<Content>> elements) {
		super(total, pagination, elements);
	}
}
