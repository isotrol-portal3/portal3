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

package com.isotrol.users.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.api.PortalUsersService;


/**
 * Tests for PortalUsersService.
 * @author Andres Rodriguez
 */
public class PortalUsersServiceImplTest extends MemoryContextTest {
	private static PortalUsersService service;

	@BeforeClass
	public static void before() {
		service = getBean(PortalUsersService.class);
	}

	/** Normal test. */
	@Test
	public void test() throws Exception {
		PortalUserDTO dto = new PortalUserDTO();
		dto.setUsername("root");
		dto.setDisplayName("Root User");
		PortalUserDTO dto2 = service.create(dto, "pwd");
		assertNotNull(dto2);
		assertNotNull(dto2.getId());
		assertEquals(dto.getUsername(), dto2.getUsername());
		assertEquals(dto.getDisplayName(), dto2.getDisplayName());
		assertNotNull(service.checkPassword("root", "pwd"));
		assertNull(service.checkPassword("root", "pwd2"));
		assertNull(service.checkPassword("root2", "pwd2"));
		service.changePassword(dto2.getId(), "pwd2");
		assertNotNull(service.checkPassword("root", "pwd2"));
		assertNull(service.checkPassword("root2", "pwd"));
		dto2.setUsername("root2");
		PortalUserDTO dto3 = service.update(dto2);
		assertEquals(dto3.getUsername(), dto2.getUsername());
		assertNotNull(service.checkPassword("root2", "pwd2"));
		assertNull(service.checkPassword("root", "pwd2"));
		PortalUserDTO dto4 = service.getById(dto3.getId());
		assertNotNull(dto4);
		assertNotNull(dto4.getId());
		assertEquals(dto3.getId(), dto4.getId());
		assertEquals(dto3.getUsername(), dto4.getUsername());
		assertEquals(dto3.getDisplayName(), dto4.getDisplayName());
	}

	/** By name and deletion test. */
	@Test
	public void nameAndDelete() throws Exception {
		PortalUserDTO dto = new PortalUserDTO();
		dto.setUsername("admin");
		dto.setDisplayName("Admin User");
		PortalUserDTO dto2 = service.create(dto, "admin");
		assertNotNull(dto2);
		assertNotNull(dto2.getId());
		assertNotNull(service.checkPassword("admin", "admin"));
		PortalUserDTO dto3 = service.getByName("admin");
		assertNotNull(dto3);
		assertNotNull(dto3.getId());
		assertEquals(dto2.getId(), dto3.getId());
		assertEquals(dto2.getUsername(), dto3.getUsername());
		assertEquals(dto2.getDisplayName(), dto3.getDisplayName());
		service.delete(dto3.getId());
		assertNull(service.getByName("admin"));
		assertNull(service.checkPassword("admin", "admin"));
	}
}
