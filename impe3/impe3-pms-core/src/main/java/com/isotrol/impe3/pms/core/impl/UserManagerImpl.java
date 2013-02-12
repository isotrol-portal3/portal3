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

import com.isotrol.impe3.pms.core.UserManager;
import com.isotrol.impe3.pms.core.dao.UserDAO;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Implementation of UserRegistry.
 * @author Andres Rodriguez.
 */
@Service
@Transactional
public final class UserManagerImpl implements UserManager {
	/** User DAO. */
	private final UserDAO userDAO;

	/**
	 * Constructor.
	 * @param userDAO User DAO.
	 */
	@Autowired
	public UserManagerImpl(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.UserManager#getRootUser()
	 */
	public UserEntity getRootUser() {
		UserEntity user = userDAO.findById(ROOT_ID, false);
		if (user != null) {
			return user;
		}
		user = new UserEntity();
		user.setId(ROOT_ID);
		user.setName(ROOT_LOGIN);
		user.setDisplayName(ROOT_NAME);
		user.setPassword(null);
		user.setRoot(true);
		user.setActive(true);
		user.setCreated(user);
		user.setUpdated(user);
		return userDAO.save(user);
	}
}
