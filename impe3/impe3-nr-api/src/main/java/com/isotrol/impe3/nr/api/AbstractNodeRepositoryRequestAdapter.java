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


import static com.isotrol.impe3.nr.api.NodeRepositoryRequest.first;
import static com.isotrol.impe3.nr.api.NodeRepositoryRequest.group;
import static com.isotrol.impe3.nr.api.NodeRepositoryRequest.page;

import java.util.List;
import java.util.Map;

import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;



/**
 * Abstract NodeRepository to NodeRepositoryRequest adapter.
 * @author Andres Rodriguez
 */
public abstract class AbstractNodeRepositoryRequestAdapter implements NodeRepository {
	/**
	 * Default Constructor.
	 */
	public AbstractNodeRepositoryRequestAdapter() {
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#getFirst(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter, com.isotrol.impe3.nr.api.NodeSort, boolean, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public Item<Node> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		Map<String, Integer> highlight) {
		return (Item<Node>) execute(first(query, filter, sort, bytes, highlight));
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#getPage(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter, com.isotrol.impe3.nr.api.NodeSort, boolean, int, int, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public Page<Node> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult,
		int pageSize, Map<String, Integer> highlight) {
		return (Page<Node>) execute(page(query, filter, sort, bytes, firstResult, pageSize, highlight));
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#count(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter)
	 */
	public Result count(NodeQuery query, NodeFilter filter) {
		return execute(NodeRepositoryRequest.count(query, filter));
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#groupBy(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter, java.util.List)
	 */
	public GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields) {
		return (GroupResult) execute(group(query, filter, fields));
	}

	/**
	 * Execute the request.
	 * @param request Request to execute.
	 * @return The request result.
	 */
	protected abstract Result execute(NodeRepositoryRequest request);

}
