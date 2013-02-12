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


import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKey;

import com.google.common.collect.Maps;
import com.isotrol.impe3.core.modules.ModuleDefinition;


/**
 * Entity that represents a component module instantiation.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "COMPONENT_DFN")
public class ComponentDfn extends WithUpdatedEntity implements WithModuleDfn {
	/** Component entity. */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "CMPT_ID", nullable = false)
	private ComponentEntity component;
	/** Instance name. */
	@Column(name = "NAME", nullable = false, length = Lengths.NAME)
	private String name;
	/** Instance description. */
	@Column(name = "DESCRIPTION", nullable = true, length = Lengths.DESCRIPTION)
	private String description;
	/** Configuration. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CNFG_ID", nullable = true)
	private ConfigurationEntity configuration;
	/** Dependencies. */
	@CollectionOfElements
	@JoinTable(name = "COMPONENT_DFN_DEPS", joinColumns = @JoinColumn(name = "CMPT_DFN_ID", nullable = false))
	@MapKey(columns = @Column(name = "DEPENDENCY_NAME", length = Lengths.NAME))
	private Map<String, RequiredDependencyValue> dependencies;

	/** Default constructor. */
	public ComponentDfn() {
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#getInstanceId()
	 */
	public UUID getInstanceId() {
		return component.getId();
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#getModuleClass()
	 */
	public String getModuleClass() {
		return component.getModuleClass();
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#setModuleClass(java.lang.String)
	 */
	public void setModuleClass(String moduleClass) {
		component.setModuleClass(moduleClass);
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#getConfiguration()
	 */
	public ConfigurationEntity getConfiguration() {
		return configuration;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#setConfiguration(com.isotrol.impe3.pms.model.ConfigurationEntity)
	 */
	public void setConfiguration(ConfigurationEntity configuration) {
		this.configuration = configuration;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#getDependencies()
	 */
	public Map<String, RequiredDependencyValue> getDependencies() {
		if (dependencies == null) {
			dependencies = Maps.newHashMap();
		}
		return dependencies;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#getModuleDefinition()
	 */
	public ModuleDefinition<?> getModuleDefinition() {
		return component.getModuleDefinition();
	}

	/**
	 * Returns the component.
	 * @return The component.
	 */
	public ComponentEntity getComponent() {
		return component;
	}

	/**
	 * Sets the component.
	 * @param portal The component.
	 */
	public void setComponent(ComponentEntity component) {
		this.component = component;
	}

}
