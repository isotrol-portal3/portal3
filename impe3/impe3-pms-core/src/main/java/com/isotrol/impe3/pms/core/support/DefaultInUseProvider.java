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
 * Default provider of EntityInUseException.
 * @author Andres Rodriguez.
 */
public class DefaultInUseProvider implements InUseProvider {
	/** Default constructor. */
	protected DefaultInUseProvider() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.InUseProvider#checkUsed(boolean, java.util.UUID)
	 */
	public final void checkUsed(boolean condition, UUID id) throws EntityInUseException {
		if (condition) {
			throw getInUseException(id);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.InUseProvider#checkUsed(boolean, java.lang.String)
	 */
	public final void checkUsed(boolean condition, String id) throws EntityInUseException {
		if (condition) {
			throw getInUseException(id);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.InUseProvider#getInUseException(java.util.UUID)
	 */
	public final EntityInUseException getInUseException(UUID id) {
		final String s = (id == null) ? null : id.toString();
		return getInUseException(s);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.support.InUseProvider#getInUseException(java.lang.String)
	 */
	public EntityInUseException getInUseException(String id) {
		return new EntityInUseException(id);
	}

}
