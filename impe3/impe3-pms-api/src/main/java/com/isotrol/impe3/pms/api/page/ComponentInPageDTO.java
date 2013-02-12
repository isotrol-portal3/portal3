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

package com.isotrol.impe3.pms.api.page;


import java.util.List;

import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;


/**
 * Value that represents a component in a page.
 * @author Andres Rodriguez
 */
public class ComponentInPageDTO extends AbstractComponentInPageDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 2791210238871396451L;
	/** Component key. */
	private ComponentKey component;
	/** Configuration items. */
	private List<ConfigurationItemDTO> configuration;
	/** Children component. */
	private List<ComponentInPageDTO> children;

	/** Default constructor. */
	public ComponentInPageDTO() {
	}

	/**
	 * Returns the component module.
	 * @return The component module.
	 */
	public ComponentKey getComponent() {
		return component;
	}

	/**
	 * Sets the component module.
	 * @param component The component module.
	 */
	public void setComponent(ComponentKey component) {
		this.component = component;
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

	/**
	 * Returns the children components.
	 * @return The children components.
	 */
	public List<ComponentInPageDTO> getChildren() {
		return children;
	}

	/**
	 * Sets the children components.
	 * @param children The children components.
	 */
	public void setChildren(List<ComponentInPageDTO> children) {
		this.children = children;
	}

	@Override
	public boolean isSpace() {
		return (component == null);
	}
	
}
