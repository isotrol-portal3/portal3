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

package com.isotrol.impe3.freemarker;


import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.test.TestContext;
import com.isotrol.impe3.test.TestContextBuilder;
import com.isotrol.impe3.test.TestIABuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Tests for ImpeTemplateLoader
 * @author Andres Rodriguez
 */
public class FreeMarkerModuleTest {
	private static TestContext context;
	private static FileId template;
	private FreeMarkerModule module = null;

	@BeforeClass
	public static void environment() {
		TestContextBuilder b = new TestIABuilder().get();
		template = b.bundle(FreeMarkerModuleTest.class, "test_module.zip");
		context = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		final FreeMarkerConfiguration config = TestSupport.config(FreeMarkerConfiguration.class, "templateBundle",
			template);
		module = context.getModule(FreeMarkerModule.class).start("config", config);
	}

	/**
	 * Date
	 */
	@Test
	public void date() throws Exception {
		final Model model = Model.create();
		model.put("timestamp", String.valueOf(System.currentTimeMillis()));
		HTMLFragment f = module.freeMarkerService().getFragment("test.ftl", context.getRenderContext(), model);
		f.writeTo(System.out, Charset.defaultCharset());
	}
}
