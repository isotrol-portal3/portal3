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
import com.isotrol.impe3.pms.gui.api.service.IContentTypesService;
import com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.ContentTypesController;

/**
 * Provider for the Content Types Service proxy.
 * @author Andrei Cojocaru
 * 
 */
public class ContentTypesServiceProvider extends
	AbstractPmsServiceProvider<IContentTypesServiceAsync> {

	/* (non-Javadoc)
	 * @see com.google.inject.Provider#get()
	 */
	/**
	 * Instantiates the remote service proxy.<br/>
	 */
	public IContentTypesServiceAsync get() {
		IContentTypesServiceAsync serviceProxy = (IContentTypesServiceAsync) GWT
				.create(IContentTypesService.class);
		ServiceDefTarget ctEndpoint = (ServiceDefTarget) serviceProxy;
		ctEndpoint.setServiceEntryPoint(getRpcBaseUrl()
				+ getSettings().contentTypesServiceUrlMapping());

		ContentTypesController serviceController = new ContentTypesController();
		serviceController.setContentTypesService(serviceProxy);

		return serviceController;
	}

}
