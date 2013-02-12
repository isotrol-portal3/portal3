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

package com.isotrol.impe3.users.connector;


import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;


/**
 * Tests for Hessian Users Connector Module.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public class HessianModuleTest {
	private static TestEnvironment environment;
	private static ModuleTester<HessianModule> tester;

	@BeforeClass
	public static void setUp() {
		// environment = new TestEnvironmentBuilder().get();
		// tester = environment.getModule(JndiModule.class);
	}

	/** Module definition. */
	@Test
	public void module() {
		@SuppressWarnings("unused")
		final ModuleDefinition<HessianModule> md = ModuleDefinition.of(HessianModule.class);
	}

	/** Start. */
	@Test
	public void start() {
		// tester.start("config", TestSupport.config(ScheduledExecutorConfig.class, "poolSize", 3));
	}

}
