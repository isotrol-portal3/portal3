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


import static com.google.common.base.Predicates.notNull;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.CommunityFilterDTO;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MemberNotFoundException;
import com.isotrol.impe3.web20.api.MigrationService;
import com.isotrol.impe3.web20.mappers.MemberDTOMapper;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.MembershipEntity;
import com.isotrol.impe3.web20.server.CommunityManager;
import com.isotrol.impe3.web20.server.MigrationManager;


@Service("migrationService")
public class MigrationServiceImpl extends AbstractWeb20Service implements MigrationService {

	/** Communities service. */
	private CommunitiesService communitiesService;
	/** Migration manager. */
	private MigrationManager migrationManager;
	/** Log table */
	private LogTableComponent logTable;
	
	//@Transactional(rollbackFor = Throwable.class)
	public void addToCommunity(String serviceId, String memberId, String communityId, String role,
		Map<String, String> properties, boolean validated) throws ServiceException {
		migrationManager.addToCommunity(serviceId, memberId, communityId, role, properties, validated);
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

	
	
	
	// No debe tener el transactional.
	public void markDeletedAllMembers(String serviceId) throws ServiceException {
		int first = 0;
		final int size = 20;
		int total = size;

		while (total > 0) {
			total = migrationManager.markDeletedMembersPage(serviceId, first, size);
		}

	}

	@Transactional(rollbackFor = Throwable.class)
	public void markeDeletedAutomaticCommunities(String serviceId) throws ServiceException {
		PageDTO<CommunityDTO> communities;
		int first = 0;

		communities = communitiesService.search(serviceId, PageFilter.of(new CommunityFilterDTO(), new PaginationDTO(0,
			1)));

		if (communities == null || communities.getTotal() == null) {
			return;
		}

		final int size = communities.getTotal();

		communities = communitiesService.search(serviceId, PageFilter.of(new CommunityFilterDTO(), new PaginationDTO(
			first, size)));
		if (communities != null && communities.getElements() != null && !communities.getElements().isEmpty()) {
			markCommunityAsDeleted(serviceId, communities.getElements());
		}

		sync();

		final List<CommunityEntity> lista = getDao().findAll(CommunityEntity.class);
		lista.size();

	}

	private void markCommunityAsDeleted(String serviceId, List<CommunityDTO> list) throws ServiceException {
		if (list != null && !list.isEmpty()) {
			for (CommunityDTO c : list) {
				if (!c.getId().equals(c.getCode())) {
					communitiesService.delete(serviceId, c.getId());
				}
			}
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public String safeCommunityCreate(String serviceId, CommunityDTO dto) throws ServiceException {
		CommunityEntity entity = findCommunityByLastCode(dto.getCode());

		if (entity != null) {
			entity.setDeleted(false);
			entity.setLastCode(null);
			fillCommunity(entity, dto);
		} else {
			entity = new CommunityEntity();
			fillCommunity(entity, dto);
			saveNewEntity(entity);
		}

		return entity.getId().toString();
	}

	private CommunityEntity fillCommunity(CommunityEntity entity, CommunityDTO dto) {
		final Calendar date = Calendar.getInstance();
		date.setTime(dto.getDate());
		entity.setDate(date);
		entity.setDescription(dto.getDescription());
		entity.setCode(dto.getCode());
		entity.setName(dto.getName());

		final Map<String, String> properties = entity.getProperties();
		properties.clear();
		final Map<String, String> dtopr = dto.getProperties();
		if (dtopr != null) {
			properties.putAll(Maps.filterKeys(Maps.filterValues(dtopr, notNull()), notNull()));
		}

		return entity;
	}

	@Transactional(rollbackFor = Throwable.class)
	public String safeMemberCreate(String serviceId, MemberDTO dto) throws ServiceException {
		MemberEntity entity = findMemberByLastCode(dto.getCode());

		if (entity != null) {
			entity.setDeleted(false);
			entity.setLastMemberCode(null);
			entity.setLastName(null);
			fillMember(entity, dto);
			// getDao().update(entity);
		} else {
			entity = new MemberEntity();
			fillMember(entity, dto);
			saveNewEntity(entity);
			
			final UUID memberuuid = entity.getId();
			
			addMembership(memberuuid, CommunityManager.GLOBAL_ID, "member", null, true);
		}

		//task(entity.getId().toString(), "MMBR", 1);

		return entity.getId().toString();
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public MemberDTO getDeletedMemberByLastCode(String serviceId, String code) throws ServiceException {
		MemberEntity entity = findMemberByLastCode(code);
		
		if (entity == null) {
			return null;
		}
		
		return MemberDTOMapper.INSTANCE.apply(entity);
	}

	public void updateMembersLogTable(String serviceId) throws ServiceException {
		int first = 0;
		int size = 50;
		
		int next = migrationManager.updateMembersLogTable(serviceId, first, size);
		
		while (next > 0) {
			next = migrationManager.updateMembersLogTable(serviceId, next, size);
		}
	}

	private void task(String uuid, String name, int task) throws ServiceException {
		logTable.insert(uuid, name, task);
	}
	
	private MemberEntity fillMember(MemberEntity entity, MemberDTO dto) {
		final Calendar date = Calendar.getInstance();
		date.setTime(dto.getDate());
		entity.setDate(date);
		entity.setDisplayName(dto.getDisplayName());
		entity.setEmail(dto.getEmail());
		entity.setMemberCode(dto.getCode());
		entity.setName(dto.getName());
		entity.setBlocked(dto.isBlocked());

		final Set<String> profiles = entity.getProfiles();
		profiles.clear();
		final Set<String> dtopf = dto.getProfiles();
		if (dtopf != null) {
			profiles.addAll(Sets.filter(dtopf, notNull()));
		}
		final Map<String, String> properties = entity.getProperties();
		properties.clear();
		final Map<String, String> dtopr = dto.getProperties();
		if (dtopr != null) {
			properties.putAll(Maps.filterKeys(Maps.filterValues(dtopr, notNull()), notNull()));
		}

		return entity;
	}

	// no transactional
	public void safeAddToGlobal(String serviceId) throws ServiceException {
		final List<String> membersId = migrationManager.getMemberWithoutGlobal(serviceId);
		for (String id : membersId) {
			if (id != null) {
				migrationManager.addToCommunity(serviceId, id, CommunityManager.GLOBAL_STR_ID, "member", null, true);
			}
		}
		
	}
	
	
	private CommunityEntity findCommunityByLastCode(String lastCode) {
		return getDao().findCommunityByLastCode(lastCode);
	}

	private MemberEntity findMemberByLastCode(String lastCode) {
		return getDao().findMemberByLastCode(lastCode);
	}

	@Autowired
	public void setCommunitiesService(CommunitiesService communitiesService) {
		this.communitiesService = communitiesService;
	}

	@Autowired
	public void setMigrationManager(MigrationManager migrationManager) {
		this.migrationManager = migrationManager;
	}
	
	@Autowired
	public void setLogTable(LogTableComponent logTable) {
		this.logTable = logTable;
	}
}
