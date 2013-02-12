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


import java.util.List;
import java.util.Map;

import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;

import com.google.common.collect.ForwardingObject;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.NodeSort;


/**
 * A forwarding node repository.
 * @author Andres Rodriguez
 */
public abstract class ForwardingNodeRepository extends ForwardingObject implements NodeRepository {

	/**
	 * Constructor
	 */
	public ForwardingNodeRepository() {
	}
	
	protected abstract NodeRepository delegate();

	public Item<Node> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		Map<String, Integer> highlight) {
		return delegate().getFirst(query, filter, sort, bytes, highlight);
	}

	public Page<Node> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult,
		int pageSize, Map<String, Integer> highlight) {
		return delegate().getPage(query, filter, sort, bytes, firstResult, pageSize, highlight);
	}
	
	public Result count(NodeQuery query, NodeFilter filter) {
		return delegate().count(query, filter);
	}
	
	public GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields) {
		return delegate().groupBy(query, filter, fields);
	}
	
	
}
