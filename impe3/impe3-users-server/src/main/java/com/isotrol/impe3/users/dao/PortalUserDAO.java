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

package com.isotrol.impe3.users.dao;


import java.util.UUID;

import net.sf.derquinsej.hib3.GenericDAO;

import com.isotrol.impe3.users.model.PortalUserEntity;


/**
 * DAO for portal users.
 * @author Andres Rodriguez.
 */
public interface PortalUserDAO extends GenericDAO<PortalUserEntity, UUID> {
	/**
	 * Finds an user by username.
	 * @param name Name.
	 * @return The requested user or {@code null} if it is not found.
	 */
	PortalUserEntity getByName(String name);
	/**
	 * Finds an user by email.
	 * @param email Email address.
	 * @return The requested user or {@code null} if it is not found.
	 */
	PortalUserEntity getByEmail(String email);
	
	/**
	 * Finds an user by username excluding a certain id.
	 * @param id ID to exclude.
	 * @param name Name.
	 * @return The requested user or {@code null} if it is not found.
	 */
	PortalUserEntity getByName(UUID id, String name);
}
