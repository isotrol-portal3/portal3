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

package com.isotrol.impe3.samples.dummy;


import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Tests for Calculator Component Module
 * @author Andres Rodriguez
 */
public class DummyComponentTest {
	private static TestEnvironment environment;
	private static ModuleTester<DummyConnectorModule> cnn = null;
	private static DummyService service = null;
	private static ModuleTester<DummyComponentModule> module = null;
	private static ComponentTester<DummyComponent> tester = null;
	private static DummyConfig config = TestSupport.config(DummyConfig.class, "text", "value");

	@BeforeClass
	public static void createEnvironment() {
		environment = new TestEnvironmentBuilder().get();
		cnn = environment.getModule(DummyConnectorModule.class);
		service = cnn.start("config", config).service();
		module = environment.getModule(DummyComponentModule.class);
		module.start("service", service, "config", config);
		tester = module.getComponent(DummyComponent.class, "component");
	}

	/**
	 * No arguments
	 */
	@Test
	public void empty() {
		tester.executeAndRenderHTML();
	}
}
