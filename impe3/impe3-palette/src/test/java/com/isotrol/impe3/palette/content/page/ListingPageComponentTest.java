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

package com.isotrol.impe3.palette.content.page;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Listing Page Component Test.
 * @author Andres Rodriguez
 */
public class ListingPageComponentTest {
	private static TestEnvironment environment;
	private static FileId template;
	private static FileId content;
	private ModuleTester<ListingPageModule> module = null;
	private ComponentTester<ListingPageComponent> tester = null;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		template = b.bundle(ListingPageComponentTest.class, "template.zip");
		content = b.bundle(ListingPageComponentTest.class, "content.zip");
		environment = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		final ListingPageModuleConfig config = TestSupport.builder(ListingPageModuleConfig.class).set("templateBundle",
			template).set("sample", content).set("templateFile", "template.ftl").get();
		module = environment.getModule(ListingPageModule.class);
		module.start("config", config);
		tester = module.getComponent(ListingPageComponent.class, "component");

	}

	@Test
	public void names() {
		System.out.println(ModuleDefinition.of(ListingPageModule.class).getName().get());
	}

	@Test
	public void edit() {
		tester.editAndRenderHTML();
	}
}
