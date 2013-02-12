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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.security.SecureRandom;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.connectors.hessian.NRConnectorModule;
import com.isotrol.impe3.connectors.nrproxy.NodeRepositoryProxyModule;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateItemDTO;
import com.isotrol.impe3.pms.api.connector.ConnectorsService;
import com.isotrol.impe3.pms.api.minst.DependencyTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ProviderDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.samples.calculator.CalculatorConnectorModule;
import com.isotrol.impe3.samples.message.EchoModule;
import com.isotrol.impe3.samples.message.MessageModule;
import com.isotrol.impe3.samples.message.OptionalEchoModule;


/**
 * Tests for ConnectorsServiceImpl.
 * @author Andres Rodriguez
 */
public class ConnectorsServiceImplTest extends MemoryContextTest {
	private static final String SIMPLE = CalculatorConnectorModule.class.getName();
	private static final String CONFIG1 = CalculatorWithConfigModule.class.getName();
	private static final String CONFIG2 = CalculatorWithConfig2Module.class.getName();
	private static final String MESSAGE = "message";
	private static final String TEXT = "text";
	private static final String PREFIX = "prefix";
	private static final String NUMBER = "number";
	private static final String ECHO = EchoModule.class.getName();
	private static final String OPTIONAL_ECHO = OptionalEchoModule.class.getName();
	private static final String MESSAGE_MODULE = MessageModule.class.getName();

	private static ConnectorsService service;

	/** Set up. */
	@BeforeClass
	public static void setUp() {
		service = getBean(ConnectorsService.class);
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

	/** Templates. */
	@Test
	public void templates() throws PMSException {
		final List<ConnectorModuleDTO> modules = service.getConnectorModules();
		Assert.assertNotNull(modules);
		Assert.assertFalse(modules.isEmpty());
		for (ConnectorModuleDTO module : modules) {
			ModuleInstanceTemplateDTO dto = service.newTemplate(module.getId());
			check(dto, module.getId());
		}
	}

	/** Simple connector without configuration. */
	@Test
	public void simple() throws PMSException {
		final ModuleInstanceTemplateDTO template1 = service.newTemplate(SIMPLE);
		check(template1, SIMPLE);
		final String name1 = testString();
		template1.setName(name1);
		final ModuleInstanceTemplateDTO template2 = service.save(template1.toModuleInstanceDTO());
		final String id2 = check(template2, SIMPLE);
		assertEquals(name1, template2.getName());
		final ModuleInstanceTemplateDTO template3 = service.get(id2);
		check(template3, SIMPLE, id2);
		assertEquals(name1, template3.getName());
		final String name2 = testString();
		template3.setName(name2);
		final ModuleInstanceTemplateDTO template4 = service.save(template3.toModuleInstanceDTO());
		check(template4, SIMPLE, id2);
		assertEquals(name2, template4.getName());
		publish();
		template4.setName("after");
		final ModuleInstanceTemplateDTO template5 = service.save(template3.toModuleInstanceDTO());
		check(template5, SIMPLE, id2);
	}

	private ModuleInstanceTemplateDTO config1Tpl() throws PMSException {
		final ModuleInstanceTemplateDTO template = service.newTemplate(CONFIG1);
		check(template, CONFIG1);
		return template;
	}

	private ModuleInstanceTemplateDTO config2Tpl() throws PMSException {
		final ModuleInstanceTemplateDTO template = service.newTemplate(CONFIG2);
		check(template, CONFIG2);
		return template;
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

	/** With required configuration 1. */
	@Test
	public void config1() throws PMSException {
		final ModuleInstanceTemplateDTO template1 = config1Tpl();
		final String name1 = testString();
		final String msg1 = testString();
		template1.setName(name1);
		cti(template1, MESSAGE).setString(msg1);
		final ModuleInstanceTemplateDTO template2 = service.save(template1.toModuleInstanceDTO());
		final String id2 = check(template2, CONFIG1);
		assertEquals(name1, template2.getName());
		assertEquals(msg1, cti(template2, MESSAGE).getString());
		final ModuleInstanceTemplateDTO template3 = service.get(id2);
		check(template3, CONFIG1, id2);
		assertEquals(name1, template3.getName());
		assertEquals(msg1, cti(template3, MESSAGE).getString());
	}

	/** With required configuration 2. */
	@Test
	public void config2() throws PMSException {
		final ModuleInstanceTemplateDTO template1 = config2Tpl();
		final String name1 = testString();
		final String msg1 = testString();
		final Integer number = new SecureRandom().nextInt();
		template1.setName(name1);
		cti(template1, MESSAGE).setString(msg1);
		cti(template1, NUMBER).setInteger(number);
		final ModuleInstanceTemplateDTO template2 = service.save(template1.toModuleInstanceDTO());
		final String id2 = check(template2, CONFIG1);
		assertEquals(name1, template2.getName());
		assertEquals(msg1, cti(template2, MESSAGE).getString());
		assertEquals(number, cti(template2, NUMBER).getInteger());
		final ModuleInstanceTemplateDTO template3 = service.get(id2);
		check(template3, CONFIG1, id2);
		assertEquals(name1, template3.getName());
		assertEquals(msg1, cti(template3, MESSAGE).getString());
		assertEquals(number, cti(template2, NUMBER).getInteger());
	}

	/** With dependency. */
	@Test
	public void dependency() throws PMSException {
		final ModuleInstanceTemplateDTO template1 = service.newTemplate(MessageModule.class.getName());
		final String name1 = testString();
		final String msg1 = testString();
		template1.setName(name1);
		cti(template1, TEXT).setString(msg1);
		@SuppressWarnings("unused")
		final ModuleInstanceTemplateDTO template2 = service.save(template1.toModuleInstanceDTO());
		final ModuleInstanceTemplateDTO template = service.newTemplate(ECHO);
		final String name = testString();
		final String msg = testString();
		template.setName(name);
		cti(template, PREFIX).setString(msg);
		final List<DependencyTemplateDTO> dts = template.getDependencies();
		assertNotNull(dts);
		assertFalse(dts.isEmpty());
		final DependencyTemplateDTO dt = dts.get(0);
		assertNull(dt.getCurrent());
		assertNotNull(dt.getProviders());
		assertFalse(dt.getProviders().isEmpty());
		dt.setCurrent(dt.getProviders().get(0));
		final ModuleInstanceTemplateDTO template3 = service.save(template.toModuleInstanceDTO());
		assertNotNull(template3);
	}

	/** Node repository. */
	private String newNR() throws PMSException {
		final ModuleInstanceTemplateDTO template = service.newTemplate(NRConnectorModule.class.getName());
		template.setName(testString());
		cti(template, "serviceUrl").setString(testString());
		return service.save(template.toModuleInstanceDTO()).getId();
	}

	/** Connector proxy. */
	@Test
	public void proxy() throws PMSException {
		final String online = newNR();
		final String offline = newNR();
		final ModuleInstanceTemplateDTO template2 = service.newTemplate(NodeRepositoryProxyModule.class.getName());
		template2.setName(testString());
		for (DependencyTemplateDTO dt : template2.getDependencies()) {
			assertNull(dt.getCurrent());
			assertNotNull(dt.getProviders());
			assertFalse(dt.getProviders().isEmpty());
			if ("online".equals(dt.getDependency().getBean())) {
				for (ProviderDTO p : dt.getProviders()) {
					if (online.equals(p.getCurrent().getId())) {
						dt.setCurrent(p);
					}
				}
			} else if ("offline".equals(dt.getDependency().getBean())) {
				for (ProviderDTO p : dt.getProviders()) {
					if (offline.equals(p.getCurrent().getId())) {
						dt.setCurrent(p);
					}
				}
			}
		}
		final ModuleInstanceTemplateDTO template3 = service.save(template2.toModuleInstanceDTO());
		assertNotNull(template3);
		boolean onlineOk = false;
		boolean offlineOk = false;
		for (DependencyTemplateDTO dt : template2.getDependencies()) {
			assertNotNull(dt.getCurrent());
			assertNotNull(dt.getProviders());
			assertFalse(dt.getProviders().isEmpty());
			if ("online".equals(dt.getDependency().getBean())) {
				assertTrue(online.equals(dt.getCurrent().getCurrent().getId()));
				onlineOk = true;
			} else if ("offline".equals(dt.getDependency().getBean())) {
				assertTrue(offline.equals(dt.getCurrent().getCurrent().getId()));
				offlineOk = true;
			}
		}
		assertTrue(onlineOk && offlineOk);
	}

	/** Create message service. */
	private String createMessage() throws PMSException {
		final ModuleInstanceTemplateDTO t = service.newTemplate(MESSAGE_MODULE);
		t.setName(testString());
		cti(t, "text").setString(testString());
		return service.save(t.toModuleInstanceDTO()).getId();
	}

	/** With optional dependency. */
	@Test
	public void optional() throws PMSException {
		final ModuleInstanceTemplateDTO template1 = service.newTemplate(OPTIONAL_ECHO);
		template1.setName(testString());
		cti(template1, "prefix").setString(testString());
		final ModuleInstanceTemplateDTO template2 = service.save(template1.toModuleInstanceDTO());
		for (DependencyTemplateDTO dt : template2.getDependencies()) {
			assertNull(dt.getCurrent());
		}
		createMessage();
		final ModuleInstanceTemplateDTO template3 = service.get(template2.getId());
		for (DependencyTemplateDTO dt : template3.getDependencies()) {
			dt.setCurrent(dt.getProviders().get(0));
		}
		final ModuleInstanceTemplateDTO template4 = service.save(template3.toModuleInstanceDTO());
		for (DependencyTemplateDTO dt : template4.getDependencies()) {
			assertNotNull(dt.getCurrent());
			dt.setCurrent(null);
		}
		final ModuleInstanceTemplateDTO template5 = service.save(template4.toModuleInstanceDTO());
		for (DependencyTemplateDTO dt : template5.getDependencies()) {
			assertNull(dt.getCurrent());
		}
	}

}
