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
 * Default provider of EntityNotFoundException.
 * @author Andres Rodriguez.
 */
public class DefaultNotFoundProvider implements NotFoundProvider {

	/** Default constructor. */
	protected DefaultNotFoundProvider() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.NotFoundProvider#getNotFoundException(java.util.UUID)
	 */
	public final EntityNotFoundException getNotFoundException(UUID id) {
		final String s = (id == null) ? null : id.toString();
		return getNotFoundException(s);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.NotFoundProvider#getNotFoundException(java.lang.String)
	 */
	public EntityNotFoundException getNotFoundException(String id) {
		return new EntityNotFoundException(id);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.NotFoundProvider#toUUID(java.lang.String)
	 */
	public final UUID toUUID(String id) throws EntityNotFoundException {
		if (id == null) {
			return null;
		}
		try {
			return UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw getNotFoundException(id);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.NotFoundProvider#checkNotNull(java.lang.Object, java.lang.String)
	 */
	public final <T> T checkNotNull(T value, String id) throws EntityNotFoundException {
		if (value == null) {
			throw getNotFoundException(id);
		}
		return value;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.NotFoundProvider#checkNotNull(java.lang.Object, java.util.UUID)
	 */
	public final <T> T checkNotNull(T value, UUID id) throws EntityNotFoundException {
		if (value == null) {
			throw getNotFoundException(id);
		}
		return value;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.NotFoundProvider#checkCondition(boolean, java.lang.String)
	 */
	public final void checkCondition(boolean condition, String id) throws EntityNotFoundException {
		if (!condition) {
			throw getNotFoundException(id);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.NotFoundProvider#checkCondition(boolean, java.util.UUID)
	 */
	public final void checkCondition(boolean condition, UUID id) throws EntityNotFoundException {
		if (!condition) {
			throw getNotFoundException(id);
		}
	}

}
