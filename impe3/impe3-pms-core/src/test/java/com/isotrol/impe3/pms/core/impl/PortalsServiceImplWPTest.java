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


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.pms.core.MemoryContextTest.PageLoader.Page;


/**
 * Tests for PortalsServiceImpl with publication.
 * @author Andres Rodriguez
 */
public class PortalsServiceImplWPTest extends MemoryContextTest {
	private PortalsService service;

	@Before
	public void setUp() {
		service = getBean(PortalsService.class);
	}

	/** Save new, publish and update. */
	@Test
	public void saveNewTemplate() throws PMSException {
		final PortalPagesLoc loc = loadPortalWithPalette();
		final PageLoader loader = new PageLoader(loc.getPortalId());
		final Page page = loader.create(PageClass.SPECIAL);
		page.setName(testString());
		page.putComponents();
		page.save();
		page.layout();
		publish();
		final PortalNameDTO t1 = service.getName(loc.getPortalId());
		t1.setName(name());
		service.setName(t1);
		final PortalNameDTO t2 = service.getName(loc.getPortalId());
		Assert.assertNotNull(t2);
		Assert.assertNotNull(t2.getId());
		Assert.assertNotNull(t2.getName());
	}
}
