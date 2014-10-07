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
package com.isotrol.impe3.oi.dao.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.derquinse.lucis.IndexNotAvailableException;
import net.sf.derquinse.lucis.Page;
import net.sf.lucis.core.DocMapper;
import net.sf.lucis.core.Factory;
import net.sf.lucis.core.LucisQuery;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.support.Queryables;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.isotrol.impe3.dto.OrderDTO;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.StringFilterDTO;
import com.isotrol.impe3.dto.StringMatchMode;
import com.isotrol.impe3.oi.api.InterviewFilterDTO;
import com.isotrol.impe3.oi.api.InterviewSelDTO;
import com.isotrol.impe3.oi.dao.IndexDAO;
import com.isotrol.impe3.oi.mappers.InterviewSelDTODocMapper;
import com.isotrol.impe3.oi.server.InterviewSchema;


/**
 * Index DAO implementation
 * @author Emilio Escobar Reyero
 */
// @Repository
public class IndexDAOImpl implements IndexDAO, InitializingBean {

	/** Store */
	// @Autowired
	private Store<Long> store;
	/** Queryable. */
	private Queryable queryable;
	/** Analyzer. */
	// @Autowired
	private Analyzer analyzer;

	/** Lucene max clause count. */
	private int maxClauseCount = 2048;

	public PageDTO<InterviewSelDTO> findInterviews(PageFilter<InterviewFilterDTO> filter) {
		final Query q = filterInterviewMapper.apply(filter);

		final Sort s = orderings(filter.getOrderings());

		final DocMapper<InterviewSelDTO> mapper = new InterviewSelDTODocMapper();
		final LucisQuery<Page<InterviewSelDTO>> lq = LucisQuery.page(q, null, s, mapper, filter.getPagination()
			.getFirst(), filter.getPagination().getSize());

		Page<InterviewSelDTO> page;
		try {
			page = queryable.query(lq);
		} catch (IndexNotAvailableException e) {
			page = Page.<InterviewSelDTO> empty();
		}

		return pageInterviewMapper.apply(page);
	}

	/**
	 * Sets lucene boolean query max clause count.
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		queryable = Queryables.simple(store);
		BooleanQuery.setMaxClauseCount(maxClauseCount);
	}

	private Sort orderings(List<OrderDTO> orders) {
		if (orders == null || orders.isEmpty()) {
			return null;
		}

		SortField[] fields = new SortField[orders.size()];

		for (int i = 0; i < orders.size(); i++) {
			final OrderDTO order = orders.get(i);
			final SortField field = new SortField(order.getName(), SortField.STRING, order.getAscending());
			fields[i] = field;
		}

		return new Sort(fields);
	}

	private final Function<PageFilter<InterviewFilterDTO>, Query> filterInterviewMapper = new Function<PageFilter<InterviewFilterDTO>, Query>() {
		public Query apply(PageFilter<InterviewFilterDTO> input) {
			final Map<String, Set<String>> classifications = input.getFilter().getClassification();
			final boolean notClass = input.getFilter().isInverse();

			final BooleanQuery classificationQuery = classification(classifications, notClass);

			final BooleanQuery conjuntion = new BooleanQuery();
			final BooleanQuery disjuntion = new BooleanQuery();

			final StringFilterDTO title = input.getFilter().getTitle();
			if (title != null) {
				if (title.isConjunction()) {
					conjuntion.add(parsedClause(title, InterviewSchema.TITLE));
				} else {
					disjuntion.add(parsedClause(title, InterviewSchema.TITLE));
				}
			}
			final StringFilterDTO description = input.getFilter().getDescription();
			if (description != null) {
				if (description.isConjunction()) {
					conjuntion.add(parsedClause(description, InterviewSchema.DESCRIPTION));
				} else {
					disjuntion.add(parsedClause(description, InterviewSchema.DESCRIPTION));
				}
			}
			final StringFilterDTO interviewee = input.getFilter().getInterviewee();
			if (interviewee != null) {
				if (interviewee.isConjunction()) {
					conjuntion.add(parsedClause(interviewee, InterviewSchema.INTERVIEWEE));
				} else {
					disjuntion.add(parsedClause(interviewee, InterviewSchema.INTERVIEWEE));
				}
			}
			final StringFilterDTO author = input.getFilter().getAuthor();
			if (author != null) {
				if (author.isConjunction()) {
					conjuntion.add(parsedClause(author, InterviewSchema.AUTHOR));
				} else {
					disjuntion.add(parsedClause(author, InterviewSchema.AUTHOR));
				}
			}

			final Integer active = input.getFilter().isActive();
			if (active != null) {
				if (active.intValue() < 0) {
					conjuntion.add(
						new TermRangeQuery(InterviewSchema.RELEASE, InterviewSchema.dateToString(new Date()),
							InterviewSchema.dateToString(InterviewSchema.getMaxDate()), false, true), Occur.MUST);
				} else if (active.intValue() == 0) {
					conjuntion.add(
						new TermRangeQuery(InterviewSchema.RELEASE, InterviewSchema.dateToString(InterviewSchema
							.getMinDate()), InterviewSchema.dateToString(new Date()), true, true), Occur.MUST);
					conjuntion.add(
						new TermRangeQuery(InterviewSchema.EXPIRATION, InterviewSchema.dateToString(new Date()),
							InterviewSchema.dateToString(InterviewSchema.getMaxDate()), true, true), Occur.MUST);
				} else {
					conjuntion.add(
						new TermRangeQuery(InterviewSchema.EXPIRATION, InterviewSchema.dateToString(InterviewSchema
							.getMinDate()), InterviewSchema.dateToString(new Date()), true, false), Occur.MUST);
				}

			}

			final Map<String, StringFilterDTO> properties = input.getFilter().getProperties();
			if (properties != null && !properties.isEmpty()) {
				for (String key : properties.keySet()) {
					final StringFilterDTO sf = properties.get(key);
					if (sf != null) {
						if (sf.isConjunction()) {
							conjuntion.add(parsedClause(sf, InterviewSchema.property(key)));
						} else {
							disjuntion.add(parsedClause(sf, InterviewSchema.property(key)));
						}
					}
				}
			}

			final BooleanQuery query = (classificationQuery != null && classificationQuery.clauses() != null && !classificationQuery
				.clauses().isEmpty()) ? classificationQuery : new BooleanQuery();
			if (conjuntion.clauses() != null && !conjuntion.clauses().isEmpty()) {
				query.add(conjuntion, Occur.MUST);
			}
			if (disjuntion.clauses() != null && !disjuntion.clauses().isEmpty()) {
				query.add(disjuntion, Occur.MUST);
			}

			if (query.clauses() == null || query.clauses().isEmpty()) {
				return new MatchAllDocsQuery();
			}

			return query;
		}

		private BooleanQuery classification(Map<String, Set<String>> classifications, boolean notClass) {
			if (classifications == null || classifications.isEmpty()) {
				return null;
			}
			final BooleanQuery query = new BooleanQuery();

			for (String set : classifications.keySet()) {
				Set<String> classes = classifications.get(set);
				for (String c : classes) {
					final Term t = new Term(InterviewSchema.CLASS, set + ":" + c);
					final TermQuery q = new TermQuery(t);
					query
						.add(new BooleanClause(q, notClass ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.SHOULD));
				}
			}

			final BooleanQuery queryFinal = new BooleanQuery();
			queryFinal.add(query, BooleanClause.Occur.MUST);

			return queryFinal;
		}

		private BooleanClause parsedClause(StringFilterDTO str, String field) {
			final Query q = parsedQuery(str, field);

			return new BooleanClause(q, str.isConjunction() ? BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD);
		}

		private Query parsedQuery(StringFilterDTO str, String field) {
			final Query q;
			final QueryParser parser;
			if (StringMatchMode.EXACT.equals(str.getMatchMode())) {
				parser = new QueryParser(Factory.get().version(), field, analyzer);
			} else if (StringMatchMode.PREFIX.equals(str.getMatchMode())) {
				parser = new QueryParser(Factory.get().version(), field, analyzer) {
					protected Query newTermQuery(Term term) {
						return new WildcardQuery(new Term(term.field(), term.text() + "*"));
					};
				};
			} else if (StringMatchMode.SUFFIX.equals(str.getMatchMode())) {
				parser = new QueryParser(Factory.get().version(), field, analyzer) {
					protected Query newTermQuery(Term term) {
						return new WildcardQuery(new Term(term.field(), "*" + term.text()));
					};
				};
			} else { // StringMatchMode.IN
				parser = new QueryParser(Factory.get().version(), field, analyzer) {
					protected Query newTermQuery(Term term) {
						return new WildcardQuery(new Term(term.field(), "*" + term.text() + "*"));
					};
				};
			}

			try {
				q = parser.parse(str.getValue());
			} catch (ParseException e) {
				return query(str, field);
			}

			return q;

		}

		private Query query(StringFilterDTO str, String field) {
			final Query q;
			if (StringMatchMode.EXACT.equals(str.getMatchMode())) {
				final Term t = new Term(field, str.getValue());
				q = new TermQuery(t);
			} else if (StringMatchMode.PREFIX.equals(str.getMatchMode())) {
				final Term t = new Term(field, str.getValue() + "*");
				q = new WildcardQuery(t);
			} else if (StringMatchMode.SUFFIX.equals(str.getMatchMode())) {
				final Term t = new Term(field, "*" + str.getValue());
				q = new WildcardQuery(t);
			} else { // StringMatchMode.IN
				final Term t = new Term(field, "*" + str.getValue() + "*");
				q = new WildcardQuery(t);
			}
			return q;
		}
	};

	private static final Function<Page<InterviewSelDTO>, PageDTO<InterviewSelDTO>> pageInterviewMapper = new Function<Page<InterviewSelDTO>, PageDTO<InterviewSelDTO>>() {
		public PageDTO<InterviewSelDTO> apply(Page<InterviewSelDTO> input) {
			final PageDTO<InterviewSelDTO> page = new PageDTO<InterviewSelDTO>();
			page.setElements(input.getItems() == null || input.getItems().isEmpty() ? Lists
				.<InterviewSelDTO> newArrayList() : input.getItems());
			page.setFirst(input.getFirstResult());
			page.setSize(input.size());
			page.setTotal(input.getTotalHits());

			return page;
		}
	};

	public void setStore(Store<Long> store) {
		this.store = store;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public void setMaxClauseCount(int maxClauseCount) {
		this.maxClauseCount = maxClauseCount;
	}
}
