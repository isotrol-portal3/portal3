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
 * A collection of HTTP session parameters.
 * @author Andres Rodriguez
 */
public interface SessionParams extends Parameters<Object> {
	/**
	 * Returns the value of a parameter as a specific type.
	 * @param parameter Parameter name.
	 * @return The value or null if there is no parameter with the provided name.
	 */
	Object get(String parameter);

	/**
	 * Returns the value of a parameter as a specific type.
	 * @param parameter Parameter name.
	 * @param type Requested type.
	 * @return The value or null if there is no parameter with the provided name.
	 * @throws ClassCastException if the value is not a subclass of the requested type.
	 */
	<T> T get(String parameter, Class<T> type);

	/**
	 * Returns a subset of this collection that contains only the provided parameter names.
	 * @param included Set of parameter name to include. A {@code null} value is considered an empty set.
	 * @return A new collection that contains only the parameter names included in the provided set.
	 */
	SessionParams subset(Set<String> included);
}
