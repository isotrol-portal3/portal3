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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.isotrol.impe3.es.common.server.AbstractService;
import com.isotrol.impe3.tickets.server.DAO;
import com.isotrol.impe3.tickets.server.SubjectManager;
import com.isotrol.impe3.tickets.server.model.SubjectEntity;


/**
 * Subject manager implementation.
 * @author Andres Rodriguez.
 */
@Component
public final class SubjectManagerImpl extends AbstractService<DAO> implements SubjectManager {
	/** Subject map. */
	private final ConcurrentMap<String, UUID> map = CacheBuilder.newBuilder().maximumSize(4096).softValues()
		.<String, UUID> build().asMap();

	/** Default constructor. */
	public SubjectManagerImpl() {
	}

	private String name(String name) {
		return checkNotNull(name, "The subject name must be provided");
	}

	/**
	 * @see com.isotrol.impe3.tickets.server.SubjectManager#getSubject(java.lang.String)
	 */
	public UUID getSubject(String name) {
		final UUID id = map.get(name(name));
		if (id != null) {
			return id;
		}
		final SubjectEntity e = getDao().findSubjectByName(name);
		if (e != null) {
			map.putIfAbsent(name, e.getId());
		}
		return map.get(name);
	}

	public UUID createSubject(String name) {
		UUID id = getSubject(name);
		if (id != null) {
			return id;
		}
		try {
			create(name);
		} catch (DataIntegrityViolationException e) {
			sync();
			create(name);
		} catch (ConstraintViolationException e2) {
			sync();
			create(name);
		}
		return checkNotNull(getSubject(name), "Unable to create subject");
	}

	private void create(String name) {
		final SubjectEntity e = new SubjectEntity();
		e.setName(name);
		saveNewEntity(e);
	}
}
