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


import static com.google.common.collect.ImmutableMap.of;
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
public class SetFiltersTest extends AbstractFiltersTest {
	private final static String SET1 = "Set1";
	private final static String SET2 = "Set2";
	private final static UUID type = UUID.randomUUID();

	@BeforeClass
	public static void buildRepo() throws InterruptedException {
		// Two with no sets
		ConstantRAMRepository.Builder b = ConstantRAMRepository.builder();
		b.add(b.newTestBuilder(type));
		b.add(b.newTestBuilder(type));
		// One in each set
		b.add(b.newTestBuilder(type).addSet(SET1));
		b.add(b.newTestBuilder(type).addSet(SET2));
		// One in both categories
		b.add(b.newTestBuilder(type).addSet(SET1).addSet(SET2));
		b.add(b.newTestBuilder(type).addSet(SET1).addSet(SET2));
		repo = b.build();
	}

	@Test
	public void count() {
		count((Filter) null, 6);
		count(builder().sets().apply(Schema.DEFAULT_SET), 2);
		count(builder().sets().apply(SET1), 3);
		count(builder().sets().apply(SET2), 3);
		count(builder().sets().apply(of(SET1, FilterType.REQUIRED, SET2, FilterType.REQUIRED)), 2);
		count(builder().sets().apply(of(SET1, FilterType.OPTIONAL, SET2, FilterType.OPTIONAL)), 4);
		count(builder().sets().apply(of(SET1, FilterType.REQUIRED, SET2, FilterType.OPTIONAL)), 2);
		count(builder().sets().apply(of(SET1, FilterType.OPTIONAL, SET2, FilterType.REQUIRED)), 2);
	}

}
