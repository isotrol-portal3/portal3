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

import java.util.Locale;
import java.util.Map;

import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.nr.api.FilterType;
import com.isotrol.impe3.nr.api.NRBooleanClause.Occur;
import com.isotrol.impe3.nr.api.NRSortField;
import com.isotrol.impe3.nr.api.NodeQuery;


/**
 * Content criteria base class.
 * @author Andres Rodriguez
 */
public abstract class ContentCriteria implements Cloneable {
	/** Default constructor. */
	protected ContentCriteria() {
	}

	/**
	 * Clones the current criteria.
	 * @return A clone of the current criteria.
	 */
	public abstract ContentCriteria clone();

	/**
	 * Whether to use request locale.
	 * @return True if the locale filter must be the request locale.
	 */
	public abstract boolean isRequestLocale();

	/**
	 * Sets whether to use request locale.
	 * @param requestLocale True if the locale filter must be the request locale.
	 * @return This criteria for method chaining.
	 */
	public abstract ContentCriteria setRequestLocale(boolean requestLocale);

	public abstract boolean isDue();

	public abstract ContentCriteria setDue(boolean due);

	/**
	 * Sets the criterion for uncategorized contents.
	 */
	public abstract ContentCriteria setUncategorized(FilterType type);

	/**
	 * Whether to fetch the bytes.
	 * @return True if the query must fetch the content bytes.
	 */
	public abstract boolean isBytes();

	/**
	 * Sets whether to fetch the bytes.
	 * @param bytes True if the query must fetch the content bytes.
	 * @return This criteria for method chaining.
	 */
	public abstract ContentCriteria setBytes(boolean bytes);

	/**
	 * Returns a content by content key. The query and highlighting are ignored.
	 * @param key Content key.
	 * @return The loaded content or {@code null} if not found.
	 * @throws NullPointerException if the key is {@code null}.
	 */
	public abstract Content getContent(ContentKey key);

	/**
	 * Returns a collection of contents by content key. The query and highlighting are ignored.
	 * @param keys Content keys to obtain.
	 * @return The loaded contents or an empty map if none it's found.
	 * @throws NullPointerException if the key is {@code null}.
	 */
	public Map<ContentKey, Content> getContents(Iterable<? extends ContentKey> keys) {
		checkNotNull(keys, "The content keys MUST be provided");
		final Map<ContentKey, Content> map = Maps.newHashMap();
		for (ContentKey key : keys) {
			final Content c = getContent(key);
			if (c != null) {
				map.put(key, c);
			}
		}
		return map;

	}

	/**
	 * Returns the first element that matches the query.
	 * @return The first result.
	 */
	public abstract Item<Content> getFirst();

	/**
	 * Returns a page of results.
	 * @param pagination Pagination.
	 * @return A page of results, if found.
	 */
	public abstract Page<Content> getPage(Pagination pagination);

	/**
	 * Returns a page of results.
	 * @param firstRecord First record.
	 * @param maxResults Manimum number of results.
	 * @return A page of results, if found.
	 */
	public abstract Page<Content> getPage(int firstRecord, int maxResults);
	
	/**
	 * Counts the number of results of the query.
	 * @return The number of hits of the query.
	 */
	public abstract Result count();

	/**
	 * Performs a grouped query.
	 * @param fields Fields to group by.
	 * @return The result of the query.
	 */
	public abstract GroupResult groupBy(Iterable<String> fields);

	/**
	 * Performs a grouped query by a single field.
	 * @param field Field to group by.
	 * @return The result of the query.
	 */
	public abstract OneLevelGroupResult<String> groupBy(String field);

	/**
	 * Performs a grouped query by a single field.
	 * @param field Field to group by.
	 * @param transformer Transformation functions.
	 * @return The result of the query.
	 */
	public abstract <T> OneLevelGroupResult<T> groupBy(String field, Function<String, ? extends T> transformer);

	/**
	 * Performs a grouped-by-content-type query.
	 * @return The result of the query.
	 */
	public abstract OneLevelGroupResult<ContentType> groupByContentType();

	/**
	 * Performs a grouped-by-category query.
	 * @return The result of the query.
	 */
	public abstract OneLevelGroupResult<Category> groupByCategory();

	/**
	 * Adds a query to the current criteria.
	 * @param query Query.
	 * @param occurr Occurrence.
	 * @return This criteria for method chaining.
	 */
	public abstract ContentCriteria addQuery(NodeQuery query, Occur occurr);

	/**
	 * Adds a query to the current criteria with MUST occurrence.
	 * @param query Query.
	 * @return This criteria for method chaining.
	 */
	public final ContentCriteria must(NodeQuery query) {
		return addQuery(query, Occur.MUST);
	}

	/**
	 * Adds a query to the current criteria with SHOULD occurrence.
	 * @param query Query.
	 * @return This criteria for method chaining.
	 */
	public final ContentCriteria should(NodeQuery query) {
		return addQuery(query, Occur.SHOULD);
	}

	/**
	 * Adds a query to the current criteria with MUST NOT occurrence.
	 * @param query Query.
	 * @return This criteria for method chaining.
	 */
	public final ContentCriteria mustNot(NodeQuery query) {
		return addQuery(query, Occur.MUST_NOT);
	}

	/**
	 * Adds a highlighting specification.
	 * @param field Field name.
	 * @param fragments Number of fragment.
	 * @return This criteria for method chaining.
	 */
	public abstract ContentCriteria highlight(String field, int fragments);

	/**
	 * Adds a set of highlighting specifications.
	 * @param specs highlighting specifications.
	 * @return This criteria for method chaining.
	 */
	public abstract ContentCriteria highlight(Map<String, Integer> specs);

	/**
	 * Adds a sort specification.
	 * @param field Sort field specification.
	 * @return This criteria for method chaining.
	 */
	public abstract ContentCriteria sort(NRSortField field);

	/**
	 * Adds a set of sort specifications.
	 * @param fields Sort field specifications.
	 * @return This criteria for method chaining.
	 */
	public ContentCriteria sort(NRSortField... fields) {
		if (fields != null) {
			for (NRSortField field : fields) {
				sort(field);
			}
		}
		return this;
	}

	/**
	 * Adds a set of sort specifications.
	 * @param fields Sort field specifications.
	 * @return This criteria for method chaining.
	 */
	public ContentCriteria sort(Iterable<NRSortField> fields) {
		if (fields != null) {
			for (NRSortField field : fields) {
				sort(field);
			}
		}
		return this;
	}

	/**
	 * Adds an normal ordering sort specification.
	 * @param field Field name.
	 * @return This criteria for method chaining.
	 */
	public ContentCriteria sort(String field) {
		if (field != null) {
			sort(NRSortField.of(field));
		}
		return this;
	}

	/**
	 * Adds an reverse ordering sort specification.
	 * @param field Field name.
	 * @return This criteria for method chaining.
	 */
	public ContentCriteria reverse(String field) {
		if (field != null) {
			sort(NRSortField.of(field, true));
		}
		return this;
	}

	/** Returns the sets filter. */
	public abstract SetFilter sets();

	/** Returns the categories filter. */
	public abstract CategoryFilter categories();
	
	/** Returns the content types filter. */
	public abstract ContentTypeFilter contentTypes();

	/** Returns the locales filter. */
	public abstract LocaleFilter locales();

	/** Returns the tags filter. */
	public abstract TagFilter tags();
	
	/** Returns the content keys filter. */
	public abstract KeyFilter keys();
	
	/**
	 * Adds a (REQUIRED) navigation key filter.
	 * @param key Navigation key to filter. If {@null} the method call is ignored.
	 * @return This criteria for method chaining.
	 */
	public ContentCriteria navigation(NavigationKey key) {
		if (key != null) {
			categories().apply(key.getCategory(), FilterType.REQUIRED);
			contentTypes().apply(key.getContentType(), FilterType.REQUIRED);
			tags().apply(key.getTag(), FilterType.REQUIRED);
		}
		return this;
	}
	
	/** Content filter. */
	public abstract class ContentFilter<T> {
		ContentFilter() {
		}
		
		/**
		 * Applies a filter.
		 * @param value Value to filter.
		 * @param type Filter type.
		 * @return The criteria for method chaining.
		 */
		public final ContentCriteria apply(T value, FilterType type) {
			if (value != null && type != null) {
				doApply(value, type);
			}
			return ContentCriteria.this;
		}
		
		protected abstract void doApply(T value, FilterType type);

		/**
		 * Applies some filters.
		 * @param type Filter type.
		 * @param values Values to filter.
		 * @return The criteria for method chaining.
		 */
		public final ContentCriteria apply(FilterType type, Iterable<? extends T> values) {
			if (values != null) {
				for (T set : values) {
					apply(set, type);
				}
			}
			return ContentCriteria.this;
		}

		/**
		 * Applies some filters.
		 * @param type Filter type.
		 * @param values Values to filter.
		 * @return The criteria for method chaining.
		 */
		public final ContentCriteria sets(FilterType type, T... values) {
			if (values != null && values.length > 0) {
				for (T set : values) {
					apply(set, type);
				}
			}
			return ContentCriteria.this;
		}
	}
	
	/** Set filter. */
	public abstract class SetFilter extends ContentFilter<String> {
		protected SetFilter() {
		}
	}

	/** Category filter. */
	public abstract class CategoryFilter extends ContentFilter<Category> {
		protected CategoryFilter() {
		}
	}

	/** Content type filter. */
	public abstract class ContentTypeFilter extends ContentFilter<ContentType> {
		protected ContentTypeFilter() {
		}
	}

	/** Locale filter. */
	public abstract class LocaleFilter extends ContentFilter<Locale> {
		protected LocaleFilter() {
		}
	}

	/** Tags filter. */
	public abstract class TagFilter extends ContentFilter<String> {
		protected TagFilter() {
		}
	}

	/** Content key filter. */
	public abstract class KeyFilter extends ContentFilter<ContentKey> {
		protected KeyFilter() {
		}
	}
	
}
