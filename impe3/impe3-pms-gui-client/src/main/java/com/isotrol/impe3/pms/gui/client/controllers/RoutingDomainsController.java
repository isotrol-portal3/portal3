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

package com.isotrol.impe3.pms.gui.client.controllers;

import java.util.List;

import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.rd.RoutingDomainDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;

/**
 * Routing Domains Service decorator with event capabilities.<br/>
 * 
 * @author Andrei Cojocaru
 *
 */
public class RoutingDomainsController extends ChangeEventSupport implements IRoutingDomainsServiceAsync {

	/**
	 * Async proxy to service.<br/>
	 */
	private IRoutingDomainsServiceAsync service = null;
	
	/**
	 * Controller provided with service proxy.<br/>
	 * @param service
	 */
	public RoutingDomainsController(IRoutingDomainsServiceAsync service) {
		super();
		this.service = service;
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync
	 * #delete(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void delete(String id, AsyncCallback<Void> callback) {
		callback.onFailure(new IllegalArgumentException("RoutingDomainsController: delete() is not implemented yet."));
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync
	 * #get(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void get(String id, AsyncCallback<RoutingDomainDTO> callback) {
		service.get(id, callback);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync
	 * #getDefault(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getDefault(AsyncCallback<RoutingDomainDTO> callback) {
		service.getDefault(callback);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync
	 * #getDomains(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getDomains(AsyncCallback<List<RoutingDomainSelDTO>> callback) {
		service.getDomains(callback);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync
	 * #save(com.isotrol.impe3.pms.api.rd.RoutingDomainDTO, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void save(RoutingDomainDTO dto, final AsyncCallback<RoutingDomainDTO> callback) {
		AsyncCallback<RoutingDomainDTO> realCallback = new AsyncCallback<RoutingDomainDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}
			public void onSuccess(RoutingDomainDTO arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE, arg0);
				RoutingDomainsController.this.notify(event);
				
				callback.onSuccess(arg0);
			}
		};
		service.save(dto, realCallback);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync
	 * #setDefault(com.isotrol.impe3.pms.api.rd.RoutingDomainDTO, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void setDefault(RoutingDomainDTO dto, AsyncCallback<RoutingDomainDTO> callback) {
		service.setDefault(dto, callback);
	}

}
