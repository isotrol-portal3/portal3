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

/**
 * 
 */
package com.isotrol.impe3.users.gui.ioc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.isotrol.impe3.pms.gui.common.util.Messages;
import com.isotrol.impe3.pms.gui.common.util.Styles;
import com.isotrol.impe3.pms.gui.common.util.Util;
import com.isotrol.impe3.users.gui.LeftMenu;
import com.isotrol.impe3.users.gui.UsersTabItemManager;
import com.isotrol.impe3.users.gui.service.IPortalUsersService;
import com.isotrol.impe3.users.gui.service.IPortalUsersServiceAsync;
import com.isotrol.impe3.users.gui.users.UserEditorPanel;
import com.isotrol.impe3.users.gui.users.UserManagementMenu;
import com.isotrol.impe3.users.gui.util.UsersMessages;
import com.isotrol.impe3.users.gui.util.UsersSettings;
import com.isotrol.impe3.users.gui.util.UsersSettingsHolder;
import com.isotrol.impe3.users.gui.util.UsersStyles;

/**
 * Defines the injectable objects and their scopes.
 * 
 * @author Andrei Cojocaru
 *
 */
public class UsersGinModule extends AbstractGinModule {

	/**
	 * Base URL for RPC calls.<br/>
	 */
	private static String rpcBaseUrl = null;
	
	/* (non-Javadoc)
	 * @see com.google.gwt.inject.client.AbstractGinModule#configure()
	 */
	@Override
	protected void configure() {
		bind(UserEditorPanel.class);
		
		bind(LeftMenu.class).in(Singleton.class);
		bind(UserManagementMenu.class).in(Singleton.class);
		
		bind(UsersTabItemManager.class).in(Singleton.class);
		
		/*
		 * i18n and Constants bundles.
		 */
		bind(UsersSettings.class).in(Singleton.class);
		bind(Messages.class).in(Singleton.class);
		bind(UsersMessages.class).in(Singleton.class);
		bind(Styles.class).in(Singleton.class);
		bind(UsersStyles.class).in(Singleton.class);
	}

	/**
	 * @return users async service proxy.
	 */
	@SuppressWarnings("unused")
	@Provides @Singleton
	private IPortalUsersServiceAsync getUsersService() {
		UsersSettings settings = UsersSettingsHolder.getInstance();
		
		IPortalUsersServiceAsync serviceProxy = (IPortalUsersServiceAsync) GWT.create(IPortalUsersService.class);
		ServiceDefTarget serviceEndpoint = (ServiceDefTarget) serviceProxy;
		serviceEndpoint.setServiceEntryPoint(getRpcBaseUrl() + settings.usersServiceUrlMapping());
		
		return serviceProxy;
	}
	
	/**
	 * base URL for RPC. The result must be prepended to each service mapping
	 * (defined server-side in the service exporters.).
	 * 
	 * @return base URL for RPC
	 */
	private static String getRpcBaseUrl() {
		if (rpcBaseUrl == null) {
			UsersSettings settings = UsersSettingsHolder.getInstance();
			if (GWT.isScript()) {
				rpcBaseUrl = Util.getBaseApplicationContext() + settings.rpcServletMapping();
			} else {
				rpcBaseUrl = settings.rpcBaseUrlHostedMode();
			}
		}
		return rpcBaseUrl;
	}
}
