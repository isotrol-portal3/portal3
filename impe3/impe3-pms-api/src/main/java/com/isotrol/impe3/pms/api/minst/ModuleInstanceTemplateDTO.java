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


import java.util.ArrayList;
import java.util.List;

import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;


/**
 * Template DTO for a module instance template.
 * @author Andres Rodriguez
 */
public class ModuleInstanceTemplateDTO extends ModuleInstanceSelDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 408683996409779322L;
	/** Dependencies. */
	private List<DependencyTemplateDTO> dependencies;
	/** Configuration. */
	private ConfigurationTemplateDTO configuration;

	/** Default constructor. */
	public ModuleInstanceTemplateDTO() {
	}

	/**
	 * Returns the dependencies.
	 * @return The dependencies.
	 */
	public List<DependencyTemplateDTO> getDependencies() {
		return dependencies;
	}

	/**
	 * Sets the dependencies.
	 * @param dependencies The dependencies.
	 */
	public void setDependencies(List<DependencyTemplateDTO> dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * Returns the configuration.
	 * @return The configuration.
	 */
	public ConfigurationTemplateDTO getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the configuration.
	 * @param configuration The configuration.
	 */
	public void setConfiguration(ConfigurationTemplateDTO configuration) {
		this.configuration = configuration;
	}

	/**
	 * Returns a ModuleInstanceDTO filled with the data from this template.
	 * @return The requested DTO.
	 */
	public ModuleInstanceDTO toModuleInstanceDTO() {
		final ModuleInstanceDTO dto = new ModuleInstanceDTO();
		dto.setId(getId());
		dto.setKey(getKey());
		dto.setName(getName());
		dto.setDescription(getDescription());
		if (configuration != null) {
			dto.setConfiguration(configuration.toConfiguationItemDTO());
		}
		if (dependencies != null) {
			final List<DependencyDTO> deps = new ArrayList<DependencyDTO>();
			for (final DependencyTemplateDTO template : dependencies) {
				if (template != null) {
					final DependencyDTO d = template.toDependencyDTO();
					if (d != null) {
						deps.add(d);
					}
				}
			}
			dto.setDependencies(deps);
		}
		return dto;
	}

}
