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


import net.sf.derquinse.lucis.Result;
import net.sf.lucis.core.LucisQuery;
import net.sf.lucis.core.Queryable;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.junit.Assert;

import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeFilter.Builder;


/**
 * Test for node filters application.
 * @author Andres Rodriguez
 */
public abstract class AbstractFiltersTest {
	static ConstantRAMRepository repo;
	static LuceneTranslator translator = new LuceneTranslator();

	static void count(Query q, Filter f, int count) {
		final LucisQuery<Result> lq = LucisQuery.count(q, f);
		final Queryable s = repo.getQueryable();
		final Result r = s.query(lq);
		final int n = r.getTotalHits();
		Assert.assertEquals(count, n);
	}

	static void count(Filter f, int count) {
		count(new MatchAllDocsQuery(), f, count);
	}

	static void count(NodeFilter f, int count) {
		count(translator.getFilter(f), count);
	}

	static void count(Builder b, int count) {
		count(b.build(), count);
	}

}
