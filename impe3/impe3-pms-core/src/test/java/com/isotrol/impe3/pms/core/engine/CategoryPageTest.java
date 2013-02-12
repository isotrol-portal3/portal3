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

package com.isotrol.impe3.pms.core.engine;


import org.junit.Test;

import com.isotrol.impe3.pms.api.connector.ConnectorsService;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.core.MemoryContextTest.PageLoader.Page;
import com.isotrol.impe3.pms.core.engine.EngineModelLoader;
import com.isotrol.impe3.samples.calculator.CalculatorComponentModule;
import com.isotrol.impe3.samples.calculator.CalculatorConnectorModule;


/**
 * Tests for EngineModelLoaderImpl.
 * @author Andres Rodriguez
 */
public class CategoryPageTest extends AbtractEngineModelTest {
	private String ct1;
	private String cg0;
	private String cg1;
	private String portalId;
	private String portalPath;
	private PageLoader loader;
	private String componentId;
	private String cipId;
	private String pageName;
	private Page page;
	private EngineModelLoader service;

	/**
	 * Portal with default page.
	 */
	@Test
	public void test() throws Exception {
		ct1 = loadContentType("ct").getId();
		cg0 = loadCategory(null, 0).getId();
		cg1 = loadCategory(null, 1).getId();
		String cg11 = loadCategory(cg1, 1).getId();
		String cg111 = loadCategory(cg11, 1).getId();
		final ConnectorsService cs = getBean(ConnectorsService.class);
		final ModuleInstanceTemplateDTO template1 = cs.newTemplate(CalculatorConnectorModule.class.getName());
		template1.setName(testString());
		cs.save(template1.toModuleInstanceDTO());
		portalId = loadPortal();
		portalPath = getBean(PortalsService.class).getName(portalId).getName().getPath();
		componentId = loadComponent(portalId, CalculatorComponentModule.class);
		loader = new PageLoader(portalId);
		page = loader.create(PageClass.CATEGORY);
		page.setCategory(cg0);
		page.putComponents();
		page.save();
		page.layout();
		page.setCategory(cg11);
		page.save();
		page.setCategory(cg111);
		page.save();
		page.setCategory(cg0);
		page.save();
	}
}
