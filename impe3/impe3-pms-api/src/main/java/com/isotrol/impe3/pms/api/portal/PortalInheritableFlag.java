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

package com.isotrol.impe3.pms.api.portal;

/**
 * Value representing an inheritable boolean property of a portal.
 * @author Andres Rodriguez
 */
public enum PortalInheritableFlag {
	INHERIT {
		@Override
		public Boolean toBooleanObject() {
			return null;
		}

		@Override
		public boolean toBoolean(boolean valueIfInherit) {
			return valueIfInherit;
		}
	},
	ON {
		@Override
		public Boolean toBooleanObject() {
			return Boolean.TRUE;
		}

		@Override
		public boolean toBoolean(boolean valueIfInherit) {
			return true;
		}
	},
	OFF {
		@Override
		public Boolean toBooleanObject() {
			return Boolean.FALSE;
		}

		@Override
		public boolean toBoolean(boolean valueIfInherit) {
			return false;
		}
	};

	/** Return ON if the input is true, OFF if false and INHERIT if null. */
	public static PortalInheritableFlag fromBoolean(Boolean b) {
		if (b == null) {
			return INHERIT;
		} else if (b.booleanValue()) {
			return ON;
		}
		return OFF;
	}

	/** Returns the value as a boolean object, with {@code null} meaning INHERIT. */
	public abstract Boolean toBooleanObject();

	/** Returns the value as a boolean value, provided a default state for the INHERIT value. */
	public abstract boolean toBoolean(boolean valueIfInherit);
}
