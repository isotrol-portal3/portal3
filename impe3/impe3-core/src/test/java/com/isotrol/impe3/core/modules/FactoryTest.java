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


import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.Path;
import com.isotrol.impe3.core.config.ConfigurationException;


/**
 * Tests for for modules using factory beans and factory methods.
 * @author Andres Rodriguez
 */
public class FactoryTest {

	private void test(Class<? extends RunnableModule> klass) throws ModuleException {
		assertNotNull(ModuleDefinition.of(klass).starter().start(null).getModule().service());
	}

	/** Factory bean. */
	@Test
	public void bean() throws ConfigurationException, ModuleException {
		test(FactoryBeanModule.class);
	}

	/** Factory method. */
	//@Test
	public void method() throws ConfigurationException, ModuleException {
		test(FactoryMethodModule.class);
	}

	interface RunnableModule extends Module {
		Runnable service();
	}

	@Path("factory-bean.xml")
	interface FactoryBeanModule extends RunnableModule {
	}

	@Path("factory-method.xml")
	interface FactoryMethodModule extends RunnableModule {
	}

}
