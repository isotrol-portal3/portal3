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

package com.isotrol.impe3.palette.html;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * HTML Module tests.
 * @author Andres Rodriguez
 */
public class HTMLModuleTest {
	private static TestEnvironment environment;
	private static FileId bundle;
	private static FileId file;
	private ModuleTester<HTMLModule> module = null;
	private ComponentTester<StyleComponent> div = null;
	private ComponentTester<HTMLComponent> html = null;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		bundle = b.bundle(HTMLModuleTest.class, "test.zip");
		file = b.bundle(HTMLModuleTest.class, "test.html");
		environment = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		/*
		 * final JSConfig config = TestSupport.builder(JSConfig.class).set("bundle", bundle).set("headerPath",
		 * "jquery-1.3.2.min.js").get(); module = environment.getModule(JSModule.class); module.start("config", config);
		 * tester = module.getComponent(JSComponent.class, "component");
		 */

	}

	@Test
	public void div() {
		module = environment.getModule(HTMLModule.class);
		module.start("config", TestSupport.builder(HTMLModuleConfig.class).get());
		div = module.getComponent(StyleComponent.class, "style");
		div.editAndRenderHTML();
		div.getComponent().setConfig(TestSupport.builder(StyleConfig.class).set("classAtt", "style").get());
		div.editAndRenderHTML();
	}
	
	@Test
	public void htmlFile() {
		module = environment.getModule(HTMLModule.class);
		module.start("config", TestSupport.builder(HTMLModuleConfig.class).get());
		html = module.getComponent(HTMLComponent.class, "html");
		html.getComponent().setHtmlConfig(TestSupport.builder(HTMLConfig.class).set("file", file).get());
		html.editAndRenderHTML();
	}

	@Test
	public void htmlBundle() {
		module = environment.getModule(HTMLModule.class);
		module.start("config", TestSupport.builder(HTMLModuleConfig.class).set("bundle", bundle).get());
		html = module.getComponent(HTMLComponent.class, "html");
		html.getComponent().setHtmlConfig(TestSupport.builder(HTMLConfig.class).set("path", "test.html").get());
		html.editAndRenderHTML();
	}

}
