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


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.derquinse.lucis.Item;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.Schema;
import com.isotrol.impe3.nr.core.DocumentBuilder;


/**
 * Test for Test Environment.
 * @author Andres Rodriguez
 */
public class ContextTest {
	private static ContentType type;
	private static Category root;
	private static Category leaf;
	private static TestContext context;

	@BeforeClass
	public static void setUp() {
		TestIABuilder b = new TestIABuilder();
		type = b.contentType("type");
		root = b.category("root", null);
		leaf = b.category("leaf", root.getId());
		TestContextBuilder tcb = b.get();
		add(tcb, root);
		add(tcb, leaf);
		context = tcb.get();
	}

	private static void add(TestContextBuilder builder, Category c) {
		final DocumentBuilder db = builder.newTestDocument(type).addCategory(c.getId());
		builder.add(db);
	}

	/** Check setup. */
	@Test
	public void check() {
		assertNotNull(context);
	}

	/** Content by category. */
	@Test
	public void byCategory() {
		final ContentLoader loader = context.getContentLoader();
		NodeQuery q = NodeQueries.term(Schema.CATEGORY, leaf.getId());
		Item<Content> item = loader.newCriteria().must(q).getFirst();
		assertNotNull(item);
		Content content = item.getItem();
		assertNotNull(content);
		assertTrue(content.getCategories().contains(leaf));
	}
}
