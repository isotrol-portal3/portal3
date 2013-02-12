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

package com.isotrol.impe3.api.content;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;

import java.util.Arrays;

import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.nr.api.NRBooleanClause.Occur;
import com.isotrol.impe3.nr.api.NRSortField;
import com.isotrol.impe3.nr.api.NodeQuery;


/**
 * Content criteria transformer.
 * @author Andres Rodriguez
 */
public abstract class ContentCriteriaTransformer {
	/** Identity transformation. */
	private static final ContentCriteriaTransformer IDENTITY = new Identity();
	/** Reset transformation. */
	private static final ContentCriteriaTransformer RESET = new Reset();

	/** Default constructor. */
	protected ContentCriteriaTransformer() {
	}

	public static ContentCriteriaTransformer identity() {
		return IDENTITY;
	}

	public static ContentCriteriaTransformer reset() {
		return RESET;
	}

	/**
	 * Transformer to add a query to the current criteria.
	 * @param query Query.
	 * @param occurr Occurrence.
	 * @return The requested transformer.
	 */
	public static ContentCriteriaTransformer addQuery(NodeQuery query, Occur occurr) {
		return new AddQuery(query, occurr);
	}

	/**
	 * Transformer to add a query to the current criteria with MUST occurrence.
	 * @param query Query.
	 * @param occurr Occurrence.
	 * @return The requested transformer.
	 */
	public static ContentCriteriaTransformer must(NodeQuery query) {
		return addQuery(query, Occur.MUST);
	}

	/**
	 * Transformer to add a query to the current criteria with SHOULD occurrence.
	 * @param query Query.
	 * @param occurr Occurrence.
	 * @return The requested transformer.
	 */
	public static ContentCriteriaTransformer should(NodeQuery query) {
		return addQuery(query, Occur.SHOULD);
	}

	/**
	 * Transformer to add a query to the current criteria with MUST NOT occurrence.
	 * @param query Query.
	 * @return The requested transformer.
	 */
	public static ContentCriteriaTransformer mustNot(NodeQuery query) {
		return addQuery(query, Occur.MUST_NOT);
	}

	/**
	 * Transformer to adds a sort specification.
	 * @param field Sort field specification.
	 * @return The requested transformer.
	 */
	public static ContentCriteriaTransformer sort(NRSortField field) {
		if (field == null) {
			return identity();
		} else {
			return new AddSort(ImmutableList.of(field));
		}
	}

	/**
	 * Transformer to adds a set of sort specifications.
	 * @param fields Sort field specifications.
	 * @return The requested transformer.
	 */
	public static ContentCriteriaTransformer sort(NRSortField... fields) {
		if (fields == null) {
			return identity();
		}
		return sort(Arrays.asList(fields));
	}

	/**
	 * Transformer to adds a set of sort specifications.
	 * @param fields Sort field specifications.
	 * @return The requested transformer.
	 */
	public static ContentCriteriaTransformer sort(Iterable<NRSortField> fields) {
		if (fields == null) {
			return identity();
		}
		final ImmutableList<NRSortField> list = ImmutableList.copyOf(filter(fields, notNull()));
		if (list.isEmpty()) {
			return identity();
		}
		return new AddSort(list);
	}

	/**
	 * Transformer to require a provided navigation.
	 * @param key Navigation key.
	 * @return The requested transformer.
	 */
	public static ContentCriteriaTransformer navigation(NavigationKey key) {
		if (key == null) {
			return identity();
		}
		return new RequiredNavigation(key);
	}

	/**
	 * Applies the transformation.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public abstract void apply(ContentCriteria criteria);

	/**
	 * Appends a new transformer.
	 * @param t The transformer to add.
	 * @return The composed transformer.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public final ContentCriteriaTransformer append(ContentCriteriaTransformer t) {
		checkNotNull(t);
		if (this == IDENTITY || this == RESET) {
			return t;
		}
		if (t == IDENTITY) {
			return this;
		}
		if (t == RESET) {
			return IDENTITY;
		}
		return new Composition(this, t);
	}

	/**
	 * Identity transformation.
	 */
	private static final class Identity extends ContentCriteriaTransformer {
		Identity() {
		}

		@Override
		public void apply(ContentCriteria criteria) {
		}

		@Override
		public String toString() {
			return "Identity transformer";
		}
	}

	/**
	 * Reset transformation.
	 */
	private static final class Reset extends ContentCriteriaTransformer {
		Reset() {
		}

		@Override
		public void apply(ContentCriteria criteria) {
		}

		@Override
		public String toString() {
			return "Reset transformer";
		}
	}

	/**
	 * Composed transformation.
	 */
	private static final class Composition extends ContentCriteriaTransformer {
		private final ContentCriteriaTransformer a;
		private final ContentCriteriaTransformer b;

		Composition(ContentCriteriaTransformer a, ContentCriteriaTransformer b) {
			this.a = checkNotNull(a);
			this.b = checkNotNull(b);
		}

		@Override
		public void apply(ContentCriteria criteria) {
			a.apply(criteria);
			b.apply(criteria);
		}

		@Override
		public String toString() {
			return String.format("Composition [%s]+[%s]", a, b);
		}
	}

	/**
	 * Add query transformation.
	 */
	private static final class AddQuery extends ContentCriteriaTransformer {
		private final NodeQuery query;
		private final Occur occurr;

		AddQuery(NodeQuery query, Occur occurr) {
			this.query = checkNotNull(query);
			this.occurr = checkNotNull(occurr);
		}

		@Override
		public void apply(ContentCriteria criteria) {
			criteria.addQuery(query, occurr);
		}

		@Override
		public String toString() {
			return String.format("Query %s [%s]", occurr, query);
		}
	}

	/**
	 * Add sort transformation.
	 */
	private static final class AddSort extends ContentCriteriaTransformer {
		private final ImmutableList<NRSortField> sort;

		AddSort(ImmutableList<NRSortField> sort) {
			this.sort = checkNotNull(sort);
		}

		@Override
		public void apply(ContentCriteria criteria) {
			criteria.sort(sort);
		}

		@Override
		public String toString() {
			return String.format("Sort %s", sort);
		}
	}

	/**
	 * Required navigation transformation.
	 */
	private static final class RequiredNavigation extends ContentCriteriaTransformer {
		private final NavigationKey key;

		RequiredNavigation(NavigationKey key) {
			this.key = checkNotNull(key);
		}

		@Override
		public void apply(ContentCriteria criteria) {
			criteria.navigation(key);
		}

		@Override
		public String toString() {
			return String.format("RequiredNavigation %s", key);
		}
	}

}
