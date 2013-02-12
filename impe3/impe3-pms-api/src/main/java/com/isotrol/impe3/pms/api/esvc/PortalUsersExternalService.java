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

package com.isotrol.impe3.pms.api.esvc;


import java.util.List;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.users.api.DuplicatePortalUserException;
import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.api.PortalUserException;
import com.isotrol.impe3.users.api.PortalUserNotFoundException;
import com.isotrol.impe3.users.api.PortalUserSelDTO;


/**
 * Portals Users external service.
 * @author Andres Rodriguez.
 */
public interface PortalUsersExternalService extends ExternalService {
	/**
	 * Returns the list of users.
	 * @param serviceId External Service Id.
	 * @return The list of users registered in the service.
	 * @throws ExternalServiceNotFoundException if the requested service is not found.
	 */
	List<PortalUserSelDTO> getUsers(String serviceId) throws PMSException;

	/**
	 * Gets the detail of a user.
	 * @param serviceId External Service Id.
	 * @param id User Id.
	 * @return The user details.
	 * @throws PortalUserNotFoundException if the user with the specified id is found.
	 * @throws ExternalServiceNotFoundException if the requested service is not found.
	 */
	PortalUserDTO getById(String serviceId, String id) throws PortalUserNotFoundException, PMSException;

	/**
	 * Gets the detail of a user.
	 * @param serviceId External Service Id.
	 * @param name Username.
	 * @return The user details or {@code null} if not found.
	 * @throws ExternalServiceNotFoundException if the requested service is not found.
	 */
	PortalUserDTO getByName(String serviceId, String name) throws PMSException;

	/**
	 * Gets the detail of a user by email address.
	 * @param serviceId External Service Id.
	 * @param email Email address.
	 * @return The user details or {@code null} if not found.
	 * @throws ExternalServiceNotFoundException if the requested service is not found.
	 */
	PortalUserDTO getByEMail(String serviceId, String email) throws PMSException;

	/**
	 * Checks the password of a user.
	 * @param serviceId External Service Id.
	 * @param username User name.
	 * @param password Password.
	 * @return The user details if the user is found and the password is correct, {@code null} otherwise.
	 * @throws ExternalServiceNotFoundException if the requested service is not found.
	 */
	PortalUserDTO checkPassword(String serviceId, String username, String password) throws PMSException;

	/**
	 * Creates a new user.
	 * @param serviceId External Service Id.
	 * @param user User Details.
	 * @param password Password.
	 * @return The created details.
	 * @throws DuplicatePortalUserException if a user with the same name exists.
	 * @throws ExternalServiceNotFoundException if the requested service is not found.
	 */
	PortalUserDTO create(String serviceId, PortalUserDTO user, String password) throws PortalUserException,
		PMSException;

	/**
	 * Updates an user.
	 * @param serviceId External Service Id.
	 * @param user User Details.
	 * @return The updated details.
	 * @throws PortalUserNotFoundException if the specified user is not found.
	 * @throws DuplicatePortalUserException if a user with the same name exists.
	 * @throws ExternalServiceNotFoundException if the requested service is not found.
	 */
	PortalUserDTO update(String serviceId, PortalUserDTO user) throws PortalUserException, PMSException;

	/**
	 * Updates an user's password.
	 * @param serviceId External Service Id.
	 * @param id User Id.
	 * @param password Password.
	 * @throws ExternalServiceNotFoundException if the requested service is not found.
	 */
	void changePassword(String serviceId, String id, String password) throws PortalUserNotFoundException, PMSException;

	/**
	 * Deletes an user.
	 * @param serviceId External Service Id.
	 * @param id User Id.
	 * @throws ExternalServiceNotFoundException if the requested service is not found.
	 */
	void delete(String serviceId, String id) throws PortalUserNotFoundException, PMSException;
}
