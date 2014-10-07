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

import java.util.Date;

import net.sf.lucis.core.Batch;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.Writer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.CommunityMemberSelDTO;
import com.isotrol.impe3.web20.api.CommunityMembersFilterDTO;
import com.isotrol.impe3.web20.api.CommunitySelDTO;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;
import com.isotrol.impe3.web20.server.CommunityManager;
import com.isotrol.impe3.web20.server.MemberSchema;
import com.isotrol.impe3.web20.server.MembershipManager;


/**
 * One method test but very complete.
 * @author Emilio Escobar Reyero
 */
public class CommunitiesAndMembersTest extends MemoryContextTest {
	private CommunitiesService communitiesService;
	private MembersService membersService;
	private MembershipManager membershipManager;
	private String serviceId = "test_service_id";

	@Before
	public void setUp() {
		communitiesService = getBean(CommunitiesService.class);
		membersService = getBean(MembersService.class);
		membershipManager = getBean(MembershipManager.class);
	}

	@Test
	public void test() {
		assertNotNull(communitiesService);
		assertNotNull(membersService);
	}

	//@Test
	public void testComplete() throws Exception {
		@SuppressWarnings("unchecked")
		final Store<Long> store = getBean(Store.class);
		final Writer writer = getBean(Writer.class);
		final Batch.Builder<Long> builder = Batch.builder();

		// Create Communities
		final CommunityDTO community01DTO = new CommunityDTO();
		community01DTO.setCode("1111");
		community01DTO.setDate(new Date());
		community01DTO.setDescription("Community Description very very long.");
		community01DTO.setName("Community Name");

		final String community01UUID = communitiesService.create(serviceId, community01DTO);

		final CommunityDTO community02DTO = new CommunityDTO();
		community02DTO.setCode("2222");
		community02DTO.setDate(new Date());
		community02DTO.setDescription("Second Community Description very very long, more than first one");
		community02DTO.setName("Second Community");

		final String community02UUID = communitiesService.create(serviceId, community02DTO);

		assertFalse(community01UUID.equals(community02UUID));

		// Create Members
		final MemberDTO member01DTO = new MemberDTO();
		member01DTO.setCode("1111");
		member01DTO.setName("1111");
		member01DTO.setDate(new Date());
		member01DTO.setDisplayName("Display 1111 name");
		member01DTO.setEmail("1111@email.es");
		member01DTO.setProfiles(Sets.newHashSet("Padre", "Profesor"));
		member01DTO.setProperties(Maps.newHashMap(ImmutableMap.of("nombre", "Nombre", "apellido1", "Apellido1",
			"apellido2", "Apellido2", "dni", "11111111A", "organismo", "Organismo")));

		final String member01UUID = membersService.create(serviceId, member01DTO);

		final MemberDTO member02DTO = new MemberDTO();
		member02DTO.setCode("2222");
		member02DTO.setName("2222");
		member02DTO.setDate(new Date());
		member02DTO.setDisplayName("Display 2222 name");
		member02DTO.setEmail("2222@email.es");
		member02DTO.setProfiles(Sets.newHashSet("Padre"));
		member02DTO.setProperties(Maps.newHashMap(ImmutableMap.of("dni", "22222222A", "organismo", "Organismo")));

		final String member02UUID = membersService.create(serviceId, member02DTO);

		final MemberDTO member03DTO = new MemberDTO();
		member03DTO.setCode("3333");
		member03DTO.setName("3333");
		member03DTO.setDate(new Date());
		member03DTO.setDisplayName("Display 3333 name");
		member03DTO.setEmail("3333@email.es");
		member03DTO.setProfiles(Sets.newHashSet("Alumno"));
		member03DTO.setProperties(Maps.newHashMap(ImmutableMap.of("dni", "33333333A", "organismo", "Organismo")));

		final String member03UUID = membersService.create(serviceId, member03DTO);

		// Add members to new Communities
		membersService.addToCommunity(serviceId, member01UUID, community01UUID, "admin", null, true);
		membersService.addToCommunity(serviceId, member01UUID, community02UUID, "member", null, false);

		membersService.addToCommunity(serviceId, member02UUID, community02UUID, "admin", null, true);

		membersService.addToCommunity(serviceId, member03UUID, community01UUID, "member", null, true);
		membersService.addToCommunity(serviceId, member03UUID, community02UUID, "member", null, true);

		member01DTO.setId(member01UUID);
		member02DTO.setId(member02UUID);
		member03DTO.setId(member03UUID);

		final Document doc01 = MembersIndexer.docMapper.apply(member01DTO);
		addMembership(doc01, member01UUID);
		builder.add(doc01);

		final Document doc02 = MembersIndexer.docMapper.apply(member02DTO);
		addMembership(doc02, member02UUID);
		builder.add(doc02);

		final Document doc03 = MembersIndexer.docMapper.apply(member03DTO);
		addMembership(doc03, member03UUID);
		builder.add(doc03);

		writer.write(store, builder.build(3L));
		// Recover communities users.

		// global community
		final PageFilter<CommunityMembersFilterDTO> cm00FilterDTO = new PageFilter<CommunityMembersFilterDTO>();
		cm00FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(CommunityManager.GLOBAL_STR_ID)
			.putValidated(true));
		PageDTO<CommunityMemberSelDTO> pagec00 = communitiesService.getCommunityMembers(serviceId, cm00FilterDTO);
		assertTrue(pagec00.getTotal() == 3);

		cm00FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(CommunityManager.GLOBAL_STR_ID).putValidated(
			false));
		pagec00 = communitiesService.getCommunityMembers(serviceId, cm00FilterDTO);
		assertTrue(pagec00.getTotal() == 0);

		// First community
		final PageFilter<CommunityMembersFilterDTO> cm01FilterDTO = new PageFilter<CommunityMembersFilterDTO>();
		cm01FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(community01UUID).putValidated(true));
		PageDTO<CommunityMemberSelDTO> pagec01 = communitiesService.getCommunityMembers(serviceId, cm01FilterDTO);
		assertTrue(pagec01.getTotal() == 2);

		cm01FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(community01UUID).putValidated(false));
		pagec01 = communitiesService.getCommunityMembers(serviceId, cm01FilterDTO);
		assertTrue(pagec01.getTotal() == 0);

		// Second community
		final PageFilter<CommunityMembersFilterDTO> cm02FilterDTO = new PageFilter<CommunityMembersFilterDTO>();
		cm02FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(community02UUID).putValidated(true));
		PageDTO<CommunityMemberSelDTO> pagec02 = communitiesService.getCommunityMembers(serviceId, cm02FilterDTO);
		assertTrue(pagec02.getTotal() == 2);

		cm02FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(community02UUID).putValidated(false));
		pagec02 = communitiesService.getCommunityMembers(serviceId, cm02FilterDTO);
		assertTrue(pagec02.getTotal() == 1);

		cm02FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(community02UUID).putValidated(null));
		pagec02 = communitiesService.getCommunityMembers(serviceId, cm02FilterDTO);
		assertTrue(pagec02.getTotal() == 3);

		// Members view
		final PageFilter<MembershipSelFilterDTO> ms01FilterDTO = new PageFilter<MembershipSelFilterDTO>();
		ms01FilterDTO.setFilter(new MembershipSelFilterDTO().putId(member01UUID).putValidated(true));
		PageDTO<MembershipSelDTO> pagem010 = membersService.getMemberships(serviceId, ms01FilterDTO);
		assertTrue(pagem010.getTotal() == 1);

		ms01FilterDTO.setFilter(new MembershipSelFilterDTO().putId(member01UUID).putValidated(false));
		PageDTO<MembershipSelDTO> pagem011 = membersService.getMemberships(serviceId, ms01FilterDTO);
		assertTrue(pagem011.getTotal() == 1);
		assertFalse(pagem010.getElements().contains(pagem011.getElements().get(0)));

		ms01FilterDTO.setFilter(new MembershipSelFilterDTO().putId(member01UUID).putValidated(null));
		PageDTO<MembershipSelDTO> pagem013 = membersService.getMemberships(serviceId, ms01FilterDTO);
		assertTrue(pagem013.getTotal() == 2);

		ms01FilterDTO.setFilter(new MembershipSelFilterDTO().putId(member01UUID).putValidated(false));
		membersService.addToCommunity(serviceId, member01UUID, community02UUID, "member", null, true);
		pagem011 = membersService.getMemberships(serviceId, ms01FilterDTO);
		assertTrue(pagem011.getTotal() == 0);

		ms01FilterDTO.setFilter(new MembershipSelFilterDTO().putId(member01UUID).putValidated(true));
		pagem010 = membersService.getMemberships(serviceId, ms01FilterDTO);
		assertTrue(pagem010.getTotal() == 2);

		// Find admins from communities
		final PageDTO<CommunitySelDTO> adminPage = communitiesService.findCommunities(serviceId, null, "admin");
		assertNotNull(adminPage);
		assertTrue(adminPage.getSize() > 0);

		final CommunitySelDTO administrator = adminPage.getElements().get(0);
		assertNotNull(administrator.getMembership());
		assertEquals("admin", administrator.getMembership().getRole());

		// Remove user from community...
		membersService.removeFromCommunity(serviceId, member03UUID, community02UUID);

		final Document doc03_1 = MembersIndexer.docMapper.apply(member03DTO);
		addMembership(doc03_1, member03UUID);
		writer.write(store, Batch.<Long> builder().update(doc03_1, MemberSchema.ID, member03UUID).build(4L));

		cm01FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(community02UUID).putValidated(true));
		pagec01 = communitiesService.getCommunityMembers(serviceId, cm01FilterDTO);
		assertTrue(pagec01.getTotal() == 1);

		final MemberDTO member3 = membersService.getById(serviceId, member03UUID);
		for (CommunityMemberSelDTO cms : pagec01.getElements()) {
			assertFalse(cms.getMember().getId().equals(member3.getId()));
		}

		// ADD AND REMOVE...
		membersService.addToCommunity(serviceId, member03UUID, community02UUID, "member", null, true);

		Document ardoc03_1 = MembersIndexer.docMapper.apply(member03DTO);
		addMembership(ardoc03_1, member03UUID);
		writer.write(store, Batch.<Long> builder().update(ardoc03_1, MemberSchema.ID, member03UUID).build(5L));

		cm01FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(community02UUID).putValidated(true));
		pagec01 = communitiesService.getCommunityMembers(serviceId, cm01FilterDTO);
		assertTrue(pagec01.getTotal() == 2);

		// final MemberDTO armember3 = membersService.getById(serviceId, member03UUID);
		// assertTrue(pagec01.getElements().contains(armember3));

		membersService.removeFromCommunity(serviceId, member03UUID, community02UUID);

		ardoc03_1 = MembersIndexer.docMapper.apply(member03DTO);
		addMembership(ardoc03_1, member03UUID);
		writer.write(store, Batch.<Long> builder().update(ardoc03_1, MemberSchema.ID, member03UUID).build(6L));

		// ADD AND REMOVE...

		// mass add
		communitiesService.addMembers(serviceId, community02UUID, null, "member", true, false);

		writer.write(
			store,
			Batch
				.<Long> builder()
				.update(addMembership(MembersIndexer.docMapper.apply(member01DTO), member01UUID), MemberSchema.ID,
					member01UUID)
				.update(addMembership(MembersIndexer.docMapper.apply(member02DTO), member02UUID), MemberSchema.ID,
					member02UUID)
				.update(addMembership(MembersIndexer.docMapper.apply(member03DTO), member03UUID), MemberSchema.ID,
					member03UUID).build(8L));

		cm00FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(community02UUID).putValidated(true));
		pagec00 = communitiesService.getCommunityMembers(serviceId, cm00FilterDTO);
		assertTrue(pagec00.getTotal() == 3);

		// Remove user....
		membersService.delete(serviceId, member03UUID);

		writer.write(store, Batch.<Long> builder().delete(MemberSchema.ID, member03UUID).build(12L));

		cm00FilterDTO.setFilter(new CommunityMembersFilterDTO().putId(CommunityManager.GLOBAL_STR_ID)
			.putValidated(true));
		pagec00 = communitiesService.getCommunityMembers(serviceId, cm00FilterDTO);
		assertTrue(pagec00.getTotal() == 2);
		assertFalse(pagec00.getElements().contains(member3));

		// Remove community...
		communitiesService.delete(serviceId, community02UUID);
		ms01FilterDTO.setFilter(new MembershipSelFilterDTO().putId(member01UUID).putValidated(true));
		pagem010 = membersService.getMemberships(serviceId, ms01FilterDTO);
		assertTrue(pagem010.getTotal() == 1);

		try {
			membersService.removeFromCommunity(serviceId, member01UUID, CommunityManager.GLOBAL_STR_ID);
		} catch (ServiceException e) {
			assertTrue(true);
		}

		membersService.delete(serviceId, member01UUID);
		membersService.delete(serviceId, member02UUID);
		communitiesService.delete(serviceId, community01UUID);
	}

	private Document addMembership(Document doc, String memberId) throws ServiceException {
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

		return doc;
	}

}
