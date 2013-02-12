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

package com.isotrol.impe3.pms.api.minst;


import java.util.List;

import com.isotrol.impe3.pms.api.AbstractWithId;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;


/**
 * DTO for a module instance.
 * @author Andres Rodriguez
 */
public class ModuleInstanceDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = -1085654973976330131L;
	/** Module key (module interface name). */
	private String key;
	/** Instance name. */
	private String name;
	/** Instance description. */
	private String description;
	/** Dependencies. */
	private List<DependencyDTO> dependencies;
	/** Configuration items. */
	private List<ConfigurationItemDTO> configuration;

	/** Default constructor. */
	public ModuleInstanceDTO() {
	}

	/**
	 * Returns the module key (module interface name).
	 * @return The module key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the module key.
	 * @param key The module key.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns the instance name.
	 * @return The instance name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the instance name.
	 * @param name The instance name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the instance description.
	 * @return The instance description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the instance description.
	 * @param description The instance description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the dependencies.
	 * @return The dependencies.
	 */
	public List<DependencyDTO> getDependencies() {
		return dependencies;
	}

	/**
	 * Sets the dependencies.
	 * @param dependencies The dependencies.
	 */
	public void setDependencies(List<DependencyDTO> dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * Returns the configuration items.
	 * @return The configuration items.
	 */
	public List<ConfigurationItemDTO> getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the configuration items.
	 * @param configuration The configuration items.
	 */
	public void setConfiguration(List<ConfigurationItemDTO> configuration) {
		this.configuration = configuration;
	}

}
