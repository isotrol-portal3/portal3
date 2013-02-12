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


import static com.isotrol.impe3.nr.api.Schema.dateToString;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;


/**
 * Queries constructor helper. This class is not final and has a protected constructor to allow subclasses to extend the
 * provided set of helper methods in a single namespace. However, this class is not instantiable.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public class NodeQueries {
	/**
	 * Constructor.
	 * @throws AssertionError as this class is not instantiable.
	 */
	protected NodeQueries() {
		throw new AssertionError();
	}

	static String text(UUID id) {
		return id.toString().toLowerCase();
	}

	/**
	 * Prepare a search node id query.
	 * @param nodeId Node id.
	 * @param nodeType Node type.
	 * @return the query.
	 */
	public static NodeQuery nodeKey(String nodeId, UUID nodeType) {
		return bool().must(Schema.ID, nodeId).must(Schema.TYPE, nodeType);
	}

	/**
	 * Create a simple term query. Low level expert method.
	 * @param field Field name.
	 * @param text Text to search
	 * @return The query.
	 */
	public static NodeQuery term(String field, String text) {
		return new NRTermQuery(NRTerm.of(field, text));
	}

	/**
	 * Create a simple term query. Low level expert method.
	 * @param field Field name.
	 * @param uuid UUID to search.
	 * @return The query.
	 */
	public static NodeQuery term(String field, UUID uuid) {
		return term(field, text(uuid));
	}

	/**
	 * Create a simple term query. Low level expert method.
	 * @param field Field name.
	 * @param date Date to search
	 * @return The query.
	 */
	public static NodeQuery term(String field, Date date) {
		return new NRTermQuery(NRTerm.of(field, dateToString(date)));
	}

	/**
	 * Returns a transformation function from string values to term queries.
	 * @param field Field name.
	 * @return The requested function.
	 */
	public static Function<String, NodeQuery> terms(final String field) {
		return new Function<String, NodeQuery>() {
			public NodeQuery apply(String from) {
				return term(field, from);
			}
		};
	}

	/**
	 * Returns a transformation function from uuid values to term queries.
	 * @param field Field name.
	 * @return The requested function.
	 */
	public static Function<UUID, NodeQuery> uuidTerms(final String field) {
		return new Function<UUID, NodeQuery>() {
			public NodeQuery apply(UUID from) {
				return term(field, from);
			}
		};
	}

	/**
	 * Returns a transformation function from date values to term queries.
	 * @param field Field name.
	 * @return The requested function.
	 */
	public static Function<Date, NodeQuery> dateTerms(final String field) {
		return new Function<Date, NodeQuery>() {
			public NodeQuery apply(Date from) {
				return term(field, from);
			}
		};
	}

	/**
	 * Returns a transformation function from field names term queries.
	 * @param termValue Term text.
	 * @return The requested function.
	 */
	public static Function<String, NodeQuery> fields(final String termValue) {
		return new Function<String, NodeQuery>() {
			public NodeQuery apply(String from) {
				return term(from, termValue);
			}
		};
	}

	/**
	 * Returns a transformation function from field names to string queries.
	 * @param termValue Term text.
	 * @return The requested function.
	 */
	public static Function<String, NodeQuery> strings(final String termValue) {
		return new Function<String, NodeQuery>() {
			public NodeQuery apply(String from) {
				return string(from, termValue);
			}
		};
	}

	/**
	 * Create a free query with future parse. Low level expert method.
	 * @param field Field name.
	 * @param text Text to search
	 * @return The query.
	 */
	public static NodeQuery string(String field, String text) {
		return new NRStringQuery(NRTerm.of(field, text));
	}

	/**
	 * Create a wildcard query if text contains '?' or '*'. Otherwise create q term query. Low level expert method.
	 * @param field Field name.
	 * @param text Text to search.
	 * @return The Query.
	 */
	public static NodeQuery wildcard(String field, String text) {
		NodeQuery query;

		if (text.indexOf('?') > -1 || text.indexOf('*') > -1) {
			query = new NRWildcardQuery(NRTerm.of(field, text));
		} else {
			query = term(field, text);
		}

		return query;
	}

	/**
	 * Create a range query between lower and upper vals. Low level expert method.
	 * @param fieldName Field name.
	 * @param lowerVal lower value
	 * @param upperVal upper value
	 * @param includeLower if true include the lower value
	 * @param includeUpper if true include the upper value
	 * @return the query.
	 */
	public static NodeQuery range(String fieldName, String lowerVal, String upperVal, boolean includeLower,
		boolean includeUpper) {
		return new NRRangeQuery(fieldName, lowerVal, upperVal, includeLower, includeUpper);
	}

	/**
	 * Create a range query between lower and upper date vals. Low level expert method.
	 * @param fieldName Field name.
	 * @param lowerVal lower date value
	 * @param upperVal upper date value
	 * @param includeLower if true include the lower value
	 * @param includeUpper if true include the upper value
	 * @return the query.
	 */
	public static NodeQuery range(String fieldName, Date lowerVal, Date upperVal, boolean includeLower,
		boolean includeUpper) {
		return new NRRangeQuery(fieldName, dateToString(lowerVal), dateToString(upperVal), includeLower, includeUpper);
	}

	/**
	 * Create a query that gets all nodes. Low level expert method.
	 * @return the query.
	 */
	public static NodeQuery matchAll() {
		return new NRMatchAllDocsQuery();
	}

	/**
	 * Create a boolean query. Very very low level expert method.
	 * @return the query.
	 */
	public static NRBooleanQuery bool() {
		return new NRBooleanQuery();
	}

	/**
	 * If q is null returns a matchAll query
	 * @param q original query
	 * @return matchAll query ir q == null
	 */
	public static NodeQuery null2All(NodeQuery q) {
		return q == null ? matchAll() : q;
	}

	/**
	 * Merges all queries with must boolean operator
	 * @param queries NodeQuery queries
	 * @return NodeQuery with must operator with all queries.
	 */
	public static NodeQuery all(Iterable<? extends NodeQuery> queries) {
		queries = Iterables.filter(queries, Predicates.notNull());
		final int n = Iterables.size(queries);
		if (n == 0) {
			return null;
		} else if (n == 1) {
			return Iterables.get(queries, 0);
		}
		final NRBooleanQuery q = bool();
		for (NodeQuery c : queries) {
			q.must(c);
		}
		return q;
	}

	/**
	 * Merges all queries with must boolean operator
	 * @param queries NodeQuery queries
	 * @return NodeQuery with must operator with all queries.
	 */
	public static NodeQuery all(NodeQuery... queries) {
		return all(Arrays.asList(queries));
	}

	/**
	 * Merges all queries with should boolean operator
	 * @param queries NodeQuery queries
	 * @return NodeQuery with should operator with all queries.
	 */
	public static NodeQuery any(Iterable<? extends NodeQuery> queries) {
		queries = Iterables.filter(queries, Predicates.notNull());
		final int n = Iterables.size(queries);
		if (n == 0) {
			return null;
		} else if (n == 1) {
			return Iterables.get(queries, 0);
		}
		final NRBooleanQuery q = bool();
		for (NodeQuery c : queries) {
			q.should(c);
		}
		return q;
	}

	public static NodeQuery any(NodeQuery... queries) {
		return any(Arrays.asList(queries));
	}

	public static NodeQuery any(final String term, Iterable<String> fields) {
		if (fields == null) {
			return null;
		}
		return any(Iterables.transform(fields, fields(term)));
	}

	public static NodeQuery any(final String term, String... fields) {
		return any(term, Arrays.asList(fields));
	}

	public static NodeQuery anyString(final String term, Iterable<String> fields) {
		if (fields == null) {
			return null;
		}
		return any(Iterables.transform(fields, strings(term)));
	}

	public static NodeQuery anyString(final String term, String... fields) {
		return anyString(term, Arrays.asList(fields));
	}

}
