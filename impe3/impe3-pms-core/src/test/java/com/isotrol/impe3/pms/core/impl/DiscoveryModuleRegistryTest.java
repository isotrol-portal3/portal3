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


import static com.google.common.collect.Iterables.isEmpty;
import static com.isotrol.impe3.core.modules.ModuleType.COMPONENT;
import static com.isotrol.impe3.core.modules.ModuleType.CONNECTOR;
import static com.isotrol.impe3.core.modules.ModuleType.INVALID;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.samples.message.MessageModule;


/**
 * Tests for DiscoveryModuleRegistry.
 * @author Andres Rodriguez
 */
public class DiscoveryModuleRegistryTest {
	/** Discovery test. */
	@Test
	public void test() throws Exception {
		ModuleRegistry registry = (ModuleRegistry) new DiscoveryModuleRegistry().getObject();
		assertFalse(isEmpty(registry.getModules()));
		assertFalse(isEmpty(registry.getModules(CONNECTOR)));
		assertFalse(isEmpty(registry.getModules(COMPONENT)));
		assertTrue(isEmpty(registry.getModules(INVALID)));
		assertNotNull(registry.getModule(MessageModule.class));
		assertNotNull(registry.getModule(MessageModule.class));
		assertNull(registry.getModule("invalid class name"));
	}
}
