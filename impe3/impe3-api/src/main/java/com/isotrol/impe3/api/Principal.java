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


import java.util.Map;
import java.util.Set;


/**
 * Interface for principals.
 * @author Andres Rodriguez
 */
public interface Principal {
	/**
	 * Returns the principal's user name.
	 * @return The principal's user name.
	 */
	String getUsername();

	/**
	 * Returns the principal's display name.
	 * @return The principal's display name.
	 */
	String getDisplayName();

	/**
	 * Returns the principal's roles.
	 * @return The principal's roles as an unmodifiable set. Never {@code null}.
	 */
	Set<String> getRoles();

	/**
	 * Returns the principal's properties.
	 * @return The principal's properties as an unmodifiable map. Never {@code null}.
	 */
	Map<String, String> getProperties();

}
