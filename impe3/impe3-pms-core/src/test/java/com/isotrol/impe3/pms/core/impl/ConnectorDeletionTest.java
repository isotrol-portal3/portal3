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
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateItemDTO;
import com.isotrol.impe3.pms.api.connector.ConnectorInUseException;
import com.isotrol.impe3.pms.api.connector.ConnectorNotFoundException;
import com.isotrol.impe3.pms.api.connector.ConnectorsService;
import com.isotrol.impe3.pms.api.minst.DependencyTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.samples.calculator.CalculatorConnectorModule;
import com.isotrol.impe3.samples.message.EchoModule;
import com.isotrol.impe3.samples.message.MessageModule;


/**
 * Tests for connectors deletion.
 * @author Andres Rodriguez
 */
public class ConnectorDeletionTest extends MemoryContextTest {
	private static final String SIMPLE = CalculatorConnectorModule.class.getName();
	private static final String TEXT = "text";
	private static final String PREFIX = "prefix";
	private static final String ECHO = EchoModule.class.getName();

	private static ConnectorsService service;

	/** Set up. */
	@BeforeClass
	public static void setUp() {
		service = getBean(ConnectorsService.class);
	}

	/** Simple connector without configuration. */
	@Test
	public void simple() throws PMSException {
		final ModuleInstanceTemplateDTO template1 = service.newTemplate(SIMPLE);
		template1.setName(testString());
		final String id = service.save(template1.toModuleInstanceDTO()).getId();
		service.delete(id);
		assertTrue(service.getConnectors().isEmpty());
	}

	private ConfigurationTemplateItemDTO cti(ModuleInstanceTemplateDTO dto, String name) {
		assertNotNull(dto.getConfiguration());
		assertNotNull(dto.getConfiguration().getItems());
		for (ConfigurationTemplateItemDTO i : dto.getConfiguration().getItems()) {
			if (name.equals(i.getKey())) {
				return i;
			}
		}
		assertTrue(false);
		return null;
	}

	/** With dependency. */
	@Test
	public void dependency() throws PMSException {
		final ModuleInstanceTemplateDTO template1 = service.newTemplate(MessageModule.class.getName());
		template1.setName(testString());
		cti(template1, TEXT).setString(testString());
		final String id1 = service.save(template1.toModuleInstanceDTO()).getId();
		final ModuleInstanceTemplateDTO template = service.newTemplate(ECHO);
		template.setName(testString());
		cti(template, PREFIX).setString(testString());
		final List<DependencyTemplateDTO> dts = template.getDependencies();
		final DependencyTemplateDTO dt = dts.get(0);
		dt.setCurrent(dt.getProviders().get(0));
		final String id2 = service.save(template.toModuleInstanceDTO()).getId();
		boolean ok = false;
		try {
			service.delete(id1);
		} catch (ConnectorInUseException e) {
			ok = true;
		}
		assertTrue(ok);
		service.delete(id2);
		service.delete(id1);
		assertTrue(service.getConnectors().isEmpty());
	}

	/** Not found. */
	@Test(expected = ConnectorNotFoundException.class)
	public void notFound() throws PMSException {
		assertTrue(service.getConnectors().isEmpty());
		service.delete(UUID.randomUUID().toString());
	}

}
