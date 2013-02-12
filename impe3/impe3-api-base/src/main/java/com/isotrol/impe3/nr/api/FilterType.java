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

package com.isotrol.impe3.nr.api;


/**
 * Enumeration of supported node filter types. The join rules are:
 * <ul>
 * <li>REQUIRED.join(REQUIRED) = REQUIRED</li>
 * <li>REQUIRED.join(OPTIONAL) = REQUIRED</li>
 * <li>REQUIRED.join(FORBIDDEN) = null</li>
 * <li>OPTIONAL.join(REQUIRED) = REQUIRED</li>
 * <li>OPTIONAL.join(OPTIONAL) = OPTIONAL</li>
 * <li>OPTIONAL.join(FORBIDDEN) = FORBIDDEN</li>
 * <li>FORBIDDEN.join(REQUIRED) = null</li>
 * <li>FORBIDDEN.join(OPTIONAL) = FORBIDDEN</li>
 * <li>FORBIDDEN.join(FORBIDDEN) = REQUIRED</li>
 * </ul>
 * @author Andres Rodriguez
 */
public enum FilterType {
	REQUIRED {
		@Override
		public FilterType join(FilterType filterType) {
			if (filterType == REQUIRED || filterType == OPTIONAL) {
				return REQUIRED;
			}
			return null;
		}
	},
	OPTIONAL {
		@Override
		public FilterType join(FilterType filterType) {
			if (filterType == OPTIONAL) {
				return OPTIONAL;
			}
			return filterType;
		}
	},
	FORBIDDEN {
		@Override
		public FilterType join(FilterType filterType) {
			if (filterType == FORBIDDEN || filterType == OPTIONAL) {
				return FORBIDDEN;
			}
			return null;
		}
	};

	/**
	 * Joins the current type with the provided one.
	 * @param filterType Filter type to join the type with.
	 * @return The resulting filter type or {@code null} is an empty filter results.
	 */
	public abstract FilterType join(FilterType filterType);
}
