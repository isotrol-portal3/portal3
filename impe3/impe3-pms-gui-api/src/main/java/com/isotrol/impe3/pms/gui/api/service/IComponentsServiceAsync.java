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
import java.util.Set;

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


/**
 * Components asynchronous service interface.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public interface IComponentsServiceAsync {

	/**
	 * Returns all registered component modules.<br/>
	 * @param callback callback to process all registered component modules.
	 */
	void getComponentModules(AsyncCallback<List<ComponentModuleDTO>> callback);

	/**
	 * Returns the portal's own registered component packages.
	 * @param portalId Portal Id.
	 * @param callback callback to process the portal's own registered component packages.
	 */
	void getComponents(String portalId, AsyncCallback<List<ModuleInstanceSelDTO>> callback);

	/**
	 * Returns the portal's own registered component with a certain correctness status.
	 * @param portalId Portal Id.
	 * @param correctness Correctness status.
	 * @param callback The requested connector instances.
	 */
	void getComponentsByCorrectness(String portalId, Correctness correctness,
		AsyncCallback<List<ModuleInstanceSelDTO>> callback);

	/**
	 * Creates a template for a new component package.
	 * @param key Module key (interface class name).
	 * @param callback callback to process the template for a new component package.
	 */
	void newTemplate(String key, AsyncCallback<ModuleInstanceTemplateDTO> callback);

	/**
	 * Returns a template to modify a component package.
	 * @param portalId
	 * @param id Component package id.
	 * @param callback
	 */
	void get(String portalId, String id, AsyncCallback<ModuleInstanceTemplateDTO> callback);

	/**
	 * Saves a connector. If the ID is null the operation is considered an insertion. Otherwise, it is considered an
	 * update.
	 * @param portalId Portal Id.
	 * @param dto Object to save.
	 * @param callback callback to process the saved object
	 */
	void save(String portalId, ModuleInstanceDTO dto, AsyncCallback<ModuleInstanceTemplateDTO> callback);

	/**
	 * Deletes a component package.
	 * @param portalId
	 * @param id Id of the component package.
	 * @param callback
	 */
	void delete(String portalId, String id, AsyncCallback<Void> callback);

	/**
	 * Returns the inherited component packages.
	 * @param portalId Portal Id.
	 * @param callback The inherited component packages.
	 */
	void getInheritedComponents(String portalId, AsyncCallback<List<InheritedComponentInstanceSelDTO>> callback);

	/**
	 * Returns the active configuration of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @param callback The component's current configuration.
	 */
	void getConfiguration(String portalId, String componentId, AsyncCallback<ConfigurationTemplateDTO> callback);

	/**
	 * Returns the active dependency set of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @param callback The component's current configuration.
	 */
	void getDependencies(String portalId, String componentId, AsyncCallback<DependencySetTemplateDTO> callback);

	/**
	 * Overrides the configuration of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @param config Configuration to apply.
	 * @param callback The updated component state.
	 */
	void overrideConfiguration(String portalId, String componentId, List<ConfigurationItemDTO> config,
		AsyncCallback<InheritedComponentInstanceSelDTO> callback);

	/**
	 * Clears the overriden configuration of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @param callback The updated component state.
	 */
	void clearConfiguration(String portalId, String componentId,
		AsyncCallback<InheritedComponentInstanceSelDTO> callback);

	/**
	 * Overrides the dependencies of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @param dependencies Dependencies to use.
	 * @param callback The updated component state.
	 */
	void overrideDependencies(String portalId, String componentId, List<DependencyDTO> dependencies,
		AsyncCallback<InheritedComponentInstanceSelDTO> callback);

	/**
	 * Clears the overriden configuration of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @param The updated component state.
	 */
	void clearDependencies(String portalId, String componentId, AsyncCallback<InheritedComponentInstanceSelDTO> callback);

	/**
	 * Export all components.
	 * @param portalId Portal Id.
	 * @param callback URL to download the exported file.
	 */
	void exportAll(String portalId, AsyncCallback<String> callback);

	/**
	 * Export some components.
	 * @param portalId Portal Id.
	 * @param ids Set containing the ids of the components to export.
	 * @param callback URL to download the exported file.
	 */
	void exportSome(String portalId, Set<String> ids, AsyncCallback<String> callback);

	/**
	 * Import components definitions.
	 * @param portalId Portal Id.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing connectors.
	 * @param callback
	 */
	void importComponents(String portalId, String fileId, boolean overwrite, AsyncCallback<Void> callback);

	/**
	 * Export all components.
	 * @param portalId Portal Id.
	 * @param callback URL to download the exported file.
	 */
	void exportAllOverrides(String portalId, AsyncCallback<String> callback);

	/**
	 * Export some components.
	 * @param portalId Portal Id.
	 * @param ids Set containing the ids of the components to export.
	 * @return callback URL to download the exported file.
	 */
	void exportSomeOverrides(String portalId, Set<String> ids, AsyncCallback<String> callback);

	/**
	 * Import components definitions.
	 * @param portalId Portal Id.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing connectors.
	 * @param callback
	 */
	void importOverrides(String portalId, String fileId, boolean overwrite, AsyncCallback<Void> callback);

}
