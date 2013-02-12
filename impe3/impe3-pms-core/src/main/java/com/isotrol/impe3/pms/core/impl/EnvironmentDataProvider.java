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

package com.isotrol.impe3.pms.core.impl;


import com.isotrol.impe3.pms.model.CategoryDfn;
import com.isotrol.impe3.pms.model.ConnectorDfn;
import com.isotrol.impe3.pms.model.ContentTypeDfn;
import com.isotrol.impe3.pms.model.PortalDfn;


/**
 * A provider for environment data. This objects are used as an input for Engine Data Loaders. They can only be used
 * inside a transaction.
 * @author Andres Rodriguez.
 */
public interface EnvironmentDataProvider {
	/**
	 * Returns the content types.
	 * @return The content types.
	 */
	Iterable<ContentTypeDfn> getContentType();

	/**
	 * Returns the categories.
	 * @return The categories.
	 */
	Iterable<CategoryDfn> getCategoriesType();

	/**
	 * Returns the connectors.
	 * @return The connectors.
	 */
	Iterable<ConnectorDfn> getConnectors();

	/**
	 * Returns the portals.
	 * @return The portals.
	 */
	Iterable<PortalDfn> getPortals();
}
