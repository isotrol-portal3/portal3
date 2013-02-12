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

package com.isotrol.impe3.connectors.hessian;


import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;

import com.caucho.hessian.client.HessianProxyFactory;
import com.isotrol.impe3.nr.api.CachingNodeRepository;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.NodeSort;


/**
 * Remote hessian Node Repository service implementation
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class NodeRepositoryHessian implements NodeRepository {
	/** Remote repository (may be cached). */
	private final NodeRepository remote;

	/** Expiration calculation. */
	private static int expiration(int value) {
		return Math.min(Math.max(800, value), 80000);
	}

	/**
	 * Constructor.
	 * @param config Module configuration.
	 */
	public NodeRepositoryHessian(NRServiceConfig config) throws MalformedURLException {
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setHessian2Reply(true);
		factory.setHessian2Request(true);
		final NodeRepository nr = (NodeRepository) factory.create(NodeRepository.class, config.serviceUrl());
		if (config.microcache() != null) {
			this.remote = new CachingNodeRepository(nr, expiration(config.microcache().intValue()));
		} else {
			this.remote = nr;
		}
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#getFirst(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter, com.isotrol.impe3.nr.api.NodeSort, boolean, java.util.Map)
	 */
	public Item<Node> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		Map<String, Integer> highlight) {
		return remote.getFirst(query, filter, sort, bytes, highlight);
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#getPage(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter, com.isotrol.impe3.nr.api.NodeSort, boolean, int, int, java.util.Map)
	 */
	public Page<Node> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult,
		int pageSize, Map<String, Integer> highlight) {
		return remote.getPage(query, filter, sort, bytes, firstResult, pageSize, highlight);
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#count(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter)
	 */
	public Result count(NodeQuery query, NodeFilter filter) {
		return remote.count(query, filter);
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepository#groupBy(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter, java.util.List)
	 */
	public GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields) {
		return remote.groupBy(query, filter, fields);
	}
}
