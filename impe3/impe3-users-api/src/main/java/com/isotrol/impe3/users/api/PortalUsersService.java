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

package com.isotrol.impe3.users.api;


import java.util.List;


/**
 * Portals Users service.
 * @author Andres Rodriguez.
 */
public interface PortalUsersService {
	/**
	 * Returns the list of users.
	 * @return The list of users registered in the service.
	 */
	List<PortalUserSelDTO> getUsers();

	/**
	 * Gets the detail of a user.
	 * @param id User Id.
	 * @return The user details.
	 * @throws PortalUserNotFoundException if the user with the specified id is found.
	 */
	PortalUserDTO getById(String id) throws PortalUserNotFoundException;

	/**
	 * Gets the detail of a user.
	 * @param name Username.
	 * @return The user details or {@code null} if not found.
	 */
	PortalUserDTO getByName(String name);

	/**
	 * Gets the detail of a user by email address.
	 * @param email Email address.
	 * @return The user details or {@code null} if not found.
	 */
	PortalUserDTO getByEMail(String email);
	
	/**
	 * Checks the password of a user.
	 * @param username User name.
	 * @param password Password.
	 * @return The user details if the user is found and the password is correct, {@code null} otherwise.
	 */
	PortalUserDTO checkPassword(String username, String password);

	/**
	 * Creates a new user.
	 * @param user User Details.
	 * @param password Password.
	 * @return The created details.
	 * @throws DuplicatePortalUserException if a user with the same name exists.
	 */
	PortalUserDTO create(PortalUserDTO user, String password) throws PortalUserException;

	/**
	 * Updates an user.
	 * @param user User Details.
	 * @return The updated details.
	 * @throws PortalUserNotFoundException if the specified user is not found.
	 * @throws DuplicatePortalUserException if a user with the same name exists.
	 */
	PortalUserDTO update(PortalUserDTO user) throws PortalUserException;

	/**
	 * Updates an user's password.
	 * @param id User Id.
	 * @param password Password.
	 */
	void changePassword(String id, String password) throws PortalUserNotFoundException;

	/**
	 * Deletes an user.
	 * @param id User Id.
	 */
	void delete(String id) throws PortalUserNotFoundException;
}
