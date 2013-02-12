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


import org.junit.Test;

import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.component.DuplicateComponentConfigurationException;
import com.isotrol.impe3.core.config.EmptyConfigurationException;
import com.isotrol.impe3.core.support.ImpeExceptionTest;


/**
 * Tests for ModuleDefinition
 * @author Andres Rodriguez
 */
public class ModuleExceptionTest extends ImpeExceptionTest {
	private static final String BEAN = "bean";

	private static interface TestModule extends Module {
	}

	/**
	 * Exception messages.
	 */
	@Test
	public void testMessages() {
		testMessage(new NonInterfaceModuleException(TestModule.class));
		testMessage(new EmptyModuleException(TestModule.class));
		testMessage(new InvalidMethodsException(TestModule.class));
		testMessage(new EmptyPathException(TestModule.class));
		testMessage(new NonLocalPathException(TestModule.class, "../module.xml"));
		testMessage(new DefinitionModuleException(TestModule.class, "test.xml", new NullPointerException("test")));
		testMessage(new InvalidProvidesException(TestModule.class, BEAN));
		testMessage(new InvalidScopeException(TestModule.class, BEAN));
		testMessage(new NonInterfaceProvidesException(TestModule.class, BEAN, String.class));
		testMessage(new ProvisionNotFoundException(TestModule.class, BEAN));
		testMessage(new InvalidProvisionTypeException(TestModule.class, BEAN, String.class));
		testMessage(new ForbiddenProvisionException(TestModule.class, BEAN));
		testMessage(new ProvidesBothException(TestModule.class));
		testMessage(new InvalidDependsException(TestModule.class, BEAN));
		testMessage(new NonInterfaceDependsException(TestModule.class, BEAN, String.class));
		testMessage(new ForbiddenDependsException(TestModule.class, BEAN, String.class));
		testMessage(new DuplicateDependsException(TestModule.class, BEAN));
		testMessage(new DuplicateModuleConfigurationException(TestModule.class));
		testMessage(new ModuleConfigurationException(TestModule.class,
			new EmptyConfigurationException(TestModule.class)));
		testMessage(new InvalidComponentException(TestModule.class, BEAN, new DuplicateComponentConfigurationException(
			Component.class)));
	}
}
