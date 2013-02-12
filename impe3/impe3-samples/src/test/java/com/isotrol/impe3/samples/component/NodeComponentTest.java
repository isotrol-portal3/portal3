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

package com.isotrol.impe3.samples.component;


import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.core.DocumentBuilder;
import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestRepositoryBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Tests for Simple Component Module
 * @author Andres Rodriguez
 */
public class NodeComponentTest {
	private static TestEnvironment env;
	private static ContentType contentType;
	private static Category category;
	private static NodeRepository repository;
	private static ModuleTester<NodeComponentModule> module;
	private static ComponentTester<NodeComponent> component;

	@BeforeClass
	public static void environment() {
		final TestEnvironmentBuilder teb = new TestEnvironmentBuilder();
		contentType = teb.contentType("contentType");
		category = teb.category("category", null);
		env = teb.get();
		TestRepositoryBuilder trb = env.getRepositoryBuilder();
		for (int i = 0; i < 100; i++) {
			trb.add(create(String.valueOf(i)));
		}
		repository = trb.getNodeRepository();
		module = env.getModule(NodeComponentModule.class);
		module.start("repository", repository);
		component = module.getComponent(NodeComponent.class, "component");
	}

	private static DocumentBuilder create(String id) {
		DocumentBuilder db = new DocumentBuilder();
		db.setNodeKey(NodeKey.of(contentType.getId(), id));
		db.setTitle("Title : " + id);
		return db;
	}

	/**
	 * No arguments
	 */
	@Test
	public void empty() {
		NodeComponentConfig c = TestSupport.builder(NodeComponentConfig.class).get();
		component.getComponent().setConfig(c);
		component.editAndRenderHTML();
	}

	/**
	 * With Id
	 */
	@Test
	public void id() {
		NodeComponentConfig c = TestSupport.config(NodeComponentConfig.class, "contentId", "23");
		component.getComponent().setConfig(c);
		component.editAndRenderHTML();
	}

}
