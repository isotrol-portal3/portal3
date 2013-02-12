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

import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;


/**
 * Interface to the node repository.
 * @author Emilio Escobar Reyero
 * 
 */
public interface NodeRepository {

	/**
	 * Returns the first element that matches a query.
	 * @param query Query to perform.
	 * @param filter Filter to apply.
	 * @param sort Sort specification.
	 * @param bytes if true recover content bytes
	 * @param highlight fields and fragment numbers
	 * @return the first result
	 */
	Item<Node> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, Map<String, Integer> highlight);

	/**
	 * Returns a page of results.
	 * @param query Query to perform.
	 * @param filter Filter to apply.
	 * @param sort Sort specification.
	 * @param bytes if true gets the data content
	 * @param firstResult First result in the page.
	 * @param pageSize Page size.
	 * @param highlight fields and fragment numbers
	 * @return A page of results, if found.
	 */
	Page<Node> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult, int pageSize,
		Map<String, Integer> highlight);

	/**
	 * Counts the number of results of a query.
	 * @param query Query to perform.
	 * @param filter Filter to apply.
	 * @return The number of hits of the query.
	 */
	Result count(NodeQuery query, NodeFilter filter);

	/**
	 * Performs a grouped query.
	 * @param query Query to perform.
	 * @param filter Filter to apply.
	 * @param fields Fields to group by.
	 * @return The result of the query.
	 */
	GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields);

}
