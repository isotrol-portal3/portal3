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

package com.isotrol.impe3.core.content;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.transform;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentQueries;
import com.isotrol.impe3.api.content.OneLevelGroupResult;
import com.isotrol.impe3.nr.api.FilterType;
import com.isotrol.impe3.nr.api.NRBooleanClause;
import com.isotrol.impe3.nr.api.NRBooleanClause.Occur;
import com.isotrol.impe3.nr.api.NRBooleanQuery;
import com.isotrol.impe3.nr.api.NRSortField;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeFilter.Builder;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.NodeSort;
import com.isotrol.impe3.nr.api.Schema;

/**
 * Content criteria implementation.
 * @author Andres Rodriguez
 */
final class ContentCriteriaImpl extends ContentCriteria {
	/** Portal. */
	private final Portal portal;
	/** Non-null if the query if content-type specific. */
	private final ContentType contentType;
	/** Node repositories. */
	private final Function<UUID, NodeRepository> repositories;
	/** Node to content function. */
	private final Function<Node, Content> node2content;
	/** Request locale. */
	private final Locale locale;
	/** Filter builder. */
	private final Builder filterBuilder;
	/** Current query. */
	private List<NRBooleanClause> queries;
	/** Whether to fetch the bytes. */
	private boolean bytes = false;
	/** Whether to use request locale. */
	private boolean requestLocale = true;
	/** Sort specification. */
	private List<NRSortField> sort = null;
	/** Hihglight specification. */
	private Map<String, Integer> highlight = null;
	/** Sets filter. */
	private final SetFilter sets = new SetFilterImpl();
	/** Categories filter. */
	private final CategoryFilter categories = new CategoryFilterImpl();
	/** Content types filter. */
	private final ContentTypeFilter contentTypes = new ContentTypeFilterImpl();
	/** Locales filter. */
	private final LocaleFilter locales = new LocaleFilterImpl();
	/** Tags filter. */
	private final TagFilter tags = new TagFilterImpl();
	/** Keys filter. */
	private final KeyFilter keys = new KeyFilterImpl();

	private static <T> List<T> copy(List<T> list) {
		if (list == null) {
			return null;
		}
		return Lists.newArrayList(list);
	}

	private static <K, V> Map<K, V> copy(Map<K, V> map) {
		if (map == null) {
			return null;
		}
		return Maps.newHashMap(map);
	}

	/**
	 * Constructor.
	 * @param repositories Node repositories.
	 * @param node2content Node to content function.
	 * @param filter Base filter.
	 */
	ContentCriteriaImpl(Portal portal, ContentType contentType, Function<UUID, NodeRepository> repositories,
			Function<Node, Content> node2content, Locale locale) {
		this.portal = checkNotNull(portal);
		this.contentType = contentType;
		this.repositories = checkNotNull(repositories);
		this.node2content = checkNotNull(node2content);
		this.locale = locale;
		this.filterBuilder = NodeFilter.builder().due(portal.isDue());
		// TODO
		// if (portal.isUncategorized()) {
		// this.filterBuilder.uncategorized(FilterType.OPTIONAL);
		// } else {
		// this.filterBuilder.uncategorized(FilterType.FORBIDDEN);
		// }
		if (!portal.getSetFilters().isEmpty()) {
			this.filterBuilder.sets().apply(portal.getSetFilters());
		}
	}

	/**
	 * Copy constructor.
	 * @param query Base query.
	 */
	private ContentCriteriaImpl(ContentCriteriaImpl query) {
		this.portal = query.portal;
		this.contentType = query.contentType;
		this.repositories = query.repositories;
		this.node2content = query.node2content;
		this.locale = query.locale;
		if (query.queries == null) {
			this.queries = null;
		} else {
			this.queries = Lists.newLinkedList(query.queries);
		}
		this.bytes = query.bytes;
		this.requestLocale = query.requestLocale;
		this.sort = copy(query.sort);
		this.highlight = copy(query.highlight);
		this.filterBuilder = query.filterBuilder.clone();
	}

	/**
	 * Returns the node repository to use.
	 * @return The node repository to use.
	 */
	private NodeRepository nr() {
		// final UUID id;
		// TODO
		return repositories.apply(null);
	}

	/**
	 * Returns the node filter to use.
	 * @return The node filter to use.
	 */
	private NodeFilter filter() {
		// TODO: parametrize request locale
		final NodeFilter filter;
		if (requestLocale && locale != null) {
			filter = filterBuilder.clone().locales().apply(locale).build();
		} else {
			filter = filterBuilder.build();
		}
		return filter;
	}

	/**
	 * Returns the sort specification to use.
	 * @return The sort specification to use.
	 */
	private NodeSort sort() {
		return sort == null ? null : NodeSort.of(sort);
	}

	/**
	 * Returns the query to use.
	 * @return The query to use.
	 */
	private NodeQuery query() {
		if (queries == null || queries.isEmpty()) {
			return NodeQueries.matchAll();
		}
		if (queries.size() == 1) {
			final NRBooleanClause c = queries.get(0);
			if (c.getOccur() == Occur.MUST_NOT) {
				return NodeQueries.bool().must(NodeQueries.matchAll()).add(c);
			} else {
				return c.getQuery();
			}
		} else {
			final NRBooleanQuery q = NodeQueries.bool();
			for (NRBooleanClause c : queries) {
				q.add(c);
			}
			return q;
		}
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#clone()
	 */
	@Override
	public ContentCriteria clone() {
		return new ContentCriteriaImpl(this);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#isBytes()
	 */
	@Override
	public boolean isBytes() {
		return bytes;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#setBytes(boolean)
	 */
	@Override
	public ContentCriteria setBytes(boolean bytes) {
		this.bytes = bytes;
		return this;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#isRequestLocale()
	 */
	@Override
	public boolean isRequestLocale() {
		return requestLocale;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#setRequestLocale(boolean)
	 */
	@Override
	public ContentCriteria setRequestLocale(boolean requestLocale) {
		if (requestLocale) {
			filterBuilder.locales().apply(locale);
			this.requestLocale = true;
		}
		return this;
	}

	@Override
	public boolean isDue() {
		return filterBuilder.isDue();
	}

	@Override
	public ContentCriteria setDue(boolean due) {
		filterBuilder.due(due);
		return this;
	}

	@Override
	public ContentCriteria setUncategorized(FilterType uncategorized) {
		filterBuilder.uncategorized(uncategorized);
		return this;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#getContent(com.isotrol.impe3.api.ContentKey)
	 */
	public Content getContent(ContentKey key) {
		checkNotNull(key, "The content key MUST be provided");
		// TODO
		final Item<Node> item = nr().getFirst(ContentQueries.contentKey(key), filter(), null, bytes, null);
		if (item == null) {
			return null;
		}
		final Node node = item.getItem();
		if (node == null) {
			return null;
		}
		return node2content.apply(node);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#getFirst()
	 */
	public Item<Content> getFirst() {
		final Item<Node> item = nr().getFirst(query(), filter(), sort(), bytes, highlight);
		if (item == null) {
			return null;
		}
		final Node node = item.getItem();
		final Content c = node == null ? null : node2content.apply(node);
		return new Item<Content>(item.getTotalHits(), item.getMaxScore(), item.getTime(), c);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#getPage(com.isotrol.impe3.api.Pagination)
	 */
	public Page<Content> getPage(Pagination pagination) {
		final int first;
		final int size;
		if (pagination == null) {
			first = 0;
			size = Pagination.SIZE;
		} else {
			first = pagination.getFirstRecord();
			Integer s = pagination.getSize();
			size = s != null ? s.intValue() : Pagination.SIZE;
		}
		final Page<Node> page = nr().getPage(query(), filter(), sort(), bytes, first, size, highlight);
		if (page == null) {
			return null;
		}
		final List<Content> results;
		final List<Node> nodes = page.getItems();
		if (nodes == null) {
			results = null;
		} else if (nodes.isEmpty()) {
			results = ImmutableList.of();
		} else {
			results = ImmutableList.<Content> copyOf(transform(nodes, node2content));
		}
		return new Page<Content>(page.getTotalHits(), page.getMaxScore(), page.getTime(), page.getFirstResult(), results);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#getPage(int, int)
	 */
	public Page<Content> getPage(int firstRecord, int maxResults) {
		final int first = Math.max(0, firstRecord);
		final int size = Math.max(1, maxResults);
		final Page<Node> page = nr().getPage(query(), filter(), sort(), bytes, first, size, highlight);
		if (page == null) {
			return null;
		}
		final List<Content> results;
		final List<Node> nodes = page.getItems();
		if (nodes == null) {
			results = null;
		} else if (nodes.isEmpty()) {
			results = ImmutableList.of();
		} else {
			results = ImmutableList.<Content> copyOf(transform(nodes, node2content));
		}
		return new Page<Content>(page.getTotalHits(), page.getMaxScore(), page.getTime(), page.getFirstResult(), results);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#count()
	 */
	@Override
	public Result count() {
		return nr().count(query(), filter());
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#groupBy(java.lang.Iterable)
	 */
	@Override
	public GroupResult groupBy(Iterable<String> fields) {
		final List<String> gbf;
		if (fields == null) {
			gbf = Lists.newArrayListWithCapacity(0);
		} else {
			gbf = Lists.newArrayList(fields);
		}
		return nr().groupBy(query(), filter(), gbf);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#groupBy(java.lang.String)
	 */
	@Override
	public OneLevelGroupResult<String> groupBy(String field) {
		checkNotNull("The field to group by must be provided");
		return OneLevelGroupResult.of(groupBy(ImmutableList.of(field)));
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#groupBy(java.lang.String,
	 *      com.google.common.base.Function)
	 */
	@Override
	public <T> OneLevelGroupResult<T> groupBy(String field, Function<String, ? extends T> transformer) {
		checkNotNull("The field to group by must be provided");
		return OneLevelGroupResult.of(groupBy(ImmutableList.of(field)), transformer);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#groupByContentType()
	 */
	@Override
	public OneLevelGroupResult<ContentType> groupByContentType() {
		final Function<String, ContentType> f = new Function<String, ContentType>() {
			public ContentType apply(String input) {
				if (input == null) {
					return null;
				}
				final UUID uuid;
				try {
					uuid = UUID.fromString(input);
				} catch (IllegalArgumentException e) {
					return null;
				}
				if (uuid == null) {
					return null;
				}
				return portal.getContentTypes().get(uuid);
			}
		};
		return groupBy(Schema.TYPE, f);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#groupByCategory()
	 */
	@Override
	public OneLevelGroupResult<Category> groupByCategory() {
		final Function<String, Category> f = new Function<String, Category>() {
			public Category apply(String input) {
				if (input == null) {
					return null;
				}
				final UUID uuid;
				try {
					uuid = UUID.fromString(input);
				} catch (IllegalArgumentException e) {
					return null;
				}
				if (uuid == null) {
					return null;
				}
				return portal.getCategories().get(uuid);
			}
		};
		return groupBy(Schema.CATEGORY, f);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#addQuery(com.isotrol.impe3.nr.api.NodeQuery,
	 *      com.isotrol.impe3.nr.api.NRBooleanClause.Occur)
	 */
	@Override
	public ContentCriteria addQuery(NodeQuery query, Occur occurr) {
		if (query == null) {
			return this;
		}
		checkNotNull(occurr, "The occurrence must be provided");
		if (queries == null) {
			queries = Lists.newLinkedList();
		}
		queries.add(NRBooleanClause.create(query, occurr));
		return this;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#highlight(java.lang.String, int)
	 */
	@Override
	public ContentCriteria highlight(String field, int fragments) {
		if (field != null) {
			if (highlight == null) {
				highlight = Maps.newHashMap();
			}
			highlight.put(field, fragments);
		}
		return this;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#highlight(java.util.Map)
	 */
	@Override
	public ContentCriteria highlight(Map<String, Integer> specs) {
		if (specs != null && !specs.isEmpty()) {
			if (highlight == null) {
				highlight = Maps.newHashMap();
			}
			highlight.putAll(specs);
		}
		return this;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#sort(com.isotrol.impe3.nr.api.NRSortField)
	 */
	@Override
	public ContentCriteria sort(NRSortField field) {
		if (field != null) {
			if (sort == null) {
				sort = Lists.newArrayList();
			}
			sort.add(field);
		}
		return this;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#sets()
	 */
	@Override
	public SetFilter sets() {
		return sets;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#categories()
	 */
	@Override
	public CategoryFilter categories() {
		return categories;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#contentTypes()
	 */
	@Override
	public ContentTypeFilter contentTypes() {
		return contentTypes;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#locales()
	 */
	@Override
	public LocaleFilter locales() {
		return locales;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#tags()
	 */
	@Override
	public TagFilter tags() {
		return tags;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentCriteria#keys()
	 */
	@Override
	public KeyFilter keys() {
		return keys;
	}

	/** Set filter. */
	private final class SetFilterImpl extends SetFilter {
		SetFilterImpl() {
		}

		@Override
		protected void doApply(String value, FilterType type) {
			filterBuilder.sets().apply(value, type);
		}

		@Override
		public String toString() {
			return toStringHelper(getClass().getSuperclass()).addValue(filterBuilder.sets().toString()).toString();
		}
	}

	/** Category filter. */
	private final class CategoryFilterImpl extends CategoryFilter {
		CategoryFilterImpl() {
		}

		@Override
		protected void doApply(Category value, FilterType type) {
			filterBuilder.categories().apply(value.getId(), type);
		}

		@Override
		public String toString() {
			return toStringHelper(getClass().getSuperclass()).addValue(filterBuilder.categories().toString()).toString();
		}
	}

	/** Content type filter. */
	private final class ContentTypeFilterImpl extends ContentTypeFilter {
		ContentTypeFilterImpl() {
		}

		@Override
		protected void doApply(ContentType value, FilterType type) {
			filterBuilder.contentTypes().apply(value.getId(), type);
		}

		@Override
		public String toString() {
			return toStringHelper(getClass().getSuperclass()).addValue(filterBuilder.contentTypes().toString()).toString();
		}
	}

	/** Locale filter. */
	private final class LocaleFilterImpl extends LocaleFilter {
		LocaleFilterImpl() {
		}

		@Override
		protected void doApply(Locale value, FilterType type) {
			filterBuilder.locales().apply(value, type);
		}

		@Override
		public String toString() {
			return toStringHelper(getClass().getSuperclass()).addValue(filterBuilder.locales().toString()).toString();
		}
	}

	/** Tags filter. */
	private final class TagFilterImpl extends TagFilter {
		TagFilterImpl() {
		}

		@Override
		protected void doApply(String value, FilterType type) {
			filterBuilder.tags().apply(value, type);
		}

		@Override
		public String toString() {
			return toStringHelper(getClass().getSuperclass()).addValue(filterBuilder.tags().toString()).toString();
		}
	}

	/** Content key filter. */
	private final class KeyFilterImpl extends KeyFilter {
		KeyFilterImpl() {
		}

		@Override
		protected void doApply(ContentKey value, FilterType type) {
			filterBuilder.keys().apply(value.getNodeKey(), type);
		}

		@Override
		public String toString() {
			return toStringHelper(getClass().getSuperclass()).addValue(filterBuilder.keys().toString()).toString();
		}
	}

}
