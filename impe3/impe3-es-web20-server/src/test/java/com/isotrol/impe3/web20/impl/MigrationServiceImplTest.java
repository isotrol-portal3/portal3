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
package com.isotrol.impe3.web20.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MigrationService;


public class MigrationServiceImplTest extends MemoryContextTest {

	MembersService membersService;
	CommunitiesService communitiesService;
	MigrationService migrationService;
	
	@Before
	public void setUp() {
		membersService = getBean(MembersService.class);
		communitiesService = getBean(CommunitiesService.class);
		migrationService = getBean(MigrationService.class);
	}

	@Test
	public void test() {
		assertNotNull(membersService);
		assertNotNull(communitiesService);
		assertNotNull(migrationService);
	}
	
	//@Test
	public void testMigration() throws ServiceException {
		List<CommunityDTO> communities = Lists.newArrayList();
		List<MemberDTO> members = Lists.newArrayList();
		
		initRepo(members, communities);
		
		Assert.assertTrue(!communities.isEmpty());
		Assert.assertTrue(!members.isEmpty());
	
		migrationService.markDeletedAllMembers(null);
		migrationService.markeDeletedAutomaticCommunities(null);
		
		MemberDTO m = migrationService.getDeletedMemberByLastCode(null, members.get(0).getCode());
		
		Assert.assertNotNull(m);
		Assert.assertEquals(m.getId(), members.get(0).getId());
		
		migrate(members, communities);
		
		
		CommunityDTO aldaba = community("aldaba");
		String aldabauuid = migrationService.safeCommunityCreate(null, aldaba);
		
		CommunityDTO dto = communitiesService.getById(null, aldabauuid);
		
		Assert.assertNotNull(dto);
		
		MemberDTO memberDto = membersService.getById(null, members.get(0).getId());
		
		Assert.assertNotNull(memberDto);
		
	}
	
	private void migrate(List<MemberDTO> members, List<CommunityDTO> communities) throws ServiceException {
		
		for(CommunityDTO c : communities) {
			if (c.getCode() != null) {
				String id = migrationService.safeCommunityCreate(null, c);
				Assert.assertNotNull(id);
			}
		}
		
		for(MemberDTO m : members) {
			String id = migrationService.safeMemberCreate(null, m);
			Assert.assertNotNull(id);
		}
		
		
		migrationService.addToCommunity(null, members.get(0).getId(), communities.get(0).getId(), "member", null, true);
		
		MemberDTO m = new MemberDTO();
		m.setCode("noglobal");
		m.setDate(new Date());
		m.setDisplayName("noglobal");
		m.setEmail("eeses");
		m.setName("nogloggg");
		migrationService.safeMemberCreate(null, m);

		migrationService.safeAddToGlobal(null);
		
		migrationService.updateMembersLogTable(null);
		
	}
	
	
	private void initRepo(List<MemberDTO> members, List<CommunityDTO> communities) throws ServiceException {
		final Random random = new Random();
		
		// Communities
		//List<CommunityDTO> communities = Lists.newArrayList();
		for (int i=0;i<150;i++) {
			CommunityDTO dto = community(String.valueOf(i));
			
			if (random.nextInt(12) >= 10) {
				dto.setCode(null);
			}
			
			dto.setId(communitiesService.create(null, dto));
			communities.add(dto);
		}
		
		// Members
		//List<MemberDTO> members = Lists.newArrayList();
		for (int i=0; i<310; i++) {
			MemberDTO dto = member(String.valueOf(i));
			dto.setId(membersService.create(null, dto));
			members.add(dto);
			
			// Memberships
			int t = random.nextInt(10);
			for (int j=0; j<t; j++) {
				membersService.addToCommunity(null, dto.getId(), communities.get(random.nextInt(150)).getId(), "member", null, true);
			}
			
		}
		
	}
	
	private MemberDTO member(String str) {
		final MemberDTO member = new MemberDTO();
		member.setCode(str);
		member.setName("qwerty" + str);
		member.setDisplayName("Apellidos y Nombre andalucía para el Display Name QWERTY " + str);
		member.setEmail("qwerty" + str + "@isotrol.com");
		member.setDate(new Date());

		return member;
	}
	
	private CommunityDTO community(String str) {
		final CommunityDTO community = new CommunityDTO();
		community.setCode(str);
		community.setDate(new Date());
		community.setName("name" + str);
		community.setDescription("description"+str);
		
		return community;
	}
	
}
