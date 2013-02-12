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

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.CategoryPagesDTO;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.ComponentKey;
import com.isotrol.impe3.pms.api.page.ContentPagesDTO;
import com.isotrol.impe3.pms.api.page.DefaultPagesDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageDTO;
import com.isotrol.impe3.pms.api.page.PageDeviceDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;


/**
 * Wrapper for the Pages async service with events capabilities
 * 
 * @author Manuel Ruiz
 * 
 */
public class PagesController extends ChangeEventSupport implements IPagesServiceAsync {

	/**
	 * Delegate that offers support for change events.<br/>
	 */
	private ChangeEventSupport changeEventSupport = null;

	/**
	 * Async proxy to real service.<br/>
	 */
	private IPagesServiceAsync service = null;

	/**
	 * Constructs a new controller that delegates the service-specific method calls to the passed service.<br/>
	 * @param service
	 */
	public PagesController(IPagesServiceAsync service) {
		this.service = service;
		this.changeEventSupport = new ChangeEventSupport();
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getDefaultPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getDefaultPages(PortalPagesLoc loc, AsyncCallback<DefaultPagesDTO> callback) {
		service.getDefaultPages(loc, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getTemplatePages(com.isotrol.impe3.pms.api.page.PortalPagesLoc, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getTemplatePages(PortalPagesLoc loc, AsyncCallback<List<Inherited<PageSelDTO>>> callback) {
		service.getTemplatePages(loc, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getSpecialPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getSpecialPages(PortalPagesLoc loc, AsyncCallback<List<Inherited<PageSelDTO>>> callback) {
		service.getSpecialPages(loc, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getErrorPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getErrorPages(PortalPagesLoc loc, AsyncCallback<List<Inherited<PageSelDTO>>> callback) {
		service.getErrorPages(loc, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getCategoryPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getCategoryPages(PortalPagesLoc loc, AsyncCallback<CategoryPagesDTO> callback) {
		service.getCategoryPages(loc, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getContentPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getContentPages(PortalPagesLoc loc, AsyncCallback<ContentPagesDTO> callback) {
		service.getContentPages(loc, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getCategoryContentPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc, java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getCategoryContentPages(PortalPagesLoc loc, String contentTypeId,
		AsyncCallback<CategoryPagesDTO> callback) {
		service.getCategoryContentPages(loc, contentTypeId, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getContentTypePages(com.isotrol.impe3.pms.api.page.PortalPagesLoc, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getContentTypePages(PortalPagesLoc loc, AsyncCallback<ContentPagesDTO> callback) {
		service.getContentTypePages(loc, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getCategoryContentTypePages(com.isotrol.impe3.pms.api.page.PortalPagesLoc, java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getCategoryContentTypePages(PortalPagesLoc loc, String contentTypeId,
		AsyncCallback<CategoryPagesDTO> callback) {
		service.getCategoryContentTypePages(loc, contentTypeId, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#newTemplate(PortalPagesLoc, PageClass,
	 * AsyncCallback)
	 */
	public void newTemplate(PortalPagesLoc loc, PageClass pageClass, AsyncCallback<PageTemplateDTO> callback) {
		service.newTemplate(loc, pageClass, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#delete(com.isotrol.impe3.pms.api.page.PageLoc,
	 * AsyncCallback)
	 */
	public void delete(final PageLoc loc, final AsyncCallback<Void> callback) {

		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				callback.onSuccess(arg0);
				PmsChangeEvent changeEvent = new PmsChangeEvent(PmsChangeEvent.DELETE, loc);
				PagesController.this.notify(changeEvent);
			}
		};

		service.delete(loc, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#get(PageLoc, AsyncCallback);
	 */
	public void get(PageLoc loc, AsyncCallback<PageTemplateDTO> callback) {
		service.get(loc, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#getLayout(PageLoc, AsyncCallback)
	 */
	public void getLayout(PageLoc loc, AsyncCallback<LayoutDTO> callback) {
		service.getLayout(loc, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#newComponentTemplate(PortalPagesLoc, ComponentKey,
	 * AsyncCallback)
	 */
	public void newComponentTemplate(PortalPagesLoc loc, ComponentKey componentKey,
		AsyncCallback<ComponentInPageTemplateDTO> callback) {
		service.newComponentTemplate(loc, componentKey, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#save(PageDTO, AsyncCallback)
	 */
	public void save(PageDTO dto, final AsyncCallback<PageTemplateDTO> callback) {

		AsyncCallback<PageTemplateDTO> realCallback = new AsyncCallback<PageTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(PageTemplateDTO page) {
				callback.onSuccess(page);
				ChangeEvent event = new PmsChangeEvent(PmsChangeEvent.ADD, page);
				PagesController.this.notify(event);
			}
		};
		service.save(dto, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#setLayout(PageLoc, List, AsyncCallback)
	 */
	public void setLayout(PageLoc loc, List<FrameDTO> frames, final AsyncCallback<LayoutDTO> callback) {
		AsyncCallback<LayoutDTO> realCallback = new AsyncCallback<LayoutDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(LayoutDTO layout) {
				ChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE, layout);
				PagesController.this.notify(event);
				callback.onSuccess(layout);
			}
		};
		service.setLayout(loc, frames, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#isCIPinLayout(PageLoc, String, AsyncCallback)
	 */
	public void isCIPinLayout(PageLoc loc, String cipId, AsyncCallback<Boolean> callback) {
		service.isCIPinLayout(loc, cipId, callback);
	}

	/**
	 * Readonly method: just forwards the call to the {@link #service}.
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync
	 * #getPalette(com.isotrol.impe3.pms.api.page.PortalPagesLoc, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getPalette(PortalPagesLoc loc, AsyncCallback<List<PaletteDTO>> callback) {
		service.getPalette(loc, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#exportAll(com.isotrol.impe3.pms.api.page.PortalPagesLoc,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportAll(PortalPagesLoc loc, AsyncCallback<String> callback) {
		service.exportAll(loc, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#importPages(com.isotrol.impe3.pms.api.page.PortalPagesLoc,
	 * java.lang.String, boolean, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importPages(PortalPagesLoc loc, String fileId, boolean overwrite, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> newCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.IMPORT, arg0);
				PagesController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.importPages(loc, fileId, overwrite, newCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync#getPageDevices(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getPageDevices(String portalId, AsyncCallback<List<PageDeviceDTO>> callback) {
		service.getPageDevices(portalId, callback);
	}

	/**
	 * @param listener
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport
	 * #addChangeListener(com.extjs.gxt.ui.client.data.ChangeListener[])
	 */
	public void addChangeListener(ChangeListener... listener) {
		changeEventSupport.addChangeListener(listener);
	}

	/**
	 * @param event
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport #notify(com.extjs.gxt.ui.client.data.ChangeEvent)
	 */
	public void notify(ChangeEvent event) {
		changeEventSupport.notify(event);
	}

	/**
	 * @param listener
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport
	 * #removeChangeListener(com.extjs.gxt.ui.client.data.ChangeListener[])
	 */
	public void removeChangeListener(ChangeListener... listener) {
		changeEventSupport.removeChangeListener(listener);
	}

	/**
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport #removeChangeListeners()
	 */
	public void removeChangeListeners() {
		changeEventSupport.removeChangeListeners();
	}

	/**
	 * @param silent
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport #setSilent(boolean)
	 */
	public void setSilent(boolean silent) {
		changeEventSupport.setSilent(silent);
	}
}
