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


import com.isotrol.impe3.pms.api.mreg.ModuleDependencyDTO;


/**
 * Template DTO for dependency edition.
 * @author Andres Rodriguez
 */
public class DependencyTemplateDTO extends ProvidedTemplateDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -2274398631399915651L;
	/** Dependency description. */
	private ModuleDependencyDTO dependency;

	/** Default constructor. */
	public DependencyTemplateDTO() {
	}

	/**
	 * Returns the dependency description.
	 * @return The dependency description.
	 */
	public ModuleDependencyDTO getDependency() {
		return dependency;
	}

	/**
	 * Sets the dependency description.
	 * @param dependency The dependency description.
	 */
	public void setDependency(ModuleDependencyDTO dependency) {
		this.dependency = dependency;
	}

	/**
	 * Returns a DependencyDTO filled with the data from this template.
	 * @return The requested DTO.
	 */
	public DependencyDTO toDependencyDTO() {
		if (dependency == null) {
			return null;
		}
		final String name = dependency.getBean();
		if (name == null) {
			return null;
		}
		final DependencyDTO dto = fill(new DependencyDTO());
		if (dto == null) {
			return null;
		}
		dto.setName(name);
		return dto;
	}
}
