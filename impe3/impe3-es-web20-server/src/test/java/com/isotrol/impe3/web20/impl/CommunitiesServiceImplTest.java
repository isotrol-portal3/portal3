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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.dto.StringFilterDTO;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.CommunityFilterDTO;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;


/**
 * Community service tests.
 * @author Emilio Escobar Reyero
 */
public class CommunitiesServiceImplTest extends MemoryContextTest {
	private CommunitiesService service;
	private String serviceId = "test_service_id";

	@Before
	public void setUp() {
		service = getBean(CommunitiesService.class);
	}

	@Test
	public void test() {
		assertNotNull(service);
	}

	@Test
	public void testCreateWithoutCode() throws ServiceException {
		final CommunityDTO community = community();
		community.setCode(null);
		final String uuid = service.create(serviceId, community);
		final CommunityDTO dto = service.getById(serviceId, uuid);
		
		assertEquals(dto.getCode(), dto.getId());
		
		service.delete(serviceId, uuid);
	}
	
	
	@Test
	public void testCreateAndDelete() {
		final CommunityDTO community = community();

		try {
			final String uuid = service.create(serviceId, community);
			final CommunityDTO dto = service.getById(serviceId, uuid);

			assertEquals(community.getCode(), dto.getCode());

			service.delete(serviceId, uuid);

			try {
				service.getById(serviceId, uuid);
			} catch (CommunityNotFoundException e) {
				assertTrue(true);
			}
		} catch (ServiceException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetByCodeAndDelete() {
		final CommunityDTO community = community("");

		try {
			final String uuid = service.create(serviceId, community);
			final CommunityDTO dto = service.getByCode(serviceId, community.getCode());

			assertNotNull(dto);
			assertEquals(community.getCode(), dto.getCode());

			service.delete(serviceId, uuid);

			try {
				service.getById(serviceId, uuid);
			} catch (CommunityNotFoundException e) {
				assertTrue(true);
			}

			final CommunityDTO bad = service.getByCode(serviceId, "bad code");

			assertNull(bad);

		} catch (ServiceException e) {
			Assert.fail();
		}
	}

	@Test
	public void testUpdate() {
		final CommunityDTO community = community("");

		try {
			final String uuid = service.create(serviceId, community);
			final CommunityDTO dto = service.getById(serviceId, uuid);

			assertEquals(community.getCode(), dto.getCode());

			dto.setCode("123456789");

			service.update(serviceId, dto);

			final CommunityDTO udto = service.getById(serviceId, uuid);

			assertFalse(community.getCode().equals(udto.getCode()));
			assertTrue(dto.getCode().equals(udto.getCode()));
			assertTrue(dto.getVersion() == 0);
			assertTrue(udto.getVersion() > 0);

			service.delete(serviceId, uuid);

			try {
				service.getById(serviceId, uuid);
			} catch (CommunityNotFoundException e) {
				assertTrue(true);
			}

			final CommunityDTO bad = service.getByCode(serviceId, "bad code");

			assertNull(bad);

		} catch (ServiceException e) {
			Assert.fail();
		}

	}

	@Test
	public void testSearch() {
		boolean ok = true;
		for (int i = 0; i < 100; i++) {
			final CommunityDTO dto = community(String.valueOf(i));

			try {
				String uuid = service.create(serviceId, dto);

				if (ok) {
					service.delete(serviceId, uuid);
				}

			} catch (ServiceException e) {
				Assert.fail();
			}
			ok = !ok;
		}

		PageFilter<CommunityFilterDTO> filter = new PageFilter<CommunityFilterDTO>();
		filter.setFilter(new CommunityFilterDTO().putName(StringFilterDTO.prefix("qwerty")));
		
		PageDTO<CommunityDTO> page1 = null;
		try {
			page1 = service.search(serviceId, filter);
		} catch (ServiceException e) {
			Assert.fail();
		}

		Assert.assertNotNull(page1);
		Assert.assertTrue(page1.getTotal().intValue() == 50);

		filter.setFilter(new CommunityFilterDTO().putName(StringFilterDTO.exact("qwerty1")));
		PageDTO<CommunityDTO> page2 = null;
		try {
			page2 = service.search(serviceId, filter);
		} catch (ServiceException e) {
			Assert.fail();
		}
		assertNotNull(page2);
		Assert.assertTrue(page2.getTotal().intValue() == 1);

		CommunityDTO byCode = service.getByCode(serviceId, "11223344A59");

		assertNotNull(byCode);
		assertNotNull(byCode.getId());
		assertEquals("11223344A59", byCode.getCode());

		List<CommunityDTO> list = page1.getElements();
		
		
		
		final Set<String> ids = Sets.newHashSet(list.get(0).getId(), list.get(1).getId(), list.get(2).getId(), UUID.randomUUID().toString());

		Map<String, CommunityDTO> communities = null;
		try {
			communities = service.getCommunitiesById(serviceId, ids);
		} catch (ServiceException e) {
			Assert.fail();
		}
		
		assertNotNull(communities);
		assertTrue(communities.size() == 3);
		assertTrue(communities.containsKey(list.get(0).getId()));
		assertEquals(list.get(0).getCode(), communities.get(list.get(0).getId()).getCode());
		
	}

	private CommunityDTO community() {
		return community(UUID.randomUUID().toString());
	}

	private CommunityDTO community(String str) {
		final CommunityDTO community = new CommunityDTO();
		community.setCode("11223344A" + str);
		community.setName("qwerty" + str);
		community.setDescription("QWERTY Keyboard" + str);
		community.setDate(new Date());

		return community;
	}
}
