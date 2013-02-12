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

package com.isotrol.impe3.pms.gui.api.service;


import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.user.AuthorizationDTO;
import com.isotrol.impe3.pms.api.user.UserDTO;
import com.isotrol.impe3.pms.api.user.UserSelDTO;

/**
 * Users service async version.
 *
 */
public interface IUsersServiceAsync {

	/**
	 * Deletes a user.
	 * Thrown exceptions:<ul>
	 * <li>AuthorizationException if not authorized to perform the operation.</li>
	 * <li>UserNotFoundException if the user is not found.</li>
	 * </ul>
	 * @param id Id of the User to delete.
	 * @param callback Callback to process the operation result.
	 */
	void delete(String id, AsyncCallback<Void> callback);
	
	/**
	 * Gets the detail of a user.<br/>
	 * Possibly thrown exceptions:<ul>
	 * <li>AuthorizationException if not authorized to perform the operation.</li>
	 * <li>UserNotFoundException if the content type is not found.</li>
	 * </ul>
	 * @param id ID of the user.
	 * @param callback Async callback to process the retrieved user details.
	 * 
	 */
	void get(String id, AsyncCallback<UserDTO> callback);
	
	
	
	/**
	 * Gets the effective authorities granted to a user.<br/>
	 * Thrown exceptions:<ul>
	 * <li>AuthorizationException if not authorized to perform the operation.</li>
	 * <li>UserNotFoundException if the user is not found.</li>
	 * </ul>
	 * @param id User Id.
	 * @param callback Async callback to process the retrieved granted authorities.
	 */
	void getGranted(String id, AsyncCallback<AuthorizationDTO> callback);
	
	/**
	 * Returns the portal-level authorities granted to a user.<br/>
	 * Thrown exceptions:<ul>
	 * <li>AuthorizationException if not authorized to perform the operation.</li>
	 * <li>UserNotFoundException if the user is not found.</li>
	 * <li>PortalNotFoundException if the portal is not found.</li>
	 * </ul>
	 * @param id User Id.
	 * @param portalId Portal Id.
	 * @param callback Async callback to process the set of granted authorities.
	 * 
	 */
	void getPortalAuthorities(String id, String portalId, AsyncCallback<Set<PortalAuthority>> callback);
	
	/**
	 * Returns all registered users.
	 * Possibly thrown exception:<ul>
	 * <li>AuthorizationException if not authorized to perform the operation.</li>
	 * </ul>
	 * @param callback Async callback to process the retrieved users list.
	 */
	void getUsers(AsyncCallback<List<UserSelDTO>> callback);
	
	/**
	 * Saves an user content type. If the ID is null the operation is considered an insertion. Otherwise, it is
	 * considered an update.<br/>
	 * Possibly thrown exceptions:<ul>
	 * <li>AuthorizationException if not authorized to perform the operation.</li>
	 * <li>DuplicateUserException if there is another user with the same name.</li>
	 * <li>UserNotFoundException if the user is not found.</li>
	 * </ul>
	 * @param dto User to save.
	 * @param callback Async Callback to process the operation result.
	 */
	void save(UserDTO dto, AsyncCallback<UserDTO> callback);
	
	/**
	 * Sets an user's password.<br/>
	 * Thrown exceptions:<ul>
	 * <li>AuthorizationException if not authorized to perform the operation.</li>
	 * <li>InvalidOperationException if no password changes are allowed.</li>
	 * <li>UserNotFoundException if the user is not found.</li>
	 * </ul>
	 * @param id User Id.
	 * @param password User password.
	 * @param callback Async callback to process the operation result
	 */
	void setPassword(String id, String password, AsyncCallback<Void> callback);
	
	/**
	 * Sets the portal-level authorities granted to a user.<br/>
	 * Possibly thrown exceptions:<ul>
	 * <li>AuthorizationException if not authorized to perform the operation.</li>
	 * <li>UserNotFoundException if the user is not found.</li>
	 * <li>PortalNotFoundException if the portal is not found.</li>
	 * </ul>
	 * @param id User Id.
	 * @param portalId Portal Id.
	 * @param granted The set of granted authorities.
	 * @param callback Async callback to process the operation result.
	 */
	void setPortalAuthorities(String id, String portalId, Set<PortalAuthority> granted, AsyncCallback<Void> callback);
}
