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

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.web20.server.RatingManager;
import com.isotrol.impe3.web20.server.RatingMap;
import com.isotrol.impe3.web20.server.RatingMap.Entry;


/**
 * Tests for RatingManagerImplTest.
 * @author Andres Rodriguez
 */
public class RatingManagerImpl2Test extends AbstractRatingTest {
	private RatingManager service;

	@Before
	public void setUp() {
		service = getBean(RatingManager.class);
	}

	private void check(RatingMap map, Long groupId) {
		for (Entry e : map.get(groupId, Integer.MAX_VALUE)) {
			assertEquals(e.getCount(), map.getResource(groupId, e.getResourceId()).getCount());
		}
	}

	@Test
	public void register() throws Exception {
		service.register(event(RESOURCE1, 2, AGG1), true);
		service.register(event(RESOURCE1, 4, AGG1), true);
		Long groupId = service.getGroupId(filter(AGG1));
		RatingMap map = service.loadTimeMap(null);
		List<Entry> entries = map.get(groupId, 10);
		assertEquals(6L, map.size());
		check(map, groupId);
	}

}
