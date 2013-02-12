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

package com.isotrol.impe3.pms.gui.api.service.external;


import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.api.PortalUserSelDTO;


/**
 * Portal users external asynchronous service
 * @author Manuel Ruiz
 * 
 */
public interface IPortalUsersExternalServiceAsync extends IExternalServiceAsync {
	
	/**
	 * Returns the list of users.
	 * @param serviceId External Service Id.
	 * @param callback
	 */
	void getUsers(String serviceId, AsyncCallback<List<PortalUserSelDTO>> callback);

	/**
	 * Gets the detail of a user.
	 * @param serviceId External Service Id.
	 * @param id User Id.
	 * @param callback
	 */
	void getById(String serviceId, String id, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Gets the detail of a user.
	 * @param serviceId External Service Id.
	 * @param name Username.
	 * @param callback
	 */
	void getByName(String serviceId, String name, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Gets the detail of a user by email address.
	 * @param serviceId External Service Id.
	 * @param email Email address.
	 * @param callback
	 */
	void getByEMail(String serviceId, String email, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Checks the password of a user.
	 * @param serviceId External Service Id.
	 * @param username User name.
	 * @param password Password.
	 * @param callback
	 */
	void checkPassword(String serviceId, String username, String password, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Creates a new user.
	 * @param serviceId External Service Id.
	 * @param user User Details.
	 * @param password Password.
	 * @param callback
	 */
	void create(String serviceId, PortalUserDTO user, String password, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Updates an user.
	 * @param serviceId External Service Id.
	 * @param user User Details.
	 * @param callback
	 */
	void update(String serviceId, PortalUserDTO user, AsyncCallback<PortalUserDTO> callback);

	/**
	 * Updates an user's password.
	 * @param serviceId External Service Id.
	 * @param id User Id.
	 * @param password Password.
	 * @param callback
	 */
	void changePassword(String serviceId, String id, String password, AsyncCallback<Void> callback);

	/**
	 * Deletes an user.
	 * @param serviceId External Service Id.
	 * @param id User Id.
	 * @param callback
	 */
	void delete(String serviceId, String id, AsyncCallback<Void> callback);

}
