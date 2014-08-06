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
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.CommunityFilterDTO;
import com.isotrol.impe3.web20.api.CommunityMemberSelDTO;
import com.isotrol.impe3.web20.api.CommunityMembersFilterDTO;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;
import com.isotrol.impe3.web20.api.CommunitySelDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MemberNotFoundException;
import com.isotrol.impe3.web20.api.MemberSelDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.dao.MemberIdxDAO;
import com.isotrol.impe3.web20.mappers.CommunityMembersSelDTOMapper;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.model.MembershipEntity;
import com.isotrol.impe3.web20.server.CommunityManager;
import com.isotrol.impe3.web20.server.MemberCountManager;


/**
 * Implementation of CommunitiesService.
 * @author Emilio Escobar Reyero.
 */
@Service("communitiesService")
public final class CommunitiesServiceImpl extends AbstractWeb20Service implements InitializingBean, CommunitiesService,
	CommunityManager {
	/** Member count manager. */
	@Autowired
	private MemberCountManager memberCountManager;
	/** Scheduler. */
	@Autowired
	private SchedulerComponent scheduler;
	/** Member index */
	@Autowired
	private MemberIdxDAO idx;
	/** Members service. */
	@Autowired
	private MembersService membersService;

	private final Function<CommunityEntity, CommunityDTO> toCommunityDTO = new Function<CommunityEntity, CommunityDTO>() {
		public CommunityDTO apply(CommunityEntity from) {
			return fillDTO(from, new CommunityDTO());
		}
	};

	private final Function<MembershipEntity, MembershipSelDTO> toMembershipSelDTO = new Function<MembershipEntity, MembershipSelDTO>() {
		public MembershipSelDTO apply(MembershipEntity entity) {
			final MembershipSelDTO dto = new MembershipSelDTO();
			dto.setId(entity.getId());
			dto.setVersion(entity.getVersion());
			dto.setCommunity(toCommunityDTO.apply(entity.getCommunity()));
			dto.setRole(entity.getRole());
			dto.setValidated(entity.getValidation() != null);
			dto.setProperties(Maps.newHashMap(entity.getProperties()));

			return dto;
		}
	};

	/** Constructor. */
	public CommunitiesServiceImpl() {
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		final Runnable global = new Runnable() {
			public void run() {
				memberCountManager.refresh();
			}
		};
		scheduler.scheduleWithFixedDelay(global, 0L, 5L, TimeUnit.SECONDS);
	}

	private <T extends CommunityDTO> T fillDTO(CommunityEntity entity, T dto) {
		final UUID id = entity.getId();
		dto.setId(id.toString());
		dto.setVersion(entity.getVersion());
		dto.setCode(entity.getCode());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setDate(entity.getDate().getTime());
		
		dto.setProperties(Maps.newHashMap(entity.getProperties()));
		
		return memberCountManager.fill(id, dto);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#search(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<CommunityDTO> search(String serviceId, PageFilter<CommunityFilterDTO> filter) {
		if (filter == null) {
			filter = new PageFilter<CommunityFilterDTO>();
			filter.setFilter(new CommunityFilterDTO());
		}
		return getDao().findCommunities(filter, toCommunityDTO);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#findCommunities(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityFilterDTO, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<CommunitySelDTO> findCommunities(String serviceId, PageFilter<CommunityFilterDTO> filter, final String role)
		throws ServiceException {
		if (filter == null) {
			filter = new PageFilter<CommunityFilterDTO>();
			filter.setFilter(new CommunityFilterDTO());
		}

		//final PageFilter<CommunityMembersFilterDTO> membersFilter = new PageFilter<CommunityMembersFilterDTO>();
		//membersFilter.setFilter(new CommunityMembersFilterDTO().putRole(role).putValidated(true));
		//membersFilter.setPagination(new PaginationDTO(0, 1));

		final Function<CommunityEntity, CommunitySelDTO> toDTO = new Function<CommunityEntity, CommunitySelDTO>() {
			public CommunitySelDTO apply(CommunityEntity from) {
				final CommunitySelDTO dto = fillDTO(from, new CommunitySelDTO());

				final PageFilter<CommunityMembersFilterDTO> cFilter = new PageFilter<CommunityMembersFilterDTO>();
				cFilter.setPagination(new PaginationDTO(0, 1));
				cFilter.setFilter(new CommunityMembersFilterDTO().putId(dto.getId()).putRole(role).putValidated(true));
				
				final PageDTO<CommunityMemberSelDTO> pageMembers = getDao().getCommunityMemberships(cFilter,
					CommunityMembersSelDTOMapper.INSTANCE);
				dto.setMembership(pageMembers.getSize() > 0 && pageMembers.getTotal() > 0 ? pageMembers.getElements()
					.get(0) : null);
				return dto;
			}
		};
		return getDao().findCommunities(filter, toDTO);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#create(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public String create(String serviceId, CommunityDTO community) throws ServiceException {
		final CommunityEntity entity = new CommunityEntity();
		fill(entity, community);
		saveNewCommunity(entity);
		return entity.getId().toString();
	}

	protected CommunityEntity saveNewCommunity(CommunityEntity entity) {
		UUID uuid = newUUID();
		entity.setId(uuid);
		if (entity.getCode() == null) {
			entity.setCode(uuid.toString());
		}
		getDao().save(entity);
		return entity;
	}
	
	
	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#update(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void update(String serviceId, CommunityDTO community) throws ServiceException {
		Preconditions.checkNotNull(community);
		final String uuid = community.getId();
		Preconditions.checkNotNull(uuid);

		if (uuid.equals(CommunityManager.GLOBAL_STR_ID)) {
			throw new CommunityNotFoundException(uuid);
		}

		final CommunityEntity entity = load(uuid);
		fill(entity, community);

	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#delete(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, String id) throws ServiceException {
		if (id != null && id.equals(CommunityManager.GLOBAL_STR_ID)) {
			throw new CommunityNotFoundException(id);
		}

		final CommunityEntity entity = load(id);
		entity.setDeleted(true);
		entity.setLastCode(entity.getCode());
		entity.setCode(entity.getId().toString());
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#getByCode(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public CommunityDTO getByCode(String serviceId, String code) {
		final CommunityEntity entity = getDao().getCommunityByCode(code);
		if (entity != null) {
			return toCommunityDTO.apply(entity);
		}
		return null;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#getById(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public CommunityDTO getById(String serviceId, String id) throws ServiceException {
		final CommunityEntity entity = load(id);
		if (entity != null) {
			return toCommunityDTO.apply(entity);
		}
		return null;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#getCommunityMembers(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityMembersFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<CommunityMemberSelDTO> getCommunityMembers(String serviceId,
		PageFilter<CommunityMembersFilterDTO> filter) throws ServiceException {
		Preconditions.checkNotNull(filter);

		final PageDTO<CommunityMemberSelDTO> page = new PageDTO<CommunityMemberSelDTO>();

		final PageDTO<String> memberIds = idx.findCommunityMembers(filter);

		if (memberIds != null) {
			page.setTotal(memberIds.getTotal());
			page.setFirst(memberIds.getFirst());
			page.setSize(memberIds.getSize());
			final List<String> uuids = memberIds.getElements();
			if (uuids != null) {
				final UUID communityuuid = getOptionalCommunityUUID(filter.getFilter().getId());
				final List<CommunityMemberSelDTO> elements = Lists
					.<CommunityMemberSelDTO> newArrayListWithExpectedSize(uuids.size());
				for (String uuid : uuids) {
					final MembershipEntity entity = getDao().getMembership(getMemberUUID(uuid), communityuuid);
					if (entity == null) {
						final MemberSelDTO m = new MemberSelDTO();
						m.setId(CommunityManager.GLOBAL_STR_ID);
						m.setDisplayName("Eliminando usuario...");
						m.setName("");
						final CommunityMemberSelDTO cm = new CommunityMemberSelDTO();
						cm.setId(-1L);
						cm.setMember(m);
						elements.add(cm);
					} else {
						elements.add(CommunityMembersSelDTOMapper.INSTANCE.apply(entity));
					}
				}
				page.setElements(elements);
			} else {
				page.setElements(Lists.<CommunityMemberSelDTO> newArrayListWithExpectedSize(0));
			}
		} else {
			page.setFirst(filter.getPagination().getFirst());
			page.setTotal(0);
			page.setSize(0);
			page.setElements(Lists.<CommunityMemberSelDTO> newArrayListWithExpectedSize(0));
		}

		return page;
	}

	private UUID getMemberUUID(String id) throws MemberNotFoundException {
		if (id == null) {
			throw new MemberNotFoundException("");
		}

		UUID uuid;
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new MemberNotFoundException(id);
		}
		return uuid;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#getCommunitiesById(java.lang.String, java.util.Set)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Map<String, CommunityDTO> getCommunitiesById(String serviceId, Set<String> ids) throws ServiceException {
		Preconditions.checkNotNull(ids);
		final Map<String, CommunityDTO> communities = Maps.newHashMapWithExpectedSize(ids.size());

		for (String id : ids) {
			try {
				final CommunityEntity entity = load(id);
				if (entity != null) {
					communities.put(id, toCommunityDTO.apply(entity));
				}
			} catch (CommunityNotFoundException e) {
				// next step.
			}
		}

		return communities;
	}

	private CommunityEntity fill(CommunityEntity entity, CommunityDTO dto) {
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

	private CommunityEntity load(String id) throws ServiceException {
		if (id == null) {
			throw new CommunityNotFoundException(id);
		}
		final UUID uuid;
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new CommunityNotFoundException(id);
		}
		final CommunityEntity entity = getDao().findById(CommunityEntity.class, uuid, false);
		if (entity == null || entity.isDeleted()) {
			throw new CommunityNotFoundException(id);
		}

		return entity;
	}

	/* Community manager. */

	/**
	 * @see com.isotrol.impe3.web20.server.CommunityManager#getGlobalCommunity()
	 */
	@Transactional(rollbackFor = Throwable.class)
	public CommunityEntity getGlobalCommunity() {
		CommunityEntity global = findById(CommunityEntity.class, GLOBAL_ID);
		if (global == null) {
			global = new CommunityEntity();
			global.setId(GLOBAL_ID);
			global.setCode(GLOBAL_STR_ID);
			global.setName(GLOBAL_STR_ID);
			global.setDate(Calendar.getInstance());
			getDao().save(global);
			sync();
		}
		return global;
	}

	/**
	 * @see com.isotrol.impe3.web20.server.CommunityManager#toCommunityDTO()
	 */
	public Function<CommunityEntity, CommunityDTO> toCommunityDTO() {
		return toCommunityDTO;
	}

	/**
	 * @see com.isotrol.impe3.web20.server.CommunityManager#toMembershipSelDTO()
	 */
	public Function<MembershipEntity, MembershipSelDTO> toMembershipSelDTO() {
		return toMembershipSelDTO;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#addMembers(java.lang.String, java.lang.String,
	 * com.isotrol.impe3.web20.api.MemberFilterDTO, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void addMembers(String serviceId, String communityId, MemberFilterDTO filter, String role,
		boolean validated, boolean keep) throws ServiceException {
		if (filter == null) {
			filter = new MemberFilterDTO();
		}
		if (role == null || communityId == null || CommunityManager.GLOBAL_STR_ID.equals(communityId)
			|| (keep && filter.getCommunityId() != null && communityId.equals(filter.getCommunityId()))) {
			return;
		}

		final PageFilter<MemberFilterDTO> pageFilter = new PageFilter<MemberFilterDTO>();
		pageFilter.setFilter(filter);
		pageFilter.setPagination(new PaginationDTO(0, 4096));

		final PageDTO<String> memberIds = idx.findMemberIds(pageFilter);

		if (memberIds != null && memberIds.getElements() != null) {
			UUID communityuuid = getOptionalCommunityUUID(communityId);
			for (String id : memberIds.getElements()) {
				if (!keep) {
					if (getDao().getMembership(getMemberUUID(id), communityuuid) != null) {
						membersService.removeFromCommunity(serviceId, id, communityId);
					}
					membersService.addToCommunity(serviceId, id, communityId, role, null, validated);
				} else {
					if (getDao().getMembership(getMemberUUID(id), communityuuid) == null) {
						membersService.addToCommunity(serviceId, id, communityId, role, null, validated);
					}
				}
			}
		}
	}

	/**
	 * @see com.isotrol.impe3.web20.api.CommunitiesService#removeMembers(java.lang.String,
	 * com.isotrol.impe3.web20.api.CommunityMembersFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void removeMembers(String serviceId, CommunityMembersFilterDTO filter) throws ServiceException {
		if (filter == null || filter.getId() == null) {
			return;
		}
		final PageFilter<CommunityMembersFilterDTO> pageFilter = new PageFilter<CommunityMembersFilterDTO>();
		pageFilter.setFilter(filter);
		pageFilter.setPagination(new PaginationDTO(0, 4096));

		final PageDTO<String> memberIds = idx.findCommunityMembers(pageFilter);
		if (memberIds != null && memberIds.getElements() != null) {
			final String communityId = filter.getId();
			for (String id : memberIds.getElements()) {
				membersService.removeFromCommunity(serviceId, id, communityId);
			}
		}
	}

}
