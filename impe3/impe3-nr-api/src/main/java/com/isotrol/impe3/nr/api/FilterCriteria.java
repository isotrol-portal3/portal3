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


import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.nr.api.NodeFilter.Builder;


/**
 * Node filter criteria implementation. A null criteria no filtering. An empty filter always returns an empty result.
 * Filters are "joined" in the the sense that operations are applied in order with the following criteria: <ul> <li>A
 * null filter performs no filtering.</li> <li>An empty criteria always returns an empty result.</li> <li>For an empty
 * criteria every operation returns the empty filter.</li> <li>OPTIONAL elements are OR'd together. The resulting filter
 * is NOT'd with the FORBIDDEN and the resulting is AND'd with the REQUIRED.</li> </ul>
 * @see FilterType for the filter type join results.
 * @author Andres Rodriguez
 */
public abstract class FilterCriteria<T> implements IsEmptyFlag, IsNullFlag {
	/** Owning builder. */
	private final Builder builder;
	/** Criteria. */
	private Map<T, FilterType> criteria;

	/**
	 * Constructor.
	 */
	FilterCriteria(Builder builder) {
		this.builder = checkNotNull(builder);
		this.criteria = null;
	}

	/**
	 * Copy constructor.
	 */
	FilterCriteria(Builder builder, FilterCriteria<T> criteria) {
		this.builder = checkNotNull(builder);
		if (criteria.criteria == null) {
			this.criteria = null;
		} else {
			this.criteria = Maps.newHashMap(criteria.criteria);
		}
	}

	public final boolean isEmpty() {
		return criteria != null && criteria.isEmpty();
	}

	public final boolean isNull() {
		return criteria == null;
	}

	/** Returns an unmodifiable view of the current criteria. */
	final Map<T, FilterType> get() {
		if (criteria == null) {
			return null;
		}
		return Collections.unmodifiableMap(criteria);
	}

	/**
	 * Applies some criteria.
	 * @param criteria Criteria to add. If the provided map is empty, an empty criteria will result.
	 * @return The filter builder for method chaining.
	 * @throws NullPointerException if the argument or any of the criteria is {@code null}.
	 */
	public final Builder apply(Map<? extends T, FilterType> criteria) {
		checkNotNull(criteria, "The criteria to apply must be provided");
		if (criteria != null && !isEmpty()) {
			if (criteria.isEmpty()) {
				this.criteria = ImmutableMap.of();
			} else {
				for (Entry<? extends T, FilterType> entry : criteria.entrySet()) {
					if (isEmpty()) {
						break;
					}
					T value = entry.getKey();
					FilterType type = entry.getValue();
					apply(value, type);
				}
			}
		}
		return builder;
	}

	/**
	 * Applies a criterion.
	 * @param value Value to add.
	 * @param type Filter type.
	 * @return The filter builder for method chaining.
	 * @throws NullPointerException if any of the arguments is {@code null}.
	 */
	public final Builder apply(T value, FilterType type) {
		checkNotNull(value, "The value to filter must be provided");
		checkNotNull(type, "The filter type must be provided");
		if (!isEmpty()) {
			if (criteria == null) {
				criteria = Maps.newHashMap();
				criteria.put(value, type);
			} else {
				final FilterType currentType = this.criteria.get(value);
				// New key.
				if (currentType == null) {
					criteria.put(value, type);
				} else {
					// Intersect key
					final FilterType newType = currentType.join(type);
					if (newType == null) {
						this.criteria = ImmutableMap.of(); // empty filter
					} else if (newType != currentType) {
						// Update value
						criteria.put(value, type);
					}
				}
			}
		}
		return builder;
	}

	/**
	 * Applies some criteria.
	 * @param type Filter type.
	 * @param values Values to add. If empty, no operation if performed.
	 * @return The filter builder for method chaining.
	 * @throws NullPointerException if the type or any of the values is {@code null}.
	 */
	public final Builder apply(FilterType type, Iterable<? extends T> values) {
		checkNotNull(type, "The filter type must be provided");
		checkNotNull(values, "The filter values to apply must be provided");
		for (T value : values) {
			if (isEmpty()) {
				break;
			}
			apply(value, type);
		}
		return builder;
	}

	/**
	 * Applies some criteria.
	 * @param type Filter type.
	 * @param values Values to add. If empty, no operation if performed.
	 * @return The filter builder for method chaining.
	 * @throws NullPointerException if the type or any of the values is {@code null}.
	 */
	public final Builder apply(FilterType type, T... values) {
		checkNotNull(values, "The filter values to apply must be provided");
		return apply(type, Arrays.asList(values));
	}

	/**
	 * Applies a REQUIRED criterion.
	 * @param value Value to add.
	 * @return The filter builder for method chaining.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public final Builder apply(T value) {
		return apply(value, FilterType.REQUIRED);
	}

	/**
	 * Applies other criteria.
	 * @param criteria Criteria to add.
	 * @return The filter builder for method chaining.
	 */
	public final Builder apply(FilterCriteria<? extends T> criteria) {
		if (criteria != null && !criteria.isNull()) {
			if (criteria.isEmpty()) {
				this.criteria = ImmutableMap.of();
			} else {
				apply(criteria.criteria);
			}
		}
		return builder;
	}
	
	@Override
	public String toString() {
		return toStringHelper(this).addValue(criteria).toString();
	}
}
