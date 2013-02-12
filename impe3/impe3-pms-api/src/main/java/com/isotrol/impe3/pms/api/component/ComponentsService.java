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

package com.isotrol.impe3.pms.api.component;


import java.util.List;
import java.util.Set;

import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.InvalidImportFileException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.minst.DependencySetTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;


/**
 * Components package service.
 * @author Andres Rodriguez.
 */
public interface ComponentsService {
	/**
	 * Returns all instantiable component modules.
	 * @return All instantiable component modules.
	 */
	List<ComponentModuleDTO> getComponentModules();

	/**
	 * Returns the portal's own registered component packages.
	 * @param portalId Portal Id.
	 * @return The portal's own registered component packages.
	 */
	List<ModuleInstanceSelDTO> getComponents(String portalId) throws PMSException;

	/**
	 * Returns the portal's own registered component with a certain correctness status.
	 * @param portalId Portal Id.
	 * @param correctness Correctness status.
	 * @return The requested connector instances.
	 * @throws PMSException 
	 */
	List<ModuleInstanceSelDTO> getComponentsByCorrectness(String portalId, Correctness correctness) throws PMSException;

	/**
	 * Creates a template for a new component package.
	 * @param key Module key (interface class name).
	 * @return A template.
	 */
	ModuleInstanceTemplateDTO newTemplate(String key) throws PMSException;

	/**
	 * Returns a template to modify a component package.
	 * @param portalId Portal Id.
	 * @param id Component package id.
	 * @return A template.
	 * @throws ComponentNotFoundException if the component module instance is not found.
	 */
	ModuleInstanceTemplateDTO get(String portalId, String id) throws PMSException;

	/**
	 * Saves a connector. If the ID is null the operation is considered an insertion. Otherwise, it is considered an
	 * update.
	 * @param portalId Portal Id.
	 * @param dto Object to save.
	 * @return The saved object.
	 * @throws ComponentNotFoundException if the component module instance is not found.
	 */
	ModuleInstanceTemplateDTO save(String portalId, ModuleInstanceDTO dto) throws PMSException;

	/**
	 * Deletes a component package.
	 * @param portalId Portal Id.
	 * @param id Id of the component package.
	 * @throws ComponentNotFoundException if the component module instance is not found.
	 * @throws ComponentInUseException if the component module instance is in use.
	 */
	void delete(String portalId, String id) throws PMSException;

	/**
	 * Returns the inherited component packages.
	 * @param portalId Portal Id.
	 * @return The inherited component packages.
	 */
	List<InheritedComponentInstanceSelDTO> getInheritedComponents(String portalId) throws PMSException;

	/**
	 * Returns the active configuration of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @return The component's current configuration.
	 */
	ConfigurationTemplateDTO getConfiguration(String portalId, String componentId) throws PMSException;

	/**
	 * Returns the active dependency set of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @return The component's current configuration.
	 */
	DependencySetTemplateDTO getDependencies(String portalId, String componentId) throws PMSException;

	/**
	 * Overrides the configuration of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @param config Configuration to apply.
	 * @return The updated component state.
	 */
	InheritedComponentInstanceSelDTO overrideConfiguration(String portalId, String componentId, List<ConfigurationItemDTO> config) throws PMSException;

	/**
	 * Clears the overriden configuration of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @return The updated component state.
	 */
	InheritedComponentInstanceSelDTO clearConfiguration(String portalId, String componentId) throws PMSException;

	/**
	 * Overrides the dependencies of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @param dependencies Dependencies to use.
	 * @return The updated component state.
	 */
	InheritedComponentInstanceSelDTO overrideDependencies(String portalId, String componentId, List<DependencyDTO> dependencies) throws PMSException;

	/**
	 * Clears the overriden configuration of an inherited component.
	 * @param portalId Portal Id.
	 * @param componentId Inherited component Id.
	 * @return The updated component state.
	 */
	InheritedComponentInstanceSelDTO clearDependencies(String portalId, String componentId) throws PMSException;
	
	/**
	 * Export all components.
	 * @param portalId Portal Id.
	 * @return URL to download the exported file.
	 */
	String exportAll(String portalId) throws PMSException;

	/**
	 * Export some components.
	 * @param portalId Portal Id.
	 * @param ids Set containing the ids of the components to export.
	 * @return URL to download the exported file.
	 */
	String exportSome(String portalId, Set<String> ids) throws PMSException;
	
	/**
	 * Import components definitions.
	 * @param portalId Portal Id.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing connectors.
	 * @throws InvalidImportFileException if unable to parse the uploaded file.
	 */
	void importComponents(String portalId, String fileId, boolean overwrite) throws PMSException;

	/**
	 * Export all components.
	 * @param portalId Portal Id.
	 * @return URL to download the exported file.
	 */
	String exportAllOverrides(String portalId) throws PMSException;

	/**
	 * Export some components.
	 * @param portalId Portal Id.
	 * @param ids Set containing the ids of the components to export.
	 * @return URL to download the exported file.
	 */
	String exportSomeOverrides(String portalId, Set<String> ids) throws PMSException;
	
	/**
	 * Import components definitions.
	 * @param portalId Portal Id.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing connectors.
	 * @throws InvalidImportFileException if unable to parse the uploaded file.
	 */
	void importOverrides(String portalId, String fileId, boolean overwrite) throws PMSException;
	
}
