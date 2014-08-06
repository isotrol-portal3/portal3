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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.RecommendationDTO;
import com.isotrol.impe3.web20.api.RecommendationFilterDTO;
import com.isotrol.impe3.web20.api.RecommendationsService;

/**
 * 
 * @author Emilio Escobar Reyero
 *
 */
public class RecommendationsServiceTest extends MemoryContextTest {

	private RecommendationsService recommendationsService;
	private MembersService membersService;
	private CommunitiesService communitiesService;
	private String serviceId = "test_service_id";

	@Before
	public void setUp() {
		recommendationsService = getBean(RecommendationsService.class);
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
		final MemberDTO recommender = new MemberDTO();
		recommender.setCode("recommender_code");
		recommender.setDate(new Date());
		recommender.setDisplayName("recommender display name");
		recommender.setEmail("recommender email");
		recommender.setName("recommender_name");
		final String recommenderId = membersService.create(serviceId, recommender);
		
		// create member
		final MemberDTO recommended = new MemberDTO();
		recommended.setCode("recommended_code");
		recommended.setDate(new Date());
		recommended.setDisplayName("recommended display name");
		recommended.setEmail("recommended email");
		recommended.setName("recommended_name");
		final String recommendedId = membersService.create(serviceId, recommended);
		
		membersService.addToCommunity(recommendedId, recommendedId, communityId, "member", null, true);
		membersService.addToCommunity(recommendedId, recommenderId, communityId, "member", null, true);
		
		
		final RecommendationDTO recommendation = new RecommendationDTO();
		recommendation.setCommunity(communityId);
		recommendation.setDate(new Date());
		recommendation.setResource("RESOURCE");
		recommendation.setRecommended(recommendedId);
		recommendation.setRecommender(recommenderId);
		
		final Long recommendationId = recommendationsService.recommendResource(serviceId, recommendation);
		
		assertNotNull(recommendationId);
		
		
		PageDTO<RecommendationDTO> page = recommendationsService.getRecommendedResources(serviceId, recommendedId, communityId, null);

		assertNotNull(page);
		assertTrue(page.getTotal() == 1);
		
		recommendationsService.removeRecommendation(serviceId, recommendationId);

		page = recommendationsService.getRecommendedResources(serviceId, recommendedId, communityId, null);

		assertNotNull(page);
		assertTrue(page.getTotal() == 0);
		
		for (int i = 0; i < 15; i++) {
			final RecommendationDTO dto = new RecommendationDTO();
			dto.setCommunity(communityId);
			dto.setDate(new Date());
			dto.setResource("RESOURCE");
			dto.setRecommended(recommendedId);
			dto.setRecommender(recommenderId);
			
			final Long id = recommendationsService.recommendResource(recommendedId, dto);
			
			assertNotNull(id);
		}
		
		final RecommendationFilterDTO filter = new RecommendationFilterDTO();
		filter.setCommunity(communityId);
		filter.setRecommender(recommenderId);
		
		PageDTO<RecommendationDTO> search = recommendationsService.search(serviceId, filter);
		assertNotNull(search);
		assertTrue(search.getTotal() == 15);
	}
}
