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

package com.isotrol.impe3.pms.ldap;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;

import com.isotrol.impe3.pms.ext.api.AuthenticationProvider;
import com.isotrol.impe3.pms.ext.api.Credentials;
import com.isotrol.impe3.pms.ext.api.Credentials.BasicCredentials;
import com.isotrol.impe3.pms.ext.api.ExternalUserDataDTO;
import com.isotrol.impe3.pms.ext.api.InvalidCredentialsException;


/**
 * Simple LDAP Authentication Provider.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class LDAPAuthenticationProvider implements AuthenticationProvider {
	private LdapAuthenticationProvider provider;
	private boolean create = false;

	public void setCreate(boolean create) {
		this.create = create;
	}

	public void setProvider(LdapAuthenticationProvider provider) {
		this.provider = provider;
	}

	public ExternalUserDataDTO authenticate(Credentials credentials) throws InvalidCredentialsException {
		if (credentials instanceof BasicCredentials) {
			BasicCredentials basic = (BasicCredentials) credentials;

			final Authentication authentication = new UsernamePasswordAuthenticationToken(basic.getLogin(),
				basic.getPassword());

			try {
				final Authentication success = provider.authenticate(authentication);

				if (success == null) {
					return null;
				}

				final ExternalUserDataDTO user = new ExternalUserDataDTO();
				user.setCreate(create);
				user.setUpdate(false);
				user.setName(basic.getLogin());
				user.setDisplayName(basic.getLogin());

				return user;
			} catch (BadCredentialsException e) {
				throw new InvalidCredentialsException(e.getMessage());
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

}
