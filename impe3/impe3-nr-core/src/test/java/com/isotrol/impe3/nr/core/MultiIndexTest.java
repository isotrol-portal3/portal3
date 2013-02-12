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


import java.util.UUID;

import net.sf.lucis.core.DirectoryProvider;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.support.Queryables;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.nr.api.NRSortField;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.NodeSort;


/**
 * Multiindex test.
 * @author Andres Rodriguez
 */
public class MultiIndexTest {
	private static final String F1 = "field1";
	private static final String F2 = "field2";
	private static final UUID TYPE = UUID.randomUUID();

	private static NodeRepository repository;

	public static String v() {
		return UUID.randomUUID().toString();
	}

	private static void sampleDoc(ConstantRAMRepository.Builder b, String field) throws InterruptedException {
		final DocumentBuilder d = b.newTestBuilder(TYPE);
		d.setField(field, v(), true, false);
		d.setField(field+"v", "v", true, false);
		b.add(d);
	}

	private static ConstantRAMRepository repo(String field, int n) throws InterruptedException {
		ConstantRAMRepository.Builder b = ConstantRAMRepository.builder();
		for (int i = 0; i < n; i++) {
			sampleDoc(b, field);
		}
		return b.build();
	}

	@BeforeClass
	public static void buildRepos() throws InterruptedException {
		ConstantRAMRepository repo1 = repo(F1, 10);
		ConstantRAMRepository repo2 = repo(F2, 20);
		Queryable queryable = Queryables.multi(ImmutableList.<DirectoryProvider> of(repo1.getStore(), repo2.getStore()));
		repository = new NodeRepositoryImpl(queryable);
	}
	
	private void query(String field) {
		repository.getFirst(NodeQueries.matchAll(), null, NodeSort.of(NRSortField.of(field)), false, null);
	}

	@Test
	public void test() {
		query(F1);
		query(F2);
	}

	@Test
	public void testQ() {
		Assert.assertTrue(repository.getFirst(NodeQueries.term(F1+"v", "v"), null, null, false, null).getTotalHits() > 1);
	}
	
}
