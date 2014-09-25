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
 * Basic dto test 
 * @author Emilio Escobar Reyero
 *
 */
public class PortalUserSelDTOTest {

	@Test
	public void dtoTest() {
		final PortalUserSelDTO user = new PortalUserSelDTO();
		
		final String id = "id";
		final String username = "username";
		final String displayName = "displayName";
		final String email = "email@dominio.com";
		final boolean active = true;
		
		user.setId(id);
		user.setActive(active);
		user.setDisplayName(displayName);
		user.setEmail(email);
		user.setUsername(username);
		
		Assert.assertEquals(id, user.getId());
		Assert.assertEquals(username, user.getUsername());
		Assert.assertEquals(displayName, user.getDisplayName());
		Assert.assertEquals(email, user.getEmail());
		Assert.assertTrue(user.isActive());
		
	}
	
	
}
