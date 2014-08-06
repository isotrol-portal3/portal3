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

package com.isotrol.impe3.pms.ldap;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.ext.api.AuthenticationProvider;
import com.isotrol.impe3.pms.ext.api.Credentials;
import com.isotrol.impe3.pms.ext.api.ExternalUserDataDTO;
import com.isotrol.impe3.pms.ext.api.InvalidCredentialsException;


public class LDAPAuthenticationProviderTest extends MemoryContextTest {

	private AuthenticationProvider ldap;

	@Before
	public void setUp() {
		ldap = getBean(AuthenticationProvider.class);
		assertNotNull(ldap);
	}

	/*
	 * @Test public void testLoginOk() { final ExternalUserDataDTO user = ldap.login(new
	 * Credentials.BasicCredentials().setLogin("eescobar").setPassword("xxxxx")); assertNotNull(user);
	 * 
	 * assertEquals("eescobar", user.getName()); }
	 */
	@Test(expected=InvalidCredentialsException.class)
	public void testLoginFail() throws Exception {
		final ExternalUserDataDTO user = ldap.authenticate(new Credentials.BasicCredentials().setLogin("eescobar")
			.setPassword("kaka"));

		assertNull(user);
	}

}
