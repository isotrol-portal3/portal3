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

package com.isotrol.impe3.samples.calculator;


import org.junit.BeforeClass;

import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;


/**
 * Default environment for Calculator Tests
 * @author Andres Rodriguez
 */
public abstract class AbstractCalculatorTest {
	static TestEnvironment environment;
	private ModuleTester<CalculatorConnectorModule> module = null;
	private CalculatorService service = null;

	@BeforeClass
	public static void createEnvironment() {
		environment = new TestEnvironmentBuilder().get();
	}
	
	protected ModuleTester<CalculatorConnectorModule> getServiceModule() {
		if (module == null) {
			module = environment.getModule(CalculatorConnectorModule.class);
		}
		return module;
	}

	protected CalculatorService getService() {
		if ( service == null) {
			service = getServiceModule().start().service();
		}
		return service;
	}
}
