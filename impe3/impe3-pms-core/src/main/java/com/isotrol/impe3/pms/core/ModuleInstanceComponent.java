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

package com.isotrol.impe3.pms.core;


import java.util.Map;

import com.isotrol.impe3.pbuf.BaseProtos.ModulePB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.core.obj.Provider;
import com.isotrol.impe3.pms.model.RequiredDependencyValue;
import com.isotrol.impe3.pms.model.WithModuleDfn;


/**
 * Module instance helper component.
 * @author Andres Rodriguez.
 */
public interface ModuleInstanceComponent {
	/**
	 * Turns a map of providers to a map o values.
	 * @param providers Providers to transform.
	 * @return Transformed dependencies.
	 */
	Map<String, RequiredDependencyValue> provider2value(Map<String, Provider> providers) throws PMSException;

	/**
	 * Copies a definition to a dto.
	 * @param dfn Module definition.
	 * @param dto Destination DTO.
	 */
	void dfn2sel(WithModuleDfn dfn, ModuleInstanceSelDTO dto);

	/**
	 * Fills a module definition.
	 * @param dto Instance DTO.
	 * @param dfn Module definition to fill.
	 * @param isNew If the definition is a new module instance.
	 * @throws PMSException If an error happens.
	 */
	void fill(ModuleInstanceDTO dto, WithModuleDfn dfn, boolean isNew) throws PMSException;

	/**
	 * Fills a module definition.
	 * @param pb Instance PB Message.
	 * @param dfn Module definition to fill.
	 * @param isNew If the definition is a new module instance.
	 * @throws PMSException If an error happens.
	 */
	void fill(ModulePB pb, WithModuleDfn dfn, boolean isNew) throws PMSException;

}
