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

package com.isotrol.impe3.pms.core.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.category.CategoriesService;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for CategoriesServiceImpl.
 * @author Andres Rodriguez
 */
public class CategoryEditionTest extends AbstractCategoryTest {
	/** Edition. */
	@Test
	public void edition() throws PMSException {
		final String id1 = loadCategory(null, 0).getId();
		final String id11 = loadCategory(id1, 0).getId();
		final String id111 = loadCategory(id11, 0).getId();
		CategoryDTO dto1 = get(id1);
		CategoryDTO dto11 = get(id11);
		CategoryDTO dto111 = get(id111);
		assertEquals(State.NEW, dto1.getState());
		assertEquals(State.NEW, dto11.getState());
		assertEquals(State.NEW, dto111.getState());
		loadTree();
		contains(id1, id11, 0);
		contains(id11, id111, 0);
		publish();
		dto1 = get(id1);
		dto11 = get(id11);
		dto111 = get(id111);
		assertEquals(State.PUBLISHED, dto1.getState());
		assertEquals(State.PUBLISHED, dto11.getState());
		assertEquals(State.PUBLISHED, dto111.getState());
		dto1.getDefaultName().setDisplayName(testString());
		dto1 = update(dto1);
		assertEquals(State.MODIFIED, dto1.getState());
		dto11 = get(id11);
		dto111 = get(id111);
		assertEquals(State.PUBLISHED, dto11.getState());
		assertEquals(State.PUBLISHED, dto111.getState());
		loadTree();
		contains(id1, id11, 0);
		contains(id11, id111, 0);
		dto11.getDefaultName().setDisplayName(testString());
		dto11 = update(dto11);
		dto1 = get(id1);
		dto111 = get(id111);
		assertEquals(State.MODIFIED, dto1.getState());
		assertEquals(State.MODIFIED, dto11.getState());
		assertEquals(State.PUBLISHED, dto111.getState());
		loadTree();
		contains(id1, id11, 0);
		contains(id11, id111, 0);
	}

}
