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

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CounterEventDTO;
import com.isotrol.impe3.web20.api.CounterFilterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;
import com.isotrol.impe3.web20.server.CounterManager;
import com.isotrol.impe3.web20.server.CounterMap;
import com.isotrol.impe3.web20.server.CounterMap.Entry;
import com.isotrol.impe3.web20.server.CounterMap.Key;


/**
 * Tests for CounterManagerImplTest.
 * @author Andres Rodriguez
 */
public class CounterManagerImplTest extends AbstractCounterTest {
	private CounterManager service;

	@Before
	public void setUp() {
		service = getBean(CounterManager.class);
		service.setBreakdown(true);
	}

	@Test
	public void test() {
		assertNotNull(service);
	}
	
	private void check(CounterMap map, Key key) {
		for (Entry e : map.get(key, Integer.MAX_VALUE)) {
			assertEquals(e.getCount(), map.getResource(key, e.getResourceId()));
		}
		
	}

	@Test
	public void register() throws ServiceException {
		final CounterEventDTO dto = event(RESOURCE1);
		service.register(dto);
		CounterMap map = service.loadTimeMap(null);
		assertEquals(2L, map.size());
		CounterFilterDTO filter = filter();
		Key key = service.getKey(filter);
		assertNotNull(key);
		List<Entry> entries = map.get(key, 10);
		assertNotNull(entries);
		assertEquals(1, entries.size());
		List<ResourceCounterDTO> resources = service.getResources(entries);
		assertNotNull(resources);
		assertEquals(1, resources.size());
		ResourceCounterDTO rc = resources.get(0);
		assertEquals(1, rc.getCount());
		assertEquals(RESOURCE1, rc.getResource());
		check(map, key);
		// Second resource
		dto.setResource(RESOURCE2);
		service.register(dto);
		map = service.loadTimeMap(100000L);
		assertEquals(4L, map.size());
		check(map, key);
	}
}
