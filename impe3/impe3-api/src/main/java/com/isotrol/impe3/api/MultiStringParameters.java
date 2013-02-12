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


import java.util.List;


/**
 * Base interface to HTTP headers and request parameters.
 * @author Andres Rodriguez
 */
public interface MultiStringParameters extends Parameters<List<String>> {
	/**
	 * Returns the value of a parameter.
	 * @param name Parameter name.
	 * @return An unmodifiable list of values or an empty list if there is no parameter with the provided name.
	 */
	List<String> get(String name);

	/**
	 * Returns the first value of a parameter.
	 * @param name Parameter name.
	 * @return The first value of the parameter or {@code null} if there are no values for the provided parameter.
	 */
	String getFirst(String name);

}
