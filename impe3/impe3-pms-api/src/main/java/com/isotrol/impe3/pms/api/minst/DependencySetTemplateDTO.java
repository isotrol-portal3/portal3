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
import java.util.ArrayList;
import java.util.List;


/**
 * Template DTO for a dependency set.
 * @author Andres Rodriguez
 */
public class DependencySetTemplateDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 477803224311870229L;
	/** Dependencies. */
	private List<DependencyTemplateDTO> dependencies;

	/** Default constructor. */
	public DependencySetTemplateDTO() {
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
	 * Returns a list of DependencyDTO filled with the data from this template.
	 * @return The requested DTO list.
	 */
	public List<DependencyDTO> toDependencyListDTO() {
		final List<DependencyDTO> deps = new ArrayList<DependencyDTO>();
		if (dependencies != null) {
			for (final DependencyTemplateDTO template : dependencies) {
				if (template != null) {
					final DependencyDTO d = template.toDependencyDTO();
					if (d != null) {
						deps.add(d);
					}
				}
			}
		}
		return deps;
	}

}
