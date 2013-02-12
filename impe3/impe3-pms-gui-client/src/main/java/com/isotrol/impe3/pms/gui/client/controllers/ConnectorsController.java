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
import java.util.Set;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;


/**
 * Wrapper for the Connectors async service with events capabilities
 * 
 * @author Manuel Ruiz
 * 
 */
public class ConnectorsController extends ChangeEventSupport implements IConnectorsServiceAsync {

	/**
	 * real proxy to the async service.<br/>
	 */
	private IConnectorsServiceAsync connectorsService = null;

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#get (java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void get(String id, AsyncCallback<ModuleInstanceTemplateDTO> callback) {

		connectorsService.get(id, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#getConnectorModules
	 * (com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getConnectorModules(AsyncCallback<List<ConnectorModuleDTO>> callback) {

		connectorsService.getConnectorModules(callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#getConnectors
	 * (com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getConnectors(AsyncCallback<List<ModuleInstanceSelDTO>> callback) {

		connectorsService.getConnectors(callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#getConnectorsByCorrectness(com.isotrol.impe3.pms.api.Correctness,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getConnectorsByCorrectness(Correctness correctness, AsyncCallback<List<ModuleInstanceSelDTO>> callback) {
		connectorsService.getConnectorsByCorrectness(correctness, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#newTemplate (java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void newTemplate(String key, AsyncCallback<ModuleInstanceTemplateDTO> callback) {

		connectorsService.newTemplate(key, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#save
	 * (com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void save(ModuleInstanceDTO dto, final AsyncCallback<ModuleInstanceTemplateDTO> callback) {

		final boolean update = dto.getId() != null;
		AsyncCallback<ModuleInstanceTemplateDTO> realCallback = new AsyncCallback<ModuleInstanceTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(ModuleInstanceTemplateDTO ct) {
				int type = 0;
				if (update) {
					type = PmsChangeEvent.UPDATE;
				} else {
					type = PmsChangeEvent.ADD;
				}
				ChangeEvent ce = new PmsChangeEvent(type, ct);
				ConnectorsController.this.notify(ce);

				callback.onSuccess(ct);
			}
		};
		connectorsService.save(dto, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#delete(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void delete(final String id, final AsyncCallback<Void> callback) {

		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				ChangeEvent ce = new PmsChangeEvent(PmsChangeEvent.DELETE, id);
				ConnectorsController.this.notify(ce);
				callback.onSuccess(arg0);
			}
		};

		connectorsService.delete(id, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#exportAll(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportAll(AsyncCallback<String> callback) {
		connectorsService.exportAll(callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#exportSome(java.util.Set,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportSome(Set<String> ids, AsyncCallback<String> callback) {
		connectorsService.exportSome(ids, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync#importConnectors(java.lang.String, boolean,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importConnectors(String fileId, boolean overwrite, final AsyncCallback<Void> callback) {

		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				ChangeEvent ce = new PmsChangeEvent(PmsChangeEvent.IMPORT, null);
				ConnectorsController.this.notify(ce);
				callback.onSuccess(arg0);
			}
		};
		connectorsService.importConnectors(fileId, overwrite, realCallback);
	}

	/**
	 * @param connectorsService the connectorsService to set
	 */
	public void setConnectorsService(IConnectorsServiceAsync connectorsService) {
		this.connectorsService = connectorsService;
	}
}
