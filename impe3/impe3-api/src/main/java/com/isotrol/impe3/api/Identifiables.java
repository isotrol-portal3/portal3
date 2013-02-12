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

package com.isotrol.impe3.api;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


/**
 * Utility classes for identifiable objects.
 * @author Andres Rodriguez
 */
public final class Identifiables {
	/** Not instantiable. */
	private Identifiables() {
		throw new AssertionError();
	}

	private static final LoadingCache<UUID, String> STRINGS = CacheBuilder.newBuilder()
		.expireAfterAccess(60, TimeUnit.SECONDS).maximumSize(1000).build(new CacheLoader<UUID, String>() {
			public String load(UUID key) {
				return key.toString().toLowerCase();
			};
		});

	public static final Function<Identifiable, UUID> ID = new Function<Identifiable, UUID>() {
		public UUID apply(Identifiable from) {
			return from.getId();
		}
	};

	public static final Function<Identifiable, String> STRING_ID = new Function<Identifiable, String>() {
		public String apply(Identifiable from) {
			return toStringId(from);
		}
	};

	public static final Function<UUID, String> UUID2STRING = new Function<UUID, String>() {
		public String apply(UUID from) {
			return toStringId(from);
		}
	};

	public static String toStringId(UUID id) {
		return id == null ? null : STRINGS.getUnchecked(id);
	}

	public static String toStringId(Identifiable id) {
		return id == null ? null : toStringId(id.getId());
	}
}
