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

package com.isotrol.impe3.palette.menu.category;


import javax.ws.rs.DefaultValue;

import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.freemarker.FreeMarkerConfiguration;
import com.isotrol.impe3.freemarker.WithTemplateFile;
import com.isotrol.impe3.palette.menu.WithFilterByContentAvailability;


/**
 * Category Menu Module Configuration.
 * @author Andres Rodriguez
 */
public interface CategoryMenuModuleConfig extends FreeMarkerConfiguration, WithTemplateFile, WithCategoryMenuConfig,
	WithFilterByContentAvailability {
	@Name("Selecci\u00f3n desacoplada")
	@Description("No utilizar el campo de categor\u00eda actual para elegir la seleccionada.")
	@DefaultValue("false")
	boolean decoupledSelection();

	@Name("Seleccionar padres")
	@Description("Marcar como seleccionadas las categor\u00edas padre de las seleccionadas.")
	@DefaultValue("false")
	boolean selectParents();
}
