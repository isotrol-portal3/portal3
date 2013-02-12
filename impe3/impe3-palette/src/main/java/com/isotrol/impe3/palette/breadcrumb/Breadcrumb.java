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

package com.isotrol.impe3.palette.breadcrumb;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


/**
 * Breadcrumb.
 * @author Andres Rodriguez
 */
public final class Breadcrumb extends ForwardingList<BreadcrumbItem> {
	/**
	 * Create a new breadcrumb.
	 * @param items Breadcrumb items.
	 * @param removeLastURI Whether the last URI must be removed.
	 * @return The created breadcrumb.
	 */
	public static Breadcrumb of(Iterable<BreadcrumbItem> items, boolean removeLastURI) {
		return new Breadcrumb(items, removeLastURI);
	}

	/** Items. */
	private final ImmutableList<BreadcrumbItem> items;

	/**
	 * Constructor.
	 * @param items Breadcrumb items.
	 * @param removeLastURI Whether the last URI must be removed.
	 */
	private Breadcrumb(Iterable<BreadcrumbItem> items, boolean removeLastURI) {
		checkNotNull(items);
		if (removeLastURI) {
			List<BreadcrumbItem> list = Lists.newArrayList(items);
			final int last = list.size() - 1;
			list.set(last, list.get(last).removeURI());
			this.items = ImmutableList.copyOf(list);
		} else {
			this.items = ImmutableList.copyOf(items);
		}
	}

	@Override
	protected List<BreadcrumbItem> delegate() {
		return items;
	}

	@Override
	public String toString() {
		return Joiner.on(" / ").join(items);
	}
}
