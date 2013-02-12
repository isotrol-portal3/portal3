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

package com.isotrol.impe3.pms.api.component;


import java.io.Serializable;

import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;


/**
 * DTO that represents a inherited component instance in a portal. The tri states have the following meaning: <ul> <li>
 * Null if the component has no dependencies or configuration. <li> True if the configuration or dependency set are
 * overriden. <li> False if the configuration or dependency set are not overriden.
 * @author Andres Rodriguez
 */
public class InheritedComponentInstanceSelDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 6876452493042736632L;
	/** Inherited component instance. */
	private ModuleInstanceSelDTO component;
	/** Tri-state for configuration. */
	private Boolean configuration;
	/** Tri-state for dependencies. */
	private Boolean dependencies;

	/** Default constructor. */
	public InheritedComponentInstanceSelDTO() {
	}

	/**
	 * Returns the inherited component instance.
	 * @return The inherited component instance.
	 */
	public ModuleInstanceSelDTO getComponent() {
		return component;
	}

	/**
	 * Sets the inherited component instance.
	 * @param value The inherited component instance.
	 */
	public void setComponent(ModuleInstanceSelDTO component) {
		this.component = component;
	}

	/**
	 * Returns the configuration state.
	 * @return The configuration state.
	 */
	public Boolean getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the configuration state.
	 * @param configuration The configuration state.
	 */
	public void setConfiguration(Boolean configuration) {
		this.configuration = configuration;
	}

	/**
	 * Returns the dependencies state.
	 * @return The dependencies state.
	 */
	public Boolean getDependencies() {
		return dependencies;
	}

	/**
	 * Sets the dependencies state.
	 * @param dependencies The dependencies state.
	 */
	public void setDependencies(Boolean dependencies) {
		this.dependencies = dependencies;
	}

}
