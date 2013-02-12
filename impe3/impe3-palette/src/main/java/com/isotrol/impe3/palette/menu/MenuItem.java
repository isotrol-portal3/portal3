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

package com.isotrol.impe3.palette.menu;


import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.List;

import com.google.common.collect.Lists;


/**
 * Menu item.
 * @author Andres Rodriguez
 */
public final class MenuItem {
	/** Item name. */
	private final String name;
	/** Item URI. */
	private final String uri;
	/** Whether the item is selected. */
	private final boolean selected;
	/** Children items. */
	private List<MenuItem> children;
	/** Parent item (may be {@code null}. */
	private MenuItem parent;

	public MenuItem(final String name, final String uri, boolean selected) {
		this.name = checkNotNull(name);
		this.uri = (uri != null) ? uri : "";
		this.selected = selected;
	}

	public MenuItem(final String name, final String uri) {
		this(name, uri, false);
	}

	public MenuItem(final String name, final URI uri, boolean selected) {
		this(name, uri != null ? uri.toASCIIString() : "", selected);
	}

	public MenuItem(final String name, final URI uri) {
		this(name, uri, false);
	}

	public String getName() {
		return name;
	}

	public String getUri() {
		return uri;
	}

	public boolean isSelected() {
		return selected;
	}

	public List<MenuItem> getChildren() {
		if (children == null) {
			children = Lists.newLinkedList();
		}
		return children;
	}

	/**
	 * Returns the parent item (may be {@code null}.
	 * @return The parent item (may be {@code null}.
	 */
	public MenuItem getParent() {
		return parent;
	}

	/**
	 * Sets the parent item.
	 * @param parent The parent item.
	 */
	public void setParent(MenuItem parent) {
		this.parent = parent;
	}

}
