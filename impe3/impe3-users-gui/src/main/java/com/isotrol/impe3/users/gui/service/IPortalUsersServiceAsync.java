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

package com.isotrol.impe3.users.gui.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.api.PortalUserSelDTO;

/**
 * GWT-RPC service for Portal Users. It is analog to {@linkcom.isotrol.impe3.users.api.PortalUsersService}, but uses
 * GWT-compatible types.
 * 
 * @author Manuel Ruiz
 * 
 */
public interface IPortalUsersServiceAsync extends RemoteService {
	
	/**
	 * Returns the list of users.
	 * @param callback
	 */
	 void getUsers(AsyncCallback<List<PortalUserSelDTO>> callback);

	/**
	 * Gets the detail of a user.
	 * @param id User Id.
	 * @param callback
	 */
	void getById(String id, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Gets the detail of a user.
	 * @param name Username.
	 * @param callback
	 */
	void getByName(String name, AsyncCallback<PortalUserDTO> callback);
	
	/**
	 * Gets the detail of a user by email address.
	 * @param email Email address.
	 * @param callback The user details or {@code null} if not found.
	 */
	void getByEMail(String email, AsyncCallback<PortalUserDTO> callback);
	
	/**
	 * Checks the password of a user.
	 * @param username User name.
	 * @param password Password.
	 * @param callback
	 */
	void checkPassword(String username, String password, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Creates a new user.
	 * @param user User Details.
	 * @param password Password.
	 * @param callback
	 */
	void create(PortalUserDTO user, String password, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Updates an user.
	 * @param user User Details.
	 * @param callback
	 */
	void update(PortalUserDTO user, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Updates an user's password.
	 * @param id User Id.
	 * @param password Password.
	 * @param callback
	 */
	void changePassword(String id, String password, AsyncCallback<Void> callback);
	
	/**
	 * Deletes an user.
	 * @param id User Id.
	 */
	void delete(String id, AsyncCallback<Void> callback);

}
