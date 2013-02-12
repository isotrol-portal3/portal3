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


import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Copyright;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.metadata.Version;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.support.WithCategories;
import com.isotrol.impe3.api.support.WithURIGenerator;
import com.isotrol.impe3.freemarker.WithFreeMarker;


/**
 * Category Menu Component Module.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
@Bundle
@Name("module")
@Copyright("Isotrol, SA. (GPL3)")
@Version("1.0")
public interface CategoryMenuModule extends WithFreeMarker, WithCategories, WithURIGenerator, Module {
	/** Provided component. */
	@Name("firstLevel")
	FirstLevelComponent firstLevel();

	/** Provided component. */
	@Name("currentLevel")
	CurrentLevelComponent currentLevel();

	/** Provided component. */
	@Name("anchored")
	AnchoredComponent anchored();

	/** Module configuration. */
	void config(CategoryMenuModuleConfig config);

	/** External predicate. */
	@Name("filterProvider")
	void filterProvider(CategoryPredicateProvider p);
}