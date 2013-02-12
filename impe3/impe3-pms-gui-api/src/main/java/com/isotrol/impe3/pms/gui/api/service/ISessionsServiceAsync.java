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


import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.AuthorizationException;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;
import com.isotrol.impe3.pms.api.InvalidOperationException;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.session.CurrentUserDTO;
import com.isotrol.impe3.pms.api.session.EnvironmentConfigDTO;
import com.isotrol.impe3.pms.api.session.SessionDTO;
import com.isotrol.impe3.pms.api.session.UserNameDTO;
import com.isotrol.impe3.pms.ext.api.Credentials;


/**
 * Asynchronous interface for Sessions service.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * @see {@link ISessionsService}, {@link com.isotrol.impe3.pms.api.session.SessionsService SessionsService}
 */
public interface ISessionsServiceAsync {

	/**
	 * Returns the names of the global authorities.<br/> Thrown exceptions:<ul> <li>AuthorizationException if not
	 * authorized to perform the operation.</li> </ul>
	 * @param callback Async callback for processing the names of the global authorities.
	 */
	void getGlobalAuthorities(AsyncCallback<Map<GlobalAuthority, String>> callback);

	/**
	 * Returns the names of the global roles.<br/> Thrown exceptions:<ul> <li>AuthorizationException if not authorized
	 * to perform the operation.</li> </ul>
	 * @param callback Async callback for processing the names of the global roles.
	 */
	void getGlobalRoles(AsyncCallback<Map<GlobalRole, String>> callback);

	/**
	 * Returns the names of the portal authorities.<br/> Thrown exceptions:<ul> <li>AuthorizationException if not
	 * authorized to perform the operation.</li> </ul>
	 * @param callback Async callback to proces the poratl authorities
	 */
	void getPortalAuthorities(AsyncCallback<Map<PortalAuthority, String>> callback);

	/**
	 * Starts a new session.
	 * @param username User name.
	 * @param password User credentials.
	 * @param callback Callback to process the obtained Session data.
	 */
	void login(Credentials credentials, AsyncCallback<SessionDTO> callback);

	/**
	 * Ends the current session.<br/>
	 * @param callback Async callback to process the end of current session.
	 */
	void logout(AsyncCallback<Void> callback);
	
	/**
	 * Returns the current session information.
	 * @param callback The current session information, or {@code null} if there is no current session.
	 */
	void getSession(AsyncCallback<SessionDTO> callback);
	
	/**
	 * Checks whether a provided locale string would be accepted.
	 * @param locale Locale to check.
	 * @param callback True if the locale is valid.
	 */
	void checkLocale(String locale, AsyncCallback<Boolean> callback);

	/**
	 * Checks whether some provided locale strings would be accepted.
	 * @param locales Locales to check.
	 * @param callback The set of invalid locales. Never {@code null}.
	 */
	void checkLocales(Iterable<String> locales, AsyncCallback<Set<String>> callback);
	
	/**
	 * Checks whether a provided URI string would be accepted.
	 * @param uri URI to check.
	 * @param mustBeAbsolute True if the URIs must be absolute to be considered valid.
	 * @param callback True if the URI is valid.
	 */
	void checkURI(String uri, boolean mustBeAbsolute, AsyncCallback<Boolean> callback);

	/**
	 * Checks whether some provided URI strings would be accepted.
	 * @param uris URIs to check.
	 * @param mustBeAbsolute True if the URIs must be absolute to be considered valid.
	 * @param callback The set of invalid URIs. Never {@code null}.
	 */
	void checkURIs(Iterable<String> uris, boolean mustBeAbsolute, AsyncCallback<Set<String>> callback);
	
	/**
	 * Returns the current environment configuration.
	 * @param callback The current environment configuration.
	 */
	void getEnvironmentConfig(AsyncCallback<EnvironmentConfigDTO> callback);

	/**
	 * Sets the current environment configuration.
	 * @param dto The current environment configuration.
	 */
	void setEnvironmentConfig(EnvironmentConfigDTO dto, AsyncCallback<Void> callback);
	
	/**
	 * Returns the current user information.
	 * @return The current user information.
	 */
	void getCurrentUser(AsyncCallback<CurrentUserDTO> callback);
	
	/**
	 * Sets the current user name.
	 * @param name New name.
	 * @throws InvalidOperationException if not allowed to change the requested field(s).
	 */
	void setUserName(UserNameDTO name, AsyncCallback<Void> callback);
	
	/**
	 * Changes the current user's password.
	 * @param currentPwd Current password.
	 * @param newPwd New password.
	 * @throws AuthorizationException if the provided current password is incorrect.
	 * @throws InvalidOperationException if not allowed to change the password.
	 */
	void changePassword(String currentPwd, String newPwd, AsyncCallback<Void> callback);
}
