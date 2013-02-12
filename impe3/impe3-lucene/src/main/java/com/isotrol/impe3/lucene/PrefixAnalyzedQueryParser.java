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

package com.isotrol.impe3.lucene;


import java.io.IOException;
import java.io.StringReader;

import net.sf.lucis.core.Factory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;


/**
 * Query parser thar uses prefix queries.
 * @author Andres Rodriguez
 */
public class PrefixAnalyzedQueryParser extends QueryParser {
	/** Analyzer to use. */
	private final Analyzer analyzer;

	/** Constructor. */
	public PrefixAnalyzedQueryParser(String f, Analyzer a) {
		super(Factory.get().version(), f, a);
		if (a != null) {
			this.analyzer = a;
		} else {
			this.analyzer = new PortalSpanishAnalyzer();
		}
	}

	@Override
	protected org.apache.lucene.search.Query getPrefixQuery(String field, String termStr) throws ParseException {
		try {
			TokenStream ts = analyzer.tokenStream(field, new StringReader(termStr));
			if (ts.incrementToken() && ts.hasAttribute(CharTermAttribute.class)) {
				String term = ts.getAttribute(CharTermAttribute.class).toString();
				if (term != null) {
					return super.getPrefixQuery(field, term);
				}
			}
		} catch (IOException e) {}
		return super.getPrefixQuery(field, termStr);
	}

}
