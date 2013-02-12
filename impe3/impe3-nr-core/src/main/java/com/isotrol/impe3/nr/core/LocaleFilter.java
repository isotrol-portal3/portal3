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

package com.isotrol.impe3.nr.core;


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Locale;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

import com.isotrol.impe3.nr.api.Schema;


/**
 * Locale Lucene filter.
 * @author Andres Rodriguez Chamorro
 */
class LocaleFilter extends Filter {
	/** Serial UID. */
	private static final long serialVersionUID = -5640228841447323207L;

	/** Locale to filter. */
	private final Locale locale;

	/**
	 * Constructor.
	 */
	LocaleFilter(Locale locale) {
		this.locale = checkNotNull(locale);
	}

	private OpenBitSet getTerm(int n, String field, String value, IndexReader reader) throws IOException {
		final OpenBitSet result = new OpenBitSet(n);
		final TermDocs td = reader.termDocs(new Term(field, value));
		try {
			while (td.next()) {
				result.set(td.doc());
			}
		}
		finally {
			td.close();
		}
		return result;
	}

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		final int n = reader.maxDoc();
		// Search for all
		final OpenBitSet all = getTerm(n, Schema.LOCALE, Schema.ALL_LOCALES, reader);
		// Locale loop
		// locale = requested || (locale = ALL && other != requested)
		final String localeValue = locale.toString();
		final OpenBitSet result = getTerm(n, Schema.OTHER_LOCALE, localeValue, reader);
		result.flip(0, n);
		result.intersect(all);
		result.union(getTerm(n, Schema.LOCALE, localeValue, reader));
		return result;
	}
}
