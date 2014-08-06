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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.ResourcesService;

public class ResourceServiceTest extends MemoryContextTest {

	private ResourcesService service;
	private String serviceId = "test_service_id";
	
	@Before
	public void setUp() {
		service = getBean(ResourcesService.class);
	}

	@Test
	public void testActivate() throws ServiceException {
		final String resourceKey = "00-00";
		final String id1 = service.activate(serviceId, resourceKey);
		final String id2 = service.activate(serviceId, resourceKey);
		
		Assert.assertEquals(id1, id2);
		
	}
	
}
