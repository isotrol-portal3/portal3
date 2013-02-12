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

import com.isotrol.impe3.pms.api.EntityNotFoundException;


/**
 * A provider of EntityNotFoundException.
 * @author Andres Rodriguez.
 */
public interface NotFoundProvider {
	/**
	 * Returns the exception.
	 * @param id Entity id.
	 * @return The requested exception.
	 */
	EntityNotFoundException getNotFoundException(UUID id);

	/**
	 * Returns the exception.
	 * @param id Entity id.
	 * @return The requested exception.
	 */
	EntityNotFoundException getNotFoundException(String id);

	/**
	 * Transform an string id into an UUID.
	 * @param id The StringUUID.
	 * @return The UUID.
	 * @throws EntityNotFoundException If the conversion fails.
	 */
	UUID toUUID(String id) throws EntityNotFoundException;

	/**
	 * Throws the provided exception if the provided argument is null.
	 * @param value Value to check.
	 * @param id Exception argument.
	 * @return The provided argument.
	 * @throws EntityNotFoundException if the argument is null.
	 */
	<T> T checkNotNull(T value, UUID id) throws EntityNotFoundException;

	/**
	 * Throws the provided exception if the provided argument is null.
	 * @param value Value to check.
	 * @param id Exception argument.
	 * @return The provided argument.
	 * @throws EntityNotFoundException if the argument is null.
	 */
	<T> T checkNotNull(T value, String id) throws EntityNotFoundException;

	/**
	 * Throws the provided exception if the provided condition is false.
	 * @param condition Condition to check.
	 * @param id Exception argument.
	 * @throws EntityNotFoundException if the argument is null.
	 */
	void checkCondition(boolean condition, UUID id) throws EntityNotFoundException;

	/**
	 * Throws the provided exception if the provided condition is false.
	 * @param condition Condition to check.
	 * @param id Exception argument.
	 * @throws EntityNotFoundException if the argument is null.
	 */
	void checkCondition(boolean condition, String id) throws EntityNotFoundException;

}
