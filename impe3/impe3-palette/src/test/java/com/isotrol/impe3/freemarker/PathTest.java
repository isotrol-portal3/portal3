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


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;


/**
 * Tests for ImpeTemplateLoader
 * @author Andres Rodriguez
 */
public class PathTest {
	private static TestEnvironment environment;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		Category c = b.category("root", null);
		Category c1 = b.category("c1", c.getId());
		Category c2 = b.category("c2", null);
		b.category("c11", c1.getId());
		environment = b.get();
	}
	
	private void yes(String path, boolean withRoot) {
		final Categories c = environment.getCategories();
		assertNotNull(c.getByPath(path, false, withRoot));
		assertNull(c.getByPath(path, false, !withRoot));
	}

	private void no(String path) {
		final Categories c = environment.getCategories();
		assertNull(c.getByPath(path, false, true));
		assertNull(c.getByPath(path, false, false));
	}
	
	/**
	 * Test
	 */
	@Test
	public void path() throws Exception {
		final Categories c = environment.getCategories();
		yes("root", true);
		yes("/root", true);
		yes("/root/", true);
		yes("root/", true);
		yes("root/c1", true);
		yes("/root/c1", true);
		yes("/c1", false);
		yes("/root/c1/c11", true);
		yes("/root/c1/c11/", true);
		yes("root/c1/c11/", true);
		yes("/c1/c11/", false);
		yes("c1/c11", false);
		yes("c1/c11/", false);
		yes("/c1/c11", false);
		no("c2");
	}
}
