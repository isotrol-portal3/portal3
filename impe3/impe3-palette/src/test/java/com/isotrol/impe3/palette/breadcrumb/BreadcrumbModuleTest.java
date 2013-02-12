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

package com.isotrol.impe3.palette.breadcrumb;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Tests for IFrameModule.
 * @author Andres Rodriguez Chamorro
 */
public class BreadcrumbModuleTest {
	private static TestEnvironment environment;
	private static ModuleTester<BreadcrumbModule> tester;
	private static BreadcrumbModule module;
	private static Category level2;
	private static ContentType type;

	@BeforeClass
	public static void setUp() {
		final BreadcrumbConfig config = TestSupport.builder(BreadcrumbConfig.class).set("includePortal", Boolean.TRUE)
			.set("generateTitle", Boolean.FALSE).set("withCategoryNav", Boolean.TRUE).set("withContentTypeNav",
				Boolean.TRUE).set("withCategoryDetail", Boolean.TRUE).set("withContentTypeDetail", Boolean.TRUE).set(
				"removeLastURI", Boolean.FALSE).set("templateFile", "file.ftl").get();
		TestEnvironmentBuilder teb = new TestEnvironmentBuilder();
		Category root = teb.category("Root", null);
		Category level1 = teb.category("Level 1", root.getId());
		type = teb.contentType("Type");
		level2 = teb.category("Level 2", level1.getId());
		environment = teb.get();
		tester = environment.getModule(BreadcrumbModule.class);
		module = tester.start("config", config);
	}

	/** Manual. */
	@Test
	public void manual() {
		ManualBreadcrumbComponent manual = module.manual();
		manual.setPortal(environment.getPortal());
		manual.execute();
		System.out.println(manual.getBreadcrumb());
	}

	/** Navigation. */
	@Test
	public void navigation() {
		NavigationBreadcrumbComponent c = module.navigation();
		c.setNavigationKey(NavigationKey.category(level2));
		c.setPortal(environment.getPortal());
		c.execute();
		final Breadcrumb b = c.getBreadcrumb();
		Assert.assertNotNull(b);
		Assert.assertFalse(b.isEmpty());
		System.out.println(b);
	}

	/** Content. */
	@Test
	public void content() {
		ContentBreadcrumbComponent c = module.content();
		c.setNavigationKey(NavigationKey.category(level2));
		c.setContentKey(ContentKey.of(type, "id"));
		c.setPortal(environment.getPortal());
		c.execute();
		final Breadcrumb b = c.getBreadcrumb();
		Assert.assertNotNull(b);
		Assert.assertFalse(b.isEmpty());
		System.out.println(b);
	}
	
}
