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


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.session.SessionsService;
import com.isotrol.impe3.pms.api.user.UserDTO;
import com.isotrol.impe3.pms.api.user.UserSelDTO;
import com.isotrol.impe3.pms.api.user.UserTemplateDTO;
import com.isotrol.impe3.pms.api.user.UsersService;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for UsersServiceImpl.
 * @author Andres Rodriguez
 */
public class UsersServiceImplTest extends MemoryContextTest {
	private static final String CREATED = "created";

	private UsersService service;
	private Map<GlobalRole, String> roles;
	private Map<GlobalAuthority, String> authorities;

	@Before
	public void setUp() throws PMSException {
		service = getBean(UsersService.class);
		roles = getBean(SessionsService.class).getGlobalRoles();
		authorities = getBean(SessionsService.class).getGlobalAuthorities();
	}

	/** Get test. */
	@Test
	public void testGet() throws PMSException {
		final List<UserSelDTO> list = service.getUsers();
		assertNotNull(list);
		assertFalse(list.isEmpty());
	}

	/** Create test. */
	@Test
	public void testCreate() throws PMSException {
		List<UserSelDTO> list = service.getUsers();
		final int n1 = list.size();
		UserTemplateDTO dto = new UserTemplateDTO(roles, authorities);
		dto.setName(CREATED);
		dto.setDisplayName(CREATED);
		dto.setActive(true);
		dto.setRoot(true);
		UserDTO u = service.save(dto.toDTO());
		assertNotNull(u);
		list = service.getUsers();
		final int n2 = list.size();
		Assert.assertEquals(n1+1, n2);
	}

}
