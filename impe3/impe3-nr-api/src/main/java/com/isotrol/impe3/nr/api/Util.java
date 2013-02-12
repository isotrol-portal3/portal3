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


import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.util.Locale;
import java.util.UUID;

import net.sf.derquinsej.i18n.Locales;

import com.google.common.base.Function;
import com.google.common.base.Predicate;


/**
 * Support methods for this package.
 * @author Andres Rodriguez Chamorro
 */
public final class Util {
	/** Not instantiable. */
	private Util() {
		throw new AssertionError();
	}

	static final Function<String, UUID> STRING2UUID = new Function<String, UUID>() {
		public UUID apply(String from) {
			return UUID.fromString(from);
		}
	};

	/** String to local function. */
	static Function<String, Locale> STRING2LOCALE = new Function<String, Locale>() {
		public Locale apply(String from) {
			return Locales.safeFromString(from);
		}
	};

	/** Not all locales. */
	static Predicate<String> NOT_ALL = not(equalTo(Schema.ALL_LOCALES));

	static Iterable<String> notAllLocales(Iterable<String> strings) {
		return filter(strings, NOT_ALL);
	}

	static Iterable<Locale> safeStrings2Locales(Iterable<String> strings) {
		return filter(transform(notAllLocales(strings), STRING2LOCALE), notNull());
	}
}
