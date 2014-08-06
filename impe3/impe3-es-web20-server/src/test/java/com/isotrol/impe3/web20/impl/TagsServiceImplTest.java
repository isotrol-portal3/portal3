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

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.TagsService;


/**
 * Tests for TagsServiceImplTest.
 * @author Andres Rodriguez
 */
public class TagsServiceImplTest extends AbstractTagTest {
	private TagsService service;

	@Before
	public void setUp() {
		service = getBean(TagsService.class);
	}

	@Test
	public void test() {
		assertNotNull(service);
	}

	@Test
	public void tag() throws ServiceException {
		service.tag(null, RESOURCE1, SET1, ImmutableSet.of(TAG1), true);
		assertNotNull(service.getMostUsed(null, SET1, 10));
		assertNotNull(service.suggest(null, SET1, TAG1, 10));
		assertEquals(1, service.getTagSet(null, SET1).size());
		service.addTag(null, SET1, TAG2, false);
		assertEquals(2, service.getTagSet(null, SET1).size());
		service.updateTag(null, SET1, TAG2, TAG1_1);
		assertEquals(2, service.getTagSet(null, SET1).size());
		service.addTag(null, SET1, TAG3, false);
		assertEquals(3, service.getTagSet(null, SET1).size());
		service.deleteTag(null, SET1, TAG1_1);
		assertEquals(2, service.getTagSet(null, SET1).size());
	}

}
