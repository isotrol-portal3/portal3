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
package com.isotrol.impe3.web20.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.UsedTagDTO;
import com.isotrol.impe3.web20.server.TagManager;
import com.isotrol.impe3.web20.server.TagMap;


/**
 * Tests for TagManagerImplTest.
 * @author Andres Rodriguez
 */
public class TagManagerImplTest extends AbstractTagTest {
	private TagManager service;

	@Before
	public void setUp() {
		service = getBean(TagManager.class);
	}

	@Test
	public void test() {
		assertNotNull(service);
	}

	private void check(int total, String set, long... sizes) {
		TagMap map = service.loadAll();
		assertNotNull(map);
		assertEquals(total, map.size());
		final long setId = service.getSet(set);
		List<UsedTagDTO> used = map.get(setId, 10000);
		assertEquals(sizes.length, used.size());
		for (int i = 0; i < sizes.length; i++) {
			UsedTagDTO dto = used.get(i);
			assertNotNull(dto);
			assertEquals(sizes[i], dto.getCount());
		}
	}

	@Test
	public void tag() throws ServiceException {
		service.tag(RESOURCE1, SET1, ImmutableSet.of(TAG1), true);
		check(1, SET1, 1);
		// /////////////////////////////////////////////////
		service.addTag(SET1, TAG2, false);
		check(1, SET1, 1);
		service.addTag(SET1, TAG2, true);
		check(1, SET1, 1, 0);
		service.tag(RESOURCE2, SET1, ImmutableSet.of(TAG2), true);
		check(2, SET1, 1, 1);
		service.updateTag(SET1, TAG2, TAG3);
		check(2, SET1, 1, 1);
		service.deleteTag(SET1, TAG3);
		check(1, SET1, 1);
		// /////////////////////////////////////////////////
		service.tag(RESOURCE1, SET1, ImmutableSet.of(TAG1, TAG2, TAG1_1), true);
		check(3, SET1, 1, 1, 1);
		List<UsedTagDTO> used = service.loadAll().suggest(service.getSet(SET1), TAG1, 10);
		assertEquals(2, used.size());
		for (UsedTagDTO u : used) {
			assertNotNull(u);
			assertNotNull(u.getName());
			assertTrue(u.getName().startsWith(TAG1));
		}
	}

}
