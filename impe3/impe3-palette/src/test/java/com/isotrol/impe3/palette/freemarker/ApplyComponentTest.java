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

package com.isotrol.impe3.palette.freemarker;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Apply FreeMarker Template Component Test.
 * @author Andres Rodriguez
 */
public class ApplyComponentTest {
	private static TestEnvironment environment;
	private static FileId template;
	private ModuleTester<ApplyModule> module = null;
	private ComponentTester<ApplyComponent> tester = null;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		template = b.bundle(ApplyComponentTest.class, "test.zip");
		environment = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		final ApplyModuleConfig config = TestSupport.builder(ApplyModuleConfig.class).set("templateBundle", template)
			.set("templateFile", "test.ftl").get();
		module = environment.getModule(ApplyModule.class);
		module.start("config", config);
		tester = module.getComponent(ApplyComponent.class, "component");
	}

	@Test
	public void editAndExecute() {
		tester.editAndRenderHTML();
		tester.executeAndRenderHTML();
	}
}
