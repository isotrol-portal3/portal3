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


import static com.isotrol.impe3.core.modules.ModuleDefinitionLoader.load;

import org.junit.Test;


/**
 * Tests for ModuleLoader
 * @author Andres Rodriguez
 */
public class ModuleDefinitionLoaderTest extends AbstractModuleTest {

	/**
	 * Not an interface.
	 */
	@Test(expected = NonInterfaceModuleException.class)
	public void testClass() throws ModuleException {
		load(TestClass.class);
	}

	/**
	 * Empty interface.
	 */
	@Test(expected = EmptyModuleException.class)
	public void test00() throws ModuleException {
		load(Test00.class);
	}

	/**
	 * Invalid methods.
	 */
	@Test(expected = InvalidMethodsException.class)
	public void test01a() throws ModuleException {
		load(Test01A.class);
	}

	/**
	 * Invalid methods.
	 */
	@Test(expected = InvalidMethodsException.class)
	public void test01b() throws ModuleException {
		load(Test01B.class);
	}

	/**
	 * Empty path annotation.
	 */
	@Test(expected = EmptyPathException.class)
	public void test02() throws ModuleException {
		load(Test02.class);
	}

	/**
	 * Non local path.
	 */
	@Test(expected = NonLocalPathException.class)
	public void test03() throws ModuleException {
		load(Test03.class);
	}

	/**
	 * XML not found.
	 */
	@Test(expected = DefinitionModuleException.class)
	public void test04() throws ModuleException {
		load(Test04.class);
	}

	/**
	 * Incorrect XML.
	 */
	@Test(expected = DefinitionModuleException.class)
	public void test05() throws ModuleException {
		load(Test05.class);
	}

	/**
	 * An action and a connector are provided.
	 */
	//@Test(expected = ProvidesBothException.class)
	public void test07() throws ModuleException {
		load(Test07.class);
	}

	/**
	 * The provided bean is not found in the module.
	 */
	@Test(expected = ProvisionNotFoundException.class)
	public void test08() throws ModuleException {
		load(Test08.class);
	}

	/**
	 * The exported bean is not of the correct type.
	 */
	@Test(expected = InvalidProvisionTypeException.class)
	public void test09() throws ModuleException {
		load(Test09.class);
	}

	/**
	 * The exported bean is an internal one.
	 */
	// @Test(expected = InternalProvisionException.class)
	public void test10() throws ModuleException {
		// load(Test10.class, Key.INTERNAL_PROVISION);
	}

	/**
	 * The module exports both connectors and components/actions..
	 */
	@Test(expected = ProvidesBothException.class)
	public void test11() throws ModuleException {
		load(Test11.class);
	}

	/**
	 * A dependency of a non-interface type has been requested.
	 */
	@Test(expected = NonInterfaceDependsException.class)
	public void test15() throws ModuleException {
		load(Test15.class);
	}

	/**
	 * A forbidden dependency has been requested.
	 */
	@Test(expected = ForbiddenDependsException.class)
	public void test16b() throws ModuleException {
		load(Test16B.class);
	}

	/**
	 * A duplicate dependency has been requested.
	 */
	@Test(expected = DuplicateDependsException.class)
	public void test17() throws ModuleException {
		load(Test17.class);
	}

	/**
	 * More than one configuration has been requested.
	 */
	@Test(expected = DuplicateModuleConfigurationException.class)
	public void test18() throws ModuleException {
		load(Test18.class);
	}

	/**
	 * Invalid module configuration.
	 */
	@Test(expected = ModuleConfigurationException.class)
	public void testInvalidConfig() throws ModuleException {
		load(TestInvalidConfig.class);
	}

	/**
	 * Invalid scope.
	 */
	@Test(expected = InvalidScopeException.class)
	public void testScope01() throws ModuleException {
		load(TestScope01.class);
	}

	/**
	 * Invalid scope.
	 */
	@Test(expected = InvalidScopeException.class)
	public void testScope02() throws ModuleException {
		load(TestScope02.class);
	}
}
