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

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.connector.ConnectorsService;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.pms.core.MemoryContextTest.PageLoader.Page;
import com.isotrol.impe3.samples.calculator.CalculatorComponentModule;
import com.isotrol.impe3.samples.calculator.CalculatorConnectorModule;


/**
 * Nested Template Test.
 * @author Andres Rodriguez
 */
public class NestedTemplateTest extends MemoryContextTest {
	private static PageLoader loader;

	/**
	 * Class set up.
	 * @throws PMSException
	 */
	@BeforeClass
	public static void load() throws PMSException {
		final ConnectorsService cs = getBean(ConnectorsService.class);
		final ModuleInstanceTemplateDTO template1 = cs.newTemplate(CalculatorConnectorModule.class.getName());
		template1.setName(testString());
		cs.save(template1.toModuleInstanceDTO());
		final String portalId = loadPortal();
		loadComponent(portalId, CalculatorComponentModule.class);
		loader = new PageLoader(portalId);
	}

	/** Nested template. */
	@Test
	public void nestedTemplate() throws Exception {
		// First template
		final Page t1 = loader.create(PageClass.TEMPLATE);
		t1.setName(testString());
		t1.putComponents();
		t1.save();
		t1.layout();
		// Second template
		final Page t2 = loader.create(PageClass.TEMPLATE);
		t2.setName(testString());
		t2.setTemplate(t1.getLoc().getId());
		t2.putComponents();
		t2.save();
		t1.layout();
		// Special page
		final Page s = loader.create(PageClass.SPECIAL);
		s.setName(testString());
		s.setTemplate(t2.getLoc().getId());
		s.putComponents();
		s.save();
		s.layout();
	}
}
