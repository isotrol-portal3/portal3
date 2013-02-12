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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.Schema;
import com.isotrol.impe3.nr.core.DocumentBuilder;
import com.isotrol.impe3.support.nr.ContentRepository;


/**
 * Test for Test Environment.
 * @author Andres Rodriguez
 */
public class RepositoryTest {
	private static ContentType type;
	private static Category root;
	private static Category leaf;
	private static TestEnvironment environment;
	private static ContentRepository repository;

	@BeforeClass
	public static void setUp() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		type = b.contentType("type");
		root = b.category("root", null);
		leaf = b.category("leaf", root.getId());
		environment = b.get();
		TestRepositoryBuilder builder = environment.getRepositoryBuilder();
		add(builder, root, "Root Node");
		add(builder, leaf, "Leaf Node");
		repository = builder.get();
	}

	private static void add(TestRepositoryBuilder builder, Category c, String title) {
		final DocumentBuilder db = new DocumentBuilder();
		db.setNodeKey(NodeKey.of(type.getId(), TestSupport.uuid().toString()));
		db.addCategory(c.getId());
		db.setTitle(title);
		builder.add(db);
	}

	/** Check setup. */
	@Test
	public void check() {
		assertNotNull(repository);
	}

	/** Node. */
	@Test
	public void node() {
		final NodeRepository nr = repository.getRepository();
		NodeQuery q = NodeQueries.term(Schema.CATEGORY, leaf.getId());
		Item<Node> item = nr.getFirst(q, null, null, false, null);
		assertNotNull(item);
		Node node = item.getItem();
		assertNotNull(node);
		assertTrue(node.getCategories().contains(leaf.getId()));
	}

	/** Content. */
	@Test
	public void content() {
		NodeQuery q = NodeQueries.term(Schema.CATEGORY, leaf.getId());
		Item<Content> item = repository.getFirst(q, null, false);
		assertNotNull(item);
		Content content = item.getItem();
		assertNotNull(content);
		Assert.assertEquals(type, content.getContentType());
		assertTrue(content.getCategories().contains(leaf));
	}

}
