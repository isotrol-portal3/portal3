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
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.session.SessionDTO;
import com.isotrol.impe3.pms.api.session.SessionsService;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.pms.ext.api.Credentials;


/**
 * Tests for SessionsServiceImpl.
 * @author Andres Rodriguez
 */
public class SessionsServiceImplTest extends MemoryContextTest {
	private SessionsService service;

	@Before
	public void setUp() {
		service = getBean(SessionsService.class);
	}

	private SessionDTO login(String user, String password) throws PMSException {
		return service.login(new Credentials.BasicCredentials().setLogin(user).setPassword(password));
	}

	/** Login test. */
	@Test
	public void test() throws PMSException {
		assertNotNull(login("root", "root"));
		assertNull(login("root", "kk"));
		assertNull(login("kk", "root"));
		assertNull(login("kk", "kk"));
		service.changePassword("root", "root2");
		assertNull(login("root", "root"));
		assertNotNull(login("root", "root2"));
	}
}
