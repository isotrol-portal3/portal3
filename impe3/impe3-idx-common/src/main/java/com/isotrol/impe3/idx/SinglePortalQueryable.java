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

package com.isotrol.impe3.idx;


import net.sf.lucis.core.DirectoryProvider;

import com.google.common.base.Function;


/**
 * Interface for Port@l content aggregation platform queryable of a single directory.
 * @author Andres Rodriguez Chamorro
 */
public interface SinglePortalQueryable extends PortalQueryable {
	Function<SinglePortalQueryable, DirectoryProvider> DIRECTORY_PROVIDER = new Function<SinglePortalQueryable, DirectoryProvider>() {
		public DirectoryProvider apply(SinglePortalQueryable input) {
			return input.getDirectoryProvider();
		}
	};

	/**
	 * Returns the directory provider to use for repository building.
	 * @return The index directory provider.
	 */
	DirectoryProvider getDirectoryProvider();
}
