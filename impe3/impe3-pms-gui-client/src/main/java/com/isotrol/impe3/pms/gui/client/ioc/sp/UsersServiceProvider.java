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
package com.isotrol.impe3.pms.gui.client.ioc.sp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.isotrol.impe3.pms.gui.api.service.IUsersService;
import com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.UsersController;

/**
 * Provider class for {@link UsersService} async proxy.
 * 
 * @author Andrei Cojocaru
 *
 */
public class UsersServiceProvider extends AbstractPmsServiceProvider<IUsersServiceAsync> {

	/* (non-Javadoc)
	 * @see com.google.inject.Provider#get()
	 */
	/**
	 * <br/>
	 */
	public IUsersServiceAsync get() {
		IUsersServiceAsync serviceProxy = (IUsersServiceAsync) GWT.create(IUsersService.class);
		ServiceDefTarget serviceEndPoint = (ServiceDefTarget) serviceProxy;
		serviceEndPoint.setServiceEntryPoint(getRpcBaseUrl()
				+ getSettings().usersServiceUrlMapping());
		
		return new UsersController(serviceProxy);
	}

}
