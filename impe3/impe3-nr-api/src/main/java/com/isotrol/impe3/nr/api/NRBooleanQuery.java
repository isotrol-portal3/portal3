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
import static com.isotrol.impe3.nr.api.NodeQueries.term;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.isotrol.impe3.nr.api.NRBooleanClause.Occur;


/**
 * Conditional query.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public final class NRBooleanQuery extends NodeQuery {

	private static final long serialVersionUID = -1236361407047940854L;

	private final List<NRBooleanClause> clauses = new LinkedList<NRBooleanClause>();

	NRBooleanQuery() {
	}

	/**
	 * Adds a new clause.
	 * @param clause Clause to add.
	 * @return This query for method chaining.
	 */
	public NRBooleanQuery add(NRBooleanClause clause) {
		checkNotNull(clause);
		clauses.add(clause);
		return this;
	}

	/**
	 * Create a new boolean query using required operator (must)
	 * 
	 * @param query the query
	 * @return the boolean query
	 */
	public NRBooleanQuery must(NodeQuery query) {
		Preconditions.checkNotNull(query);
		clauses.add(new NRBooleanClause(query, Occur.MUST));
		return this;
	}

	/**
	 * Create a new boolean query using default conjuntion operator meaning or
	 * 
	 * @param query original query
	 * @return the new boolean query
	 */
	public NRBooleanQuery should(NodeQuery query) {
		Preconditions.checkNotNull(query);
		clauses.add(new NRBooleanClause(query, Occur.SHOULD));
		return this;
	}

	/**
	 * Create a new boolean query using not operator. Cannot be used with just one term.
	 * 
	 * @param query the original query
	 * @return new boolean query
	 */
	public NRBooleanQuery mustNot(NodeQuery query) {
		Preconditions.checkNotNull(query);
		clauses.add(new NRBooleanClause(query, Occur.MUST_NOT));
		return this;
	}

	/**
	 * First create a term query using field and text and then create a new boolean query using required operator
	 * (must).
	 * 
	 * @param field document field for the term query
	 * @param text value for the term query
	 * @return new boolean query
	 */
	public NRBooleanQuery must(String field, String text) {
		return must(term(field, text));
	}

	/**
	 * First create a term query using field and text and then create a new boolean query using default conjuntion
	 * operator meaning or
	 * 
	 * @param field document field for the term query
	 * @param text value for the term query
	 * @return new boolean query
	 */
	public NRBooleanQuery should(String field, String text) {
		return should(term(field, text));
	}

	/**
	 * First create a term query using field and text and then create a new boolean query using not operator. Cannot be
	 * used with just one term.
	 * 
	 * @param field document field for the term query
	 * @param text value for the term query
	 * @return new boolean query
	 */
	public NRBooleanQuery mustNot(String field, String text) {
		return mustNot(term(field, text));
	}

	/**
	 * First create a term query using field and id and then create a new boolean query using required operator (must).
	 * 
	 * @param field document field for the term query
	 * @param id value for the term query
	 * @return new boolean query
	 */
	public NRBooleanQuery must(String field, UUID id) {
		return must(term(field, id));
	}

	/**
	 * First create a term query using field and id and then create a new boolean query using default conjuntion
	 * operator meaning or
	 * 
	 * @param field document field for the term query
	 * @param id value for the term query
	 * @return new boolean query
	 */
	public NRBooleanQuery should(String field, UUID id) {
		return should(term(field, id));
	}

	/**
	 * First create a term query using field and id and then create a new boolean query using not operator. Cannot be
	 * used with just one term.
	 * 
	 * @param field document field for the term query
	 * @param id value for the term query
	 * @return new boolean query
	 */
	public NRBooleanQuery mustNot(String field, UUID id) {
		return mustNot(term(field, id));
	}

	/**
	 * First create a term query using field and date and then create a new boolean query using required operator
	 * (must).
	 * 
	 * @param field document field for the term query
	 * @param date value for the term query
	 * @return new boolean query
	 */
	public NRBooleanQuery must(String field, Date date) {
		return must(term(field, date));
	}

	/**
	 * First create a term query using field and date and then create a new boolean query using default conjuntion
	 * operator meaning or
	 * 
	 * @param field document field for the term query
	 * @param date value for the term query
	 * @return new boolean query
	 */
	public NRBooleanQuery should(String field, Date date) {
		return should(term(field, date));
	}

	/**
	 * First create a term query using field and date and then create a new boolean query using not operator. Cannot be
	 * used with just one term.
	 * 
	 * @param field document field for the term query
	 * @param date value for the term query
	 * @return new boolean query
	 */
	public NRBooleanQuery mustNot(String field, Date date) {
		return mustNot(term(field, date));
	}

	public List<NRBooleanClause> getClauses() {
		return Collections.unmodifiableList(clauses);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getBoost(), clauses);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NRBooleanQuery) {
			final NRBooleanQuery q = (NRBooleanQuery) obj;

			return getBoost() == q.getBoost() && Objects.equal(clauses, q.clauses);
		}
		return false;
	}
}
