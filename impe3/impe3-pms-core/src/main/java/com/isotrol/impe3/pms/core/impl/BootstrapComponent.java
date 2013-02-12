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

package com.isotrol.impe3.pms.core.impl;


import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.pms.core.DeviceManager;
import com.isotrol.impe3.pms.core.EnvironmentManager;
import com.isotrol.impe3.pms.core.MigrationManager;
import com.isotrol.impe3.pms.core.PMSContext;
import com.isotrol.impe3.pms.core.UserManager;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Component that bootstraps the root user and the default environment.
 * @author Andres Rodriguez.
 */
@Component
public final class BootstrapComponent implements InitializingBean {
	private final UserManager user;
	private final EnvironmentManager environment;
	private final DeviceManager device;
	private final PMSContext context;
	private final MigrationManager migrations;

	/** Default constructor. */
	@Autowired
	public BootstrapComponent(UserManager user, EnvironmentManager environment, DeviceManager device,
		PMSContext context, MigrationManager migrations) {
		this.user = user;
		this.environment = environment;
		this.device = device;
		this.context = context;
		this.migrations = checkNotNull(migrations);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		UserEntity root = user.getRootUser();
		device.findOrCreate(root, DeviceType.HTML, DeviceManager.DEFAULT, "The Web", true, DeviceType.HTML.getWidth());
		context.setEnvironmentId(environment.getDefaultEnvironment(root).getId());
		try {
			migrations.publishedFlag();
		} catch (Exception e) {
			Loggers.pms().error("Unable to perform published flag migration", e);
		}
	}
}
