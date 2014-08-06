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
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.dto.NotFoundException;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;
import com.isotrol.impe3.web20.api.CommunityNoticesService;
import com.isotrol.impe3.web20.api.MemberNotFoundException;
import com.isotrol.impe3.web20.api.NoticeDTO;
import com.isotrol.impe3.web20.api.NoticeFilterDTO;
import com.isotrol.impe3.web20.mappers.NoticeDTOMapper;
import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.NoticeEntity;

/**
 * 
 * @author Emilio Escobar Reyero
 */
@Service("noticesService")
public class CommunityNoticesServiceImpl extends AbstractWeb20Service implements CommunityNoticesService {

	/**
	 * @see com.isotrol.impe3.web20.api.CommunityNoticesService#getCommunityNotices(java.lang.String, com.isotrol.impe3.web20.api.NoticeFilterDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<NoticeDTO> getCommunityNotices(String serviceId, NoticeFilterDTO filter) throws ServiceException {
		if (filter == null) {
			throw new CommunityNotFoundException("");
		}
		return getDao().findNotices(filter, NoticeDTOMapper.INSTANCE);
	}
	
	/**
	 * @see com.isotrol.impe3.web20.api.CommunityNoticesService#create(java.lang.String, com.isotrol.impe3.web20.api.NoticeDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Long create(String serviceId, NoticeDTO dto) throws ServiceException {
		final NoticeEntity entity = new NoticeEntity();
		fill(entity, dto);
		getDao().save(entity);
		return entity.getId();
	}
	
	/**
	 * @see com.isotrol.impe3.web20.api.CommunityNoticesService#delete(java.lang.String, java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, Long id) throws ServiceException {
		if (id == null) {
			throw new NotFoundException();
		}
		final NoticeEntity entity = load(id);
		entity.setDeleted(true);
		
	}
	
	/**
	 * @see com.isotrol.impe3.web20.api.CommunityNoticesService#update(java.lang.String, com.isotrol.impe3.web20.api.NoticeDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void update(String serviceId, NoticeDTO dto) throws ServiceException {
		Preconditions.checkNotNull(dto);
		final Long id = dto.getId();
		Preconditions.checkNotNull(id);
		
		final NoticeEntity entity = load(id);
		fill(entity, dto);
	}
	
	/**
	 * @see com.isotrol.impe3.web20.api.CommunityNoticesService#getById(java.lang.String, java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public NoticeDTO getById(String serviceId, Long id) throws ServiceException {
		final NoticeEntity entity = load(id);
		
		if (entity != null) {
			return NoticeDTOMapper.INSTANCE.apply(entity);
		}
		
		return null;
	}
	
	private void fill(NoticeEntity entity, NoticeDTO dto) throws ServiceException {
		final Calendar date = Calendar.getInstance();
		date.setTime(dto.getDate());
		entity.setDate(date);
		entity.setDescription(dto.getDescription());
		entity.setTitle(dto.getTitle());
		
		if (dto.getExpiration() != null) {
			final Calendar expiration = Calendar.getInstance();
			expiration.setTime(dto.getExpiration());
			entity.setExpiration(expiration);
		}
		if (dto.getRelease() != null) {
			final Calendar release = Calendar.getInstance();
			release.setTime(dto.getRelease());
			entity.setRelease(release);
		}
		
		entity.setCommunity(loadCommunity(dto.getCommunity()));
		entity.setMember(loadMember(dto.getMember()));
	}
	
	private MemberEntity loadMember(String id) throws MemberNotFoundException {
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

	
	private NoticeEntity load(Long id) throws ServiceException {
		if (id == null) {
			throw new NotFoundException();
		}
		
		final NoticeEntity entity = getDao().findById(NoticeEntity.class, id, false);
		if (entity == null || entity.isDeleted()) {
			throw new NotFoundException();
		}
		
		return entity;
	}
	
}
