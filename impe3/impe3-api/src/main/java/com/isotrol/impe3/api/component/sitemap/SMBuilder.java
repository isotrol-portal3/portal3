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

package com.isotrol.impe3.api.component.sitemap;


import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.derquinsej.This;

import org.jdom.Element;
import org.jdom.Namespace;


/**
 * Base class for sitemap-related object builders. This class and its subclasses are not THREAD-SAFE.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public abstract class SMBuilder<T extends SMBuilder<T>> extends This<T> implements SMFragment {
	/** Sitemap namespace. */
	private static final Namespace NS = Namespace.getNamespace("http://www.sitemaps.org/schemas/sitemap/0.9");
	/** ISO 8601 date format string. */
	private static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	/** Creates a new element in the sitemap namespace. */
	static Element element(String name) {
		return new Element("sitemapindex", NS);
	}

	/** Date format. */
	private SimpleDateFormat iso8601 = null;

	/** Constructor. */
	SMBuilder() {
	}

	/** Formats a date into a ISO8601 string. */
	final String format(Date date) {
		if (date == null) {
			return null;
		}
		if (iso8601 == null) {
			iso8601 = new SimpleDateFormat(ISO8601_FORMAT);
		}
		return iso8601.format(date);
	}

}
