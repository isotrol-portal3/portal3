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


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;

import com.google.common.collect.Maps;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.category.CategoriesService;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Abstract test for categories.
 * @author Andres Rodriguez
 */
public abstract class AbstractCategoryTest extends MemoryContextTest {
	private CategoriesService service;
	private CategoryTreeDTO root;
	private Map<String, CategoryTreeDTO> map;

	private void check(CategorySelDTO dto) {
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertNotNull(dto.getName());
		assertNotNull(dto.getName());
	}

	private static void contains(Iterable<CategoryTreeDTO> categories, String id) {
		for (CategoryTreeDTO node : categories) {
			if (id.equals(node.getNode().getId())) {
				return;
			}
		}
		assertTrue(false);
	}

	static void contains(CategoryTreeDTO tree, String id) {
		assertNotNull(tree);
		assertNotNull(tree.getChildren());
		contains(tree.getChildren(), id);
	}

	static void contains(List<CategoryTreeDTO> categories, String id, int order) {
		assertNotNull(categories);
		assertTrue(categories.size() > order);
		CategoryTreeDTO tree = categories.get(order);
		assertNotNull(tree);
		assertNotNull(tree.getNode());
		assertTrue(id.equals(tree.getNode().getId()));
	}

	static void contains(CategoryTreeDTO tree, String id, int order) {
		assertNotNull(tree);
		assertNotNull(tree.getChildren());
		contains(tree.getChildren(), id, order);
	}

	void contains(String node, String id, int order) {
		final CategoryTreeDTO dto = map.get(node);
		contains(dto, id, order);
	}

	@Before
	public void setUp() {
		service = getBean(CategoriesService.class);
	}

	private void loadTree(CategoryTreeDTO dto) {
		map.put(dto.getNode().getId(), dto);
		for (CategoryTreeDTO child : dto.getChildren()) {
			loadTree(child);
		}
	}

	final void loadTree() throws PMSException {
		map = Maps.newHashMap();
		root = service.getCategories();
		loadTree(root);
	}

	private void showTree(CategoryTreeDTO t, int level) {
		final StringBuilder b = new StringBuilder();
		if (level > 0) {
			for (int i = 0; i < level; i++) {
				b.append("--");
			}
			b.append('>');
		}
		b.append(String.format("[%s] %s", t.getNode().getId(), t.getNode().getName()));
		System.out.println(b.toString());
		for (CategoryTreeDTO c : t.getChildren()) {
			showTree(c, level + 1);
		}
	}

	final void showTree() {
		showTree(root, 0);
	}

	final CategoryDTO get(String id) throws PMSException {
		return service.get(id);
	}
	
	final CategoryDTO update(CategoryDTO dto) throws PMSException {
		return service.update(dto);
	}

}
