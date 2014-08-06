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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.RatingEventDTO;
import com.isotrol.impe3.web20.api.RatingsService;
import com.isotrol.impe3.web20.api.SourceDTO;


/**
 * Tests for RatingsServiceImplTest.
 * @author Andres Rodriguez
 */
public class RatingsServiceImplTest extends AbstractRatingTest {
	private RatingsService service;

	@Before
	public void setUp() {
		service = getBean(RatingsService.class);
	}

	@Test
	public void test() {
		assertNotNull(service);
	}

	@Test
	public void register() throws ServiceException {
		assertNotNull(service.getBestRated(null, filter(), 1000));
		final RatingEventDTO dto = event(RESOURCE1, 1);
		service.register(null, dto, true, true);
		service.register(null, dto, true, true);
		service.register(null, dto, true, true);
		assertNotNull(service.getBestRated(null, filter(), 1000));
	}
/*
	@Test
	public void rating() throws ServiceException {
		final SourceDTO source = new SourceDTO();
		source.setOrigin("guest:192.168.109.121");
		final RatingEventDTO dto = event(RESOURCE1, 1);
		dto.setSource(source);
		service.register(null, dto, false, false);
		
		boolean rated = service.isRated(null, dto);
		
		Assert.assertTrue(rated);
		
		final SourceDTO source2 = new SourceDTO();
		source2.setOrigin("guest:192.168.109.122");
		dto.setSource(source2);
		
		boolean notRated = service.isRated(null, dto);
		
		Assert.assertTrue(!notRated);
	}
*/	
}
