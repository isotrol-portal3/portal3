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


import java.util.Set;


/**
 * Base interface to cookies, headers, and request and session parameters.
 * @author Andres Rodriguez
 * @param <V> Value type.
 */
public interface Parameters<V> {
	/**
	 * Return the names of the parameters contained in this collection.
	 * @return A set with the names of the parameters.
	 */
	Set<String> getNames();
	/**
	 * Returns the value of the provided parameter.
	 * @param name The requested parameter.
	 * @return The requested parameter or {@code null} if there is no parameter with the provided name.
	 */
	V get(String name);
	/**
	 * Checks if the collection constains a parameter with the provided name.
	 * @param name Parameter name to check.
	 * @return True if the collection contains a parameter with the provided name.
	 * @throws NullPointerException if the provided parameter is null.
	 */
	boolean contains(String name);

	/**
	 * Returns a subset of this collection that contains only the provided parameter names.
	 * @param included Set of parameter name to include. A {@code null} value is considered an empty set.
	 * @return A new collection that contains only the parameter names included in the provided set.
	 */
	Parameters<V> subset(Set<String> included);
}
