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

package com.isotrol.impe3.pms.model;


import java.util.UUID;

import com.isotrol.impe3.core.modules.ModuleDefinition;


/**
 * Interface for entities that represent a module definition.
 * @author Andres Rodriguez
 */
public interface WithModuleDfn extends WithIdVersion, WithConfiguration, WithDependencies {
	/**
	 * Returns the definition id.
	 * @return The definition id.
	 */
	UUID getId();

	/**
	 * Returns the instance id.
	 * @return The instance id.
	 */
	UUID getInstanceId();

	/**
	 * Returns the module definition.
	 * @return The module definition.
	 * @throws IllegalStateException If the module definition can't be provided.
	 */
	ModuleDefinition<?> getModuleDefinition();

	/**
	 * Returns the module class name.
	 * @return The module class name.
	 */
	String getModuleClass();

	/**
	 * Sets the module class name.
	 * @param moduleClass The module class name.
	 */
	void setModuleClass(String moduleClass);

	/**
	 * Returns the instance name.
	 * @return The instance name.
	 */
	String getName();

	/**
	 * Sets the instance name.
	 * @param name The instance name.
	 */
	void setName(String name);

	/**
	 * Returns the instance description.
	 * @return The instance description.
	 */
	String getDescription();

	/**
	 * Sets the instance description.
	 * @param description The instance description.
	 */
	void setDescription(String description);
}
