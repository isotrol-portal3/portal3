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

package com.isotrol.impe3.pms.core.impl;


import java.lang.reflect.ParameterizedType;
import java.util.UUID;

import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.model.Entity;


/**
 * Abstract implementation for services providing UUID generator and general DAO.
 * @author Andres Rodriguez.
 * @param <T> Entity type.
 */
public abstract class AbstractEntityService<T extends Entity> extends AbstractContextService {
	/** Entity type. */
	private final Class<T> type;

	/**
	 * Constructor.
	 */
	public AbstractEntityService() {
		@SuppressWarnings("unchecked")
		final Class<T> klass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
			.getActualTypeArguments()[0];
		this.type = klass;

	}

	protected final Class<T> getType() {
		return type;
	}

	protected final T findById(UUID id) {
		return findById(type, id);
	}

	protected T load(String id) throws EntityNotFoundException {
		return load(toUUID(id));
	}

	protected T load(UUID id) throws EntityNotFoundException {
		return load(getType(), id);
	}

	protected T saveNew(T entity) throws PMSException {
		return saveNewEntity(entity);
	}

	/**
	 * Deletes an entity.
	 * @param entity Entity.
	 */
	protected void delete(T entity) {
		deleteEntity(entity);
	}

}
