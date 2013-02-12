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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.portal.PortalNotFoundException;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for portal deletion.
 * @author Andres Rodriguez
 */
public class PortalDeletionTest extends MemoryContextTest {
	private PortalsService service;

	@Before
	public void setUp() {
		service = getBean(PortalsService.class);
	}

	/** Create and delete. */
	@Test
	public void ok() throws PMSException {
		assertTrue(service.getPortals().getChildren().isEmpty());
		String id = loadPortal();
		assertEquals(1, service.getPortals().getChildren().size());
		service.delete(id);
		assertTrue(service.getPortals().getChildren().isEmpty());
	}

	/** Nor found. */
	@Test(expected = PortalNotFoundException.class)
	public void notFound() throws PMSException {
		assertTrue(service.getPortals().getChildren().isEmpty());
		service.delete(UUID.randomUUID().toString());
	}
}
