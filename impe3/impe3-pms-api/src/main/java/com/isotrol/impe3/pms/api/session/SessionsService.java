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

package com.isotrol.impe3.pms.api.session;


import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.pms.api.AuthorizationException;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;
import com.isotrol.impe3.pms.api.InvalidOperationException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.ext.api.Credentials;


/**
 * Session management service.
 * @author Andres Rodriguez.
 */
public interface SessionsService {
	/**
	 * Starts a new session.
	 * @param credentials User credentials.
	 * @return The new session if successful, {@code null} if not.
	 * @throws LockedUserException if the user is locked.
	 * @throws InactiveUserException if the user is inactive.
	 */
	SessionDTO login(Credentials credentials) throws PMSException;

	/**
	 * Ends the current session.
	 */
	void logout();

	/**
	 * Returns the current session information.
	 * @return The current session information, or {@code null} if there is no current session.
	 */
	SessionDTO getSession();

	/**
	 * Returns the names of the global authorities.
	 * @return The names of the global authorities.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 */
	Map<GlobalAuthority, String> getGlobalAuthorities() throws PMSException;

	/**
	 * Returns the names of the global authorities.
	 * @return The names of the global authorities.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 */
	Map<PortalAuthority, String> getPortalAuthorities() throws PMSException;

	/**
	 * Returns the names of the global roles.
	 * @return The names of the global roles.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 */
	Map<GlobalRole, String> getGlobalRoles() throws PMSException;

	/**
	 * Checks whether a provided locale string would be accepted.
	 * @param locale Locale to check.
	 * @return True if the locale is valid.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 */
	boolean checkLocale(String locale) throws PMSException;

	/**
	 * Checks whether some provided locale strings would be accepted.
	 * @param locales Locales to check.
	 * @return The set of invalid locales. Never {@code null}.
	 * @throws NullPointerException if the argument is {@code null} or any of the locales is {@code null}.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 */
	Set<String> checkLocales(Iterable<String> locales) throws PMSException;

	/**
	 * Checks whether a provided URI string would be accepted.
	 * @param uri URI to check.
	 * @param mustBeAbsolute True if the URIs must be absolute to be considered valid.
	 * @return True if the URI is valid.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 */
	boolean checkURI(String uri, boolean mustBeAbsolute) throws PMSException;

	/**
	 * Checks whether some provided URI strings would be accepted.
	 * @param uris URIs to check.
	 * @param mustBeAbsolute True if the URIs must be absolute to be considered valid.
	 * @return The set of invalid URIs. Never {@code null}.
	 * @throws NullPointerException if the argument is {@code null} or any of the locales is {@code null}.
	 * @throws AuthorizationException if not authorized to perform the operation.
	 */
	Set<String> checkURIs(Iterable<String> uris, boolean mustBeAbsolute) throws PMSException;

	/**
	 * Returns the current environment configuration.
	 * @return The current environment configuration.
	 */
	EnvironmentConfigDTO getEnvironmentConfig() throws PMSException;

	/**
	 * Sets the current environment configuration.
	 * @param dto The current environment configuration.
	 */
	void setEnvironmentConfig(EnvironmentConfigDTO dto);

	/**
	 * Returns the current user information.
	 * @return The current user information.
	 */
	CurrentUserDTO getCurrentUser() throws PMSException;
	
	/**
	 * Sets the current user name.
	 * @param name New name.
	 * @throws InvalidOperationException if not allowed to change the requested field(s).
	 */
	void setUserName(UserNameDTO name) throws PMSException;
	
	/**
	 * Changes the current user's password.
	 * @param currentPwd Current password.
	 * @param newPwd New password.
	 * @throws AuthorizationException if the provided current password is incorrect.
	 * @throws InvalidOperationException if not allowed to change the password.
	 */
	void changePassword(String currentPwd, String newPwd) throws PMSException;

}
