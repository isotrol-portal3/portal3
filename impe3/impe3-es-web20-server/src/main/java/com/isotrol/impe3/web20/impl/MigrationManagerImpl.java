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


import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MemberNotFoundException;
import com.isotrol.impe3.web20.api.MemberSelDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;
import com.isotrol.impe3.web20.mappers.MemberMiniDTOMapper;
import com.isotrol.impe3.web20.mappers.MemberSelDTOMapper;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.MembershipEntity;
import com.isotrol.impe3.web20.server.CommunityManager;
import com.isotrol.impe3.web20.server.MemberMiniDTO;
import com.isotrol.impe3.web20.server.MigrationManager;


@Service("migrationManager")
public class MigrationManagerImpl extends AbstractWeb20Service implements MigrationManager {

	/** Members service. */
	private MembersService membersService;
	/** Log table */
	private LogTableComponent logTable;

	/**
	 * @see com.isotrol.impe3.web20.server.MigrationManager#markDeletedMembersPage(java.lang.String, int, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int markDeletedMembersPage(String serviceId, int first, int size) throws ServiceException {
		final PageDTO<MemberSelDTO> members = getDao().findMembers(
			PageFilter.of(new MemberFilterDTO(), new PaginationDTO(first, size)), MemberSelDTOMapper.INSTANCE);

		int total = 0;

		if (members != null && members.getElements() != null && !members.getElements().isEmpty()) {
			total = members.getElements().size();
			markMemberAsDeleted(serviceId, members.getElements());
		}

		// sync();

		return total;
	}

	private void markMemberAsDeleted(String serviceId, List<MemberSelDTO> list) throws ServiceException {
		if (list != null && !list.isEmpty()) {
			// int i = 0;
			for (MemberSelDTO m : list) {
				// final long t0 = System.currentTimeMillis();
				PageDTO<MembershipSelDTO> memberships = membersService.getMemberships(serviceId, (PageFilter.of(
					new MembershipSelFilterDTO().putId(m.getId()), new PaginationDTO(0, 1024))));

				if (memberships != null && memberships.getElements() != null && !memberships.getElements().isEmpty()) {
					for (MembershipSelDTO c : memberships.getElements()) {
						if (c.getCommunity() != null && !c.getCommunity().getId().equals(c.getCommunity().getCode())) {
							removeFromCommunity(serviceId, m.getId(), c.getCommunity().getId());
						}
					}
				}
				// final String id = m.getId();

				delete(serviceId, m.getId());

				// task(m.getId().toString(), "MMBR", 0);
				// final long t1 = System.currentTimeMillis();
				// final long t = t1 - t0;
				// System.out.println("["+i+"]--------> MEMBER["+id+"] ---->"+t+"ms.");
				// i = i + 1;
			}
		}
	}

	private void removeFromCommunity(String serviceId, String memberId, String communityId) throws ServiceException {
		Preconditions.checkNotNull(memberId);
		Preconditions.checkNotNull(communityId);

		final UUID communityuuid;
		try {
			communityuuid = UUID.fromString(communityId);
		} catch (IllegalArgumentException e) {
			throw new CommunityNotFoundException(communityId);
		}

		if (communityuuid.equals(CommunityManager.GLOBAL_ID)) {
			throw new CommunityNotFoundException(communityId);
		}

		final UUID memberuuid;
		try {
			memberuuid = UUID.fromString(memberId);
		} catch (IllegalArgumentException e) {
			throw new MemberNotFoundException(memberId);
		}

		MembershipEntity entity = getDao().getMembership(memberuuid, communityuuid);

		if (entity != null) {
			entity.setDeletion(Calendar.getInstance());
		}
	}

	private void delete(String serviceId, String id) throws ServiceException {
		final MemberEntity entity = load(id);
		entity.setDeleted(true);
		entity.setLastMemberCode(entity.getMemberCode());
		entity.setLastName(entity.getName());
		entity.setMemberCode(entity.getId().toString());
		entity.setName(entity.getId().toString());
	}

	private MemberEntity load(String id) throws MemberNotFoundException {
		if (id == null) {
			throw new MemberNotFoundException(id);
		}
		final UUID uuid;
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new MemberNotFoundException(id);
		}
		final MemberEntity entity = getDao().findById(MemberEntity.class, uuid, false);
		if (entity == null || entity.isDeleted()) {
			throw new MemberNotFoundException(id);
		}

		return entity;
	}

	
	
	@Transactional(rollbackFor = Throwable.class)
	public List<String> getMemberWithoutGlobal(String serviceId) throws ServiceException {
		final List objs = getDao().findMembersNoGlobal();
		return Lists.transform(objs, new Function<String, String>() {
			public String apply(String from) {
			return from.toString();
		}
		});
	}


	@Transactional(rollbackFor = Throwable.class)
	public int updateMembersLogTable(String serviceId, int first, int size) throws ServiceException {
		final PageDTO<MemberMiniDTO> page = getDao().findMembersWithDeleted(new PaginationDTO(first, size),
			MemberMiniDTOMapper.INSTANCE);

		if (page == null || page.getElements() == null || page.getElements().isEmpty()) {
			return -1;
		}

		final List<MemberMiniDTO> members = page.getElements();

		for (MemberMiniDTO member : members) {
			if (member.isDeleted()) {
				task(member.getId().toString(), "MMBR", 0);
			} else {
				task(member.getId().toString(), "MMBR", 1);
			}
		}

		return first + size;
	}

	private void task(String uuid, String name, int task) throws ServiceException {
		logTable.insert(uuid, name, task);
	}

	
	@Transactional(rollbackFor = Throwable.class)
	public void addToCommunity(String serviceId, String memberId, String communityId, String role,
		Map<String, String> properties, boolean validated) throws ServiceException {

		Preconditions.checkNotNull(memberId);
		Preconditions.checkNotNull(communityId);
		Preconditions.checkNotNull(role);

		final UUID memberuuid;
		try {
			memberuuid = UUID.fromString(memberId);
		} catch (IllegalArgumentException e) {
			throw new MemberNotFoundException(memberId);
		}

		final UUID communityuuid;
		try {
			communityuuid = UUID.fromString(communityId);
		} catch (IllegalArgumentException e) {
			throw new CommunityNotFoundException(communityId);
		}

		/*
		if (communityuuid.equals(CommunityManager.GLOBAL_ID)) {
			throw new CommunityNotFoundException(communityId);
		}
		*/

		addMembership(memberuuid, communityuuid, role, properties, validated);
		
	}

	
	private MembershipEntity addMembership(UUID memberuuid, UUID communityuuid, String role,
		Map<String, String> properties, boolean validated) throws ServiceException {
		MembershipEntity entity = getDao().getMembershipDeleted(memberuuid, communityuuid);

		if (entity == null) {
			CommunityEntity community = getDao().findById(CommunityEntity.class, communityuuid, false);
			if (community == null || community.isDeleted()) {
				throw new CommunityNotFoundException(communityuuid.toString());
			}
			MemberEntity member = getDao().findById(MemberEntity.class, memberuuid, false);
			if (member == null || member.isDeleted()) {
				throw new MemberNotFoundException(memberuuid.toString());
			}

			entity = new MembershipEntity();
			entity.setCommunity(community);
			entity.setMember(member);
			entity.setRole(role);
			entity.setRequest(Calendar.getInstance());

			Map<String, String> epro = entity.getProperties();
			epro.clear();
			if (properties != null) {
				epro.putAll(properties);
			}

			if (validated) {
				entity.setValidation(Calendar.getInstance());
			}
			getDao().save(entity);
		} else {
			Map<String, String> epro = entity.getProperties();
			epro.clear();
			if (properties != null) {
				epro.putAll(properties);
			}

			entity.setRole(role);
			if (validated) {
				entity.setValidation(Calendar.getInstance());
			}
			
			entity.setDeletion(null);
		}

		return entity;
	}

	
	
	@Autowired
	public void setMembersService(MembersService membersService) {
		this.membersService = membersService;
	}

	@Autowired
	public void setLogTable(LogTableComponent logTable) {
		this.logTable = logTable;
	}

}
