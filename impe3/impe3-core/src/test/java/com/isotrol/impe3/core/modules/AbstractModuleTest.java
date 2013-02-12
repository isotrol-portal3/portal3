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


import java.io.Serializable;
import java.text.CharacterIterator;

import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.Path;
import com.isotrol.impe3.core.config.ConfigurationDefinition;


/**
 * Tests for ModuleDefinition
 * @author Andres Rodriguez
 */
abstract class AbstractModuleTest {
	static MessageConfig config(String text) {
		return ConfigurationDefinition.of(MessageConfig.class).builder().set("text", text).get();
	}

	static MessageConfig config() {
		return config("text");
	}


	/**
	 * Not an interface.
	 */
	static class TestClass implements Module {
	}

	/**
	 * Empty interface.
	 */
	static interface Test00 extends Module {
	}

	/**
	 * Invalid methods.
	 */
	static interface Test01A extends Module {
		Runnable invalid(String a, String b);
	}

	static interface Test01B extends Module {
		void invalid(String a, Integer b, Integer c);
	}

	static interface Base extends Module {
		void iterator(CharacterIterator iterator);
	}

	/**
	 * Empty path annotation.
	 */
	@Path("")
	static interface Test02 extends Base {
	}

	/**
	 * Non local path.
	 */
	@Path("../module.xml")
	static interface Test03 extends Base {
	}

	/**
	 * XML not found.
	 */
	@Path("doesnotexist.xml")
	static interface Test04 extends Base {
	}

	/**
	 * Incorrect XML.
	 */
	@Path("impe3-module-bad.xml")
	static interface Test05 extends Base {
	}

	/**
	 * Provides both a connector and an action.
	 */
	static interface Test07 extends Base {
		Runnable moduleBean();
		String component();
	}

	/**
	 * The provided bean is not found in the module.
	 */
	static interface Test08 extends Base {
		Runnable provides();
	}

	/**
	 * The exported bean is not of the correct type.
	 */
	static interface Test09 extends Base {
		Serializable moduleBean();
	}

	/**
	 * The exported bean is an internal one.
	 */
	static interface Test10 extends Base {
		Serializable moduleBean();
	}

	/**
	 * The module exports both connectors and components/actions..
	 */
	static interface BaseConnector extends Base {
		Runnable moduleBean();
	}

	static interface Test11 extends BaseConnector {
		ComponentBean component();
	}

	/**
	 * Invalid module configuration.
	 */
	static interface TestInvalidConfig extends BaseConnector {
		void config(BadConfig config);
	}

	static interface BadConfig extends Configuration {
		Runnable param();
	}

	/**
	 * A dependency of a non-interface type has been requested.
	 */
	static interface Test15 extends BaseConnector {
		void depends(String argument);
	}

	/**
	 * A forbidden dependency has been requested.
	 */
	static interface Test16B extends BaseConnector {
		void depends(Component argument);
	}

	/**
	 * A duplicate dependency has been requested.
	 */
	static interface Test17 extends BaseConnector {
		void depends(Runnable argument);

		void depends(Serializable argument);
	}

	/**
	 * More than one configuration has been requested.
	 */
	static interface Test18 extends BaseConnector {
		void config1(Config config);

		void config2(Config config);
	}

	static interface Config extends Configuration {
		String param();
	}

	/**
	 * Invalid scope.
	 */
	@Path("bad-scope.xml")
	static interface TestScope01 extends BaseConnector {
	}

	/**
	 * Invalid scope.
	 */
	@Path("bad-scope.xml")
	static interface TestScope02 extends Module {
		ComponentBean component();
	}

}
