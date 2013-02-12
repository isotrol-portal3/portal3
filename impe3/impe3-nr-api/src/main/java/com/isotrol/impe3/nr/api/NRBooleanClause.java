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

import com.google.common.base.Objects;


/**
 * Clause in boolean query
 * @author Emilio Escobar Reyero
 * 
 */
public final class NRBooleanClause implements Serializable {

	private static final long serialVersionUID = -8080776516803611743L;

	private final Occur occur;
	private final NodeQuery query;

	/**
	 * Enumeration for conditional clauses
	 * @author Emilio Escobar Reyero
	 */
	public enum Occur {
		/** AND */
		MUST,
		/** OR */
		SHOULD,
		/** NOT */
		MUST_NOT
	}

	/**
	 * Creates new a boolean clause.
	 * @param query Query
	 * @param occur Condition.
	 * @return The created clause.
	 */
	public static NRBooleanClause create(NodeQuery query, Occur occur) {
		return new NRBooleanClause(checkNotNull(query, "The query must be provided"), checkNotNull(occur,
			"The occurrence must be provided"));
	}

	/**
	 * Create a boolean clause for a query.
	 * @param query Query
	 * @param occur Condition
	 */
	NRBooleanClause(final NodeQuery query, final Occur occur) {
		this.query = query;
		this.occur = occur;
	}

	public Occur getOccur() {
		return occur;
	}

	public NodeQuery getQuery() {
		return query;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(query, occur);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NRBooleanClause) {
			final NRBooleanClause c = (NRBooleanClause) obj;
			return Objects.equal(occur, c.occur) && Objects.equal(query, c.query);
		}
		return false;
	}
}
