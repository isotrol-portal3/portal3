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
package com.isotrol.impe3.idx.d6;


import java.util.List;

import com.isotrol.impe3.idx.d6.Drupal6Content.Drupal6ContentBuilder;


/**
 * Database reader test oriented interface.
 */
public interface Drupal6DatabaseReader {

	/**
	 * Create a content builder from content id
	 * @param id content original id
	 * @return the builder
	 */
	Drupal6ContentBuilder createBuilder(String id);

	/**
	 * Reads content categories
	 * @param id content id
	 * @return list of categories ids
	 */
	List<String> readContentCategories(String id);
}
