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

package com.isotrol.impe3.api;

import java.util.Locale;

/**
 * A hierarchy of categories.
 * @author Andres Rodriguez
 */
public interface Categories extends IdentifiableHierarchy<Category> {
	/**
	 * Returns the root category.
	 * @return The root category or {@code null} if the hierarchy is empty.
	 */
	Category getRoot();
	
	/**
	 * Returns a category by path
	 * @param path Path. Segments are separated by '/'
	 * @param encoded If the path is encoded.
	 * @param includeRoot If the the root category path segment is to be considered.
	 * @return The category with the specified path or {@code null} if not found.
	 */
	Category getByPath(String path, boolean encoded, boolean includeRoot);

	/**
	 * Returns a category by path
	 * @param locale Locale to use.
	 * @param path Path. Segments are separated by '/'
	 * @param encoded If the path is encoded.
	 * @param includeRoot If the the root category path segment is to be considered.
	 * @return The category with the specified path or {@code null} if not found.
	 */
	Category getByPath(Locale locale, String path, boolean encoded, boolean includeRoot);

}
