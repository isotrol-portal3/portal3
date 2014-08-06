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
package com.isotrol.impe3.web20.client.counter;


import static org.junit.Assert.assertNotNull;
import net.sf.derquinsej.Proxies;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;
import com.isotrol.impe3.web20.api.CountersService;


/**
 * Basic module test.
 * @author Emilio Escobar Reyero
 */
public class CounterTest {
	private static TestEnvironment environment;
	private ModuleTester<CounterModule> module;
	private static CountersService service;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		environment = b.get();
		service = Proxies.unsupported(CountersService.class);
	}

	@Before
	public void setUp() throws Exception {
		module = environment.getModule(CounterModule.class);
		module.start("moduleConfig", TestSupport.builder(CounterConfig.class).set("counterType", "1").get(), "service",
			service);
	}

	@Test
	public void testModule() {
		assertNotNull(module);
	}

	@Test
	public void testComponent() {
		ComponentTester<CounterComponent> component = module.getComponent(CounterComponent.class, "component");
		assertNotNull(component);
		component.executeOk();

		CounterComponent real = component.getComponent();
		assertNotNull(real);
		assertNotNull(real.getAccion());
	}

	@Test
	public void testExporter() {
		ComponentTester<ActionExporterComponent> component = module.getComponent(ActionExporterComponent.class,
			"exporter");
		assertNotNull(component);
		component.executeOk();

		ActionExporterComponent real = component.getComponent();
		assertNotNull(real);
		assertNotNull(real.getAccion());
	}

}
