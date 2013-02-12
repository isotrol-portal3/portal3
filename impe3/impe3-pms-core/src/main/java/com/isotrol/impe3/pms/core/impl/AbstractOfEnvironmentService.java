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


import java.util.UUID;

import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.OfEnvironment;


/**
 * Abstract implementation for context information to entities directly related tu an environment.
 * @author Andres Rodriguez.
 * @param <T> Entity type.
 */
public abstract class AbstractOfEnvironmentService<T extends OfEnvironment> extends AbstractEntityService<T> {
	/**
	 * Constructor.
	 */
	public AbstractOfEnvironmentService() {
	}

	@Override
	protected T load(UUID id) throws EntityNotFoundException {
		return loadOfEnvironment(getType(), id, getDefaultNFP());
	}

	@Override
	protected T load(String id) throws EntityNotFoundException {
		return loadOfEnvironment(getType(), id, getDefaultNFP());
	}

	protected T saveNew(EnvironmentEntity environment, T entity) throws PMSException {
		entity.setEnvironment(environment);
		return super.saveNew(entity);
	};

	protected T saveNew(T entity) throws PMSException {
		return saveNew(getEnvironment(), entity);
	};

}
