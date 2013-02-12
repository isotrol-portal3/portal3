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


import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.pms.api.EntityInUseException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageInUseException;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageNotFoundException;
import com.isotrol.impe3.pms.api.page.PagesService;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.pms.core.MemoryContextTest.PageLoader.Page;


/**
 * Tests for PagesServiceImpl.
 * @author Andres Rodriguez
 */
public class PageDeletionTest extends MemoryContextTest {
	private static PagesService service;
	private static PortalPagesLoc portal;
	private static PageLoader loader;
	private static String cg0;

	/**
	 * Set up.
	 * @throws PMSException
	 */
	@BeforeClass
	public static void setUp() throws PMSException {
		cg0 = loadCategory(null, 0).getId();
		service = getBean(PagesService.class);
		portal = loadPortalWithPalette();
		loader = new PageLoader(portal.getPortalId());
	}

	/** One page. */
	@Test
	public void one() throws PMSException {
		PageLoc special = loader.loadSpecial();
		service.delete(special);
	}

	/** One category page. */
	@Test
	public void oneCategory() throws PMSException {
		PageLoc p = loader.loadCategory(cg0, true);
		service.delete(p);
	}

	/** Template. */
	@Test(expected = PageInUseException.class)
	public void template() throws Exception {
		final PageLoc template = loader.loadTemplate();
		// Create a new page based on the template
		final Page page = loader.create(PageClass.SPECIAL);
		page.setName(testString());
		page.putComponents();
		page.setTemplate(template.getId());
		page.save();
		service.delete(template);
	}

	/** One page and publish. */
	@Test
	public void oneP() throws PMSException {
		PageLoc special = loader.loadSpecial();
		publish();
		service.delete(special);
	}

	/** Template and publish. */
	@Test(expected = EntityInUseException.class)
	public void templateP() throws Exception {
		final PageLoc template = loader.loadTemplate();
		// Create a new page based on the template
		final Page page = loader.create(PageClass.SPECIAL);
		page.setName(testString());
		page.putComponents();
		page.setTemplate(template.getId());
		page.save();
		publish();
		service.delete(template);
	}

	/** From child portal. */
	@Test(expected = PageNotFoundException.class)
	public void fromChild() throws Exception {
		final PageLoc special = loader.loadSpecial();
		final String childPortal = loadPortal(portal.getPortalId());
		special.setPortalId(childPortal);
		service.delete(special);
	}

}
