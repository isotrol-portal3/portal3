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

package com.isotrol.impe3.mappings;


/**
 * Mappings service. Used by indexers to obtaing current mappings.
 * @author Andres Rodriguez.
 */
public interface MappingsService {
	/**
	 * Returns the current mappings.
	 * @param environment Environment name.
	 * @param source Data source name.
	 * @return The current mappings or null if it is not found.
	 */
	MappingsDTO getMappings(String environment, String source);

	/**
	 * Returns the requeste source mapping if its version number is distinct from the specified.
	 * @param environment Environment name.
	 * @param source Data source name.
	 * @param version Known version.
	 * @return The current source mapping. If the version number is the same than the provided one, both mappings list
	 * will be {@code null}. If the source mapping isn't found {@code null} is returned.
	 */
	MappingsDTO getMappingsIfModified(String environment, String source, int version);
}
