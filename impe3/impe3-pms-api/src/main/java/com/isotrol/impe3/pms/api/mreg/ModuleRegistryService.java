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

package com.isotrol.impe3.pms.api.mreg;


import java.util.List;


/**
 * Module registry service.
 * @author Andres Rodriguez.
 */
public interface ModuleRegistryService {
	/**
	 * Returns all registered connector modules.
	 * @return All registered connector modules.
	 */
	List<ConnectorModuleDTO> getConnectors();

	/**
	 * Returns all registered component modules.
	 * @return All registered component modules.
	 */
	List<ComponentModuleDTO> getComponents();

	/**
	 * Returns all registered invalid modules.
	 * @return All registered invalid modules.
	 */
	List<InvalidModuleDTO> getInvalids();

	/**
	 * Returns the module names that were not found.
	 * @return The module names that were not found.
	 */
	List<String> getNotFound();

	/**
	 * Returns the module types that were not assignable to Module.
	 * @return The module names that were assignable to Module.
	 */
	List<String> getNotModule();

}
