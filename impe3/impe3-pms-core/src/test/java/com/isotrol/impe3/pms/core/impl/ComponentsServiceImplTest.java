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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.component.ComponentNotFoundException;
import com.isotrol.impe3.pms.api.component.ComponentsService;
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.minst.DependencySetTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.samples.component.SimpleComponentModule;


/**
 * Tests for ComponentsServiceImpl.
 * @author Andres Rodriguez
 */
public class ComponentsServiceImplTest extends MemoryContextTest {
	private static final String SIMPLE = SimpleComponentModule.class.getName();

	private ComponentsService service;
	private String portalId;
	private PortalsService portalsService;

	/**
	 * Set up.
	 * @throws PMSException
	 */
	@Before
	public void setUp() throws PMSException {
		service = getBean(ComponentsService.class);
		portalsService = getBean(PortalsService.class);
		portalId = loadPortal();
	}

	private void setParent(String portal, String parent) throws PMSException {
		portalsService.setParent(portal, parent);

	}

	private String check(ModuleInstanceTemplateDTO template, String key) {
		assertNotNull(template);
		return template.getId();
	}

	private String check(ModuleInstanceTemplateDTO template, String key, String id) {
		assertNotNull(id);
		check(template, key);
		assertEquals(id, template.getId());
		return id;
	}

	private void checkNumber(String id, int own, int inherited) throws PMSException {
		List<ModuleInstanceSelDTO> components = service.getComponents(id);
		assertNotNull(components);
		assertEquals(own, components.size());
		List<InheritedComponentInstanceSelDTO> ic = service.getInheritedComponents(id);
		assertNotNull(ic);
		assertEquals(inherited, ic.size());
	}

	/** Templates. */
	@Test
	public void templates() throws PMSException {
		final List<ComponentModuleDTO> modules = service.getComponentModules();
		Assert.assertNotNull(modules);
		Assert.assertFalse(modules.isEmpty());
		for (ComponentModuleDTO module : modules) {
			ModuleInstanceTemplateDTO dto = service.newTemplate(module.getId());
			check(dto, module.getId());
		}
	}

	/** Simple component without configuration. */
	@Test
	public void simple() throws PMSException {
		final ModuleInstanceTemplateDTO template1 = service.newTemplate(SIMPLE);
		check(template1, SIMPLE);
		final String name1 = testString();
		template1.setName(name1);
		final ModuleInstanceTemplateDTO template2 = service.save(portalId, template1.toModuleInstanceDTO());
		final String id2 = check(template2, SIMPLE);
		assertEquals(name1, template2.getName());
		final ModuleInstanceTemplateDTO template3 = service.get(portalId, id2);
		check(template3, SIMPLE, id2);
		assertEquals(name1, template3.getName());
		final String name2 = testString();
		template3.setName(name2);
		final ModuleInstanceTemplateDTO template4 = service.save(portalId, template3.toModuleInstanceDTO());
		check(template4, SIMPLE, id2);
		assertEquals(name2, template4.getName());
		List<ModuleInstanceSelDTO> components = service.getComponents(portalId);
		assertNotNull(components);
		assertFalse(components.isEmpty());
		List<InheritedComponentInstanceSelDTO> inherited = service.getInheritedComponents(portalId);
		assertNotNull(inherited);
		assertTrue(inherited.isEmpty());
	}

	/** Inheritance. */
	@Test
	public void inheritance() throws PMSException {
		String p1 = loadPortal();
		checkNumber(p1, 0, 0);
		loadSimpleComponent(p1);
		checkNumber(p1, 1, 0);
		String p2 = loadPortal();
		checkNumber(p2, 0, 0);
		setParent(p2, p1);
		checkNumber(p2, 0, 1);
		String cid = loadSimpleComponent(p1);
		checkNumber(p2, 0, 2);
		service.delete(p1, cid);
		checkNumber(p2, 0, 1);
		loadSimpleComponent(p1);
		checkNumber(p2, 0, 2);
		loadSimpleComponent(p2);
		checkNumber(p2, 1, 2);
		setParent(p2, null);
		checkNumber(p2, 1, 0);
	}

	/** Not inherited. */
	@Test(expected = ComponentNotFoundException.class)
	public void notInherited() throws PMSException {
		String p1 = loadPortal();
		String c1 = loadSimpleComponent(p1);
		service.getConfiguration(p1, c1);
	}

	/** Can't override config. */
	@Test(expected = ComponentNotFoundException.class)
	public void noConfig() throws PMSException {
		String p1 = loadPortal();
		String c1 = loadSimpleComponent(p1);
		String p2 = loadPortal();
		portalsService.setParent(p2, p1);
		service.getConfiguration(p2, c1);
	}

	/** Can't override deps. */
	@Test(expected = ComponentNotFoundException.class)
	public void noDeps() throws PMSException {
		String p1 = loadPortal();
		String c1 = loadSimpleComponent(p1);
		String p2 = loadPortal();
		portalsService.setParent(p2, p1);
		service.getDependencies(p2, c1);
	}

	private InheritedComponentInstanceSelDTO getInherited(String portal, String id) throws PMSException {
		for (InheritedComponentInstanceSelDTO dto : service.getInheritedComponents(portal)) {
			if (id.equals(dto.getComponent().getId())) {
				return dto;
			}
		}
		Assert.fail();
		return null;
	}

	private void checkInherited(String portal, String id, Boolean config, Boolean deps) throws PMSException {
		InheritedComponentInstanceSelDTO dto = getInherited(portal, id);
		assertEquals(config, dto.getConfiguration());
		assertEquals(deps, dto.getDependencies());
	}

	private void checkConfig(String portal, String id, String text) throws PMSException {
		String config = Dummy.getText(service.getConfiguration(portal, id));
		assertEquals(text, config);
	}

	private void checkDep(String portal, String id, String cnnId) throws PMSException {
		String did = Dummy.getDep(service.getDependencies(portal, id).getDependencies());
		assertEquals(cnnId, did);
	}

	private void check(String portal, String id, Boolean config, Boolean deps, String text, String cnnId)
		throws PMSException {
		checkInherited(portal, id, config, deps);
		checkConfig(portal, id, text);
		checkDep(portal, id, cnnId);
	}

	private void overrideC(String portal, String id, String text) throws PMSException {
		ConfigurationTemplateDTO dto = service.getConfiguration(portal, id);
		Dummy.setText(dto, text);
		service.overrideConfiguration(portal, id, dto.toConfiguationItemDTO());
	}

	private void overrideD(String portal, String id, String cnnId) throws PMSException {
		DependencySetTemplateDTO dto = service.getDependencies(portal, id);
		Dummy.setDep(dto.getDependencies(), cnnId);
		service.overrideDependencies(portal, id, dto.toDependencyListDTO());
	}

	/** Overriding. */
	@Test
	public void override() throws PMSException {
		String pc = "parent";
		String cc = "child";
		String cc2 = "child2";
		String cnn1 = Dummy.loadConnector("cnn1");
		String cnn2 = Dummy.loadConnector("cnn2");
		String p1 = loadPortal();
		String cmp1 = Dummy.loadComponent(p1, pc, cnn1);
		String p2 = loadPortal();
		setParent(p2, p1);
		check(p2, cmp1, false, false, pc, cnn1);
		overrideC(p2, cmp1, cc);
		check(p2, cmp1, true, false, cc, cnn1);
		overrideC(p2, cmp1, cc2);
		check(p2, cmp1, true, false, cc2, cnn1);
		service.clearConfiguration(p2, cmp1);
		check(p2, cmp1, false, false, pc, cnn1);
		overrideD(p2, cmp1, cnn2);
		check(p2, cmp1, false, true, pc, cnn2);
		overrideC(p2, cmp1, cc);
		check(p2, cmp1, true, true, cc, cnn2);
		overrideC(p2, cmp1, cc2);
		check(p2, cmp1, true, true, cc2, cnn2);
		service.clearDependencies(p2, cmp1);
		check(p2, cmp1, true, false, cc2, cnn1);
		service.clearConfiguration(p2, cmp1);
		check(p2, cmp1, false, false, pc, cnn1);
	}

}
