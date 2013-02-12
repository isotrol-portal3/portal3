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

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.content.Group;
import com.isotrol.impe3.api.content.GroupItem;


/**
 * A group of elements.
 * @author Andres Rodriguez
 * @param <E> Page element type.
 */
public class ListingGroup<E> implements Group<E> {
	/** Group title. */
	private final String title;
	/** Group type. */
	private final String type;
	/** Group elements. */
	private final List<GroupItem<E>> items;

	/**
	 * Constructor.
	 * @param title Group title.
	 * @param type Group type.
	 * @param items Group items.
	 */
	public ListingGroup(final String title, final String type, final List<GroupItem<E>> items) {
		this.title = title;
		this.type = type;
		if (items == null) {
			this.items = Lists.newArrayList();
		} else {
			this.items = items;
		}
	}

	/**
	 * Constructor.
	 * @param title Group title.
	 * @param items Group items.
	 */
	public ListingGroup(final String title, final List<GroupItem<E>> items) {
		this(title, null, items);
	}

	/**
	 * Returns the group title.
	 * @return The group title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the group type.
	 * @return The group type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Return the group items.
	 * @return The group items.
	 */
	public List<GroupItem<E>> getItems() {
		return items;
	}
}
