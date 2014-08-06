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
package com.isotrol.impe3.oi.impl;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.oi.api.InterviewDTO;
import com.isotrol.impe3.oi.api.InterviewFilterDTO;
import com.isotrol.impe3.oi.api.InterviewSelDTO;
import com.isotrol.impe3.oi.api.InterviewsService;
import com.isotrol.impe3.oi.api.QaADTO;
import com.isotrol.impe3.oi.api.QaAFilterDTO;
import com.isotrol.impe3.oi.dao.IndexDAO;
import com.isotrol.impe3.oi.mappers.InterviewDTOMapper;
import com.isotrol.impe3.oi.mappers.QaADTOMapper;
import com.isotrol.impe3.oi.model.ClassEntity;
import com.isotrol.impe3.oi.model.InterviewEntity;
import com.isotrol.impe3.oi.server.ClassKey;

/**
 * Interviews service implementation. 
 * @author Emilio Escobar Reyero.
 */
@Service("oiInterviewsService")
public class InterviewsServiceImpl extends AbstractOiService implements InterviewsService {
	/** Member component. */
	@Autowired
	private OIMemberComponent memberComponent;
	
	/** Index access. */
	@Autowired
	private IndexDAO index;
	
	/** Log table access. */
	@Autowired
	private OILogTableComponent logTableComponent;
	
	/** Classification component */
	@Autowired
	private ClassComponent classComponent;
	
	/** Constructor. */
	public InterviewsServiceImpl() {
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.InterviewsService#create(java.lang.String, com.isotrol.impe3.oi.api.InterviewDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public String create(String serviceId, InterviewDTO interview) throws ServiceException {
		final InterviewEntity entity = new InterviewEntity();
		fillEntity(entity, interview);
		saveNewEntity(entity);
		final String uuid = entity.getId().toString();
		updateTask(uuid);
		return uuid;
	}

	/**
	 * @see com.isotrol.impe3.oi.api.InterviewsService#delete(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, String id) throws ServiceException {
		if (id == null) {
			throw new ServiceException();
		}
		final InterviewEntity entity = loadInterviewEntity(id);
		entity.setDeleted(true);
		final String uuid = entity.getId().toString();
		deleteTask(uuid);
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.InterviewsService#getById(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public InterviewDTO getById(String serviceId, String id) throws ServiceException {
		final InterviewEntity entity = loadInterviewEntity(id);
		if (entity != null) {
			return InterviewDTOMapper.INSTANCE.apply(entity);
		}
		return null;
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.InterviewsService#getInterviewQuestions(java.lang.String, com.isotrol.impe3.dto.PageFilter)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<QaADTO> getInterviewQuestions(String serviceId, PageFilter<QaAFilterDTO> filter)
		throws ServiceException {
		Preconditions.checkNotNull(filter);
		Preconditions.checkNotNull(filter.getFilter());
		Preconditions.checkNotNull(filter.getFilter().getId());
		
		if (filter.getFilter().getMember() != null) {
			filter.getFilter().setMemberId(memberComponent.getMember(filter.getFilter().getMember()));
		}
		
		return getDao().findQaAs(filter, QaADTOMapper.INSTANCE);
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.InterviewsService#search(java.lang.String, com.isotrol.impe3.dto.PageFilter)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<InterviewSelDTO> search(String serviceId, PageFilter<InterviewFilterDTO> filter) throws ServiceException {
		if (filter == null) {
			filter = new PageFilter<InterviewFilterDTO>();
			filter.setFilter(new InterviewFilterDTO());
		}
		//return getDao().findInterviews(filter, InterviewSelDTOMapper.INSTANCE);
		return index.findInterviews(filter);
	}

	/**
	 * @see com.isotrol.impe3.oi.api.InterviewsService#update(java.lang.String, com.isotrol.impe3.oi.api.InterviewDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void update(String serviceId, InterviewDTO interview) throws ServiceException {
		Preconditions.checkNotNull(interview);
		final String uuid = interview.getId();
		Preconditions.checkNotNull(uuid);
		final InterviewEntity entity = loadInterviewEntity(uuid);
		fillEntity(entity, interview);
		updateTask(uuid);
	}

	/**
	 * @see com.isotrol.impe3.oi.api.InterviewsService#tag(java.lang.String, java.lang.String, java.util.Set, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void classify(String serviceId, String resource, String set, Set<String> classes) throws ServiceException {
		checkNotNull(resource);
		checkNotNull(set);
		if (classes == null) {
			classes = ImmutableSet.of();
		}
		final long setId = classComponent.getSet(set);
		// final InterviewEntity entity = findById(InterviewEntity.class, resource);
		final InterviewEntity entity = loadInterviewEntity(resource);
		final Set<ClassEntity> previous = entity.getClasses();
		final Set<ClassEntity> newSet = Sets.newHashSet();
		for (ClassEntity classification : entity.getClasses()) {
			if (setId != classification.getSet().getId()) {
				newSet.add(classification);
			}
		}
		for (String name : classes) {
			final long nameId = classComponent.getName(name);
			final ClassEntity classification = classComponent.get(new ClassKey(setId, nameId));

			newSet.add(classification);
		}
		previous.clear();
		previous.addAll(newSet);

		updateTask(entity.getId().toString());
	}

	
	/**
	 * Fill an entity from a DTO. 
	 * @param entity The entity.
	 * @param dto The DTO.
	 */
	private void fillEntity(InterviewEntity entity, InterviewDTO dto) {
		entity.setDate(date2calendar(dto.getDate()));
		entity.setAuthor(dto.getAuthor());
		entity.setDescription(dto.getDescription());
		entity.setExpiration(date2calendar(dto.getExpiration()));
		entity.setInterviewee(dto.getInterviewee());
		entity.setNewQuestionsAllowed(dto.isNewQuestionsAllowed());
		entity.setRelease(date2calendar(dto.getRelease()));
		entity.setTitle(dto.getTitle());
		final Map<String, String> properties = entity.getProperties();
		properties.clear();
		if (dto.getProperties() != null) {
			properties.putAll(dto.getProperties());
		}
		
		Set<ClassEntity> classes = entity.getClasses();
		classes.clear();
		if (dto.getClasses() != null) {
			Map<String, Set<String>> clasificacion = dto.getClasses();
			for (String keySet : clasificacion.keySet()) {
				
				Set<String> classSet = clasificacion.get(keySet);
				for (String clase : classSet) {
					final ClassEntity classEntity = classComponent.get(keySet, clase);
					
					classes.add(classEntity);
				}
			}
		}
	}

	private void updateTask(String uuid) throws ServiceException {
		task(uuid, 1);
	}

	private void deleteTask(String uuid) throws ServiceException {
		task(uuid, 0);
	}

	private void task(String uuid, int task) throws ServiceException {
		task(uuid, "INTE", task);
	}

	private void task(String uuid, String name, int task) throws ServiceException {
		logTableComponent.insert(uuid, name, task);
	}
	
	public void setIndex(IndexDAO index) {
		this.index = index;
	}
	
	public void setLogTableComponent(OILogTableComponent logTableComponent) {
		this.logTableComponent = logTableComponent;
	}
	
	public void setMemberComponent(OIMemberComponent memberComponent) {
		this.memberComponent = memberComponent;
	}
	
	public void setClassComponent(ClassComponent classComponent) {
		this.classComponent = classComponent;
	}
}
