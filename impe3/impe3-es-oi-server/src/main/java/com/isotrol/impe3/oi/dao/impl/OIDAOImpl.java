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
package com.isotrol.impe3.oi.dao.impl;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.isotrol.impe3.dto.OrderDTO;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.hib.query.FilterSupport;
import com.isotrol.impe3.hib.query.PageSupport;
import com.isotrol.impe3.oi.api.InterviewFilterDTO;
import com.isotrol.impe3.oi.api.QaAFilterDTO;
import com.isotrol.impe3.oi.dao.DAO;
import com.isotrol.impe3.oi.model.ClassEntity;
import com.isotrol.impe3.oi.model.ClassNameEntity;
import com.isotrol.impe3.oi.model.ClassSetEntity;
import com.isotrol.impe3.oi.model.InterviewEntity;
import com.isotrol.impe3.oi.model.OIMemberEntity;
import com.isotrol.impe3.oi.model.QaAEntity;
import com.isotrol.impe3.oi.model.OISequenceEntity;
import com.isotrol.impe3.oi.server.ClassKey;
import com.isotrol.impe3.oi.model.OILogTableEntity;
import com.isotrol.impe3.oi.server.LogTableFilterDTO;



/**
 * DAO implementation.
 * @author Emilio Escobar Reyero
 */
@Repository
public class OIDAOImpl extends com.isotrol.impe3.hib.dao.DAOImpl implements DAO {

	/** Constructor. */
	public OIDAOImpl() {
	}

	/**
	 * @see com.isotrol.impe3.oi.dao.DAO#getClassification(com.isotrol.impe3.oi.server.ClassKey)
	 */
	public ClassEntity getClassification(ClassKey key) {
		checkNotNull(key);
		return unique(ClassEntity.class, getNamedQuery(ClassEntity.BY_KEY).setLong(0, key.getSet()).setLong(1,
			key.getName()));
	}

	/**
	 * @see com.isotrol.impe3.oi.dao.DAO#getClassName(java.lang.String)
	 */
	public ClassNameEntity getClassName(String name) {
		checkNotNull(name);
		return unique(ClassNameEntity.class, ClassNameEntity.BY_NAME, name);
	}

	/**
	 * @see com.isotrol.impe3.oi.dao.DAO#getClassSet(java.lang.String)
	 */
	public ClassSetEntity getClassSet(String name) {
		checkNotNull(name);
		return unique(ClassSetEntity.class, ClassSetEntity.BY_NAME, name);
	}

	/**
	 * @see com.isotrol.impe3.oi.dao.DAO#loadClassesBySetName(java.lang.String)
	 */
	public List<ClassEntity> loadClassesBySetName(String set) {
		checkNotNull(set);
		return list(ClassEntity.class, ClassEntity.BY_SET_NAME, set);
	}

	/**
	 * @see com.isotrol.impe3.oi.dao.DAO#loadAppliedClasses()
	 */
	public List<ClassEntity> loadAppliedClasses() {
		return list(ClassEntity.class, getNamedQuery(ClassEntity.USED));
	}

	/**
	 * @see com.isotrol.impe3.oi.dao.DAO#findInterviews(com.isotrol.impe3.dto.PageFilter,
	 * com.google.common.base.Function)
	 */
	public <D> PageDTO<D> findInterviews(PageFilter<InterviewFilterDTO> filter,
		Function<? super InterviewEntity, ? extends D> transformer) {
		checkNotNull(filter, "A page filter for interviews must be provided");
		checkNotNull(filter.getFilter(), "A search filter for interviews must be provided");
		checkNotNull(transformer, "A transformation function for interviews must be provided");
		final Criteria count = getInterviewCountCriteria(filter);
		final Criteria select = getInterviewSelectCriteria(filter);
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	private Criteria getInterviewSelectCriteria(PageFilter<InterviewFilterDTO> filter) {
		Criteria c = getInterviewCountCriteria(filter);
		orderSelect(c, filter.getOrderings());
		return c;
	}

	private Criteria getInterviewCountCriteria(PageFilter<InterviewFilterDTO> filter) {
		Criteria c = newCriteria(InterviewEntity.class);
		c.add(Restrictions.eq("deleted", Boolean.FALSE));

		c = FilterSupport.or(c, "title", filter.getFilter().getTitle(), "author", filter.getFilter().getAuthor(),
			"interviewee", filter.getFilter().getInterviewee(), "description", filter.getFilter().getDescription());

		
		if (filter.getFilter().isActive() == null) {
			// nothing to do.
		} else if (filter.getFilter().isActive().intValue() < 0) {
			// released
		} else if (filter.getFilter().isActive() == 0) {
			// true released and not expired
			c.add(Restrictions.or(Restrictions.isNull("expiration"), Restrictions.gt("expiration", Calendar
				.getInstance())));
			c.add(Restrictions.le("release", Calendar.getInstance()));
		} else {
			// expired
			c.add(Restrictions.or(Restrictions.or(Restrictions.isNull("release"), Restrictions.gt("release", Calendar
				.getInstance())), Restrictions.le("expiration", Calendar.getInstance())));
		}
		
		c.createAlias("classes", "c", Criteria.LEFT_JOIN);
		
		return c;
	}

	/**
	 * Adds order to criteria.
	 * @param c The criteria.
	 * @param orderings The orderings.
	 */
	private void orderSelect(Criteria c, List<OrderDTO> orderings) {
		if (orderings != null && c != null) {
			for (OrderDTO order : orderings) {
				c.addOrder(order.getAscending() ? Order.asc(order.getName()) : Order.desc(order.getName()));
			}
		}
	}
	
	/**
	 * @see com.isotrol.impe3.oi.dao.DAO#findQaAs(com.isotrol.impe3.dto.PageFilter, com.google.common.base.Function)
	 */
	public <D> PageDTO<D> findQaAs(PageFilter<QaAFilterDTO> filter, Function<? super QaAEntity, ? extends D> transformer) {
		checkNotNull(filter, "A page filter for questions must be provided");
		checkNotNull(filter.getFilter(), "A search filter for questions must be provided");
		checkNotNull(transformer, "A transformation function for questions must be provided");
		final Criteria count = getQaACountCriteria(filter);
		final Criteria select = getQaASelectCriteria(filter);
		return PageSupport.getPage(count, select, filter.getPagination(), transformer);
	}

	private Criteria getQaASelectCriteria(PageFilter<QaAFilterDTO> filter) {
		Criteria c = getQaACountCriteria(filter);
		orderSelect(c, filter.getOrderings());
		return c;
	}

	private Criteria getQaACountCriteria(PageFilter<QaAFilterDTO> filter) {
		Criteria c = newCriteria(QaAEntity.class);
		c.add(Restrictions.eq("deleted", Boolean.FALSE));
		
		if (filter.getFilter().getId() != null) {
			c.add(Restrictions.eq("interview.id", filter.getFilter().getId()));
		}

		if (filter.getFilter().getMemberId() != null) {
			c.add(Restrictions.eq("member.id", filter.getFilter().getMemberId()));
		}
		
		if (filter.getFilter().isValid() == null) {
			// nothing to do
		} else if (filter.getFilter().isValid()) {
			c.add(Restrictions.eq("valid", Boolean.TRUE));
		} else {
			c.add(Restrictions.eq("valid", Boolean.FALSE));
		}
		
		if (filter.getFilter().isAnswered() == null) {
			// nothing to do
		} else if (filter.getFilter().isAnswered()) {
			c.add(Restrictions.isNotNull("answer"));
		} else {
			c.add(Restrictions.isNull("answer"));
		}
		
		return c;
	}
	
	/**
	 * @see com.isotrol.impe3.oi.dao.DAO#getMemberById(java.lang.String)
	 */
	public OIMemberEntity getMemberById(String key) {
		checkNotNull(key);
		return unique(OIMemberEntity.class, OIMemberEntity.BY_MEMBERID, key);
	}
	
	/**
	 * @throws SequenceNotFoundException
	 * @see com.isotrol.impe3.oi.dao.DAO#getNextValue(java.lang.String)
	 */
	public synchronized long getNextValue(String id) throws ServiceException {
		OISequenceEntity seq = (OISequenceEntity) getSession().get(OISequenceEntity.class, id, LockMode.UPGRADE);
		if (seq == null) {
			seq = new OISequenceEntity(id, 0);
			try {
				getSession().save(seq);
				flush();
			} catch (Exception e) {
				seq = (OISequenceEntity) getSession().get(OISequenceEntity.class, id, LockMode.UPGRADE);
				if (seq == null) {
					throw new ServiceException();
				}
			}
		}

		return nextValue(seq);
	}

	private long nextValue(OISequenceEntity entity) {
		final long next = entity.getNext();
		getSession().update(entity);
		return next;
	}
	
	/**
	 * @see com.isotrol.impe3.oi.dao.DAO#findBatch(com.isotrol.impe3.web20.server.LogTableFilterDTO,
	 * com.google.common.base.Function)
	 */
	public <D> List<D> findBatch(LogTableFilterDTO filter, Function<? super OILogTableEntity, ? extends D> transformer) {
		Query query = getNamedQuery(OILogTableEntity.BY_NAME).setString(0, filter.getName())
			.setLong(1, filter.getFirst()).setMaxResults(filter.getSize());

		final List<OILogTableEntity> entities = list(OILogTableEntity.class, query);

		return Lists.newArrayList(Iterables.transform(entities, transformer));
	}
}
