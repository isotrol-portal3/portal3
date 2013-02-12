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

package com.isotrol.impe3.idx;


import net.sf.lucis.core.Queryable;

import com.google.common.base.Function;


/**
 * Interface for Port@l content aggregation platform queryable objects.
 * @author Andres Rodriguez Chamorro
 */
public interface PortalQueryable {
	Function<PortalQueryable, String> NAME = new Function<PortalQueryable, String>() {
		public String apply(PortalQueryable input) {
			return input.getName();
		}
	};
	Function<PortalQueryable, String> DESCRIPTION = new Function<PortalQueryable, String>() {
		public String apply(PortalQueryable input) {
			return input.getDescription();
		}
	};

	/**
	 * Returns the object name.
	 * @return The object name.
	 */
	String getName();

	/**
	 * Returns the object description.
	 * @return The object description.
	 */
	String getDescription();

	/**
	 * Returns the queryable to use for repository building.
	 * @return The requested queryable.
	 */
	Queryable getQueryable();
}
