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

import java.io.Serializable;
import java.util.Arrays;

import com.google.common.collect.Iterables;


/**
 * Encapsulate sort criteia for queries
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public final class NodeSort implements Serializable {

	private static final long serialVersionUID = 3593090815369460184L;

	private final NRSortField[] fields;

	private NodeSort(NRSortField[] fields) {
		this.fields = fields;
	}

	private NodeSort(NRSortField field) {
		this(new NRSortField[] {field});
	}

	/**
	 * Create a sort criteria.
	 * @param field
	 * @return
	 */
	public static NodeSort of(NRSortField field) {
		return new NodeSort(field);
	}

	/**
	 * 
	 * @param fields
	 * @return
	 */
	public static NodeSort of(NRSortField... fields) {
		return new NodeSort(fields);
	}

	/**
	 * Create a sort criteria.
	 * @param fields Sort field specifications.
	 * @return The sort criteria.
	 */
	public static NodeSort of(Iterable<NRSortField> fields) {
		checkNotNull(fields, "The sort field specifications must be provided");
		return new NodeSort(Iterables.toArray(fields, NRSortField.class));
	}

	public NRSortField[] getFields() {
		return this.fields;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(fields);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NodeSort) {
			return Arrays.equals(fields, ((NodeSort)obj).fields);
		}
		return false;
	}
	
	
}
