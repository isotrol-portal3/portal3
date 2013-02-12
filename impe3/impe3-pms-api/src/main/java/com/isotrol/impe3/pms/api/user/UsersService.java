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

package com.isotrol.impe3.pms.api.user;


import java.util.List;
import java.util.Set;

import com.isotrol.impe3.pms.api.AuthorizationException;
import com.isotrol.impe3.pms.api.InvalidOperationException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PortalAuthority;


/**
 * Module registry service.
 * @author Andres Rodriguez.
 */
public interface UsersService {
	/**
	 * Returns all registered users.
	 * @return All registered users.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 */
	List<UserSelDTO> getUsers() throws PMSException;

	/**
	 * Gets the detail of a user.
	 * @param id ID of the user.
	 * @return The requested detail.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws UserNotFoundException if the user is not found.
	 */
	UserDTO get(String id) throws PMSException;

	/**
	 * Saves an user content type. If the ID is null the operation is considered an insertion. Otherwise, it is
	 * considered an update.
	 * @param dto User to save.
	 * @return The saved user.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws DuplicateUserException if there is another user with the same name.
	 * @throws UserNotFoundException if the user is not found.
	 */
	UserDTO save(UserDTO dto) throws PMSException;
	
	/**
	 * Returns the portal-level authorities granted to a user.
	 * @param id User Id.
	 * @param portalId Portal Id.
	 * @return The set of granted authorities.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws UserNotFoundException if the user is not found.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	Set<PortalAuthority> getPortalAuthorities(String id, String portalId) throws PMSException;
	
	/**
	 * Sets the portal-level authorities granted to a user.
	 * @param id User Id.
	 * @param portalId Portal Id.
	 * @param granted The set of granted authorities.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws UserNotFoundException if the user is not found.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	void setPortalAuthorities(String id, String portalId, Set<PortalAuthority> granted) throws PMSException;

	/**
	 * Deletes an user.
	 * @param id User Id.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws UserNotFoundException if the user is not found.
	 */
	void delete(String id) throws PMSException;

	/**
	 * Sets an user's password.
	 * @param id User Id.
	 * @param password User password.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws InvalidOperationException if no password changes are allowed.
	 * @throws UserNotFoundException if the user is not found.
	 */
	void setPassword(String id, String password) throws PMSException;
	
	/**
	 * Gets the effective authorities granted to a user.
	 * @param id User Id.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 * @throws UserNotFoundException if the user is not found.
	 */
	AuthorizationDTO getGranted(String id) throws PMSException;

}
