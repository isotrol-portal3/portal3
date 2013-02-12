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

package com.isotrol.impe3.palette.redirect;


import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;


/**
 * Redirection Module Test.
 * @author Andres Rodriguez
 */
public class RedirectModuleTest {
	private static TestEnvironment environment;
	private ModuleTester<RedirectModule> module = null;
	private ComponentTester<ToCategoryComponent> category = null;
	private ComponentTester<ToContentTypeComponent> contentType = null;
	private ComponentTester<ToURIComponent> uri = null;
	private ComponentTester<ToSpecialComponent> special = null;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		environment = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		module = environment.getModule(RedirectModule.class);
		module.start();
		category = module.getComponent(ToCategoryComponent.class, "toCategory");
		contentType = module.getComponent(ToContentTypeComponent.class, "toContentType");
		uri = module.getComponent(ToURIComponent.class, "toURI");
		special = module.getComponent(ToSpecialComponent.class, "toSpecial");
	}

	private void check(RedirectComponent c) {
		assertNotNull(c.getUriGenerator());
		assertNotNull(c.getContentTypes());
		assertNotNull(c.getUriGenerator());
	}

	@Test
	public void ok() {
		check(category.getComponent());
		check(contentType.getComponent());
		check(uri.getComponent());
		check(special.getComponent());
		category.executeOk();
		contentType.executeOk();
		uri.getComponent().execute();
		special.getComponent().execute();
	}
}
