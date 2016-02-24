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
package com.isotrol.impe3.tickets.server.impl;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.es.common.server.AbstractService;
import com.isotrol.impe3.tickets.domain.Ticket;
import com.isotrol.impe3.tickets.domain.TicketConstraints;
import com.isotrol.impe3.tickets.domain.TicketManager;
import com.isotrol.impe3.tickets.server.DAO;
import com.isotrol.impe3.tickets.server.SubjectManager;
import com.isotrol.impe3.tickets.server.model.SubjectEntity;
import com.isotrol.impe3.tickets.server.model.TicketEntity;


/**
 * Ticket manager implementation.
 * @author Andres Rodriguez.
 */
@Service
public final class TicketManagerImpl extends AbstractService<DAO> implements TicketManager {
	@Autowired
	private SubjectManager subjectManager;

	/** Default constructor. */
	public TicketManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.tickets.domain.TicketManager#create(java.lang.String,
	 * com.isotrol.impe3.tickets.domain.TicketConstraints, java.util.Map)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public UUID create(String subject, TicketConstraints constraints, Map<String, String> properties) {
		checkNotNull(subject);
		checkNotNull(constraints);
		final UUID subjectId = subjectManager.createSubject(subject);
		checkNotNull(subjectId);
		final SubjectEntity subjectEntity = findById(SubjectEntity.class, subjectId);
		checkNotNull(subjectEntity);
		final TicketEntity t = new TicketEntity();
		t.setExpiration(constraints.getTimestamp());
		t.setUses(constraints.getUses());
		t.setSubject(subjectEntity);
		if (properties != null) {
			t.getPayload().putAll(properties);
		}
		saveNewEntity(t);
		return t.getId();
	}

	/**
	 * @see com.isotrol.impe3.tickets.domain.TicketManager#consume(java.lang.String, java.util.UUID)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Ticket consume(String subject, UUID id) {
		checkNotNull(subject);
		final UUID subjectId = subjectManager.getSubject(subject);
		checkArgument(subjectId != null);
		if (id == null) {
			return null;
		}
		final TicketEntity t = getDao().findById(TicketEntity.class, id, true);
		if (t == null) {
			return null;
		}
		return t.consume();
	}
	
	/**
	 * @see com.isotrol.impe3.tickets.domain.TicketManager#hasSubject(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public boolean hasSubject(String subject) {
		checkNotNull(subject);
		final UUID subjectId = subjectManager.getSubject(subject);
		return (subjectId != null);
	}

}
