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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.RatingEventDTO;
import com.isotrol.impe3.web20.server.RatingManager;
import com.isotrol.impe3.web20.server.RatingMap;
import com.isotrol.impe3.web20.server.RatingMap.Entry;


/**
 * Tests for RatingManagerImplTest.
 * @author Andres Rodriguez
 */
public class RatingManagerImplTest extends AbstractRatingTest {
	private RatingManager service;

	@Before
	public void setUp() {
		service = getBean(RatingManager.class);
	}

	@Test
	public void test() {
		assertNotNull(service);
	}

	private void check(RatingMap map, Long groupId) {
		for (Entry e : map.get(groupId, Integer.MAX_VALUE)) {
			assertEquals(e.getCount(), map.getResource(groupId, e.getResourceId()).getCount());
		}
	}

	@Test
	public void register() throws ServiceException {
		final RatingEventDTO dto = event(RESOURCE1, 2);
		service.register(dto, true);
		RatingMap map = service.loadTimeMap(null);
		assertEquals(2L, map.size());
		check(map, null);
		List<Entry> entries = map.get(null, 10);
		assertNotNull(entries);
		assertFalse(entries.isEmpty());
		assertTrue(entries.size() == 1);
		Entry e = entries.get(0);
		assertEquals(2, e.getMin());
		assertEquals(2, e.getMax());
		assertEquals(2.0, e.getMean(), 0.1);
		service.register(event(RESOURCE1, 4), true);
		map = service.loadTimeMap(100000L);
		assertEquals(4L, map.size());
		check(map, null);
		entries = map.get(null, 10);
		assertNotNull(entries);
		assertFalse(entries.isEmpty());
		assertTrue(entries.size() == 1);
		e = entries.get(0);
		assertEquals(2, e.getMin());
		assertEquals(4, e.getMax());
		assertEquals(3.0, e.getMean(), 0.1);
	}

}
