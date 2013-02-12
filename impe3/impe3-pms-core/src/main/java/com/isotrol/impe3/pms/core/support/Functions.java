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

package com.isotrol.impe3.pms.core.support;


import com.google.common.base.Function;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.pms.model.NameValue;


/**
 * Several commonly used functions.
 * @author Andres Rodriguez.
 */
public final class Functions {
	/** Not instantiable. */
	private Functions() {
		throw new AssertionError();
	}

	public static Name value2name(NameValue value) {
		return Name.of(value.getName(), value.getPath());
	}

	public static Function<NameValue, Name> VALUE2NAME = new Function<NameValue, Name>() {
		public Name apply(NameValue from) {
			return value2name(from);
		}
	};
}
