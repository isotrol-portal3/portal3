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

package com.isotrol.impe3.pms.core.support;


import java.util.UUID;

import com.isotrol.impe3.pms.api.EntityInUseException;


/**
 * A provider of EntityNotFoundException.
 * @author Andres Rodriguez.
 */
public interface InUseProvider {
	/**
	 * Returns the exception.
	 * @param id Entity id.
	 * @return The requested exception.
	 */
	EntityInUseException getInUseException(UUID id);

	/**
	 * Returns the exception.
	 * @param id Entity id.
	 * @return The requested exception.
	 */
	EntityInUseException getInUseException(String id);

	/**
	 * Throws the provided exception if the provided condition is true.
	 * @param condition Condition to check.
	 * @param id Exception argument.
	 * @throws EntityInUseException if the condition is true.
	 */
	void checkUsed(boolean condition, UUID id) throws EntityInUseException;

	/**
	 * Throws the provided exception if the provided condition is true.
	 * @param condition Condition to check.
	 * @param id Exception argument.
	 * @throws EntityInUseException if the argument is true.
	 */
	void checkUsed(boolean condition, String id) throws EntityInUseException;

}
