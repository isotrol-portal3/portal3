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
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;


/**
 * A caching node repository decorator.
 * @author Andres Rodriguez
 */
public class CachingNodeRepository implements NodeRepository {
	/** Cached repository. */
	private final NodeRepository cached;
	/** Query cache. */
	private final LoadingCache<NodeRepositoryRequest, Result> cache;

	/**
	 * Constructor.
	 * @param repository Repository to cache.
	 * @param expiration Cache expiration.
	 */
	public CachingNodeRepository(NodeRepository repository, int expiration) {
		this.cached = checkNotNull(repository, "The repository to cache is mandatory");
		final int exp = Math.max(100, expiration);
		this.cache = CacheBuilder.newBuilder().concurrencyLevel(32).expireAfterWrite(exp, TimeUnit.MILLISECONDS)
			.softValues().build(new Loader());
	}

	private static RuntimeException ex(ExecutionException e) {
		final Throwable cause = e.getCause();
		if (cause instanceof RuntimeException) {
			return (RuntimeException) cause;
		} else {
			return new UncheckedExecutionException(cause);
		}
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#getFirst(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter, com.isotrol.impe3.nr.api.NodeSort, boolean, java.util.Map)
	 */
	public final Item<Node> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		Map<String, Integer> highlight) {
		try {
			@SuppressWarnings("unchecked")
			final Item<Node> result = (Item<Node>) cache.get(NodeRepositoryRequest.first(query, filter, sort, bytes,
				highlight));
			return result;
		} catch (ExecutionException e) {
			throw ex(e);
		}
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#getPage(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter, com.isotrol.impe3.nr.api.NodeSort, boolean, int, int, java.util.Map)
	 */
	public final Page<Node> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult,
		int pageSize, Map<String, Integer> highlight) {
		try {
			@SuppressWarnings("unchecked")
			final Page<Node> result = (Page<Node>) cache.get(NodeRepositoryRequest.page(query, filter, sort, bytes,
				firstResult, pageSize, highlight));
			return result;
		} catch (ExecutionException e) {
			throw ex(e);
		}
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#count(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter)
	 */
	public final Result count(NodeQuery query, NodeFilter filter) {
		try {
			final Result result = cache.get(NodeRepositoryRequest.count(query, filter));
			return result;
		} catch (ExecutionException e) {
			throw ex(e);
		}
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#groupBy(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter, java.util.List)
	 */
	public final GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields) {
		try {
			final GroupResult result = (GroupResult) cache.get(NodeRepositoryRequest.group(query, filter, fields));
			return result;
		} catch (ExecutionException e) {
			throw ex(e);
		}
	}

	/**
	 * Cache loader.
	 * @author Andres Rodriguez
	 */
	private final class Loader extends CacheLoader<NodeRepositoryRequest, Result> {
		public Result load(NodeRepositoryRequest input) {
			return input.execute(cached);
		}
	}
}
