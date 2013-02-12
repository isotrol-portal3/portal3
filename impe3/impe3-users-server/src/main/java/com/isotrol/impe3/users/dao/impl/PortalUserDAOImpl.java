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

package com.isotrol.impe3.users.dao.impl;


import java.util.UUID;

import net.sf.derquinsej.hib3.GenericDAOImpl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.users.dao.PortalUserDAO;
import com.isotrol.impe3.users.model.PortalUserEntity;


/**
 * Abstract DAO implementaton for Entities.
 * @author Andres Rodriguez.
 * @param <T> Entity type.
 */
@Repository
public class PortalUserDAOImpl extends GenericDAOImpl<PortalUserEntity, UUID> implements PortalUserDAO {
	/**
	 * Constructor.
	 */
	public PortalUserDAOImpl() {
	}

	/**
	 * @see net.sf.derquinsej.hib3.GenericDAOImpl#setSessionFactory(org.hibernate.SessionFactory)
	 */
	@Autowired
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public PortalUserEntity getByName(String name) {
		Preconditions.checkNotNull(name);
		return unique(getNamedQuery(PortalUserEntity.BY_NAME).setString(0, name));
	}

	public PortalUserEntity getByEmail(String email) {
		if (email == null) {
			return null;
		}
		return first(getNamedQuery(PortalUserEntity.BY_EMAIL).setString(0, email));
	}

	public PortalUserEntity getByName(UUID id, String name) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(id);
		return unique(getNamedQuery(PortalUserEntity.BY_NAME_OTHER_ID).setString(0, name).setParameter(1, id));
	}

}
