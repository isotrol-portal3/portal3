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
package com.isotrol.impe3.web20.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class MemberEntityTest {

	private final UUID uuid = UUID.randomUUID();
	private final String name = "name";
	private final String displayName = "displayName";
	private final Calendar date = new GregorianCalendar();
	private final Boolean deleted = Boolean.FALSE;
	private final String email = "email";
	private final String memberCode = "memberCode";
	
	@Test
	public void testEntityAttributes() {
		final MemberEntity member = new MemberEntity();
		
		Assert.assertNotNull(member);
		
		member.setId(uuid);
		member.setName(name);
		member.setDisplayName(displayName);
		member.setDate(date);
		member.setDeleted(deleted);
		member.setEmail(email);
		member.setMemberCode(memberCode);
		
		Assert.assertEquals(uuid, member.getId());
		Assert.assertEquals(name, member.getName());
		Assert.assertEquals(displayName, member.getDisplayName());
		Assert.assertEquals(date, member.getDate());
		Assert.assertEquals(deleted, member.isDeleted());
		Assert.assertEquals(email, member.getEmail());
		Assert.assertEquals(memberCode, member.getMemberCode());
		
	}
	
	
}
