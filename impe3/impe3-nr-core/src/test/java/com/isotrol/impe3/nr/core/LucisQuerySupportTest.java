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


import net.sf.lucis.core.Factory;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.junit.Assert;
import org.junit.Test;

import com.isotrol.impe3.lucene.PortalStandardAnalyzer;
import com.isotrol.impe3.nr.api.NRBooleanQuery;
import com.isotrol.impe3.nr.api.NRSortField;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeSort;


public class LucisQuerySupportTest {

	private NodeSort createLucisSort() {
		return NodeSort.of(new NRSortField[] {NRSortField.of("UNO", false), NRSortField.of("DOS", true)});
	}

	private LuceneTranslator translator() {
		return new LuceneTranslator(new PortalStandardAnalyzer());
	}

	@Test
	public void lucisSortTest() {
		Sort sort = new Sort(new SortField[] {new SortField("UNO", SortField.STRING, false),
			new SortField("DOS", SortField.STRING, true)});
		NodeSort lsort = createLucisSort();

		Sort test = translator().sort(lsort);

		Assert.assertNotNull(test);
		// Assert.assertEquals(sort, test);
		Assert.assertTrue(test instanceof Sort);
		Assert.assertEquals(sort.toString(), test.toString());
	}

	@Test
	public void lucisRangeQueryTest() {
		final String fieldName = "FIELD_NAME";
		final String lowerVal = "090101";
		final String upperVal = "090201";
		final boolean includeLower = true;
		final boolean includeUpper = true;

		Query query = new TermRangeQuery(fieldName, lowerVal, upperVal, includeLower, includeUpper);
		query.setBoost(0.9f);
		NodeQuery lquery = NodeQueries.range(fieldName, lowerVal, upperVal, includeLower, includeUpper);
		lquery.setBoost(0.9f);

		Query test = translator().query(lquery);

		Assert.assertNotNull(test);
		Assert.assertTrue(query.getBoost() == test.getBoost());
		Assert.assertEquals(query, test);
	}

	@Test
	public void lucisWildcardQueryTest() {
		Term term = new Term("FIELD", "TEST*");

		Query query = new WildcardQuery(term);
		query.setBoost(0.9f);
		NodeQuery lquery = NodeQueries.wildcard("FIELD", "TEST*");
		lquery.setBoost(0.9f);

		Query test = translator().query(lquery);

		Assert.assertNotNull(test);
		Assert.assertTrue(query.getBoost() == test.getBoost());
		Assert.assertEquals(query, test);
	}

	@Test
	public void lucisStringQueryTest() {
		try {
			QueryParser parser = new QueryParser(Factory.get().version(), "FIELD", new PortalStandardAnalyzer());

			Query query = parser.parse("TEST the QUERY");
			NodeQuery lquery = NodeQueries.string("FIELD", "TEST the QUERY");

			Query test = translator().query(lquery);

			Assert.assertNotNull(test);
			Assert.assertTrue(query.getBoost() == test.getBoost());
			Assert.assertEquals(query, test);
		} catch (ParseException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void lucisTermQueryTest() {
		Term term = new Term("FIELD", "TEST");

		Query query = new TermQuery(term);
		NodeQuery lquery = NodeQueries.term("FIELD", "TEST");

		Query test = translator().query(lquery);

		Assert.assertNotNull(test);
		Assert.assertTrue(query.getBoost() == test.getBoost());
		Assert.assertEquals(query, test);
	}

	@Test
	public void lucisMatchAllDocsQueryTest() {

		Query query = new MatchAllDocsQuery();
		NodeQuery lquery = NodeQueries.matchAll();

		Query test = translator().query(lquery);

		Assert.assertNotNull(test);
		Assert.assertTrue(query.getBoost() == test.getBoost());
		Assert.assertEquals(query, test);
	}

	@Test
	public void lucisBooleanQueryTest() {
		Query query = createBooleanQuery();
		NodeQuery lquery = createLucisBooleanQuery();

		Query test = translator().query(lquery);

		Assert.assertNotNull(test);
		Assert.assertTrue(query.getBoost() == test.getBoost());
		Assert.assertEquals(query, test);
	}

	@Test
	public void lucisMultiBooleanQueryTest() {
		Query query1 = createBooleanQuery();
		Query query2 = new TermQuery(new Term("FIELD1", "TEST"));

		BooleanClause clause1 = new BooleanClause(query1, BooleanClause.Occur.SHOULD);
		BooleanClause clause2 = new BooleanClause(query2, BooleanClause.Occur.MUST);

		BooleanQuery query = new BooleanQuery();

		query.add(clause1);
		query.add(clause2);

		NodeQuery lquery1 = createLucisBooleanQuery();
		NodeQuery lquery2 = NodeQueries.term("FIELD1", "TEST");

		NRBooleanQuery lquery = NodeQueries.bool();

		lquery.should(lquery1);
		lquery.must(lquery2);

		Query test = translator().query(lquery);

		Assert.assertNotNull(test);
		Assert.assertEquals(query, test);

	}

	private BooleanQuery createBooleanQuery() {
		Query query1 = new TermRangeQuery("FIELD_NAME", "090101", "090201", true, true);
		Query query2 = new TermQuery(new Term("FIELD", "TEST"));
		Query query3 = new MatchAllDocsQuery();
		Query query4 = new WildcardQuery(new Term("FIELD", "TEST*"));

		BooleanQuery query = new BooleanQuery();

		BooleanClause clause1 = new BooleanClause(query1, BooleanClause.Occur.MUST);
		BooleanClause clause2 = new BooleanClause(query2, BooleanClause.Occur.MUST_NOT);
		BooleanClause clause3 = new BooleanClause(query3, BooleanClause.Occur.SHOULD);
		BooleanClause clause4 = new BooleanClause(query4, BooleanClause.Occur.MUST);

		query.add(clause1);
		query.add(clause2);
		query.add(clause3);
		query.add(clause4);

		return query;
	}

	private NRBooleanQuery createLucisBooleanQuery() {
		NodeQuery lquery1 = NodeQueries.range("FIELD_NAME", "090101", "090201", true, true);
		NodeQuery lquery2 = NodeQueries.term("FIELD", "TEST");
		NodeQuery lquery3 = NodeQueries.matchAll();
		NodeQuery lquery4 = NodeQueries.wildcard("FIELD", "TEST*");

		NRBooleanQuery lquery = NodeQueries.bool();

		lquery.must(lquery1);
		lquery.mustNot(lquery2);
		lquery.should(lquery3);
		lquery.must(lquery4);

		return lquery;
	}

}
