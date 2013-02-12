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

package com.isotrol.impe3.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;
import java.util.UUID;

import com.google.common.base.Objects;
import com.isotrol.impe3.api.Category;


/**
 * Key for category routing maps.
 * @author Andres Rodriguez
 */
final class CRKey {
	private final Locale locale;
	private final UUID id;
	private final int hash;

	CRKey(final Locale locale, final Category category) {
		this.locale = checkNotNull(locale);
		this.id = checkNotNull(category.getId());
		this.hash = Objects.hashCode(locale, this.id);
	}

	Locale getLocale() {
		return locale;
	}

	UUID getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CRKey) {
			final CRKey k = (CRKey) obj;
			return hash == k.hash && Objects.equal(id, k.id) && Objects.equal(locale, k.locale);
		}
		return false;
	}
}
