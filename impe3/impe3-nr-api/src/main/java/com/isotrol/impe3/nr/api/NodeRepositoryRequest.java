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


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.derquinse.lucis.Result;

import com.google.common.base.Objects;


/**
 * Object representing a node repository request.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public final class NodeRepositoryRequest implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -7169316009487220048L;
	private static final int FIRST = 0;
	private static final int PAGE = 1;
	private static final int GROUP = 2;
	private static final int COUNT = 3;

	/** Request type. */
	private final int type;
	/** Node Query. */
	private final NodeQuery query;
	/** Node filter. */
	private final NodeFilter filter;
	/** Node sort. */
	private final NodeSort sort;
	/** Bytes. */
	private final boolean bytes;
	/** First result. */
	private final int firstResult;
	/** Page size. */
	private final int pageSize;
	/** Highlight. */
	private final Map<String, Integer> highlight;
	/** Group fields. */
	private final List<String> fields;
	/** Hash calculated. */
	private transient volatile boolean hashCalculated = false;
	/** Hash value. */
	private transient int hash;

	public static NodeRepositoryRequest first(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		Map<String, Integer> highlight) {
		return new NodeRepositoryRequest(FIRST, query, filter, sort, bytes, -1, -1, highlight, null);
	}

	public static NodeRepositoryRequest page(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		int firstResult, int pageSize, Map<String, Integer> highlight) {
		return new NodeRepositoryRequest(PAGE, query, filter, sort, bytes, firstResult, pageSize, highlight, null);
	}

	public static NodeRepositoryRequest group(NodeQuery query, NodeFilter filter, List<String> fields) {
		return new NodeRepositoryRequest(GROUP, query, filter, null, false, -1, -1, null, fields);

	}

	public static NodeRepositoryRequest count(NodeQuery query, NodeFilter filter) {
		return new NodeRepositoryRequest(COUNT, query, filter, null, false, -1, -1, null, null);
	}

	private NodeRepositoryRequest(int type, NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		int firstResult, int pageSize, Map<String, Integer> highlight, List<String> fields) {
		this.type = type;
		this.query = query;
		this.filter = filter;
		this.sort = sort;
		this.bytes = bytes;
		this.firstResult = firstResult;
		this.pageSize = pageSize;
		this.highlight = highlight;
		this.fields = fields;

		this.hash = Objects.hashCode(query, filter, sort, bytes, firstResult, pageSize, highlight);
	}

	@Override
	public int hashCode() {
		if (!hashCalculated) {
			synchronized (this) {
				if (!hashCalculated) {
					hash = Objects.hashCode(type, query, filter, sort, bytes, firstResult, pageSize, highlight, fields);
					hashCalculated = true;
				}
			}
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof NodeRepositoryRequest) {
			final NodeRepositoryRequest r = (NodeRepositoryRequest) obj;
			return type == r.type && bytes == r.bytes && firstResult == r.firstResult && pageSize == r.pageSize
				&& hashCode() == r.hashCode() && equal(query, r.query) && equal(filter, r.filter)
				&& equal(sort, r.sort) && equal(highlight, r.highlight) && equal(fields, r.fields);
		}
		return false;
	}

	/**
	 * Execute the request in a node repository.
	 * @param nr Node repository.
	 * @return The request result.
	 */
	public Result execute(NodeRepository nr) {
		checkNotNull(nr, "Node repository not provided");
		switch (type) {
			case FIRST:
				return nr.getFirst(query, filter, sort, bytes, highlight);
			case PAGE:
				return nr.getPage(query, filter, sort, bytes, firstResult, pageSize, highlight);
			case GROUP:
				return nr.groupBy(query, filter, fields);
			case COUNT:
				return nr.count(query, filter);
			default:
				throw new AssertionError("Invalid node repository request type.");
		}
	}
}
