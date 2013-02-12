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

package com.isotrol.impe3.pms.core.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.isotrol.impe3.pms.core.dao.UserDAO;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Abstract DAO implementaton for Users.
 * @author Andres Rodriguez.
 */
@Repository
public final class UserDAOImpl extends EntityDAOImpl<UserEntity> implements UserDAO {
	/**
	 * Constructor.
	 */
	public UserDAOImpl() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.UserDAO#getNotDeleted()
	 */
	public List<UserEntity> getNotDeleted() {
		return list(getNamedQuery(UserEntity.NOT_DELETED));
	}
	
	/**
	 * @see com.isotrol.impe3.pms.core.dao.UserDAO#getByName(java.lang.String)
	 */
	public UserEntity getByName(String name) {
		return first(getNamedQuery(UserEntity.BY_NAME).setString("name", name));
	}

}
