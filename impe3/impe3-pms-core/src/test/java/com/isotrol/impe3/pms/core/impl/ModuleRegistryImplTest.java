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


import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleRegistryService;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for ModuleRegistryImpl.
 * @author Andres Rodriguez
 */
public class ModuleRegistryImplTest extends MemoryContextTest {
	/** Discovery-based test. */
	@Test
	public void test() {
		final ModuleRegistryService service = getBean(ModuleRegistryService.class);
		assertFalse(service.getConnectors().isEmpty());
		for (ConnectorModuleDTO dto : service.getConnectors()) {
			assertNotNull(dto);
			assertNotNull(dto.getId());
			assertNotNull(dto.getName());
			assertTrue(dto.getName().length() > 0);
			assertNotNull(dto.getDescription());
		}
		assertNotNull(service.getComponents());
		assertNotNull(service.getInvalids());
		assertNotNull(service.getNotFound());
		assertNotNull(service.getNotModule());
	}
}
