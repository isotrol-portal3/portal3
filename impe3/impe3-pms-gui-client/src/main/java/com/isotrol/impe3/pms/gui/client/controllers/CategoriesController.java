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


import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;


/**
 * Categories Service proxy decorator with event firing capabilities. Calls to service must be done through this
 * Controller instances.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class CategoriesController implements ICategoriesServiceAsync, ChangeEventSource {

	/**
	 * Async proxy to service.<br/>
	 */
	private ICategoriesServiceAsync categoriesService = null;

	/**
	 * Delegate object that implements {@link ChangeEventSource} interface.<br/>
	 */
	private ChangeEventSupport changeEventSupport = null;

	/**
	 * Default constructor.<br/>
	 */
	public CategoriesController() {
		this.changeEventSupport = new ChangeEventSupport();
	}

	/**
	 * Fires {@link ChangeEventSource#Add} event.<br/> (non-Javadoc)
	 * 
	 */
	public void create(CategoryDTO category, String parentId, int order, final AsyncCallback<CategoryDTO> callback) {
		AsyncCallback<CategoryDTO> realCallback = new AsyncCallback<CategoryDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(CategoryDTO category) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.ADD, category);
				CategoriesController.this.notify(event);
				callback.onSuccess(category);
			}
		};
		categoriesService.create(category, parentId, order, realCallback);
	}

	/**
	 * Fires {@link ChangeEventSource#Remove} event.<br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync #delete(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void delete(String id, final AsyncCallback<CategoryTreeDTO> callback) {
		AsyncCallback<CategoryTreeDTO> realCallback = new AsyncCallback<CategoryTreeDTO>() {
			public void onFailure(Throwable exception) {
				callback.onFailure(exception);
			}

			public void onSuccess(CategoryTreeDTO categoryTreeDTO) {
				// DTOModel<CategoryTreeDTO> model = new DTOModel<CategoryTreeDTO>(
				// new DummyDTOModelData<CategoryTreeDTO>(categoryTreeDTO));
				// ChangeEvent event = new ChangeEvent(Remove, model, model);
				ChangeEvent event = new PmsChangeEvent(PmsChangeEvent.DELETE, categoryTreeDTO);
				CategoriesController.this.notify(event);
				callback.onSuccess(categoryTreeDTO);
			}
		};
		categoriesService.delete(id, realCallback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync #get(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void get(String id, final AsyncCallback<CategoryDTO> callback) {
		categoriesService.get(id, callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync
	 * #getCategories(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getCategories(final AsyncCallback<CategoryTreeDTO> callback) {
		categoriesService.getCategories(callback);
	}

	/**
	 * Fires {@link ChangeEventSource#Remove} event.<br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync #move(java.lang.String, java.lang.String, int,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void move(String categoryId, String parentId, int order, final AsyncCallback<CategoryTreeDTO> callback) {
		AsyncCallback<CategoryTreeDTO> realCallback = new AsyncCallback<CategoryTreeDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(CategoryTreeDTO arg0) {
				ChangeEvent event = new PmsChangeEvent(PmsChangeEvent.DELETE, arg0);
				CategoriesController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		categoriesService.move(categoryId, parentId, order, realCallback);
	}

	/**
	 * Fires {@link ChangeEventSource#Update} event.<br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync
	 * #update(com.isotrol.impe3.pms.gui.api.model.category.CategoryDTO, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void update(CategoryDTO model, final AsyncCallback<CategoryDTO> callback) {
		AsyncCallback<CategoryDTO> realCallback = new AsyncCallback<CategoryDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(CategoryDTO dto) {

				ChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE, dto);
				CategoriesController.this.notify(event);
				callback.onSuccess(dto);
			}
		};
		categoriesService.update(model, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync#exportBranch(java.lang.String, boolean,
	 * boolean, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportBranch(String id, boolean exportRoot, boolean allLevels, AsyncCallback<String> callback) {
		categoriesService.exportBranch(id, exportRoot, allLevels, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync#importCategories(java.lang.String, boolean,
	 * boolean, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importCategories(String id, String fileId, boolean sameLevel, boolean overwrite, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				ChangeEvent event = new PmsChangeEvent(PmsChangeEvent.IMPORT, null);
				CategoriesController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		categoriesService.importCategories(id, fileId, sameLevel, overwrite, realCallback);
	}

	/**
	 * @param categoriesService the categoriesService to set
	 */
	public void setCategoriesService(ICategoriesServiceAsync categoriesService) {
		this.categoriesService = categoriesService;
	}

	/*
	 * Delegate ChangeEventSource methods to changeEventsSupport object:
	 */
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
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport#notify(com.extjs.gxt.ui.client.data.ChangeEvent)
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
	 * 
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport#removeChangeListeners()
	 */
	public void removeChangeListeners() {
		changeEventSupport.removeChangeListeners();
	}

	/**
	 * @param silent
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport#setSilent(boolean)
	 */
	public void setSilent(boolean silent) {
		changeEventSupport.setSilent(silent);
	}
}
