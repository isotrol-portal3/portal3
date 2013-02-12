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


/**
 * DTO for the dependency of a module.
 * @author Andres Rodriguez
 */
public final class ModuleDependencyDTO extends ModuleRelationDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -2006182514812633861L;
	/** If the dependency is required. */
	private boolean required;
	/** If the dependency is satisfiable. */
	private boolean satisfiable;

	/** Default constructor. */
	public ModuleDependencyDTO() {
	}

	/**
	 * Returs whether the dependency is required.
	 * @return True if the dependency is required.
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Sets whether the dependency is required.
	 * @param required True if the dependency is required.
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Returs whether the dependency is satisfiable.
	 * @return True if the dependency is satisfiable.
	 */
	public boolean isSatisfiable() {
		return satisfiable;
	}

	/**
	 * Sets whether the dependency is satisfiable.
	 * @param satisfiable True if the dependency is satisfiable.
	 */
	public void setSatisfiable(boolean satisfiable) {
		this.satisfiable = satisfiable;
	}

}
