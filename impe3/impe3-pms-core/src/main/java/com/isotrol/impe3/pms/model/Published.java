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

package com.isotrol.impe3.pms.model;


import java.util.UUID;

import com.google.common.base.Function;


/**
 * Interface for entities that have ever been published.
 * @author Andres Rodriguez
 */
public interface Published {
	Function<Published, UUID> ENTITY_ID = new Function<Published, UUID>() {
		public UUID apply(Published from) {
			return from.getEntityId();
		}
	}; 
	
	/**
	 * Returns the id of the published entity.
	 * @return The id of the published entity.
	 */
	UUID getEntityId();

	Function<Published, UUID> DFN_ID = new Function<Published, UUID>() {
		public UUID apply(Published from) {
			return from.getDefinitionId();
		}
	}; 
	
	/**
	 * Returns the id of the published definition.
	 * @return The id of the published definition.
	 */
	UUID getDefinitionId();

	Function<Published, UUID> EDITION_ID = new Function<Published, UUID>() {
		public UUID apply(Published from) {
			return from.getEditionId();
		}
	}; 

	/**
	 * Returns the id of the edition.
	 * @return The id of the edition.
	 */
	UUID getEditionId();

}
