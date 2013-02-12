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

package com.isotrol.impe3.pms.gui.api.service;


import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.api.mreg.InvalidModuleDTO;


/**
 * Modules registry asynchronous service
 * 
 * @author Manuel Ruiz
 * 
 */
public interface IModuleRegistryServiceAsync extends RemoteService {

	/**
	 * Returns the connectors modules
	 * @param callback
	 */
	void getConnectors(AsyncCallback<List<ConnectorModuleDTO>> callback);

	/**
	 * Returns the invalid modules
	 * @param callback
	 */
	void getInvalids(AsyncCallback<List<InvalidModuleDTO>> callback);

	/**
	 * Returns the components modules
	 * @param callback
	 */
	void getComponents(AsyncCallback<List<ComponentModuleDTO>> callback);
	
	/**
	 * Returns the module names that were not found.<br/>
	 * @param callback async callback to process the module names that were not found.
	 */
	void getNotFound(AsyncCallback<List<String>> callback);
	
	/**
	 * Returns the module types that were not assignable to Module.<br/>
	 * @param callback async callback to process the module types that were not assignable to Module.
	 */
	void getNotModule(AsyncCallback<List<String>> callback);
}
