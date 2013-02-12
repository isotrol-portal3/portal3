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


import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.edition.EditionsService;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypesService;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for EditionsServiceImpl.
 * @author Andres Rodriguez
 */
public class EditionsServiceImplTest extends MemoryContextTest {
	private static EditionsService service;

	/**
	 * Set up.
	 * @throws PMSException
	 */
	@BeforeClass
	public static void setUp() throws PMSException {
		service = getBean(EditionsService.class);
	}

	/** Lastest. */
	@Test
	public void lastest() throws PMSException {
		assertNotNull(service.getLastEditions());
	}

	private int size() throws PMSException {
		return service.getLastEditions().size();
	}

	/** Publish. */
	@Test
	public void publishTest() throws PMSException {
		ContentTypeDTO ct1 = loadContentType();
		assertEquals(State.NEW, ct1.getState());
		publish();
		int n1 = size();
		assertTrue(n1 > 0);
		ct1 = getBean(ContentTypesService.class).get(ct1.getId());
		assertEquals(State.PUBLISHED, ct1.getState());
		ct1.setDescription(testString());
		ct1 = getBean(ContentTypesService.class).save(ct1);
		assertEquals(State.MODIFIED, ct1.getState());
		publish();
		int n2 = size();
		assertTrue(n2 == n1 + 1);
		publish();
		int n3 = size();
		assertTrue(n2 == n3);
	}

}
