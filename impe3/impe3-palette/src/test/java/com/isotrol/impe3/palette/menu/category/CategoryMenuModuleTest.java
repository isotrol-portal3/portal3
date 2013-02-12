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

package com.isotrol.impe3.palette.menu.category;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * HTML Module tests.
 * @author Andres Rodriguez
 */
public class CategoryMenuModuleTest {
	private static TestEnvironment environment;
	private static FileId bundle;
	private static Category c2;
	private ModuleTester<CategoryMenuModule> module = null;

	@SuppressWarnings("unused")
	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		Category root = b.category("root", null);
		Category c1 = b.category("c1", root.getId());
		c2 = b.category("c2", root.getId());
		Category c3 = b.category("c3", root.getId());
		Category c21 = b.category("c21", c2.getId());
		Category c22 = b.category("c22", c2.getId());
		bundle = b.bundle(CategoryMenuModuleTest.class, "menu.zip");
		environment = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		module = environment.getModule(CategoryMenuModule.class);
		module.start("config",
			TestSupport.config(CategoryMenuModuleConfig.class, "templateBundle", bundle, "templateFile", "menu.ftl"));
	}

	@Test
	public void first() {
		final ComponentTester<FirstLevelComponent> first = module.getComponent(FirstLevelComponent.class, "firstLevel");
		first.editAndRenderHTML();
	}

	@Test
	public void current() {
		final ComponentTester<CurrentLevelComponent> current = module.getComponent(CurrentLevelComponent.class,
			"currentLevel");
		current.executeOk();
	}

	@Test
	public void currentNoNK() {
		final ComponentTester<CurrentLevelComponent> current = module.getComponent(CurrentLevelComponent.class,
			"currentLevel");
		current.executeOk();
	}

	@Test
	public void anchored() {
		final ComponentTester<AnchoredComponent> anchored = module.getComponent(AnchoredComponent.class, "anchored");
		anchored.getComponent().setComponentConfig(TestSupport.config(CategoryMenuConfig.class, "current", c2));
		anchored.executeOk();
	}

}
