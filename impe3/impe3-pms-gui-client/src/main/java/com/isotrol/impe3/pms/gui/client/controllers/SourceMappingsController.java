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
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.gui.common.data.DTOModel;
import com.isotrol.impe3.pms.api.smap.SourceMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingSelDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.SourceMappingSelModelData;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;


/**
 * Wrapper for the Source Mapping async service with events capabilities
 * 
 * @author Manuel Ruiz
 * 
 */
public class SourceMappingsController extends ChangeEventSupport implements ISourceMappingsServiceAsync {

	/**
	 * real proxy to the async service.<br/>
	 */
	private ISourceMappingsServiceAsync sourceMappingService = null;

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#delete (java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void delete(final String id, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent changeEvent = new PmsChangeEvent(ChangeEventSource.Remove, id);
				SourceMappingsController.this.notify((ChangeEvent) changeEvent);
				callback.onSuccess(arg0);
			}
		};
		sourceMappingService.delete(id, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync #get (java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void get(String id, AsyncCallback<SourceMappingTemplateDTO> callback) {
		sourceMappingService.get(id, callback);

	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#getSourceMappings
	 * (com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getSourceMappings(AsyncCallback<List<SourceMappingSelDTO>> callback) {
		sourceMappingService.getSourceMappings(callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#save
	 * (com.isotrol.impe3.pms.gui.api.model.mapping.SourceMappingModel, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void save(SourceMappingDTO dto, final AsyncCallback<SourceMappingTemplateDTO> callback) {

		final boolean update = dto.getId() != null;
		AsyncCallback<SourceMappingTemplateDTO> realCallback = new AsyncCallback<SourceMappingTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(SourceMappingTemplateDTO ct) {
				int type = 0;
				if (update) {
					type = Update;
				} else {
					type = Add;
				}

				SourceMappingSelDTO sm = ct.getMapping();
				DTOModel<SourceMappingSelDTO> dtoModel = new DTOModel<SourceMappingSelDTO>(
					new SourceMappingSelModelData(sm));
				ChangeEvent ce = new ChangeEvent(type, dtoModel);
				SourceMappingsController.this.notify(ce);

				callback.onSuccess(ct);
			}
		};
		sourceMappingService.save(dto, realCallback);

	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#newTemplate
	 * (com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void newTemplate(AsyncCallback<SourceMappingTemplateDTO> callback) {
		sourceMappingService.newTemplate(callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#exportAll(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportAll(AsyncCallback<String> callabck) {
		sourceMappingService.exportAll(callabck);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#exportCategories(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportCategories(String id, AsyncCallback<String> callback) {
		sourceMappingService.exportCategories(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#exportContentTypes(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportContentTypes(String id, AsyncCallback<String> callback) {
		sourceMappingService.exportContentTypes(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#exportSets(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportSets(String id, AsyncCallback<String> callback) {
		sourceMappingService.exportSets(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#exportSome(java.util.Set,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportSome(Set<String> ids, AsyncCallback<String> callback) {
		sourceMappingService.exportSome(ids, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#importCategories(java.lang.String,
	 * boolean, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importCategories(final String mappingId, String fileId, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent changeEvent = new PmsChangeEvent(PmsChangeEvent.IMPORT, mappingId);
				SourceMappingsController.this.notify((ChangeEvent) changeEvent);
				callback.onSuccess(arg0);
			}
		};
		sourceMappingService.importCategories(mappingId, fileId, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#importContentTypes(java.lang.String,
	 * boolean, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importContentTypes(final String mappingId, String fileId, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent changeEvent = new PmsChangeEvent(PmsChangeEvent.IMPORT, mappingId);
				SourceMappingsController.this.notify((ChangeEvent) changeEvent);
				callback.onSuccess(arg0);
			}
		};
		sourceMappingService.importContentTypes(mappingId, fileId, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#importMappings(java.lang.String, boolean,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importMappings(String fileId, boolean overwrite, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent changeEvent = new PmsChangeEvent(PmsChangeEvent.IMPORT, null);
				SourceMappingsController.this.notify((ChangeEvent) changeEvent);
				callback.onSuccess(arg0);
			}
		};
		sourceMappingService.importMappings(fileId, overwrite, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync#importSets(java.lang.String, boolean,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importSets(final String mappingId, String fileId, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent changeEvent = new PmsChangeEvent(PmsChangeEvent.IMPORT, mappingId);
				SourceMappingsController.this.notify((ChangeEvent) changeEvent);
				callback.onSuccess(arg0);
			}
		};
		sourceMappingService.importSets(mappingId, fileId, realCallback);
	}

	/**
	 * @param sourceMappingService the sourceMappingService to set
	 */
	public void setSourceMappingService(ISourceMappingsServiceAsync sourceMappingService) {
		this.sourceMappingService = sourceMappingService;
	}
}
