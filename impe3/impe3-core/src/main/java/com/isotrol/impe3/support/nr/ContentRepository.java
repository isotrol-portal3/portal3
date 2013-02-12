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

package com.isotrol.impe3.support.nr;


import static com.isotrol.impe3.nr.api.NodeQueries.all;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.nr.api.NRBooleanQuery;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.NodeSort;
import com.isotrol.impe3.nr.api.Schema;


/**
 * A content repository.
 * @author Andres Rodriguez
 */
public final class ContentRepository {
	private final NodeRepository repository;
	/** Known content types. */
	private final ContentTypes contentTypes;
	/** Known categories. */
	private final Categories categories;

	private final Function<Node, Content> node2content = new Function<Node, Content>() {
		public Content apply(Node from) {
			if (from == null) {
				return null;
			}
			return new ContentNode(contentTypes, categories, from);
		}
	};

	/**
	 * Constructs a new content repository.
	 * @param repository Node repository.
	 * @param contentTypes Known content types.
	 * @param categories Known categories.
	 */
	public ContentRepository(final NodeRepository repository, final ContentTypes contentTypes,
		final Categories categories) {
		Preconditions.checkNotNull(repository);
		Preconditions.checkNotNull(contentTypes);
		Preconditions.checkNotNull(categories);
		this.repository = repository;
		this.contentTypes = contentTypes;
		this.categories = categories;
	}

	public final NodeRepository getRepository() {
		return repository;
	}

	public final Content getContent(Node node) {
		return node2content.apply(node);
	}

	public final List<Content> getContents(Iterable<? extends Node> nodes) {
		return Lists.newArrayList(Iterables.transform(nodes, node2content));
	}

	/**
	 * Returns the first element that matches a query.
	 * @param query Query to perform.
	 * @param sort Sort specification.
	 * @param highlight collection of fields (and number of fragments) you want to recover highlighted
	 * @return The first result.
	 */
	public Item<Content> getFirst(NodeQuery query, NodeSort sort, boolean bytes, Map<String, Integer> highlight) {

		final NodeQuery queryfiltered = expirationReleasedFilter(query);

		final Item<Node> item = repository.getFirst(queryfiltered, null, sort, bytes, highlight);
		if (item == null) {
			return null;
		}
		return new Item<Content>(item.getTotalHits(), item.getMaxScore(), item.getTime(), node2content.apply(item
			.getItem()));
	}

	/**
	 * Returns the first element that matches a query.
	 * @param query Query to perform.
	 * @param sort Sort specification.
	 * @return The first result.
	 */
	public Item<Content> getFirst(NodeQuery query, NodeSort sort, boolean bytes) {
		return getFirst(query, sort, bytes, null);
	}

	/**
	 * Returns a content by content key.
	 * @param key Content key.
	 * @param locale Requested locale.
	 * @return The first result.
	 */
	public Item<Content> getContent(ContentKey key, Locale locale, boolean bytes) {
		Preconditions.checkNotNull(key);
		NodeQuery q = NodeQueries.nodeKey(key.getContentId(), key.getContentType().getId());
		if (locale != null && locale.getDisplayLanguage() != null) {
			q = NodeQueries.bool().must(q).must(Schema.LOCALE, locale.getDisplayLanguage());
		}
		return getFirst(q, null, bytes);
	}

	/**
	 * Returns a page of results.
	 * @param query Query to perform.
	 * @param sort Sort specification.
	 * @param firstResult First result in the page.
	 * @param pageSize Page size.
	 * @param highlight collection of fields (and number of fragments) you want to recover highlighted
	 * @return A page of results, if found.
	 */
	public Page<Content> getPage(NodeQuery query, NodeSort sort, boolean bytes, int firstResult, int pageSize,
		Map<String, Integer> highlight) {
		final NodeQuery queryfiltered = expirationReleasedFilter(query);

		final Page<Node> page = repository.getPage(queryfiltered, null, sort, bytes, firstResult, pageSize, highlight);
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
			results = ImmutableList.<Content> copyOf(Iterables.transform(nodes, node2content));
		}
		return new Page<Content>(page.getTotalHits(), page.getMaxScore(), page.getTime(), page.getFirstResult(),
			results);
	}

	/**
	 * Returns a page of results.
	 * @param query Query to perform.
	 * @param sort Sort specification.
	 * @param firstResult First result in the page.
	 * @param pageSize Page size.
	 * @return A page of results, if found.
	 */
	public Page<Content> getPage(NodeQuery query, NodeSort sort, boolean bytes, int firstResult, int pageSize) {
		return getPage(query, sort, bytes, firstResult, pageSize, null);
	}

	public NodeQuery category(Category category) {
		if (category == null) {
			return null;
		}
		return NodeQueries.term(Schema.CATEGORY, category.getId());
	}

	public NodeQuery contentType(ContentType contentType) {
		if (contentType == null) {
			return null;
		}
		return NodeQueries.term(Schema.TYPE, contentType.getId());
	}

	public NodeQuery tag(String tag) {
		if (tag == null) {
			return null;
		}
		return NodeQueries.term(Schema.TAG, tag);
	}

	public NodeQuery navigationKey(NavigationKey key) {
		if (key == null) {
			return null;
		}
		return all(category(key.getCategory()), tag(key.getTag()), contentType(key.getContentType()));
	}

	public NodeQuery contentKey(ContentKey key) {
		if (key == null) {
			return null;
		}
		final NRBooleanQuery q = NodeQueries.bool();
		q.must(contentType(key.getContentType()));
		q.must(NodeQueries.term(Schema.ID, key.getContentId()));
		return q;
	}

	private NodeQuery expirationReleasedFilter(NodeQuery query) {
		final NRBooleanQuery range = NodeQueries.bool();
		range.must(NodeQueries.range(Schema.RELEASEDATE, Schema.getMinDateString(), Schema.dateToString(new Date()),
			true, true));
		range.must(NodeQueries.range(Schema.EXPIRATIONDATE, Schema.dateToString(new Date()), Schema.getMaxDateString(),
			false, true));

		if (query == null) {
			return range;
		}

		range.must(query);

		return range;
	}
}
