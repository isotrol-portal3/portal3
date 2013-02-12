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
import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.api.service.external.INodeRepositoryExternalService;
import com.isotrol.impe3.pms.gui.api.service.external.INodeRepositoryExternalServiceAsync;
import com.isotrol.impe3.pms.gui.client.i18n.NrSettings;

/**
 * Service provider for the Nodes Repository external service.
 * 
 * @author Andrei Cojocaru
 *
 */
public class NodeRepositoryExternalServiceProvider 
	extends AbstractServiceProvider<INodeRepositoryExternalServiceAsync> {

	/**
	 * NR specific settings bundle.<br/>
	 */
	private NrSettings nrSettings = null;
	
	/* (non-Javadoc)
	 * @see com.google.inject.Provider#get()
	 */
	/**
	 * <br/>
	 */
	public INodeRepositoryExternalServiceAsync get() {
		INodeRepositoryExternalServiceAsync serviceProxy = (INodeRepositoryExternalServiceAsync) GWT
			.create(INodeRepositoryExternalService.class);
		ServiceDefTarget serviceEndpoint = (ServiceDefTarget) serviceProxy;
		String url = getRpcBaseUrl()	+ nrSettings.nrServiceUrlMapping();
		serviceEndpoint.setServiceEntryPoint(url);
		
		String msg = "Nodes Repository service proxy created. URL = " + url; 
		GWT.log(msg, null);
		
		return serviceProxy;
	}

	/**
	 * Injects the Nodes Repository specific settings bundle.<br/>
	 * @param nrSettings
	 */
	@Inject
	public void setNrSettings(NrSettings nrSettings) {
		this.nrSettings = nrSettings;
	}

}
