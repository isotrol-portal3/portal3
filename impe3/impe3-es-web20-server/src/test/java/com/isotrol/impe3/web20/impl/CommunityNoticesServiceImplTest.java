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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.OrderDTO;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.dto.StringFilterDTO;
import com.isotrol.impe3.dto.StringMatchMode;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.CommunityNoticesService;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.NoticeDTO;
import com.isotrol.impe3.web20.api.NoticeFilterDTO;


/**
 * Notices test.
 * @author Emilio Escobar Reyero
 */
public class CommunityNoticesServiceImplTest extends MemoryContextTest {
	private String serviceId = "test_service_id";
	private CommunityNoticesService notices;
	private MembersService members;
	private CommunitiesService communities;

	@Before
	public void setUp() {
		notices = getBean(CommunityNoticesService.class);
		members = getBean(MembersService.class);
		communities = getBean(CommunitiesService.class);
	}

	@Test
	public void test() {
		assertNotNull(notices);
		assertNotNull(members);
		assertNotNull(communities);
	}

	@Test
	public void testCreate() throws ServiceException {
		final String memberId = member("");
		final String communityId = community("");

		// you must verify member could add notice.
		final NoticeDTO dto = notice(memberId, communityId);
		final Long id = notices.create(serviceId, dto);

		assertNotNull(id);
	}

	@Test
	public void testUpdate() throws ServiceException {
		final String memberId = member("update_");
		final String communityId = community("update_");

		// you must verify member could add notice.
		final NoticeDTO dto = notice(memberId, communityId);
		final Long id = notices.create(serviceId, dto);

		assertNotNull(id);

		final NoticeDTO recovered = notices.getById(serviceId, id);

		assertEquals(dto.getDescription(), recovered.getDescription());

		recovered.setDescription("new Description");

		notices.update(serviceId, recovered);

		final NoticeDTO updated = notices.getById(serviceId, id);

		assertFalse(updated.getDescription().equals(dto.getDescription()));
		assertEquals("new Description", updated.getDescription());
	}

	@Test
	public void testSearch() throws ServiceException {
		final String memberId = member("testSearch_");
		final String communityId1 = community("1_");
		final String communityId2 = community("2_");

		for (int i = 0; i < 100; i++) {
			notices.create(serviceId, notice(memberId, communityId1));
		}

		final NoticeFilterDTO fullFilter = new NoticeFilterDTO();
		fullFilter.setCommunityId(communityId1);
		fullFilter.setTitle(new StringFilterDTO("Notice", StringMatchMode.IN));

		PageDTO<NoticeDTO> fullPage = notices.getCommunityNotices(serviceId, fullFilter);

		assertNotNull(fullPage);
		assertTrue(fullPage.getTotal() == 100);

		NoticeDTO expired = notice(memberId, communityId1);
		expired.setExpiration(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24)));
		long toRemove1 = notices.create(serviceId, expired);

		fullPage = notices.getCommunityNotices(serviceId, fullFilter);

		assertNotNull(fullPage);
		assertTrue(fullPage.getTotal() == 101);

		fullFilter.setActive(true);

		fullPage = notices.getCommunityNotices(serviceId, fullFilter);

		assertNotNull(fullPage);
		assertTrue(fullPage.getTotal() == 100);

		fullFilter.setActive(false);

		fullPage = notices.getCommunityNotices(serviceId, fullFilter);

		assertNotNull(fullPage);
		assertTrue(fullPage.getTotal() == 1);
		
		notices.delete(serviceId, toRemove1);
		
		final NoticeFilterDTO filter = new NoticeFilterDTO();
		filter.setCommunityId(communityId1);

		final PageDTO<NoticeDTO> page = notices.getCommunityNotices(serviceId, filter);

		assertNotNull(page);
		assertTrue(page.getTotal() == 100);

		filter.setCommunityId(communityId2);

		final PageDTO<NoticeDTO> pageEmpty = notices.getCommunityNotices(serviceId, filter);

		assertNotNull(pageEmpty);
		assertTrue(pageEmpty.getTotal() == 0);

		filter.setCommunityId(communityId1);

		final NoticeFilterDTO erarserFilter = new NoticeFilterDTO();
		erarserFilter.setCommunityId(communityId1);
		erarserFilter.setPagination(new PaginationDTO(0, 100));

		final PageDTO<NoticeDTO> eraserPage = notices.getCommunityNotices(serviceId, erarserFilter);
		assertNotNull(eraserPage);
		assertTrue(eraserPage.getTotal() == 100);
		assertTrue(eraserPage.getSize() == 100);

		for (NoticeDTO dto : eraserPage.getElements()) {
			notices.delete(serviceId, dto.getId());
		}

		final PageDTO<NoticeDTO> pageEmptyAgain = notices.getCommunityNotices(serviceId, filter);

		assertNotNull(pageEmptyAgain);
		assertTrue(pageEmptyAgain.getTotal() == 0);

	}

	private NoticeDTO notice(String memberId, String communityId) {
		final NoticeDTO dto = new NoticeDTO();
		dto.setCommunity(communityId);
		dto.setMember(memberId);
		dto.setDate(new Date());
		dto.setDescription("Notice on " + communityId + " by " + memberId + ". Description!");
		dto.setTitle("Notice on " + communityId + " by " + memberId);
		dto.setRelease(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24)));
		
		return dto;
	}

	@Test
	public void testOrder() throws ServiceException {
		final String memberId = member("order_");
		final String communityId1 = community("order_");
		
		
		final NoticeDTO notice1 = notice(memberId, communityId1);
		final NoticeDTO notice2 = notice(memberId, communityId1);
		final NoticeDTO notice3 = notice(memberId, communityId1);


		final long now = System.currentTimeMillis();
		final long one = now + (1000 * 60 * 24);
		final long two = one + (1000 * 60 * 24);

		
		notice1.setRelease(new Date(now));
		notice1.setTitle("NOW");
		notice2.setRelease(new Date(two));
		notice2.setTitle("TWO");
		notice3.setRelease(new Date(one));
		notice3.setTitle("ONE");


		final long n1 = notices.create(serviceId, notice1);
		final long n2 = notices.create(serviceId, notice2);
		final long n3 = notices.create(serviceId, notice3);

		final NoticeFilterDTO fullFilter = new NoticeFilterDTO();
		fullFilter.setCommunityId(communityId1);
		fullFilter.addOrdering(new OrderDTO("release", true));
		
		final PageDTO<NoticeDTO> fullPage = notices.getCommunityNotices(serviceId, fullFilter);

		assertNotNull(fullPage);
		
		final List<NoticeDTO> elements = fullPage.getElements();
		
		assertNotNull(elements);
		
		assertTrue(elements.size() == 3);
		
		assertTrue(elements.get(0).getId().longValue() == n1);
		assertTrue(elements.get(1).getId().longValue() == n3);
		assertTrue(elements.get(2).getId().longValue() == n2);
	}
	
	
	private String member(String prefix) throws ServiceException {
		final MemberDTO dto = new MemberDTO();
		dto.setCode(prefix + "code");
		dto.setName(prefix + "name");
		dto.setDisplayName("displayName");
		dto.setEmail("member@email.es");
		dto.setDate(new Date());

		return members.create(serviceId, dto);
	}

	private String community(String prefix) throws ServiceException {
		final CommunityDTO dto = new CommunityDTO();
		dto.setCode(prefix + "code");
		dto.setDate(new Date());
		dto.setDescription(prefix + "description");
		dto.setName(prefix + "name");

		return communities.create(serviceId, dto);
	}

}
