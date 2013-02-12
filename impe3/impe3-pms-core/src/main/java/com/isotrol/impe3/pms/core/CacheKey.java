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


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.google.common.base.Objects;


/**
 * Loader cache keys.
 * @author Andres Rodriguez
 */
public final class CacheKey {
	public static CacheKey create(UUID id, int version) {
		return new CacheKey(id, version);
	}

	private final UUID id;
	private final Integer version;
	private final int hash;

	private CacheKey(UUID id, Integer version) {
		this.id = checkNotNull(id);
		this.version = version;
		this.hash = Objects.hashCode(id, version);
	}

	public UUID getId() {
		return id;
	}

	public Integer getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof CacheKey) {
			final CacheKey k = (CacheKey) obj;
			return hash == k.hash && equal(id, k.id) && equal(version, k.version);
		}
		return false;
	}

}
