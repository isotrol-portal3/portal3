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


import java.util.List;
import java.util.Map;

import net.sf.derquinse.lucis.Group;
import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;


/**
 * A NodeRepository implementation with no nodes.
 * @author Andres Rodriguez
 */
public final class EmptyNodeRepository implements NodeRepository {
	/** Default instance. */
	private static final EmptyNodeRepository INSTANCE = new EmptyNodeRepository();

	/** Returns the default instance. */
	public static NodeRepository get() {
		return INSTANCE;
	}

	/**
	 * Constructor.
	 */
	public EmptyNodeRepository() {
	}

	public Page<Node> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult,
		int pageSize, Map<String, Integer> highlight) {
		return new Page<Node>(0, 0.0f, 0L, firstResult, null);
	}

	public Item<Node> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		Map<String, Integer> highlight) {
		return new Item<Node>(0, 0.0f, 0L, null);
	}

	public Result count(NodeQuery query, NodeFilter filter) {
		return new Result(0, 0.0f, 0L);
	}

	public GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields) {
		return new GroupResult(new Group(), 0.0f, 0L);
	}
}
