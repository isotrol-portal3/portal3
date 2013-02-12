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
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PagesService;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.pms.core.MemoryContextTest.PageLoader.Page;


/**
 * More tests for page templates.
 * @author Andres Rodriguez
 */
public class Pages2Test extends MemoryContextTest {
	private static PagesService service;
	private static PortalPagesLoc portal;
	private static PageLoader loader;

	/**
	 * Set up.
	 * @throws PMSException
	 */
	@BeforeClass
	public static void setUp() throws PMSException {
		service = getBean(PagesService.class);
		portal = loadPortalWithPalette();
		loader = new PageLoader(portal.getPortalId());
	}

	private void check(Collection<?> list, int size) {
		assertNotNull(list);
		assertEquals(size, list.size());
	}

	/** Test. */
	@Test
	public void templates() throws PMSException {
		check(service.getTemplatePages(portal), 0);
		// Create one template.
		final PageLoc t1 = loader.loadTemplate();
		check(service.getTemplatePages(portal), 1);
		// Not allowed for itself
		check(loader.get(t1).getDTO().getTemplates(), 0);
		// Create second template
		final PageLoc t2 = loader.loadTemplate();
		// Both allowed for each other
		check(service.getTemplatePages(portal), 2);
		check(loader.get(t1).getDTO().getTemplates(), 1);
		Page p2 = loader.get(t2);
		check(p2.getDTO().getTemplates(), 1);
		// T2 based on t1
		p2.setTemplate(t1.getId());
		p2.save();
		p2 = loader.get(t2);
		assertNotNull(p2.getDTO().getTemplate());
		// Checks
		check(service.getTemplatePages(portal), 2);
		check(loader.get(t1).getDTO().getTemplates(), 0);
		check(p2.getDTO().getTemplates(), 1);
	}

	/** Test. */
	@Test
	public void special() throws PMSException {
		check(loader.getSpecialPages(), 0);
		loader.loadSpecial();
		check(loader.getSpecialPages(), 1);
		loader.loadSpecial();
		check(loader.getSpecialPages(), 2);
	}

}
