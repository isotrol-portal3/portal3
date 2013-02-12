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

package com.isotrol.impe3.test;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileId;


/**
 * Test for Test Environment.
 * @author Andres Rodriguez
 */
public class EnvironmentTest {
	private static final String FILE = "test.png";
	private final TestEnvironmentBuilder b = new TestEnvironmentBuilder();
	
	/** Content types. */
	@Test
	public void types() {
		ContentType type = b.contentType("type");
		ContentTypes types = b.get().getContentTypes();
		assertNotNull(types);
		assertTrue(types.containsKey(type.getId()));
		assertTrue(types.containsValue(type));
	}

	/** Categories. */
	@Test
	public void categories() {
		Category c = b.category("root", null);
		Categories categories = b.get().getCategories();
		assertNotNull(categories);
		assertTrue(categories.containsKey(c.getId()));
		assertTrue(categories.containsValue(c));
	}
	
	/** Files. */
	@Test
	public void file() {
		final FileId file = b.file(getClass(), FILE);
		assertNotNull(file);
		final FileData data = b.get().getFileLoader().load(file);
		assertNotNull(data);
	}

}
