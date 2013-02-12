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

package com.isotrol.impe3.palette.freemarker;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestContext;
import com.isotrol.impe3.test.TestContextBuilder;
import com.isotrol.impe3.test.TestIABuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Generate XML using FreeMarker Template Component Test.
 * @author Andres Rodriguez
 */
public class XMLComponentTest {
	private static TestContext context;
	private static FileId template;
	private ModuleTester<XMLModule> module = null;
	private ComponentTester<XMLComponent> tester = null;

	@BeforeClass
	public static void environment() {
		TestContextBuilder b = new TestIABuilder().get();
		template = b.bundle(XMLComponentTest.class, "testxml.zip");
		context = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		final XMLModuleConfig config = TestSupport.builder(XMLModuleConfig.class).set("templateBundle", template)
			.set("templateFile", "test.ftl").set("parse", true).get();
		module = context.getModule(XMLModule.class);
		module.start("config", config);
		tester = module.getComponent(XMLComponent.class, "component");
	}

	@Test
	public void execute() {
		tester.executeOk();
	}
}
