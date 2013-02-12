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
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;


/**
 * Tests for ModuleDefinition
 * @author Andres Rodriguez
 */
public class MessageModuleTest {
	private static final String HELLO = "Hello";
	private static TestEnvironment environment;

	@BeforeClass
	public static void environment() {
		environment = new TestEnvironmentBuilder().get();
	}

	/**
	 * Message Service
	 */
	@Test
	public void testMessageService() {
		final MessageConfig config = config(MessageConfig.class, "text", HELLO);
		final MessageModule module = environment.getModule(MessageModule.class).start("config", config);
		final MessageService service = module.service();
		assertEquals(service.tell(), HELLO);
	}
}
