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

package com.isotrol.impe3.core.impl;


import java.util.Map;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;


/**
 * Factory class for different content type collection implementations.
 * @author Andres Rodriguez
 */
public final class ContentTypesFactory {
	/** Not instantiable. */
	private ContentTypesFactory() {
		throw new AssertionError();
	}

	private static final Empty EMPTY = new Empty();

	/**
	 * Returns an empty collection.
	 */
	public static ContentTypes of() {
		return EMPTY;
	}

	/**
	 * Returns a collection of cookies containing the provided content types.
	 * @param types Content types to add to the collection.
	 * @return The requested collection.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if there are more than one content type with the same id.
	 */
	public static ContentTypes of(Iterable<ContentType> types) {
		Preconditions.checkNotNull(types);
		final ImmutableMap.Builder<UUID, ContentType> builder = ImmutableMap.builder();
		for (final ContentType type : types) {
			builder.put(type.getId(), type);
		}
		return new Immutable(builder.build());
	}

	/**
	 * Abstract collection.
	 * @author Andres Rodriguez
	 */
	private abstract static class Abstract extends AbstractIdentifiableMap<ContentType> implements ContentTypes {
		private Abstract() {
		}
	}

	/**
	 * Empty collection.
	 * @author Andres Rodriguez
	 */
	private static final class Empty extends Abstract {
		private Empty() {
		}

		@Override
		protected Map<UUID, ContentType> delegate() {
			return ImmutableMap.of();
		}
	}

	/**
	 * Immutable collection.
	 * @author Andres Rodriguez
	 */
	private static final class Immutable extends Abstract {
		private ImmutableMap<UUID, ContentType> map;

		private Immutable(ImmutableMap<UUID, ContentType> map) {
			this.map = map;
		}

		@Override
		protected Map<UUID, ContentType> delegate() {
			return map;
		}
	}
}
