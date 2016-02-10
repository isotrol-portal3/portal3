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

package com.isotrol.impe3.pms.gui.client.ioc.sp;


import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.api.service.IIndexerService;
import com.isotrol.impe3.pms.gui.api.service.IIndexerServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.IndexersController;
import com.isotrol.impe3.pms.gui.client.i18n.NrSettings;


/**
 * Provider for the Indexer service proxy.
 * 
 * @author Zineb Ghamiri
 * 
 */
public class IndexerServiceProvider extends AbstractPmsServiceProvider<IIndexerServiceAsync> {
	
	
	private NrSettings nrSettings = null;

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.Provider#get()
	 */
	/**
	 * Returns an instance of the External Services service proxy.<br/>
	 */
	public IIndexerServiceAsync get() {
	
	IIndexerServiceAsync serviceProxy = (IIndexerServiceAsync) GWT
			.create(IIndexerService.class);
		ServiceDefTarget serviceEndpoint = (ServiceDefTarget) serviceProxy;
		String url = getRpcBaseUrl() + nrSettings.indexServiceUrlMapping();
		serviceEndpoint.setServiceEntryPoint(url);
		
		String msg = "Index service proxy created. URL = " + url; 
		GWT.log(msg, null);
		
		IndexersController serviceController = new IndexersController(serviceProxy);
		
		return serviceController;
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
