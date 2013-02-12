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


import com.isotrol.impe3.pms.api.AbstractWithStateAndIdAndCorrectness;
import com.isotrol.impe3.pms.api.mreg.ModuleSelDTO;


/**
 * DTO for module instance selection.
 * @author Andres Rodriguez
 */
public class ModuleInstanceSelDTO extends AbstractWithStateAndIdAndCorrectness {
	/** Serial UID. */
	private static final long serialVersionUID = 7734806924654786184L;
	/** Module. */
	private ModuleSelDTO module;
	/** Instance name. */
	private String name;
	/** Instance description. */
	private String description;

	/** Default constructor. */
	public ModuleInstanceSelDTO() {
	}

	/**
	 * Returns the module.
	 * @return The module.
	 */
	public ModuleSelDTO getModule() {
		return module;
	}

	/**
	 * Sets the module.
	 * @param module The module.
	 */
	public void setModule(ModuleSelDTO module) {
		this.module = module;
	}

	/**
	 * Returns the module key (module interface name).
	 * @return The module key.
	 */
	public String getKey() {
		if (module != null) {
			return module.getId();
		}
		return null;
	}

	/**
	 * Returns the instance name.
	 * @return The instance name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the instance name.
	 * @param name The instance name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the instance description.
	 * @return The instance description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the instance description.
	 * @param description The instance description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
