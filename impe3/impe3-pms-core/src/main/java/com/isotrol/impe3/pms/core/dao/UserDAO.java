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

package com.isotrol.impe3.pms.core.dao;


import java.util.List;

import com.isotrol.impe3.pms.model.UserEntity;


/**
 * DAO for Users.
 * @author Andres Rodriguez.
 */
public interface UserDAO extends EntityDAO<UserEntity> {
	/**
	 * Returns the users that are not deleted.
	 * @return The list of user that are not deleted.
	 */
	List<UserEntity> getNotDeleted();
	/**
	 * Returns a user by login name.
	 * @param name Login name.
	 * @return The requested user or {@code null} if not found.
	 */
	UserEntity getByName(String name);
}
