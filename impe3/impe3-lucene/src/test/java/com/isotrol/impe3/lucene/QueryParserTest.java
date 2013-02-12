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


import net.sf.lucis.core.Factory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.junit.Test;


public class QueryParserTest {

	private void out(Analyzer a, Query q) {
		System.out.printf("Analyzer [%s], Query [%s]: [%s]\n", a.getClass().getName(), q.getClass().getName(),
			q.toString());
	}

	private void test(String orig) throws ParseException {
		Analyzer a1 = new PortalSpanishAnalyzer();
		Analyzer a2 = new PortalStandardAnalyzer();
		final QueryParser parser1 = new PrefixAnalyzedQueryParser("FIELD", a1);
		final QueryParser parser2 = new QueryParser(Factory.get().version(), "FIELD", a2);
		out(a1, parser1.parse(orig));
		out(a2, parser2.parse(orig));
	}

	@Test
	public void parserTest() throws ParseException {
		test("m\u00e1laga");
	}

	@Test
	public void parserWildcardTest() throws ParseException {
		test("m\u00e1laga*");
	}

}
