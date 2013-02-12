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

package com.isotrol.impe3.samples.message;


import static com.isotrol.impe3.test.TestSupport.config;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;


/**
 * Tests for ModuleDefinition
 * @author Andres Rodriguez
 */
public class OptionalEchoModuleTest {
	private static final String TEST = "test message";
	private static TestEnvironment environment;

	@BeforeClass
	public static void environment() {
		environment = new TestEnvironmentBuilder().get();
	}

	/**
	 * Echo module
	 */
	@Test
	public void testModule() {
		ModuleDefinition.of(OptionalEchoModule.class);
	}

	@Test
	public void withoutDependency() {
		final EchoConfig c = config(EchoConfig.class,"prefix", "prefix");
		final String s = environment.getModule(OptionalEchoModule.class).start("config", c).echo().tell();
		assertTrue(s.indexOf("EMPTY") >= 1);
	}

	@Test
	public void withDependency() {
		final MessageConfig config = config(MessageConfig.class, "text", TEST);
		final MessageModule module = environment.getModule(MessageModule.class).start("config", config);
		final MessageService service = module.service();
		final EchoConfig c = ConfigurationDefinition.of(EchoConfig.class).builder().set("prefix", "prefix").get();
		final String s = environment.getModule(OptionalEchoModule.class).start("config", c,"service", service).echo().tell();
		assertTrue(s.indexOf(TEST) >= 1);
	}

}
