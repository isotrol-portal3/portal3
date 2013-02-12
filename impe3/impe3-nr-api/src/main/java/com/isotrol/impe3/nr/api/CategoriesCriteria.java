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

package com.isotrol.impe3.nr.api;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.UUID;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.isotrol.impe3.nr.api.NodeFilter.Builder;


/**
 * Categories filter criteria.
 * @see FilterCriteria for a general description of criteria.
 * @author Andres Rodriguez
 */
public final class CategoriesCriteria extends FilterCriteria<Optional<UUID>> {
	/**
	 * Constructor.
	 */
	CategoriesCriteria(Builder builder) {
		super(builder);
	}

	/**
	 * Copy constructor.
	 */
	CategoriesCriteria(Builder builder, CategoriesCriteria criteria) {
		super(builder, criteria);
	}

	/**
	 * Applies a category criterion.
	 * @param id Category id to filter ({@code null} for uncategorized nodes).
	 * @param type Filter type.
	 * @return The filter builder for method chaining.
	 * @throws NullPointerException if any of the arguments is {@code null}.
	 */
	public final Builder apply(UUID id, FilterType type) {
		return apply(Optional.fromNullable(id), type);
	}

	/**
	 * Applies some criteria.
	 * @param type Filter type.
	 * @param values Values to add. If empty, no operation if performed.
	 * @return The filter builder for method chaining.
	 * @throws NullPointerException if the type or any of the values is {@code null}.
	 */
	public final Builder apply(FilterType type, UUID... values) {
		checkNotNull(values, "The filter values to apply must be provided");
		List<Optional<UUID>> list = Lists.newArrayListWithCapacity(values.length);
		for (UUID id : values) {
			list.add(Optional.fromNullable(id));
		}
		return apply(type, list);
	}

	/**
	 * Applies a REQUIRED criterion.
	 * @param id Category id to filter ({@code null} for uncategorized nodes).
	 * @return The filter builder for method chaining.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public final Builder apply(UUID id) {
		return apply(Optional.fromNullable(id), FilterType.REQUIRED);
	}

	/**
	 * Applies the provided criterion to the uncategorized nodes.
	 * @param type Filter type.
	 * @return The filter builder for method chaining.
	 * @throws NullPointerException if the filter type is {@code null}.
	 */
	public final Builder uncategorized(FilterType type) {
		return apply(Optional.<UUID> absent(), type);
	}

}
