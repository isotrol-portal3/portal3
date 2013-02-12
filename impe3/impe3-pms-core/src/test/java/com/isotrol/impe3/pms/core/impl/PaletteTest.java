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


import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.palette.content.filter.FilterModule;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.component.ComponentsService;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.page.PagesService;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for palette loading.
 * @author Andres Rodriguez
 */
public class PaletteTest extends MemoryContextTest {
	private ComponentsService service;
	private PagesService pages;

	/**
	 * Set up.
	 * @throws PMSException
	 */
	@Before
	public void setUp() throws PMSException {
		service = getBean(ComponentsService.class);
		pages = getBean(PagesService.class);
	}

	/** Simple component without configuration. */
	@Test
	public void palette() throws PMSException {
		String portalId = loadPortal();
		final ModuleInstanceTemplateDTO template1 = service.newTemplate(FilterModule.class.getName());
		service.save(portalId, template1.toModuleInstanceDTO());
		List<PaletteDTO> palette = pages.getPalette(new PortalPagesLoc(portalId, getDefaultDeviceId(portalId)));
		assertNotNull(palette);
	}

}
