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


import org.springframework.util.StringUtils;

import com.isotrol.impe3.api.component.Action;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ExtractAction;
import com.isotrol.impe3.api.component.Inject;


/**
 * Login actions exporter component.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public class LoginComponent implements Component {
	private static final String LOGIN = "login";
	private static final String LOGOUT = "logout";
	private ActionsConfig actionsConfig;
	private LoginConfig config;

	public LoginComponent() {
	}

	public void setConfig(LoginConfig config) {
		this.config = config;
	}

	@Inject
	public void setActionsConfig(ActionsConfig actionsConfig) {
		this.actionsConfig = actionsConfig;
	}

	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	@ExtractAction
	public Action getLogin() {
		String name = LOGIN;
		if (actionsConfig != null && StringUtils.hasText(actionsConfig.loginName())) {
			name = actionsConfig.loginName();
		} else if (config != null && StringUtils.hasText(config.loginName())) {
			name = config.loginName();
		}
		return Action.of(LOGIN, name);
	}

	@ExtractAction
	public Action getLogout() {
		String name = LOGOUT;
		if (actionsConfig != null && StringUtils.hasText(actionsConfig.logoutName())) {
			name = actionsConfig.logoutName();
		} else if (config != null && StringUtils.hasText(config.logoutName())) {
			name = config.logoutName();
		}
		return Action.of(LOGOUT, name);
	}
}
