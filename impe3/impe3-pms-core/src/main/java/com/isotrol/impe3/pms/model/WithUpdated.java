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


/**
 * Interface for entities with an update record.
 * @author Andres Rodriguez
 */
public interface WithUpdated {
	/**
	 * Returns the update record.
	 * @return The update record.
	 */
	Updated getUpdated();

	/**
	 * Sets the update record.
	 * @param updated The update record.
	 */
	void setUpdated(Updated updated);

	/**
	 * Sets the update record.
	 * @param user The user updating the record.
	 */
	void setUpdated(UserEntity user);

}
