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

package com.isotrol.impe3.pms.core.obj;


import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.EnvironmentManager;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.pms.core.engine.EngineModelLoader;


/**
 * Tests for CategoriesObject.
 * @author Andres Rodriguez
 */
public class CategoriesObjectTest extends MemoryContextTest {
	@Test
	public void test() throws PMSException {
		String id1 = loadCategory(null, 0).getId();
		loadCategory(id1, 0).getId();
		Categories c = getBean(EngineModelLoader.class).getOffline(EnvironmentManager.NAME).getCategories();
		assertEquals(3, c.size());
		assertEquals(1, c.getFirstLevel().size());
		UUID l1k = c.getFirstLevel().get(0).getId();
		assertEquals(1, c.getChildren(l1k).size());
		UUID l2k = c.getChildren(l1k).get(0).getId();
		assertEquals(1, c.getChildren(l2k).size());
	}
}