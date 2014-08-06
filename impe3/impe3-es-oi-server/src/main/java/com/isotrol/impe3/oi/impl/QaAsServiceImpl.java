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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.oi.api.QaADTO;
import com.isotrol.impe3.oi.api.QaAFilterDTO;
import com.isotrol.impe3.oi.api.QaAsService;
import com.isotrol.impe3.oi.mappers.QaADTOMapper;
import com.isotrol.impe3.oi.model.OIMemberEntity;
import com.isotrol.impe3.oi.model.QaAEntity;


/**
 * Questions and Answers service. 
 * @author Emilio Escobar Reyero
 */
@Service("oiQuestionsService")
public final class QaAsServiceImpl extends AbstractOiService implements QaAsService {
	/** Member component. */
	@Autowired
	private OIMemberComponent memberComponent;
	
	/** Constructor. */
	public QaAsServiceImpl() {
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#getById(java.lang.String, java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public QaADTO getById(String serviceId, Long id) throws ServiceException {
		final QaAEntity entity = loadQaAEntity(id);
		if (entity != null) {
			return QaADTOMapper.INSTANCE.apply(entity);
		}
		return null;
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#ask(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Long ask(String serviceId, String interview, String question, String member) throws ServiceException {
		final QaADTO dto = new QaADTO();
		dto.setDate(new Date());
		dto.setInterview(interview);
		dto.setQuestion(question);
		dto.setMemberId(member);
		
		return create(serviceId, dto);
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#answer(java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void answer(String serviceId, Long id, String answer) throws ServiceException {
		final QaAEntity entity = loadQaAEntity(id);
		entity.setAnswer(answer);
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#rate(java.lang.String, java.lang.Long, java.lang.String, java.lang.Double, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void rate(String serviceId, Long id, String member, Double value, boolean allowRateCreator)
		throws ServiceException {
		checkNotNull(member);		
		checkNotNull(value);
		final QaAEntity entity = loadQaAEntity(id);
		if (!allowRateCreator && entity.getMember() != null && member.equals(entity.getMember().getMemberId())) {
			throw new ServiceException();
		}
		
		entity.setRate(Double.valueOf(entity.getRate() + value.doubleValue()));
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#order(java.lang.String, java.lang.Long, java.lang.Integer)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void order(String serviceId, Long id, Integer value) throws ServiceException {
		checkNotNull(value);
		final QaAEntity entity = loadQaAEntity(id);
		entity.setOrder(value);
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#validate(java.lang.String, java.lang.Long, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void validate(String serviceId, Long id, boolean valid) throws ServiceException {
		final QaAEntity entity = loadQaAEntity(id);
		entity.setValid(valid);
	}

	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#create(java.lang.String, com.isotrol.impe3.oi.api.QaADTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Long create(String serviceId, QaADTO dto) throws ServiceException {
		QaAEntity entity = new QaAEntity();
		fill(entity, dto);
		getDao().save(entity);
		return entity.getId();
	}

	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#update(java.lang.String, com.isotrol.impe3.oi.api.QaADTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void update(String serviceId, QaADTO dto) throws ServiceException {
		checkNotNull(dto);
		checkNotNull(dto.getId());
		final QaAEntity entity = loadQaAEntity(dto.getId());
		fill(entity, dto);
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#delete(java.lang.String, java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, Long id) throws ServiceException {
		checkNotNull(id);
		final QaAEntity entity = loadQaAEntity(id);
		entity.setDeleted(true);
	}
	
	/**
	 * @see com.isotrol.impe3.oi.api.QaAsService#search(java.lang.String, com.isotrol.impe3.dto.PageFilter)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDTO<QaADTO> search(String serviceId, PageFilter<QaAFilterDTO> filter) throws ServiceException {
		if (filter == null) {
			filter = new PageFilter<QaAFilterDTO>();
			filter.setFilter(new QaAFilterDTO());
		}
		
		if (filter.getFilter().getMember() != null) {
			filter.getFilter().setMemberId(memberComponent.getMember(filter.getFilter().getMember()));
		}
		
		return getDao().findQaAs(filter, QaADTOMapper.INSTANCE);
	}
	
	
	private void fill(QaAEntity entity, QaADTO dto) throws ServiceException {
		entity.setAnswer(dto.getAnswer());
		entity.setDate(date2calendar(dto.getDate()));
		entity.setOrder(dto.getOrder());
		entity.setQuestion(dto.getQuestion());
		entity.setRate(dto.getRate());
		entity.setValid(dto.isValid());
		
		entity.setInterview(loadInterviewEntity(dto.getInterview()));
		
		Long id = memberComponent.getMember(dto.getMemberId());
		entity.setMember(getDao().findById(OIMemberEntity.class, id, false));
	}
	
	private QaAEntity loadQaAEntity(Long id) throws ServiceException {
		if (id == null) {
			throw new ServiceException();
		}
		final QaAEntity entity = getDao().findById(QaAEntity.class, id, false);
		if (entity == null) {
			throw new ServiceException();
		}
		return entity;
	}
	
	public void setMemberComponent(OIMemberComponent memberComponent) {
		this.memberComponent = memberComponent;
	}
}
