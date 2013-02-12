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

package com.isotrol.impe3.palette.metadata;


import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * HTML Module tests.
 * @author Andres Rodriguez
 */
public class TitleModuleTest {
	private static TestEnvironment environment;
	private ModuleTester<TitleModule> module = null;
	private ComponentTester<SimpleTitleComponent> simple = null;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		environment = b.get();
	}

	@Test
	public void simple() {
		module = environment.getModule(TitleModule.class);
		module.start("moduleConfig", TestSupport.builder(SimpleTitleConfig.class).set("title", "Test Title").get());
		simple = module.getComponent(SimpleTitleComponent.class, "simple");
		simple.editAndRenderHTML();
		simple.executeAndRenderHTML();
		simple.getComponent()
			.setConfig(TestSupport.builder(SimpleTitleConfig.class).set("title", "Test Title 2").get());
		simple.editAndRenderHTML();
		simple.executeAndRenderHTML();
	}
}
