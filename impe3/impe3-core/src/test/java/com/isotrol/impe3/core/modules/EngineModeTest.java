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


import org.junit.Assert;
import org.junit.Test;

import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.modules.Path;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.config.ConfigurationException;


/**
 * Tests for ModuleDefinition
 * @author Andres Rodriguez
 */
public class EngineModeTest {
	/**
	 * Message Service with engine mode
	 */
	@Test
	public void test() throws ConfigurationException, ModuleException {
		final String m = "Hello!";
		final MessageConfig config = ConfigurationDefinition.of(MessageConfig.class).builder().set("text", m).get();
		final ModuleDefinition<MessageWithModeModule> md = ModuleDefinition.of(MessageWithModeModule.class);
		final MessageService service = md.starter().put("config", config).put("mode", EngineMode.OFFLINE).start(null)
			.getModule().service();
		Assert.assertEquals(service.tell(), m);
	}

	@Path("messages.xml")
	private interface MessageWithModeModule extends MessageModule {
		/** Required engine mode. */
		void mode(EngineMode mode);
	}

}
