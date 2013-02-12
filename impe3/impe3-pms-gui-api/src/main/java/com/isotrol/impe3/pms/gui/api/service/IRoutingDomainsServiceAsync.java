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
package com.isotrol.impe3.pms.gui.api.service;


import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.isotrol.impe3.pms.api.rd.RoutingDomainDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainSelDTO;


/**
 * Routing domains service asynchronous interface.
 * @see IRoutingDomainsService
 * @author Andrei Cojocaru
 * 
 */
public interface IRoutingDomainsServiceAsync extends RemoteService {
	/**
	 * Returns the routing domains.
	 * @param callback Callback for processing the returned routing domains.
	 */
	void getDomains(AsyncCallback<List<RoutingDomainSelDTO>> callback);

	/**
	 * Returns the detail of a routing domain.
	 * @param id Routing domain id.
	 * @param callback Callback for processing the requested routing domain.
	 */
	void get(String id, AsyncCallback<RoutingDomainDTO> callback);

	/**
	 * Saves a routing domain. If the id is {@code null} it is considered an insert, otherwise it is an update.
	 * @param dto Routing domain to save.
	 * @param callback Callback for processing the saved routing domain.
	 */
	void save(RoutingDomainDTO dto, AsyncCallback<RoutingDomainDTO> callback);

	/**
	 * Deletes a routing domain.
	 * @param id Routing domain id.
	 * @param callback Callback.
	 */
	void delete(String id, AsyncCallback<Void> callback);

	/**
	 * Returns the default routing domain.<br/>
	 * @param callback Callback for processing the returned default routing domain.
	 */
	void getDefault(AsyncCallback<RoutingDomainDTO> callback);

	/**
	 * Sets the default routing domain.<br/>
	 * @param dto The default routing domain.
	 * @param callback Callback for processing the returned default routing domain.
	 */
	void setDefault(RoutingDomainDTO dto, AsyncCallback<RoutingDomainDTO> callback);
}
