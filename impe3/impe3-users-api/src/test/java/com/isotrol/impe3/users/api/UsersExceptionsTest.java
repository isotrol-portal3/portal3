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

package com.isotrol.impe3.users.api;

import org.junit.Assert;
import org.junit.Test;

/**
 * Really dummy test
 * @author Emilio Escobar Reyero
 *
 */
public class UsersExceptionsTest {

	@Test
	public void portalUserNotFoundExceptionTest() {
		PortalUserNotFoundException e1 = new PortalUserNotFoundException();
		
		Assert.assertNotNull(e1);
		
		PortalUserNotFoundException e2 = new PortalUserNotFoundException("idportal");
		
		Assert.assertNotNull(e2);
	}
	
	@Test
	public void duplicatePortalUserExceptionTest() {
		DuplicatePortalUserException e1 = new DuplicatePortalUserException();
		Assert.assertNotNull(e1);

		DuplicatePortalUserException e2 = new DuplicatePortalUserException("username");
		Assert.assertNotNull(e2);

		DuplicatePortalUserException e3 = new DuplicatePortalUserException("id", "username");
		Assert.assertNotNull(e3);
		
		Assert.assertEquals(e2.getUsername(), e3.getUsername());
	}
	
	@Test
	public void portalUserExceptionTest() {
		PortalUserException e1 = new PortalUserException();
		
		Assert.assertNotNull(e1);
		
		PortalUserException e2 = new PortalUserException("idportal");
		
		Assert.assertNotNull(e2);
	}
}
