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
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.minst.DependencySetTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;
import com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;


/**
 * Wrapper for the Components async service with events capabilities
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class ComponentsController extends ChangeEventSupport implements IComponentsServiceAsync {

	/**
	 * Wrapped components service proxy.<br/>
	 */
	private IComponentsServiceAsync service = null;

	/**
	 * Constructs a new controller that delegates the service-specific method calls to the passed service.<br/>
	 * @param service
	 */
	public ComponentsController(IComponentsServiceAsync service) {
		this.service = service;
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync #delete(java.lang.String, java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void delete(final String portalId, final String id, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> newCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.DELETE, id);
				ComponentsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};

		service.delete(portalId, id, newCallback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync #get(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void get(String portalId, String id, AsyncCallback<ModuleInstanceTemplateDTO> callback) {
		service.get(portalId, id, callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync
	 * #getComponentModules(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getComponentModules(AsyncCallback<List<ComponentModuleDTO>> callback) {
		service.getComponentModules(callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync #getComponents(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getComponents(String portalId, AsyncCallback<List<ModuleInstanceSelDTO>> callback) {
		service.getComponents(portalId, callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync #newTemplate(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void newTemplate(String key, AsyncCallback<ModuleInstanceTemplateDTO> callback) {
		service.newTemplate(key, callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync #save(java.lang.String,
	 * com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void save(final String portalId, final ModuleInstanceDTO dto,
		final AsyncCallback<ModuleInstanceTemplateDTO> callback) {
		AsyncCallback<ModuleInstanceTemplateDTO> newCallback = new AsyncCallback<ModuleInstanceTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(ModuleInstanceTemplateDTO arg0) {
				int type = PmsChangeEvent.UPDATE;
				if (dto.getId() == null) {
					type = PmsChangeEvent.ADD;
				}
				ChangeEvent event = new PmsChangeEvent(type, arg0);
				ComponentsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};

		service.save(portalId, dto, newCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync#clearConfiguration(java.lang.String,
	 * java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void clearConfiguration(String portalId, String componentId,
		final AsyncCallback<InheritedComponentInstanceSelDTO> callback) {

		AsyncCallback<InheritedComponentInstanceSelDTO> newCallback = new AsyncCallback<InheritedComponentInstanceSelDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(InheritedComponentInstanceSelDTO arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE, arg0);
				ComponentsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.clearConfiguration(portalId, componentId, newCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync#clearDependencies(java.lang.String,
	 * java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void clearDependencies(String portalId, String componentId,
		final AsyncCallback<InheritedComponentInstanceSelDTO> callback) {

		AsyncCallback<InheritedComponentInstanceSelDTO> newCallback = new AsyncCallback<InheritedComponentInstanceSelDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(InheritedComponentInstanceSelDTO arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE, arg0);
				ComponentsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.clearDependencies(portalId, componentId, newCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync#getConfiguration(java.lang.String,
	 * java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getConfiguration(String portalId, String componentId, AsyncCallback<ConfigurationTemplateDTO> callback) {
		service.getConfiguration(portalId, componentId, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync#getDependencies(java.lang.String,
	 * java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getDependencies(String portalId, String componentId, AsyncCallback<DependencySetTemplateDTO> callback) {
		service.getDependencies(portalId, componentId, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync#getInheritedComponents(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getInheritedComponents(String portalId, AsyncCallback<List<InheritedComponentInstanceSelDTO>> callback) {
		service.getInheritedComponents(portalId, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync#overrideConfiguration(java.lang.String,
	 * java.lang.String, java.util.List, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void overrideConfiguration(String portalId, String componentId, List<ConfigurationItemDTO> config,
		final AsyncCallback<InheritedComponentInstanceSelDTO> callback) {

		AsyncCallback<InheritedComponentInstanceSelDTO> newCallback = new AsyncCallback<InheritedComponentInstanceSelDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(InheritedComponentInstanceSelDTO arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE, arg0);
				ComponentsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.overrideConfiguration(portalId, componentId, config, newCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync#overrideDependencies(java.lang.String,
	 * java.lang.String, java.util.List, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void overrideDependencies(String portalId, String componentId, List<DependencyDTO> dependencies,
		final AsyncCallback<InheritedComponentInstanceSelDTO> callback) {

		AsyncCallback<InheritedComponentInstanceSelDTO> newCallback = new AsyncCallback<InheritedComponentInstanceSelDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(InheritedComponentInstanceSelDTO arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE, arg0);
				ComponentsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.overrideDependencies(portalId, componentId, dependencies, newCallback);
	}

	public void exportAll(String portalId, AsyncCallback<String> callback) {
		service.exportAll(portalId, callback);
	}

	public void exportAllOverrides(String portalId, AsyncCallback<String> callback) {
		service.exportAllOverrides(portalId, callback);
	}

	public void exportSome(String portalId, Set<String> ids, AsyncCallback<String> callback) {
		service.exportSome(portalId, ids, callback);
	}

	public void exportSomeOverrides(String portalId, Set<String> ids, AsyncCallback<String> callback) {
		service.exportSomeOverrides(portalId, ids, callback);
	}

	public void importComponents(String portalId, String fileId, boolean overwrite, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> newCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.IMPORT, arg0);
				ComponentsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.importComponents(portalId, fileId, overwrite, newCallback);
	}

	public void importOverrides(String portalId, String fileId, boolean overwrite, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> newCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.IMPORT, arg0);
				ComponentsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.importOverrides(portalId, fileId, overwrite, newCallback);
	}

	public void getComponentsByCorrectness(String portalId, Correctness correctness,
		AsyncCallback<List<ModuleInstanceSelDTO>> callback) {
		service.getComponentsByCorrectness(portalId, correctness, callback);
	}

}
