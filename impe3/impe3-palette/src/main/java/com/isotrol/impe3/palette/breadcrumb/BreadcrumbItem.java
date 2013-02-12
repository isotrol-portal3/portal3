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

import java.net.URI;

import com.google.common.base.Function;


/**
 * Breadcrumb item.
 * @author Andres Rodriguez
 */
public final class BreadcrumbItem {
	public static final Function<BreadcrumbItem, String> NOMBRE = new Function<BreadcrumbItem, String>() {
		public String apply(BreadcrumbItem from) {
			return from.getName();
		}
	};

	public static final Function<BreadcrumbItem, String> URI = new Function<BreadcrumbItem, String>() {
		public String apply(BreadcrumbItem from) {
			return from.getUri();
		}
	};

	/** Item name. */
	private final String name;
	/** Item destination URI. */
	private final String uri;

	/**
	 * Constructor.
	 * @param name Item name.
	 * @param uri Item destination URI.
	 */
	public BreadcrumbItem(final String name, final String uri) {
		this.name = checkNotNull(name);
		this.uri = (uri != null) ? uri : "";
	}

	/**
	 * Constructor.
	 * @param name Item name.
	 * @param uri Item destination URI.
	 */
	public BreadcrumbItem(final String name, final URI uri) {
		this(name, uri != null ? uri.toASCIIString() : "");
	}

	/**
	 * Returns the item name.
	 * @return The item name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the item destination URI.
	 * @return The item destination URI.
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Returns an item with the same name and no URI.
	 * @return An item with the same name and no URI.
	 */
	public BreadcrumbItem removeURI() {
		if (uri.length() == 0) {
			return this;
		}
		return new BreadcrumbItem(name, (String) null);
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", name, uri);
	}
}
