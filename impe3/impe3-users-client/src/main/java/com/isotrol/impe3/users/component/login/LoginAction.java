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

package com.isotrol.impe3.users.component.login;


import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.isotrol.impe3.api.support.BasicPrincipal;
import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.api.PortalUsersService;


/**
 * Login actions.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public class LoginAction extends AbstractAction {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private PortalUsersService service;

	public LoginAction() {
	}

	public void setService(PortalUsersService service) {
		this.service = service;
	}

	@POST
	public Response login(@FormParam("username") String username, @FormParam("password") String password) {
		if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
			return getResponse(false);
		}
		final PortalUserDTO dto;
		try {
			dto = service.checkPassword(username, password);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(String.format("Unable to check password for [%s]", username), e);
			}
			return getResponse(false);
		}
		if (dto == null) {
			if (logger.isTraceEnabled()) {
				logger.trace(String.format("Invalid login for username [%s]", username));
			}
			return getResponse(false);
		}
		getPrincipalContext().login(
			new BasicPrincipal(dto.getUsername(), dto.getDisplayName(), dto.getRoles(), dto.getProperties()));
		return getResponse(true);
	}
}
