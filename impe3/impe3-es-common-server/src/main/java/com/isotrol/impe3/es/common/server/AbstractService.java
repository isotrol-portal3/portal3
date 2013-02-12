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

package com.isotrol.impe3.es.common.server;


import java.io.Serializable;
import java.util.UUID;

import net.sf.derquinsej.uuid.UUIDGenerator;

import org.springframework.beans.factory.annotation.Autowired;

import com.isotrol.impe3.hib.dao.DAO;
import com.isotrol.impe3.hib.model.Entity;


/**
 * Abstract implementation for services providing UUID generator and general DAO.
 * @author Andres Rodriguez.
 * @param <D> General DAO type.
 */
public abstract class AbstractService<D extends DAO> {
	/** UUID Generator. */
	private UUIDGenerator uuidGenerator;
	/** General DAO. */
	private D dao;

	/**
	 * Constructor.
	 */
	public AbstractService() {
	}

	@Autowired
	public void setUuidGenerator(UUIDGenerator uuidGenerator) {
		this.uuidGenerator = uuidGenerator;
	}

	@Autowired
	public void setDao(D dao) {
		this.dao = dao;
	}

	/**
	 * Generates and return a new UUID.
	 * @return A new UUID.
	 */
	protected final UUID newUUID() {
		return uuidGenerator.get();
	}

	/**
	 * Returns the general DAO.
	 * @return The general DAO.
	 */
	protected final D getDao() {
		return dao;
	}

	/**
	 * Finds an entity by id.
	 * @param type Entity type.
	 * @param id Entity id.
	 * @return The loaded entity or {@code null} if not found.
	 */
	protected final <T> T findById(Class<T> type, Serializable id) {
		return dao.findById(type, id, false);
	}

	/**
	 * Saves a new entity with a new UUID identifier.
	 * @param entity Entity to save.
	 * @return The saved entity.
	 */
	protected <T extends Entity> T saveNewEntity(T entity) {
		entity.setId(newUUID());
		dao.save(entity);
		return entity;
	}

	/**
	 * Deletes an entity.
	 * @param entity Entity.
	 */
	protected void deleteEntity(Object entity) {
		getDao().delete(entity);
	}

	protected final void flush() {
		dao.flush();
	}

	protected final void sync() {
		flush();
		dao.clear();
	}
}
