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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.isotrol.impe3.core.modules.ModuleDefinition;


/**
 * Entity that represents a component module instantiation.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "COMPONENT")
@NamedQueries( {
	@NamedQuery(name = ComponentEntity.USED, query = "from PortalEntity as p join p.current.pages as g join g.components as c where p.environment.id = :envId and c.component = :entity"),
	@NamedQuery(name = ComponentEntity.DFNS, query = "from ComponentDfn d where d.component.portal.environment.id = :envId and d.component.id = :id")})
public class ComponentEntity extends OfPortal {
	/** Is the component instance used in offline. */
	public static final String USED = "component.used";
	/** Definitions of a component instance. */
	public static final String DFNS = "component.dfns";
	/** Module class name. */
	@Column(name = "MODULE_CLASS", nullable = false)
	private String moduleClass;

	/** Default constructor. */
	public ComponentEntity() {
	}

	/**
	 * Returns the module class name.
	 * @return The module class name.
	 */
	public String getModuleClass() {
		return moduleClass;
	}

	/**
	 * Sets the module class name.
	 * @param moduleClass The module class name.
	 */
	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}

	/**
	 * Returns the module definition.
	 * @return The module definition.
	 * @throws IllegalStateException If the module definition can't be provided.
	 */
	public ModuleDefinition<?> getModuleDefinition() {
		try {
			return ModuleDefinition.of(moduleClass);
		} catch (RuntimeException e) {
			throw new IllegalStateException(e);
		}
	}
}
