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
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MemberNotFoundException;
import com.isotrol.impe3.web20.api.MemberSelDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;
import com.isotrol.impe3.web20.dao.MemberIdxDAO;
import com.isotrol.impe3.web20.mappers.MemberDTOMapper;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.MembershipEntity;
import com.isotrol.impe3.web20.server.CommunityManager;


/**
 * Implementation of MembersService.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero.
 */
@Service("membersService")
public final class MembersServiceImpl extends AbstractWeb20Service implements MembersService {
	/** Community manager. */
	@Autowired
	private CommunityManager communityManager;

	/** Members indexed DAO. */
	private MemberIdxDAO idx;

	private LogTableComponent logTable;

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#search(java.lang.String,
	 * com.isotrol.impe3.web20.api.MemberFilterDTO)
	 */
	// @Transactional(rollbackFor = Throwable.class)
	public PageDTO<MemberSelDTO> search(String serviceId, PageFilter<MemberFilterDTO> filter) {
		if (filter == null) {
			filter = new PageFilter<MemberFilterDTO>();
		}
		// return getDao().findMembers(filter, MemberSelDTOMapper.INSTANCE);
		return idx.findMembers(filter);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#create(java.lang.String, com.isotrol.impe3.web20.api.MemberDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public String create(String serviceId, MemberDTO member) throws ServiceException {
		final MemberEntity entity = new MemberEntity();
		fill(entity, member);
		saveNewEntity(entity);
		final UUID uuid = entity.getId();

		addMembership(uuid, CommunityManager.GLOBAL_ID, "member", null, true);

		updateTask(uuid.toString());

		return uuid.toString();
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#delete(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, String id) throws ServiceException {
		final MemberEntity entity = load(id);
		entity.setDeleted(true);
		entity.setLastMemberCode(entity.getMemberCode());
		entity.setLastName(entity.getName());
		entity.setMemberCode(entity.getId().toString());
		entity.setName(entity.getId().toString());

		deleteTask(entity.getId().toString());
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#getByCode(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public MemberDTO getByCode(String serviceId, String code) throws ServiceException {
		final MemberEntity entity = getDao().getMemberByCode(code);
		if (entity != null) {
			return MemberDTOMapper.INSTANCE.apply(entity);
		}
		return null;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#getById(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public MemberDTO getById(String serviceId, String id) throws ServiceException {
		final MemberEntity entity = load(id);
		if (entity != null) {
			return MemberDTOMapper.INSTANCE.apply(entity);
		}
		return null;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#getByName(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public MemberDTO getByName(String serviceId, String name) throws ServiceException {
		MemberEntity entity = getDao().getMemberByName(name);
		if (entity != null) {
			return MemberDTOMapper.INSTANCE.apply(entity);
		}
		return null;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#update(java.lang.String, com.isotrol.impe3.web20.api.MemberDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void update(String serviceId, MemberDTO member) throws ServiceException {
		Preconditions.checkNotNull(member);
		final String uuid = member.getId();
		Preconditions.checkNotNull(uuid);
		final MemberEntity entity = load(uuid);
		fill(entity, member);

		updateTask(uuid);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#getMembership(java.lang.String, java.lang.String,
	 * java.lang.Boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<MembershipSelDTO> getMemberships(String serviceId, PageFilter<MembershipSelFilterDTO> filter)
		throws ServiceException {
		Preconditions.checkNotNull(filter);
		Preconditions.checkNotNull(filter.getFilter());
		filter.setFilter(filter.getFilter().putGlobal(false));
		return getDao().getMemberMemberships(filter, communityManager.toMembershipSelDTO());
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#addToCommunity(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
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

		if (communityuuid.equals(CommunityManager.GLOBAL_ID)) {
			throw new CommunityNotFoundException(communityId);
		}

		addMembership(memberuuid, communityuuid, role, properties, validated);

		updateTask(memberId);
	}

	private MembershipEntity addMembership(UUID memberuuid, UUID communityuuid, String role,
		Map<String, String> properties, boolean validated) throws ServiceException {
		MembershipEntity entity = getDao().getMembership(memberuuid, communityuuid);

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
		}

		return entity;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.MembersService#removeFromCommunity(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void removeFromCommunity(String serviceId, String memberId, String communityId) throws ServiceException {
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

		updateTask(memberId);
	}

	private MemberEntity fill(MemberEntity entity, MemberDTO dto) {
		final Calendar date = Calendar.getInstance();
		date.setTime(dto.getDate());
		entity.setDate(date);
		entity.setDisplayName(dto.getDisplayName());
		entity.setEmail(dto.getEmail());
		entity.setMemberCode(dto.getCode());
		entity.setName(dto.getName());
		entity.setBlocked(dto.isBlocked());

		// final Set<FavoriteEntity> favorites = entity.getFavorites();
		// TODO no se contemplan en el dto.

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

	private void updateTask(String uuid) throws ServiceException {
		task(uuid, 1);
	}

	private void deleteTask(String uuid) throws ServiceException {
		task(uuid, 0);
	}

	private void task(String uuid, int task) throws ServiceException {
		task(uuid, "MMBR", task);
	}

	private void task(String uuid, String name, int task) throws ServiceException {
		logTable.insert(uuid, name, task);
	}

	@Autowired
	public void setIdx(MemberIdxDAO idx) {
		this.idx = idx;
	}

	@Autowired
	public void setLogTable(LogTableComponent logTable) {
		this.logTable = logTable;
	}

}
