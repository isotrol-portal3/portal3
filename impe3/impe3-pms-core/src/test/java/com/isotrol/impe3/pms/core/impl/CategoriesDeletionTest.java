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


import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.category.CategoriesService;
import com.isotrol.impe3.pms.api.category.CategoryInUseException;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Category deletion tests.
 * @author Andres Rodriguez
 */
public class CategoriesDeletionTest extends MemoryContextTest {
	private CategoriesService service;

	@Before
	public void setUp() {
		service = getBean(CategoriesService.class);
	}

	private void error(String id) throws PMSException {
		boolean ok = false;
		try {
			service.delete(id);
		} catch (CategoryInUseException e) {
			ok = true;
		}
		assertTrue(ok);
	}

	/** Tree test. */
	@Test
	public void test() throws PMSException {
		final CategoryTreeDTO root = service.getCategories();
		error(root.getNode().getId());
		String c1 = loadCategory(null, 0).getId();
		String c1_1 = loadCategory(c1, 0).getId();
		String c1_1_1 = loadCategory(c1_1, 0).getId();
		error(c1);
		error(c1_1);
		error(service.getCategories().getNode().getId());
		service.delete(c1_1_1);
		error(c1);
		error(service.getCategories().getNode().getId());
		service.delete(c1_1);
		service.delete(c1);
		error(service.getCategories().getNode().getId());
	}
}
