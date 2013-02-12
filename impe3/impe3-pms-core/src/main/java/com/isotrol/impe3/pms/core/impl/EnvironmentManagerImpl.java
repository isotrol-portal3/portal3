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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.CategoryManager;
import com.isotrol.impe3.pms.core.EnvironmentManager;
import com.isotrol.impe3.pms.core.RoutingDomainManager;
import com.isotrol.impe3.pms.core.dao.EnvironmentDAO;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Implementation of EnvironmentManager.
 * @author Andres Rodriguez.
 */
@Service
public final class EnvironmentManagerImpl extends AbstractEntityService<EnvironmentEntity> implements
	EnvironmentManager {
	/** Environment DAO. */
	private EnvironmentDAO environmentDAO;
	/** Category Manager. */
	private CategoryManager categoryManager;
	/** Routing domain manager. */
	@Autowired
	private RoutingDomainManager routingDomainManager;

	/**
	 * Constructor.
	 */
	public EnvironmentManagerImpl() {
	}

	@Autowired
	public void setEnvironmentDAO(EnvironmentDAO environmentDAO) {
		this.environmentDAO = environmentDAO;
	}

	@Autowired
	public void setCategoryManager(CategoryManager categoryManager) {
		this.categoryManager = categoryManager;
	}

	private EnvironmentEntity create(UserEntity user, String name, String description) throws PMSException {
		final EnvironmentEntity e = new EnvironmentEntity();
		e.setId(newUUID());
		e.setCreated(user);
		e.setUpdated(user);
		e.setName(name);
		e.setDescription(description);
		environmentDAO.save(e);
		environmentDAO.flush();
		categoryManager.getRoot(e);
		return e;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.EnvironmentManager#getDefaultEnvironment(com.isotrol.impe3.pms.model.UserEntity)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public EnvironmentEntity getDefaultEnvironment(UserEntity user) throws PMSException {
		EnvironmentEntity e = environmentDAO.getByName(NAME);
		if (e == null) {
			e = create(user, NAME, DESCRIPTION);
		}
		routingDomainManager.getDefault(e, user);
		return e;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.EnvironmentManager#getDefaultEnvironment()
	 */
	@Transactional(rollbackFor = Throwable.class)
	public EnvironmentEntity getDefaultEnvironment() {
		EnvironmentEntity e = environmentDAO.getByName(NAME);
		if (e == null) {
			throw new IllegalStateException("No default environment");
		}
		return e;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.EnvironmentManager#getEnvironment(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public EnvironmentEntity getEnvironment(String name) {
		return environmentDAO.getByName(name);
	}
}
