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
package com.isotrol.impe3.web20.client.populartop;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.core.config.ConfigurationBuilder;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;
import com.isotrol.impe3.web20.client.counter.CounterConfig;
import com.isotrol.impe3.web20.client.counter.CounterModule;

/**
 * Testing top component. 
 * @author Emilio Escobar Reyero
 */
public class ComponentTest {
	private static TestEnvironment environment;
	private ModuleTester<TopResourcesModule> module;
	
	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		environment = b.get();
	}
	
	@Before
	public void setUp() throws Exception {
		module = environment.getModule(TopResourcesModule.class);
		
		final ConfigurationBuilder<TopResourcesConfig> moduleConfig = TestSupport.builder(TopResourcesConfig.class);
		moduleConfig.set("counterType", "TYPE1");
		moduleConfig.set("pageSize", 10);
		
		//module.start("moduleConfig", moduleConfig.get());
		
		
	}
	
	@Test
	public void testModule() {
		//assertNotNull(module);
		assertTrue(true);
	}
	/*
	@Test
	public void testComponent() {
		
	}
	*/
}
