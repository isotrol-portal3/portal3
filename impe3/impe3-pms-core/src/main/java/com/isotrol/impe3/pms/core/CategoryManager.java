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


import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.obj.CategoriesObject;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;


/**
 * Category manager.
 * @author Andres Rodriguez.
 */
public interface CategoryManager extends GenericManager<CategoriesObject> {
	/** Root category name. */
	String NAME = "Root";
	/** Root category path segment. */
	String PATH = "root";

	/**
	 * Gets the root category, creating it if needed.
	 * @return The default environment.
	 */
	CategoryEntity getRoot(EnvironmentEntity environment) throws PMSException;
}
