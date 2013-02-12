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
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Maps;
import com.isotrol.impe3.core.modules.ModuleDefinition;


/**
 * Connector definition.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "CONNECTOR_DFN")
public class ConnectorDfn extends Definition<ConnectorDfn, ConnectorEntity, ConnectorEdition> implements WithModuleDfn {
	/** Connector. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CNNT_ID", nullable = false)
	private ConnectorEntity entity;
	/** Module class name. */
	@Column(name = "MODULE_CLASS", nullable = false)
	private String moduleClass;
	/** Instance name. */
	@Column(name = "NAME", nullable = false)
	private String name;
	/** Instance description. */
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;
	/** Configuration. */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CNFG_ID", nullable = true)
	private ConfigurationEntity configuration;
	/** Dependencies. */
	@ElementCollection
	@JoinTable(name = "CONNECTOR_DEPS", joinColumns = @JoinColumn(name = "CNNT_DFN_ID", nullable = false))
	@MapKeyColumn(name = "DEPENDENCY_NAME", length = Lengths.NAME)
	private Map<String, RequiredDependencyValue> dependencies;
	/** Editions. */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "published")
	private Set<ConnectorEdition> editions;

	/** Default constructor. */
	public ConnectorDfn() {
	}

	/**
	 * Returns the connector.
	 * @return The connector.
	 */
	public ConnectorEntity getEntity() {
		return entity;
	}

	/**
	 * Sets the connector.
	 * @param entity The connector.
	 */
	public void setEntity(ConnectorEntity entity) {
		this.entity = entity;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.Definition#getEditions()
	 */
	@Override
	public Set<ConnectorEdition> getEditions() {
		return editions;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#getInstanceId()
	 */
	public UUID getInstanceId() {
		if (entity != null) {
			return entity.getId();
		}
		return null;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#getModuleClass()
	 */
	public String getModuleClass() {
		return moduleClass;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithModuleDfn#setModuleClass(java.lang.String)
	 */
	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
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
		try {
			return ModuleDefinition.of(moduleClass);
		} catch (RuntimeException e) {
			throw new IllegalStateException(e);
		}
	}
}
