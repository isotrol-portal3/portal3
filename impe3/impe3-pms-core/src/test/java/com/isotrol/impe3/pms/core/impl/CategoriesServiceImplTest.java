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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.category.CategoriesService;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for CategoriesServiceImpl.
 * @author Andres Rodriguez
 */
public class CategoriesServiceImplTest extends MemoryContextTest {
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

	private static void contains(CategoryTreeDTO tree, String id) {
		assertNotNull(tree);
		assertNotNull(tree.getChildren());
		contains(tree.getChildren(), id);
	}

	private static void contains(List<CategoryTreeDTO> categories, String id, int order) {
		assertNotNull(categories);
		assertTrue(categories.size() > order);
		CategoryTreeDTO tree = categories.get(order);
		assertNotNull(tree);
		assertNotNull(tree.getNode());
		assertTrue(id.equals(tree.getNode().getId()));
	}

	private static void contains(CategoryTreeDTO tree, String id, int order) {
		assertNotNull(tree);
		assertNotNull(tree.getChildren());
		contains(tree.getChildren(), id, order);
	}

	private void contains(String node, String id, int order) {
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

	private void loadTree() throws PMSException {
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
		b.append(String.format("[%s] %s" , t.getNode().getId(), t.getNode().getName()));
		System.out.println(b.toString());
		for (CategoryTreeDTO c : t.getChildren()) {
			showTree(c, level+1);
		}
	}
	
	
	@SuppressWarnings("unused")
	private void showTree() {
		showTree(root, 0);
	}

	/** Tree test. 
	 * @throws PMSException */
	@Test
	public void test() throws PMSException {
		final CategoryTreeDTO root = service.getCategories();
		assertNotNull(root);
		check(root.getNode());
	}

	/** Single insert test. */
	@Test
	public void fields() throws PMSException {
		final String name = testString();
		final String path = testString();
		final NameDTO nameDTO = new NameDTO(name, path);
		final CategoryDTO dto = new CategoryDTO();
		dto.setDefaultName(nameDTO);
		dto.setLocalizedNames(new HashMap<String, NameDTO>());
		final CategoryDTO saved = service.create(dto, null, 0);
		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(name, saved.getDefaultName().getDisplayName());
		assertEquals(path, saved.getDefaultName().getPath());
		loadTree();
		contains(root.getChildren(), saved.getId());
	}

	/** Full test. */
	@Test
	public void full() throws PMSException {
		final String id1 = loadCategory(null, 0).getId();
		loadTree();
		contains(root, id1);
		contains(root, id1, 0);
		final String id2 = loadCategory(null, 0).getId();
		loadTree();
		contains(root, id2, 0);
		contains(root, id1, 1);
		final String id3 = loadCategory(null, 2).getId();
		loadTree();
		contains(root, id2, 0);
		contains(root, id1, 1);
		contains(root, id3, 2);
		final String id4 = loadCategory(null, 1).getId();
		loadTree();
		contains(root, id2, 0);
		contains(root, id4, 1);
		contains(root, id1, 2);
		contains(root, id3, 3);
		service.move(id1, null, 0);
		loadTree();
		contains(root, id1, 0);
		contains(root, id2, 1);
		contains(root, id4, 2);
		contains(root, id3, 3);
		service.move(id4, id1, 33);
		loadTree();
		contains(root, id1, 0);
		contains(id1, id4, 0);
		contains(root, id2, 1);
		contains(root, id3, 2);
		final String id5 = loadCategory(id1, 1).getId();
		loadTree();
		contains(id1, id4, 0);
		contains(id1, id5, 1);
	}
	
	/**
	 * Test for {@link CategoriesService#move(String, String, int)}.<br/>
	 * @throws PMSException 
	 */
	@Test
	public void move() throws PMSException {
		String id1 = loadCategory(null, 0).getId();
		/*String id2 = */loadCategory(null, 1)/*.getId()*/;
		String id3 = loadCategory(null, 2).getId();
		String id4 = loadCategory(null, 3).getId();
		String id5 = loadCategory(null, 4).getId();
		loadTree();
		//showTree();
		// append cat3 to cat5:
		service.move(id3, id5, 0);
		loadTree();
		//showTree();
		// insert cat4 at cat5[0]:
		service.move(id4, id5, 0);
		loadTree();
		//showTree();
		// append cat3 to cat1:
		service.move(id3, id1, 0);
		loadTree();
		//showTree();
	}

	/** Insert with locale. */
	@Test
	public void locale() throws PMSException {
		final String id1 = loadCategory(null, 0).getId();
		final CategoryDTO dto = service.get(id1);
		dto.getLocalizedNames().put(Locale.CANADA.toString(), name());
		service.update(dto);
	}
}
