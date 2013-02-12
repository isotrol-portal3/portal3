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


import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.pbuf.BaseProtos.ConfigurationPB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.model.ConfigurationEntity;


/**
 * Connectors service.
 * @author Andres Rodriguez.
 */
public interface ConfigurationManager {
	/**
	 * Creates a new configuration.
	 * @param cd Configuration definition.
	 * @param configuration Configuration DTO.
	 * @return The created configuration entity.
	 * @throws PMSException If an error occurs.
	 */
	ConfigurationEntity create(ConfigurationDefinition<?> cd, Collection<ConfigurationItemDTO> configuration)
		throws PMSException;

	/**
	 * Creates a new configuration.
	 * @param cd Configuration definition.
	 * @param configuration Configuration PB message.
	 * @return The created configuration entity.
	 * @throws PMSException If an error occurs.
	 */
	ConfigurationEntity create(ConfigurationDefinition<?> cd, ConfigurationPB configuration) throws PMSException;

	/**
	 * Updates an existing configuration.
	 * @param cd Configuration definition.
	 * @param entity Configuration entity.
	 * @return The updated entity.
	 * @throws PMSException If an error occurs.
	 */
	ConfigurationEntity update(ConfigurationDefinition<?> cd, ConfigurationEntity entity,
		Collection<ConfigurationItemDTO> configuration) throws PMSException;

	/**
	 * Updates an existing configuration.
	 * @param cd Configuration definition.
	 * @param entity Configuration entity.
	 * @return The updated entity.
	 * @throws PMSException If an error occurs.
	 */
	ConfigurationEntity update(ConfigurationDefinition<?> cd, ConfigurationEntity entity, ConfigurationPB configuration)
		throws PMSException;

	/**
	 * Updates an existing configuration.
	 * @param cd Configuration definition.
	 * @param id Configuration entity id.
	 * @return The updated entity.
	 * @throws PMSException If an error occurs.
	 */
	ConfigurationEntity update(ConfigurationDefinition<?> cd, UUID id, Collection<ConfigurationItemDTO> configuration)
		throws PMSException;

	/**
	 * Updates an existing configuration.
	 * @param cd Configuration definition.
	 * @param id Configuration entity id.
	 * @return The updated entity.
	 * @throws PMSException If an error occurs.
	 */
	ConfigurationEntity update(ConfigurationDefinition<?> cd, UUID id, ConfigurationPB configuration)
		throws PMSException;

	/**
	 * Deletes a configuration.
	 * @param entity Configuration to delete.
	 */
	void delete(ConfigurationEntity entity);

	/**
	 * Converts a configuration message into a configuration dto.
	 * @param configuration Configuration message.
	 * @return The configuration DTO.
	 */
	List<ConfigurationItemDTO> pb2dto(ConfigurationPB configuration) throws PMSException;

	/**
	 * Duplicates a configuration.
	 * @param entity Configuration to duplicate.
	 * @return The duplicated configration.
	 */
	ConfigurationEntity duplicate(ConfigurationEntity entity) throws PMSException;

}
