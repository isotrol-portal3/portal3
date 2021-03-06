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


import java.util.Locale;

import net.sf.derquinsej.i18n.Locales;

import com.google.common.base.Predicate;


/**
 * More utility methods for Locales.
 * @author Andres Rodriguez.
 */
public final class MoreLocales {
	/** Not instantiable. */
	private MoreLocales() {
		throw new AssertionError();
	}

	/** Fallback locale. */
	public static final Locale FALLBACK = new Locale("es");

	/**
	 * Returns a locale parsed from a string. Allowed values are: <ul> <li>language</li> <li>language_country</li>
	 * <li>language_country_variant</li> </ul>
	 * @param localeString String to parse.
	 * @param fallback Fallback value.
	 * @return The parsed locale or the fallback value if unable to parse it.
	 */
	public static Locale fromString(String localeString, Locale fallback) {
		Locale l = Locales.safeFromString(localeString);
		return l != null ? l : fallback;
	}

	/**
	 * Valid locale string predicate.
	 */
	public static final Predicate<String> VALID = new Predicate<String>() {
		public boolean apply(String input) {
			return Locales.safeFromString(input) != null;
		}
	};
}
