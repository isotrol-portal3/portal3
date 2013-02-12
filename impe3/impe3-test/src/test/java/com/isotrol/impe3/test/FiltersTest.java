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

package com.isotrol.impe3.test;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.nr.api.FilterType;
import com.isotrol.impe3.nr.api.Schema;


/**
 * Test for node filters application.
 * @author Andres Rodriguez
 */
public class FiltersTest {
	private static Category cat1;
	private static Category cat2;
	private static ContentType type1;
	private static ContentType type2;
	private static TestContext context;

	private ContentCriteria criteria;

	@BeforeClass
	public static void buildRepo() {
		TestIABuilder iab = new TestIABuilder();
		cat1 = iab.category("cat1", null);
		cat2 = iab.category("cat2", null);
		type1 = iab.contentType("type1");
		type2 = iab.contentType("type2");
		TestContextBuilder tcb = iab.get();
		// Two uncategorized
		tcb.add(1, type1);
		tcb.add(1, type2);
		// One in each category
		tcb.add(1, type1, cat1);
		tcb.add(1, type2, cat2);
		// One in both categories
		tcb.add(1, type1, cat1, cat2);
		tcb.add(1, type2, cat1, cat2);
		// Build
		context = tcb.get();
	}

	@Before
	public void setUp() {
		criteria = context.getContentLoader().newCriteria();
	}

	private void count(int n) {
		Assert.assertEquals(n, criteria.count().getTotalHits());
	}

	@Test
	public void countAll() {
		count(6);
	}

	@Test
	public void countAllWithFilter() {
		criteria.setUncategorized(FilterType.OPTIONAL);
		criteria.categories().apply(cat1, FilterType.OPTIONAL).categories().apply(cat2, FilterType.OPTIONAL);
		count(6);
	}

	@Test
	public void countUnategorized() {
		criteria.setUncategorized(FilterType.REQUIRED);
		count(2);
	}


	@Test
	public void countCategorized() {
		//criteria.setUncategorized(FilterType.FORBIDDEN);
		criteria.categories().apply(cat1, FilterType.OPTIONAL).categories().apply(cat2, FilterType.OPTIONAL);
		count(4);
	}

	@Test
	public void countCat1() {
		criteria.setUncategorized(FilterType.FORBIDDEN);
		criteria.categories().apply(cat1, FilterType.REQUIRED);
		count(3);
	}

	@Test
	public void countCat2() {
		criteria.setUncategorized(FilterType.FORBIDDEN);
		criteria.categories().apply(cat2, FilterType.REQUIRED);
		count(3);
	}

	@Test
	public void countType1() {
		criteria.contentTypes().apply(type1, FilterType.REQUIRED);
		count(3);
	}

	@Test
	public void countType2() {
		criteria.contentTypes().apply(type2, FilterType.REQUIRED);
		count(3);
	}

	@Test
	public void countDefaultSet() {
		criteria.sets().apply(Schema.DEFAULT_SET, FilterType.REQUIRED);
		count(6);
	}

	@Test
	public void countOtherSet() {
		criteria.sets().apply("Bad", FilterType.REQUIRED);
		count(0);
	}

	@Test
	public void countC1T2() {
		criteria.categories().apply(cat1, FilterType.REQUIRED);
		criteria.contentTypes().apply(type2, FilterType.REQUIRED);
		count(1);
	}

}
