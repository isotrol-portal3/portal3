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

package com.isotrol.impe3.api.support;


import java.util.Locale;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.isotrol.impe3.api.LocaleURIGenerator;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;


/**
 * Default locale URI generator.
 * @author Andres Rodriguez
 */
public class DefaultLocaleURIGenerator implements LocaleURIGenerator {
	private static final DefaultLocaleURIGenerator INSTANCE = new DefaultLocaleURIGenerator();

	public static LocaleURIGenerator get() {
		return INSTANCE;
	}

	/** Constructor. */
	private DefaultLocaleURIGenerator() {
	}

	/**
	 * @see com.isotrol.impe3.api.LocaleURIGenerator#getTransformer(com.isotrol.impe3.api.Portal, java.util.Locale)
	 */
	public Function<PathSegments, PathSegments> getTransformer(Portal portal, Locale locale) {
		return Functions.identity();
	}

}
