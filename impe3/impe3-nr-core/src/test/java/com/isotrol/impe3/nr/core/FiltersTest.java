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


import static com.isotrol.impe3.nr.api.NodeFilter.builder;

import java.util.UUID;

import org.apache.lucene.search.Filter;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.nr.api.FilterType;
import com.isotrol.impe3.nr.api.Schema;


/**
 * Test for node filters application.
 * @author Andres Rodriguez
 */
public class FiltersTest extends AbstractFiltersTest {
	private final static UUID cat1 = UUID.randomUUID();
	private final static UUID cat2 = UUID.randomUUID();
	private final static UUID type1 = UUID.randomUUID();
	private final static UUID type2 = UUID.randomUUID();

	@BeforeClass
	public static void buildRepo() throws InterruptedException {
		// Two uncategorized
		ConstantRAMRepository.Builder b = ConstantRAMRepository.builder();
		b.add(b.newTestBuilder(type1));
		b.add(b.newTestBuilder(type2));
		// One in each category
		b.add(b.newTestBuilder(type1).addCategory(cat1));
		b.add(b.newTestBuilder(type2).addCategory(cat2));
		// One in both categories
		b.add(b.newTestBuilder(type1).addCategory(cat1).addCategory(cat2));
		b.add(b.newTestBuilder(type2).addCategory(cat1).addCategory(cat2));
		repo = b.build();
	}

	@Test
	public void countAll() {
		count((Filter) null, 6);
	}

	@Test
	public void countAllWithFilter() {
		count(builder().uncategorized(FilterType.OPTIONAL).categories().apply(FilterType.OPTIONAL, cat1, cat2), 6);
	}

	@Test
	public void countCategorized() {
		count(builder().categories().apply(FilterType.OPTIONAL, cat1, cat2), 4);
	}

	@Test
	public void countCat1() {
		count(builder().categories().apply(FilterType.REQUIRED, cat1), 3);
	}

	@Test
	public void countCat2() {
		count(builder().categories().apply(FilterType.REQUIRED, cat2), 3);
	}

	@Test
	public void countType1() {
		count(builder().contentTypes().apply(FilterType.REQUIRED, type1), 3);
	}

	@Test
	public void countType2() {
		count(builder().contentTypes().apply(FilterType.REQUIRED, type2), 3);
	}

	@Test
	public void countDefaultSet() {
		count(builder().sets().apply(FilterType.REQUIRED, Schema.DEFAULT_SET), 6);
	}

	@Test
	public void countOtherSet() {
		count(builder().sets().apply(FilterType.REQUIRED, "other"), 0);
	}

	@Test
	public void countC1T2() {
		count(builder().uncategorized(FilterType.OPTIONAL).categories().apply(FilterType.OPTIONAL, cat1).contentTypes().apply(FilterType.REQUIRED, type1),
			3);
	}

}
