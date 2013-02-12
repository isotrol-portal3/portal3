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


import java.util.List;
import java.util.UUID;

import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.model.DependencySetEntity;


/**
 * Dependency set manager.
 * @author Andres Rodriguez.
 */
public interface DependencySetManager {
	/**
	 * Fills a module definition.
	 * @param instanceId Module instance id.
	 * @param md Module definition.
	 * @param dependencies Dependencies to save.
	 * @param entity Destination set. If {@code null} it will be created.
	 * @return The saved entity.
	 */
	DependencySetEntity save(UUID instanceId, ModuleDefinition<?> md, List<DependencyDTO> dependencies, DependencySetEntity entity) throws PMSException;

	/**
	 * Deletes a set.
	 * @param entity Set to delete.
	 */
	void delete(DependencySetEntity entity);

	/**
	 * Duplicates a set.
	 * @param entity Set to duplicate.
	 * @return The duplicated set.
	 */
	DependencySetEntity duplicate(DependencySetEntity entity) throws PMSException;

}
