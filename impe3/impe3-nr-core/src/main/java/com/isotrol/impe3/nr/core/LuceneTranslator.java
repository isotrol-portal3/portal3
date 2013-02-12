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

package com.isotrol.impe3.nr.core;


import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanFilter;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilterClause;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TermsFilter;
import org.apache.lucene.search.WildcardQuery;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.isotrol.impe3.lucene.EmptyFilter;
import com.isotrol.impe3.lucene.PortalStandardAnalyzer;
import com.isotrol.impe3.lucene.PrefixAnalyzedQueryParser;
import com.isotrol.impe3.nr.api.FilterType;
import com.isotrol.impe3.nr.api.NRBooleanClause;
import com.isotrol.impe3.nr.api.NRBooleanQuery;
import com.isotrol.impe3.nr.api.NRMatchAllDocsQuery;
import com.isotrol.impe3.nr.api.NRRangeQuery;
import com.isotrol.impe3.nr.api.NRSortField;
import com.isotrol.impe3.nr.api.NRStringQuery;
import com.isotrol.impe3.nr.api.NRTerm;
import com.isotrol.impe3.nr.api.NRTermQuery;
import com.isotrol.impe3.nr.api.NRWildcardQuery;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeSort;
import com.isotrol.impe3.nr.api.Schema;


/**
 * Converts Node Repository queries to Lucene queries.
 * @author Emilio Escobar Reyero.
 * @author Andres Rodriguez
 */
public final class LuceneTranslator {
	static final Map<FilterType, Occur> TYPE_MAP = ImmutableMap.of(FilterType.REQUIRED, Occur.MUST,
		FilterType.OPTIONAL, Occur.SHOULD, FilterType.FORBIDDEN, Occur.MUST_NOT);
	private final ImmutableMap<Class<?>, Object> functions;

	private final Analyzer analyzer;

	private static Term allLocalesTerm() {
		return new Term(Schema.LOCALE, Schema.ALL_LOCALES);
	}

	/** Term query shorcut. */
	private static TermQuery tq(String field, String value) {
		return new TermQuery(new Term(field, value));
	}

	/** Filter criteria map to query. */
	private static <K> BooleanQuery map2query(Function<? super K, Query> f, Map<K, FilterType> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		BooleanQuery bq = new BooleanQuery();
		for (Entry<K, FilterType> e : map.entrySet()) {
			bq.add(new BooleanClause(f.apply(e.getKey()), TYPE_MAP.get(e.getValue())));
		}
		return bq;
	}

	public LuceneTranslator() {
		this(new PortalStandardAnalyzer());
	}

	public LuceneTranslator(Analyzer analyzer) {
		this.analyzer = analyzer;
		this.functions = createFunctionsMap();
	}

	/**
	 * Translate a node query to lucene query
	 * @param query original query
	 * @return lucene query
	 */
	public Query query(NodeQuery query) {
		Preconditions.checkNotNull(query);
		Preconditions.checkArgument(functions.containsKey(query.getClass()));
		@SuppressWarnings("unchecked")
		final Function<Object, Object> f = (Function<Object, Object>) functions.get(query.getClass());
		final Query q = (Query) f.apply(query);
		q.setBoost(query.getBoost());
		return q;
	}

	/**
	 * Translate a node sort to lucene sort
	 * @param lsort original sort
	 * @return lucene sort
	 */
	public Sort sort(final NodeSort lsort) {

		if (lsort == null) {
			return null;
		}

		NRSortField[] lfields = lsort.getFields();

		SortField[] fields = new SortField[lfields.length];

		for (int i = 0; i < lfields.length; i++) {
			final SortField field = new SortField(lfields[i].getField(), lfields[i].getType(), lfields[i].isReverse());
			fields[i] = field;
		}

		Sort sort = new Sort(fields);

		return sort;
	}

	private BooleanQuery boolQuery(BooleanClause.Occur occur, Iterable<Query> queries) {
		if (queries == null) {
			return null;
		}
		boolean empty = true;
		final BooleanQuery bq = new BooleanQuery();
		for (Query q : filter(queries, notNull())) {
			empty = false;
			final BooleanClause clause = new BooleanClause(q, occur);
			bq.add(clause);
		}
		return empty ? null : bq;
	}

	private BooleanQuery and(Iterable<Query> queries) {
		return boolQuery(BooleanClause.Occur.MUST, queries);
	}

	private Query locales(Map<Locale, FilterType> locales) {
		if (locales == null) {
			return new TermQuery(allLocalesTerm());
		}
		return map2query(LocaleQuery.INSTANCE, locales);
	}

	/**
	 * Translate a node filter to lucene query
	 * @param filter original filter
	 * @return A lucene query representing the filter or {@code null} if the filter is a null filter.
	 */
	public Query query(NodeFilter filter) {
		if (NodeFilter.isNull(filter)) {
			return new TermQuery(allLocalesTerm());
		}
		final List<Query> queries = Lists.newArrayListWithCapacity(4);
		queries.add(map2query(SetQuery.INSTANCE, filter.sets()));
		queries.add(map2query(CategoryQuery.INSTANCE, filter.categories()));
		queries.add(map2query(ContentTypeQuery.INSTANCE, filter.contentTypes()));
		queries.add(locales(filter.locales()));
		queries.add(map2query(TagQuery.INSTANCE, filter.tags()));
		queries.add(map2query(KeyQuery.INSTANCE, filter.keys()));
		return and(queries);
	}

	/**
	 * Translate a node query and a node filter into lucene query
	 * @param query Node query.
	 * @param filter Node filter.
	 * @return A lucene query representing the query and the filter.
	 */
	public Query query(NodeQuery query, NodeFilter filter) {
		Query q = query(query);
		Query f = query(filter);
		if (f == null) {
			return q;
		}
		BooleanQuery both = new BooleanQuery();
		both.add(new BooleanClause(q, BooleanClause.Occur.MUST));
		both.add(new BooleanClause(f, BooleanClause.Occur.MUST));
		return both;
	}

	private Term get(NRTerm term) {
		return new Term(term.getField(), term.getText());
	}

	private ImmutableMap<Class<?>, Object> createFunctionsMap() {
		ImmutableMap.Builder<Class<?>, Object> builder = ImmutableMap.builder();

		builder.put(NRTermQuery.class, new Function<NRTermQuery, TermQuery>() {
			public TermQuery apply(NRTermQuery input) {
				return new TermQuery(get(input.getTerm()));
			}
		});

		builder.put(NRWildcardQuery.class, new Function<NRWildcardQuery, WildcardQuery>() {
			public WildcardQuery apply(NRWildcardQuery input) {
				return new WildcardQuery(get(input.getTerm()));
			}
		});

		builder.put(NRRangeQuery.class, new Function<NRRangeQuery, Query>() {
			public Query apply(NRRangeQuery input) {
				return new TermRangeQuery(input.getFieldName(), input.getLowerVal(), input.getUpperVal(), input
					.isIncludeLower(), input.isIncludeUpper());
			}
		});

		builder.put(NRMatchAllDocsQuery.class, new Function<NRMatchAllDocsQuery, Query>() {
			public Query apply(NRMatchAllDocsQuery input) {
				return new MatchAllDocsQuery();
			}
		});

		builder.put(NRBooleanQuery.class, new Function<NRBooleanQuery, Query>() {
			public Query apply(NRBooleanQuery input) {
				final BooleanQuery query = new BooleanQuery();
				for (BooleanClause clause : Iterables.transform(input.getClauses(),
					new Function<NRBooleanClause, BooleanClause>() {

						public BooleanClause apply(NRBooleanClause input) {
							final Query q = query(input.getQuery());
							final BooleanClause.Occur occur;
							switch (input.getOccur()) {
								case MUST:
									occur = BooleanClause.Occur.MUST;
									break;
								case SHOULD:
									occur = BooleanClause.Occur.SHOULD;
									break;
								case MUST_NOT:
									occur = BooleanClause.Occur.MUST_NOT;
									break;
								default:
									throw new IllegalStateException();
							}
							return new BooleanClause(q, occur);
						};
					})) {

					if (clause != null && clause.getQuery() != null && clause.getQuery().toString().length() > 0) {
						query.add(clause);
					}
				}
				return query;
			}
		});

		builder.put(NRStringQuery.class, new Function<NRStringQuery, Query>() {
			public Query apply(NRStringQuery input) {
				final NRTerm term = input.getTerm();
				final QueryParser parser = new PrefixAnalyzedQueryParser(term.getField(), analyzer);

				try {
					return parser.parse(term.getText());
				} catch (ParseException e) {
					throw new IllegalArgumentException(e);
				}
			}
		});

		return builder.build();
	}

	/** Set to query. */
	private enum SetQuery implements Function<String, Query> {
		INSTANCE;

		public Query apply(String input) {
			return tq(Schema.NODESET, input);
		}
	}

	/** Category to query. */
	private enum CategoryQuery implements Function<Optional<UUID>, Query> {
		INSTANCE;

		public Query apply(Optional<UUID> input) {
			if (input.isPresent()) {
				return tq(Schema.CATEGORY, input.get().toString());
			}
			return tq(Schema.CATEGORY, Schema.NULL_UUID);
		}
	}

	/** Content type to query. */
	private enum ContentTypeQuery implements Function<UUID, Query> {
		INSTANCE;

		public Query apply(UUID input) {
			return tq(Schema.TYPE, input.toString());
		}
	}

	/** Locale to query. */
	private enum LocaleQuery implements Function<Locale, Query> {
		INSTANCE;

		public Query apply(Locale input) {
			final String v = input.toString();
			final BooleanQuery inner = new BooleanQuery();
			inner.add(new BooleanClause(tq(Schema.LOCALE, Schema.ALL_LOCALES), BooleanClause.Occur.MUST));
			inner.add(new BooleanClause(tq(Schema.OTHER_LOCALE, v), BooleanClause.Occur.MUST_NOT));
			final BooleanQuery outer = new BooleanQuery();
			outer.add(new BooleanClause(tq(Schema.LOCALE, v), BooleanClause.Occur.SHOULD));
			outer.add(new BooleanClause(inner, BooleanClause.Occur.SHOULD));
			return outer;
		}
	}

	/** Tag to query. */
	private enum TagQuery implements Function<String, Query> {
		INSTANCE;

		public Query apply(String input) {
			return tq(Schema.TAG, input);
		}
	}

	/** Node key to query. */
	private enum KeyQuery implements Function<NodeKey, Query> {
		INSTANCE;

		public Query apply(NodeKey input) {
			return tq(Schema.NODEKEY, input.toString());
		}
	}

	// Filters

	/** Filter criteria map to lucene filter. */
	private static <K> void map2filter(List<Filter> filters, Function<? super K, Filter> f, Map<K, FilterType> map) {
		if (map != null && !map.isEmpty()) {
			BooleanFilter bf = new BooleanFilter();
			for (Entry<K, FilterType> e : map.entrySet()) {
				bf.add(new FilterClause(f.apply(e.getKey()), TYPE_MAP.get(e.getValue())));
			}
			filters.add(bf);
		}
	}

	private static void dueFilter(List<Filter> filters) {
		final String now = Schema.dateToString(new Date());
		filters.add(new TermRangeFilter(Schema.RELEASEDATE, Schema.getMinDateString(), now, true, true));
		filters.add(new TermRangeFilter(Schema.EXPIRATIONDATE, now, Schema.getMaxDateString(), true, true));
	}

	private static TermsFilter tf(String field, String value) {
		TermsFilter tf = new TermsFilter();
		add(tf, field, value);
		return tf;
	}

	private static void add(TermsFilter tf, String field, String value) {
		tf.addTerm(new Term(field, value));
	}

	private static Filter allLocalesFilter() {
		final TermsFilter tf = new TermsFilter();
		tf.addTerm(allLocalesTerm());
		return tf;
	}

	/**
	 * Node filter to Lucene filter translator.
	 * @param filter Filter to translate
	 * @return The lucene filter.
	 */
	public Filter getFilter(NodeFilter filter) {
		if (NodeFilter.isNull(filter)) {
			return allLocalesFilter();
		}
		if (NodeFilter.isEmpty(filter)) {
			return EmptyFilter.create();
		}
		final List<Filter> filters = newArrayListWithCapacity(7);
		// Due filter.
		if (filter.isDue()) {
			dueFilter(filters);
		}
		// Collections
		map2filter(filters, SetFilter.INSTANCE, filter.sets());
		map2filter(filters, CategoryFilter.INSTANCE, filter.categories());
		map2filter(filters, ContentTypeFilter.INSTANCE, filter.contentTypes());
		if (filter.locales() == null) {
			filters.add(allLocalesFilter());
		} else {
			map2filter(filters, Locale2Filter.INSTANCE, filter.locales());
		}
		map2filter(filters, TagFilter.INSTANCE, filter.tags());
		map2filter(filters, KeyFilter.INSTANCE, filter.keys());
		// Done
		if (filters.isEmpty()) {
			return null;
		}
		if (filters.size() == 1) {
			return filters.get(0);
		}
		final BooleanFilter bf = new BooleanFilter();
		for (Filter f : filters) {
			bf.add(new FilterClause(f, Occur.MUST));
		}
		return bf;
	}

	/** Set to filter. */
	private enum SetFilter implements Function<String, Filter> {
		INSTANCE;

		public Filter apply(String input) {
			return tf(Schema.NODESET, input);
		}
	}

	/** Category to filter. */
	private enum CategoryFilter implements Function<Optional<UUID>, Filter> {
		INSTANCE;

		public Filter apply(Optional<UUID> input) {
			if (input.isPresent()) {
				return tf(Schema.CATEGORY, input.get().toString());
			}
			return tf(Schema.CATEGORY, Schema.NULL_UUID);
		}
	}

	/** Content type to filter. */
	private enum ContentTypeFilter implements Function<UUID, Filter> {
		INSTANCE;

		public Filter apply(UUID input) {
			return tf(Schema.TYPE, input.toString());
		}
	}

	/** Locale to filter. */
	private enum Locale2Filter implements Function<Locale, Filter> {
		INSTANCE;

		public Filter apply(Locale input) {
			return new LocaleFilter(input);
		}
	}

	/** Tag to filter. */
	private enum TagFilter implements Function<String, Filter> {
		INSTANCE;

		public Filter apply(String input) {
			return tf(Schema.TAG, input);
		}
	}

	/** Node key to filter. */
	private enum KeyFilter implements Function<NodeKey, Filter> {
		INSTANCE;

		public Filter apply(NodeKey input) {
			return tf(Schema.NODEKEY, input.toString());
		}
	}

}
