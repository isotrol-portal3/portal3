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


import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Category Menu own Configuration items.
 * @author Andres Rodriguez
 */
public interface WithCategoryMenuConfig {
	@Optional
	@Name("Categor\u00eda")
	@Description("Categor\u00eda a usar como actual en lugar de la navegacion.")
	Category current();

	@Optional
	@Name("Incluir no Visibles")
	@Description("Si se deben incluir las categor\u00edas no visibles. Nulo se considera falso")
	Boolean includeNotVisible();

	@Optional
	@Name("Incluir no Rutables")
	@Description("Si se deben incluir las categor\u00edas no rutables. Nulo se considera falso")
	Boolean includeNotRoutable();

	@Optional
	@Name("Nivel de Profundidad")
	@Description("Niveles adicionales a incluir en el menu. Nulo se considera 0 y negativo todos los niveles.")
	Integer depth();

	@Optional
	@Name("Categor\u00eda Seleccionada")
	@Description("Fuerza la selecci\u00f3n de una categor\u00eda.")
	Category selected();
}
