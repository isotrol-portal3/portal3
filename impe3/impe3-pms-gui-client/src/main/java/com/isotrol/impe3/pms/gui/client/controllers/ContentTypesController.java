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
package com.isotrol.impe3.pms.gui.client.controllers;


import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;


/**
 * Wrapper for the Content Types async service with events capabilities
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class ContentTypesController extends ChangeEventSupport implements IContentTypesServiceAsync {

	/**
	 * real proxy to the async service.<br/>
	 */
	private IContentTypesServiceAsync contentTypesService = null;

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync #delete(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void delete(final String id, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				ChangeEvent ce = new PmsChangeEvent(PmsChangeEvent.DELETE, id);
				ContentTypesController.this.notify(ce);
				callback.onSuccess(arg0);
			}
		};
		contentTypesService.delete(id, realCallback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync #get(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void get(String id, AsyncCallback<ContentTypeDTO> callback) {
		contentTypesService.get(id, callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync
	 * #getContentTypes(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getContentTypes(AsyncCallback<List<ContentTypeSelDTO>> callback) {
		contentTypesService.getContentTypes(callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsyn
	 * c#save(com.isotrol.impe3.pms.api.type.ContentTypeDTO, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void save(ContentTypeDTO model, final AsyncCallback<ContentTypeDTO> callback) {
		final boolean update = model.getId() != null;
		AsyncCallback<ContentTypeDTO> realCallback = new AsyncCallback<ContentTypeDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(ContentTypeDTO ct) {
				int type = 0;
				if (update) {
					type = PmsChangeEvent.UPDATE;
				} else {
					type = PmsChangeEvent.ADD;
				}
				PmsChangeEvent ce = new PmsChangeEvent(type, ct);
				ContentTypesController.this.notify(ce);
				callback.onSuccess(ct);
			}
		};
		contentTypesService.save(model, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync#exportAll(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportAll(AsyncCallback<String> callback) {
		contentTypesService.exportAll(callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync#exportSome(java.util.Set,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportSome(Set<String> ids, AsyncCallback<String> callback) {
		contentTypesService.exportSome(ids, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync#importTypes(java.lang.String, boolean,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importTypes(String fileId, boolean overwrite, final AsyncCallback<Void> callback) {

		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent ce = new PmsChangeEvent(PmsChangeEvent.REFRESH, arg0);
				ContentTypesController.this.notify(ce);
				callback.onSuccess(arg0);
			}
		};

		contentTypesService.importTypes(fileId, overwrite, realCallback);
	}

	/**
	 * @param contentTypesService the contentTypesService to set
	 */
	public void setContentTypesService(IContentTypesServiceAsync contentTypesService) {
		this.contentTypesService = contentTypesService;
	}
}
