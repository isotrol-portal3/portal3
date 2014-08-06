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


import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CounterEventDTO;
import com.isotrol.impe3.web20.api.CounterFilterDTO;
import com.isotrol.impe3.web20.api.CountersService;


/**
 * Tests for CounterManagerImplTest.
 * @author Andres Rodriguez
 */
public class CountersServiceImplTest extends AbstractCounterTest {
	private CountersService service;

	@Before
	public void setUp() {
		service = getBean(CountersService.class);
	}

	@Test
	public void test() {
		assertNotNull(service);
	}

	@Test
	public void register() throws ServiceException {
		CounterFilterDTO filter = filter();
		assertNotNull(service.getGreatestHits(null, filter, 1000));
		final CounterEventDTO dto = event(RESOURCE1);
		service.register(null, dto, true);
		service.register(null, dto, true);
		service.register(null, dto, true);
		assertNotNull(service.getGreatestHits(null, filter, 1000));
	}

}
