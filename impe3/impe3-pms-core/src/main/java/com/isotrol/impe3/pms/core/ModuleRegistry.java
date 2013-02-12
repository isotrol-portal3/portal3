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

package com.isotrol.impe3.pms.core;


import java.util.Locale;
import java.util.Set;

import com.google.common.base.Function;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.modules.Dependency;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.ModuleType;
import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.api.mreg.InvalidModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleDependencyDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleSelDTO;


/**
 * Object that encapsulates a module registry.
 * @author Andres Rodriguez.
 */
public interface ModuleRegistry {
	/**
	 * Returns all registered modules.
	 * @return All registered modules.
	 */
	Iterable<ModuleDefinition<?>> getModules();

	/**
	 * Returns all registered modules of the specified type.
	 * @param moduleType Requested type.
	 * @return All registered modules of the specified type.
	 */
	Iterable<ModuleDefinition<?>> getModules(ModuleType moduleType);

	/**
	 * Returns the definition of a module if registered in this registry.
	 * @param moduleClass Requested module.
	 * @return The module definition of {@code null} if the module is not registered.
	 */
	<T extends Module> ModuleDefinition<T> getModule(Class<T> moduleClass);

	/**
	 * Returns the definition of a module if registered in this registry.
	 * @param moduleClassName Requested module class name.
	 * @return The module definition of {@code null} if no module with the specified class name is registered.
	 */
	ModuleDefinition<?> getModule(String moduleClassName);
	
	/**
	 * Returns the module names that were not found.
	 * @return The module names that were not found.
	 */
	Set<String> getNotFound();

	/**
	 * Returns the module types that were not assignable to Module.
	 * @return The module names that were assignable to Module.
	 */
	Set<Class<?>> getNotModule();
	
	/**
	 * Returns the transformation function from a dependency to a DTO
	 * @param definition Module definition.
	 * @param locale Requested locale. Optional.
	 * @return The requested function.
	 * @throws NullPointerException if the definition is null.
	 * @throws IllegalArgumentException if the definition is not part of the registry.
	 */
	Dependency2DTO dependency2dto(ModuleDefinition<?> definition, Locale locale);	

	/**
	 * Returns a connector module.
	 * @param definition Module definition.
	 * @param locale Requested locale. Optional.
	 * @return The requested connector.
	 * @throws NullPointerException if the definition is null.
	 * @throws IllegalArgumentException if the definition is of an incorrect type.
	 */
	ConnectorModuleDTO getConnector(ModuleDefinition<?> definition, Locale locale);

	/**
	 * Returns a module selection DTO.
	 * @param definition Module definition.
	 * @param locale Requested locale. Optional.
	 * @return The requested connector.
	 * @throws NullPointerException if the definition is null.
	 * @throws IllegalArgumentException if the definition is not part of the registry.
	 */
	ModuleSelDTO getModuleSel(ModuleDefinition<?> definition, Locale locale);

	/**
	 * Returns a component module.
	 * @param definition Module definition.
	 * @param locale Requested locale. Optional.
	 * @return The requested connector.
	 * @throws NullPointerException if the definition is null.
	 * @throws IllegalArgumentException if the definition is of an incorrect type.
	 */
	ComponentModuleDTO getComponent(ModuleDefinition<?> definition, Locale locale);

	/**
	 * Returns all registered connector modules.
	 * @param locale Requested locale. Optional.
	 * @return All registered connector modules.
	 */
	Iterable<ConnectorModuleDTO> getConnectors(Locale locale);

	/**
	 * Returns all registered component modules.
	 * @param locale Requested locale. Optional.
	 * @return All registered component modules.
	 */
	Iterable<ComponentModuleDTO> getComponents(Locale locale);

	/**
	 * Returns all registered invalid modules.
	 * @param locale Requested locale. Optional.
	 * @return All registered invalid modules.
	 */
	Iterable<InvalidModuleDTO> getInvalids(Locale locale);
	
	interface Dependency2DTO extends Function<Dependency, ModuleDependencyDTO> {
	}

}
