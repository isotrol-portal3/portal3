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


import java.io.Serializable;



/**
 * DTO for a dependency provider template.
 * @author Andres Rodriguez
 */
public class ProviderDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -6506309327898595203L;
	/** Provider connector. */
	private ModuleInstanceSelDTO current;
	/** Provider bean. */
	private String bean;
	/** Provider bean description. */
	private String description;

	/** Default constructor. */
	public ProviderDTO() {
	}

	/**
	 * Returns the provider connector.
	 * @return The provider connector.
	 */
	public ModuleInstanceSelDTO getCurrent() {
		return current;
	}

	/**
	 * Sets the provider connector.
	 * @param dependency The provider connector.
	 */
	public void setCurrent(ModuleInstanceSelDTO current) {
		this.current = current;
	}

	/**
	 * Returns the provider bean.
	 * @return The provider bean.
	 */
	public String getBean() {
		return bean;
	}

	/**
	 * Sets the provider bean.
	 * @param bean The provider bean.
	 */
	public void setBean(String bean) {
		this.bean = bean;
	}

	/**
	 * Returns the provider bean description.
	 * @return The provider bean description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the provider bean description.
	 * @param description The provider bean description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
