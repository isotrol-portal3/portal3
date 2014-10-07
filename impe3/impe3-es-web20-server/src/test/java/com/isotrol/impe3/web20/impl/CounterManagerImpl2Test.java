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
import com.isotrol.impe3.web20.api.ResourceCounterDTO;
import com.isotrol.impe3.web20.server.CounterManager;
import com.isotrol.impe3.web20.server.CounterMap;


/**
 * Tests for CounterManagerImplTest.
 * @author Andres Rodriguez
 */
public class CounterManagerImpl2Test extends AbstractCounterTest {
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
	
	@Test
	public void aggregation() throws ServiceException {
		for (int i = 0; i < 100; i++) {
			service.register(event(RESOURCE1));
		}
		for (int i = 0; i < 50; i++) {
			service.register(event(RESOURCE2));
		}
		for (int i = 0; i < 10; i++) {
			service.register(event(RESOURCE1, AGG1));
		}
		for (int i = 0; i < 20; i++) {
			service.register(event(RESOURCE2, AGG1));
		}
		final CounterMap map = service.loadTimeMap(null);
		List<ResourceCounterDTO> resources = service.getResources(map.get(service.getKey(filter()), 10));
		assertEquals(2, resources.size());
		assertEquals(RESOURCE1, resources.get(0).getResource());
		assertEquals(RESOURCE2, resources.get(1).getResource());
		resources = service.getResources(map.get(service.getKey(filter(AGG1)), 10));
		assertEquals(2, resources.size());
		assertEquals(RESOURCE2, resources.get(0).getResource());
		assertEquals(RESOURCE1, resources.get(1).getResource());
		assertEquals(20L, resources.get(0).getCount());
		assertEquals(10L, resources.get(1).getCount());
	}
	
}
