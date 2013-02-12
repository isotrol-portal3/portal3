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

package com.isotrol.impe3.core.modules;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.config.ConfigurationException;


/**
 * Tests for ModuleDefinition
 * @author Andres Rodriguez
 */
public class ModuleDefinitionTest extends AbstractModuleTest {

	private void testInvalid(Class<? extends Module> module) {
		ModuleDefinition<?> m = ModuleDefinition.getSafe(module);
		assertNotNull(m);
		assertEquals(m.getModuleType(), ModuleType.INVALID);
	}

	/**
	 * Test invalid.
	 */
	@Test
	public void testInvalids() throws ModuleException {
		testInvalid(TestClass.class);
		testInvalid(Test00.class);
		testInvalid(Test01A.class);
		testInvalid(Test01B.class);
		testInvalid(Test02.class);
		testInvalid(Test03.class);
		testInvalid(Test04.class);
		testInvalid(Test05.class);
		//testInvalid(Test07.class);
		testInvalid(Test08.class);
		testInvalid(Test09.class);
		testInvalid(Test11.class);
		testInvalid(Test15.class);
		testInvalid(Test16B.class);
		testInvalid(Test17.class);
		testInvalid(Test18.class);
		testInvalid(TestInvalidConfig.class);
	}

	/**
	 * Message Service
	 */
	@Test
	public void testMessageService() throws ConfigurationException, ModuleException {
		final String m = "Hello!";
		final ModuleDefinition<MessageModule> md = ModuleDefinition.of(MessageModule.class);
		final MessageService service = md.starter().put("config", config(m)).start(null).getModule().service();
		Assert.assertEquals(service.tell(), m);
	}

	/**
	 * Bad starter
	 */
	@Test(expected = NullPointerException.class)
	public void badStarter1() throws ConfigurationException, ModuleException {
		final ModuleDefinition<MessageModule> md = ModuleDefinition.of(MessageModule.class);
		md.starter().put(null, null).start(null);
	}

	/**
	 * Bad starter
	 */
	@Test(expected = IllegalArgumentException.class)
	public void badStarter2() throws ConfigurationException, ModuleException {
		final ModuleDefinition<MessageModule> md = ModuleDefinition.of(MessageModule.class);
		md.starter().put("config2", config()).start(null);
	}

	/**
	 * Bad starter
	 */
	@Test(expected = NullPointerException.class)
	public void badStarter3() throws ConfigurationException, ModuleException {
		final ModuleDefinition<MessageModule> md = ModuleDefinition.of(MessageModule.class);
		md.starter().put("config", null).start(null);
	}

	/**
	 * Bad starter
	 */
	@Test(expected = IllegalArgumentException.class)
	public void badStarter4() throws ConfigurationException, ModuleException {
		final ModuleDefinition<MessageModule> md = ModuleDefinition.of(MessageModule.class);
		md.starter().put("config", 2).start(null);
	}

	/**
	 * Bad starter
	 */
	@Test(expected = IllegalStateException.class)
	public void badStarter5() {
		final ModuleDefinition<MessageModule> md = ModuleDefinition.of(MessageModule.class);
		md.starter().start(null);
	}

	private StartedModule<MessageModule> started() {
		final ModuleDefinition<MessageModule> md = ModuleDefinition.of(MessageModule.class);
		return md.starter().put("config", config()).start(null);
	}

	/**
	 * Bad provision
	 */
	@Test(expected = NullPointerException.class)
	public void badProvision1() {
		started().getProvision(null);
	}

	/**
	 * Bad provision
	 */
	@Test(expected = IllegalArgumentException.class)
	public void badProvision2() {
		started().getProvision("notFound");
	}

}
