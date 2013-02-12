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

package com.isotrol.impe3.pms.api.mreg;


import java.io.Serializable;

import com.isotrol.impe3.pms.api.Described;


/**
 * Abstract DTO for module dependencies and provisions.
 * @author Andres Rodriguez
 */
public abstract class ModuleRelationDTO implements Described, Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 1345704308462846587L;
	/** Bean Name. */
	private String bean;
	/** Name. */
	private String name;
	/** Type (class name). */
	private String type;
	/** Description. */
	private String description;

	/** Default constructor. */
	public ModuleRelationDTO() {
	}

	/**
	 * Returns the bean name.
	 * @return The bean name
	 */
	public String getBean() {
		return bean;
	}

	/**
	 * Sets the bean name.
	 * @param bean The bean name.
	 */
	public void setBean(String bean) {
		this.bean = bean;
	}

	/**
	 * Returns the name.
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the type.
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * @param type The type.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the description.
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * @param description The description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
