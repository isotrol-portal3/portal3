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
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.FavoriteDTO;
import com.isotrol.impe3.web20.api.FavoriteFilterDTO;
import com.isotrol.impe3.web20.api.FavoritesService;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.server.CommunityManager;

/**
 * 
 * @author Emilio Escobar Reyero
 */
public class FavoritesServiceImplTest extends MemoryContextTest {
	private FavoritesService favoritesService;
	private MembersService membersService;
	private CommunitiesService communitiesService;
	private String serviceId = "test_service_id";
	
	@Before
	public void setUp() {
		favoritesService = getBean(FavoritesService.class);
		membersService = getBean(MembersService.class);
		communitiesService = getBean(CommunitiesService.class);
	}
	
	@Test
	public void testService() throws ServiceException {
		// create community
		final CommunityDTO community = new CommunityDTO();
		community.setCode("community_code");
		community.setDate(new Date());
		community.setDescription("Community description");
		community.setName("community_name");
		final String communityId = communitiesService.create(serviceId, community);
		
		// create member
		final MemberDTO member = new MemberDTO();
		member.setCode("member_code");
		member.setDate(new Date());
		member.setDisplayName("member display name");
		member.setEmail("member email");
		member.setName("member_name");
		final String memberId = membersService.create(serviceId, member);
		
		// add favorites
		final FavoriteDTO dto = new FavoriteDTO();
		dto.setCommunity(communityId);
		dto.setDate(new Date());
		dto.setMember(memberId);
		dto.setDescription("long description");
		dto.setResource("RESOURCE");
		final Long favoriteId = favoritesService.add(serviceId, dto);
		
		assertNotNull(favoriteId);
		
		for (int i = 0; i < 15; i++) {
			dto.setResource("RESOURCE" + i);
			assertNotNull(favoritesService.add(serviceId, dto));
		}
		
		// search favorites
		final FavoriteFilterDTO filter = new FavoriteFilterDTO();
		filter.setMember(memberId);
		filter.setCommunity(communityId);
		
		final PageDTO<FavoriteDTO> page = favoritesService.getCommunityFavorites(serviceId, filter);
		
		assertNotNull(page);
		assertTrue(page.getTotal() > 0);
		
		final FavoriteDTO fav = page.getElements().get(0);
		assertNotNull(fav);
		assertNotNull(fav.getDescription());
		
		filter.setCommunity(CommunityManager.GLOBAL_STR_ID);
		
		final PageDTO<FavoriteDTO> pageGlobal = favoritesService.getCommunityFavorites(serviceId, filter);
		
		assertNotNull(pageGlobal);
		assertTrue(pageGlobal.getTotal() == 0);
		
		
	}
	
}
