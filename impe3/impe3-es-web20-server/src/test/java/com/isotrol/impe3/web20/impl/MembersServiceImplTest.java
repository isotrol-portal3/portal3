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
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.UUID;

import net.sf.lucis.core.Batch;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.Writer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.dto.StringFilterDTO;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MemberSelDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;
import com.isotrol.impe3.web20.server.MemberSchema;
import com.isotrol.impe3.web20.server.MembershipManager;


/**
 * Utility class to deal with the Core Application Context during tests.
 * @author Andres Rodriguez
 */
public class MembersServiceImplTest extends MemoryContextTest {
	private MembersService membersService;
	private CommunitiesService communitiesService;
	private MembershipManager membershipManager;
	private String serviceId = "test_service_id";

	@Before
	public void setUp() {
		membersService = getBean(MembersService.class);
		communitiesService = getBean(CommunitiesService.class);
		membershipManager = getBean(MembershipManager.class);
	}

	@Test
	public void test() {
		assertNotNull(membersService);
		assertNotNull(communitiesService);
	}

	@Test
	public void testCreateAndDelete() {

		final MemberDTO member = member();

		try {
			final String uuid = membersService.create(serviceId, member);
			final MemberDTO dto = membersService.getById(serviceId, uuid);

			assertEquals(member.getCode(), dto.getCode());

			membersService.delete(serviceId, uuid);

		} catch (ServiceException e) {
			Assert.fail();
		}

	}

	@Test
	public void testGetByCodeAndDelete() {

		final MemberDTO member01 = member("01code");
		final MemberDTO member02 = member("02code");

		try {
			final String uuid01 = membersService.create(serviceId, member01);
			final String uuid02 = membersService.create(serviceId, member02);
			final MemberDTO dto01 = membersService.getById(serviceId, uuid01);
			final MemberDTO dto02 = membersService.getById(serviceId, uuid02);

			assertEquals(member01.getCode(), dto01.getCode());
			assertEquals(member02.getCode(), dto02.getCode());

			final MemberDTO dto03 = membersService.getByCode(serviceId, dto01.getCode());
			assertEquals(dto01.getCode(), dto03.getCode());

			membersService.delete(serviceId, uuid01);
			membersService.delete(serviceId, uuid02);
		} catch (ServiceException e) {
			Assert.fail();
		}

	}

	@Test
	public void testGetByNameAndDelete() {

		final MemberDTO member01 = member("01name");
		final MemberDTO member02 = member("02name");

		try {
			final String uuid01 = membersService.create(serviceId, member01);
			final String uuid02 = membersService.create(serviceId, member02);
			final MemberDTO dto01 = membersService.getById(serviceId, uuid01);
			final MemberDTO dto02 = membersService.getById(serviceId, uuid02);

			assertEquals(member01.getCode(), dto01.getCode());
			assertEquals(member02.getCode(), dto02.getCode());

			final MemberDTO dto03 = membersService.getByName(serviceId, dto01.getName());
			assertEquals(dto01.getCode(), dto03.getCode());

			membersService.delete(serviceId, uuid01);
			membersService.delete(serviceId, uuid02);
		} catch (ServiceException e) {
			Assert.fail();
		}

	}

	@Test
	public void testCreateUpdateDelete() {

		final MemberDTO member = member("");
		String uuid = "";
		MemberDTO dto = new MemberDTO();

		try {
			uuid = membersService.create(serviceId, member);
		} catch (ServiceException e) {
			Assert.fail();
		}

		try {
			dto = membersService.getById(serviceId, uuid);
		} catch (ServiceException e) {
			Assert.fail();
		}

		Assert.assertNull(member.getId());
		Assert.assertNotNull(dto.getId());
		assertEquals(member.getEmail(), dto.getEmail());
		assertEquals(member.getName(), dto.getName());
		assertEquals(member.getCode(), dto.getCode());
		assertEquals(member.getDate(), dto.getDate());
		assertEquals(member.getDisplayName(), dto.getDisplayName());

		dto.setDisplayName("new display name");

		try {
			membersService.update(serviceId, dto);
		} catch (ServiceException e) {
			Assert.fail();
		}

		Assert.assertFalse(dto.getDisplayName().equals(member.getDisplayName()));

		try {
			membersService.delete(serviceId, dto.getId());
		} catch (ServiceException e) {
			Assert.fail();
		}

		try {
			membersService.getById(serviceId, uuid);
		} catch (ServiceException e) {
			Assert.assertTrue(true);
		}

	}

	//@Test
	public void testSearch() throws Exception {
		@SuppressWarnings("unchecked")
		final Store<Long> store = getBean(Store.class);
		final Writer writer = getBean(Writer.class);
		final Batch.Builder<Long> builder = Batch.builder();

		//
		CommunityDTO community = new CommunityDTO();
		community.setCode("c_code");
		community.setDate(new Date());
		community.setDescription("description");
		community.setName("community_name");
		String communityId = communitiesService.create(serviceId, community);

		// adds members.
		boolean ok = true;
		for (int i = 0; i < 100; i++) {
			final MemberDTO dto = member(String.valueOf(i));

			try {
				String uuid = membersService.create(serviceId, dto);
				membersService.addToCommunity(serviceId, uuid.toString(), communityId, "member", null, true);
				dto.setId(uuid);

				if (ok) {
					membersService.delete(serviceId, uuid);

					builder.delete(MemberSchema.ID, uuid);
				} else {
					Document doc = MembersIndexer.docMapper.apply(dto);
					addMembership(doc, uuid);
					builder.add(doc);
				}

			} catch (ServiceException e) {
				Assert.fail();
			}
			ok = !ok;
		}

		writer.write(store, builder.build(100L));

		PageFilter<MemberFilterDTO> filter = new PageFilter<MemberFilterDTO>();

		MemberFilterDTO mfilter = new MemberFilterDTO();
		mfilter.setMemberCode(StringFilterDTO.prefix("11223344A"));

		filter.setFilter(mfilter);

		PageDTO<MemberSelDTO> page = null;
		try {
			page = membersService.search(serviceId, filter);
		} catch (ServiceException e) {
			Assert.fail();
		}
		Assert.assertNotNull(page);
		Assert.assertTrue(page.getTotal().intValue() == 50);

		mfilter = new MemberFilterDTO();
		mfilter.setCommunityId(UUID.randomUUID().toString());
		filter.setFilter(mfilter);
		PageDTO<MemberSelDTO> page2 = null;
		try {
			page2 = membersService.search(serviceId, filter);
		} catch (ServiceException e) {
			Assert.fail();
		}
		Assert.assertNotNull(page2);
		Assert.assertTrue(page2.getTotal().intValue() == 0);

		mfilter.setCommunityId(communityId);
		filter.setFilter(mfilter);
		try {
			page2 = membersService.search(serviceId, filter);
		} catch (ServiceException e) {
			Assert.fail();
		}
		Assert.assertNotNull(page2);
		Assert.assertTrue(page2.getTotal().intValue() == 50);

		// --------------------------------------------------------------

		PageFilter<MemberFilterDTO> displayFilter = new PageFilter<MemberFilterDTO>();
		MemberFilterDTO mDisplayFilter = new MemberFilterDTO();
		mDisplayFilter.setDisplayName(StringFilterDTO.in("qwerty name"));
		// mDisplayFilter.setName(StringFilterDTO.in("qwerty"));
		// mDisplayFilter.setMemberCode(StringFilterDTO.in("qwerty"));

		displayFilter.setFilter(mDisplayFilter);

		PageDTO<MemberSelDTO> displayPage = null;
		try {
			displayPage = membersService.search(serviceId, displayFilter);
		} catch (ServiceException e) {
			Assert.fail();
		}
		Assert.assertNotNull(displayPage);

		mDisplayFilter = new MemberFilterDTO();
		mDisplayFilter.setDisplayName(StringFilterDTO.in("lucía"));
		// mDisplayFilter.setName(StringFilterDTO.in("qwerty"));
		// mDisplayFilter.setMemberCode(StringFilterDTO.in("qwerty"));
		displayFilter.setFilter(mDisplayFilter);

		try {
			displayPage = membersService.search(serviceId, displayFilter);
		} catch (ServiceException e) {
			Assert.fail();
		}
		Assert.assertNotNull(displayPage);
	}

	private void addMembership(Document doc, String memberId) throws ServiceException {
		PageFilter<MembershipSelFilterDTO> filter = new PageFilter<MembershipSelFilterDTO>();
		filter.setFilter(new MembershipSelFilterDTO().putId(memberId));
		filter.setPagination(new PaginationDTO(0, 1024));
		PageDTO<MembershipSelDTO> page = membershipManager.getMemberships(null, filter);

		for (MembershipSelDTO dto : page.getElements()) {
			doc.add(new Field(MemberSchema.COMMUNITY, dto.getCommunity().getId(), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
			doc.add(new Field(MemberSchema.COMMUNITYROL, dto.getCommunity().getId() + ":" + dto.getRole(),
				Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field(MemberSchema.COMMUNITYSTATUS, dto.getCommunity().getId() + ":" + dto.isValidated(),
				Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
	}

	private MemberDTO member() {
		return member(UUID.randomUUID().toString());
	}

	private MemberDTO member(String str) {
		final MemberDTO member = new MemberDTO();
		member.setCode("11223344A" + str);
		member.setName("qwerty" + str);
		member.setDisplayName("Apellidos y Nombre andalucía para el Display Name QWERTY " + str);
		member.setEmail("qwerty" + str + "@isotrol.com");
		member.setDate(new Date());

		return member;
	}

}
