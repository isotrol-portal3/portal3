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
package com.isotrol.impe3.web20.dao.impl;


import java.util.List;
import java.util.Map;

import net.sf.derquinse.lucis.IndexNotAvailableException;
import net.sf.derquinse.lucis.Page;
import net.sf.lucis.core.DocMapper;
import net.sf.lucis.core.Factory;
import net.sf.lucis.core.LucisQuery;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.support.Queryables;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
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
import org.apache.lucene.search.WildcardQuery;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.dto.OrderDTO;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.StringFilterDTO;
import com.isotrol.impe3.dto.StringMatchMode;
import com.isotrol.impe3.web20.api.CommunityMembersFilterDTO;
import com.isotrol.impe3.web20.api.MemberFilterDTO;
import com.isotrol.impe3.web20.api.MemberSelDTO;
import com.isotrol.impe3.web20.dao.MemberIdxDAO;
import com.isotrol.impe3.web20.mappers.MemberSelDTODocMapper;
import com.isotrol.impe3.web20.server.MemberSchema;


/**
 * Member index access implementation.
 * @author Emilio Escobar Reyero
 */
// @Repository
public class MemberIdxDAOImpl implements MemberIdxDAO, InitializingBean {

	/** Store */
	private Store<Long> store;
	/** Queryable. */
	private Queryable queryable;
	/** Analyzer. */
	private Analyzer analyzer;

	/** Lucene max clause count. */
	private int maxClauseCount = 2048;

	/**
	 * Sets lucene boolean query max clause count.
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		BooleanQuery.setMaxClauseCount(maxClauseCount);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.MemberIdxDAO#findMembers(com.isotrol.impe3.dto.PageFilter)
	 */
	public PageDTO<MemberSelDTO> findMembers(PageFilter<MemberFilterDTO> filter) {
		final Query q = filterMemberMapper.apply(filter);

		final Sort s = orderings(filter.getOrderings());

		final DocMapper<MemberSelDTO> mapper = new MemberSelDTODocMapper();
		final LucisQuery<Page<MemberSelDTO>> lq = LucisQuery.page(q, null, s, mapper,
			filter.getPagination().getFirst(), filter.getPagination().getSize());

		Page<MemberSelDTO> page;
		try {
			page = queryable.query(lq);
		} catch (IndexNotAvailableException e) {
			page = Page.<MemberSelDTO> empty();
		}

		return pageMemberMapper.apply(page);
	}

	/**
	 * @see com.isotrol.impe3.web20.dao.MemberIdxDAO#findMemberIds(com.isotrol.impe3.dto.PageFilter)
	 */
	public PageDTO<String> findMemberIds(PageFilter<MemberFilterDTO> filter) {
		final Query q = filterMemberMapper.apply(filter);
		final Sort s = orderings(filter.getOrderings());
		final DocMapper<String> mapper = new DocMapper<String>() {
			public String map(int id, float score, Document doc, Multimap<String, String> fragments) {
				return doc.get(MemberSchema.ID);
			}
		};
		final LucisQuery<Page<String>> lq = LucisQuery.page(q, null, s, mapper, filter.getPagination().getFirst(),
			filter.getPagination().getSize());

		Page<String> page;
		try {
			page = queryable.query(lq);
		} catch (IndexNotAvailableException e) {
			page = Page.<String> empty();
		}

		return pageCommunityMembersMapper.apply(page);

	}

	/**
	 * @see com.isotrol.impe3.web20.dao.MemberIdxDAO#findCommunityMembers(com.isotrol.impe3.dto.PageFilter)
	 */
	public PageDTO<String> findCommunityMembers(PageFilter<CommunityMembersFilterDTO> filter) {
		final Query q = filerCommunityMembersMapper.apply(filter);

		final Sort s = orderings(filter.getOrderings());

		final DocMapper<String> mapper = new DocMapper<String>() {
			public String map(int id, float score, Document doc, Multimap<String, String> fragments) {
				return doc.get(MemberSchema.ID);
			}
		};
		final LucisQuery<Page<String>> lq = LucisQuery.page(q, null, s, mapper, filter.getPagination().getFirst(),
			filter.getPagination().getSize());

		Page<String> page;
		try {
			page = queryable.query(lq);
		} catch (IndexNotAvailableException e) {
			page = Page.<String> empty();
		}
		return pageCommunityMembersMapper.apply(page);
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

	// @Autowired
	public void setStore(Store<Long> store) {
		this.store = store;
		queryable = Queryables.simple(store);
	}

	// @Autowired
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public void setMaxClauseCount(int maxClauseCount) {
		this.maxClauseCount = maxClauseCount;
	}

	private final Function<PageFilter<MemberFilterDTO>, Query> filterMemberMapper = new Function<PageFilter<MemberFilterDTO>, Query>() {
		public Query apply(PageFilter<MemberFilterDTO> input) {
			final BooleanQuery communityQuery = new BooleanQuery();

			final String communityId = input.getFilter().getCommunityId();
			final String communityRole = input.getFilter().getRole();
			final boolean notCommunity = input.getFilter().isInverse();
			final BooleanClause clause = community(communityId, communityRole, notCommunity);
			if (clause != null) {
				communityQuery.add(clause);
			}

			final BooleanQuery conjuntion = new BooleanQuery();
			final BooleanQuery disjuntion = new BooleanQuery();

			final StringFilterDTO name = input.getFilter().getName();
			if (name != null) {
				if (name.isConjunction()) {
					conjuntion.add(parsedClause(name, MemberSchema.NAME));
				} else {
					disjuntion.add(parsedClause(name, MemberSchema.NAME));
				}
			}
			final StringFilterDTO code = input.getFilter().getMemberCode();
			if (code != null) {
				if (code.isConjunction()) {
					conjuntion.add(clause(code, MemberSchema.CODE));
				} else {
					disjuntion.add(clause(code, MemberSchema.CODE));
				}
			}
			final StringFilterDTO displayName = input.getFilter().getDisplayName();
			if (displayName != null) {
				if (displayName.isConjunction()) {
					conjuntion.add(parsedClause(displayName, MemberSchema.DISPLAYNAME));
				} else {
					disjuntion.add(parsedClause(displayName, MemberSchema.DISPLAYNAME));
				}
			}

			final StringFilterDTO profile = input.getFilter().getProfile();
			if (profile != null) {
				if (profile.isConjunction()) {
					conjuntion.add(clause(profile, MemberSchema.PROFILE));
				} else {
					disjuntion.add(clause(profile, MemberSchema.PROFILE));
				}
			}

			final Map<String, StringFilterDTO> properties = input.getFilter().getProperties();
			if (properties != null && !properties.isEmpty()) {
				for (String key : properties.keySet()) {
					final StringFilterDTO sf = properties.get(key);
					if (sf != null) {
						if (sf.isConjunction()) {
							conjuntion.add(parsedClause(sf, MemberSchema.property(key)));
						} else {
							disjuntion.add(parsedClause(sf, MemberSchema.property(key)));
						}
					}
				}
			}

			final BooleanQuery query = (communityQuery.clauses() != null && !communityQuery.clauses().isEmpty()) ? communityQuery
				: new BooleanQuery();
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

		private BooleanClause community(String communityId, String communityRole, boolean notCommunity) {

			if (communityId != null && communityRole != null) {
				final Term t = new Term(MemberSchema.COMMUNITYROL, communityId + ":" + communityRole);
				final TermQuery q = new TermQuery(t);
				return new BooleanClause(q, notCommunity ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.MUST);
			} else if (communityId != null && communityRole == null) {
				final Term t = new Term(MemberSchema.COMMUNITY, communityId);
				final TermQuery q = new TermQuery(t);
				return new BooleanClause(q, notCommunity ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.MUST);
			}

			return null;
		}

	};

	private final Function<PageFilter<CommunityMembersFilterDTO>, Query> filerCommunityMembersMapper = new Function<PageFilter<CommunityMembersFilterDTO>, Query>() {
		public Query apply(PageFilter<CommunityMembersFilterDTO> input) {

			final BooleanQuery communityQuery = new BooleanQuery();

			final String communityId = input.getFilter().getId();
			final String role = input.getFilter().getRole();
			final Boolean validated = input.getFilter().isValidated();

			if (communityId != null && role == null && validated == null) {
				communityQuery.add(community(communityId));
			} else {
				if (role != null) {
					communityQuery.add(role(communityId, role));
				}
				if (validated != null) {
					communityQuery.add(valid(communityId, validated));
				}
			}

			final BooleanQuery conjuntion = new BooleanQuery();
			final BooleanQuery disjuntion = new BooleanQuery();

			final MemberFilterDTO memberFilter = input.getFilter().getMemberFilter();

			if (memberFilter != null) {

				final StringFilterDTO name = memberFilter.getName();
				if (name != null) {
					if (name.isConjunction()) {
						conjuntion.add(clause(name, MemberSchema.NAME));
					} else {
						disjuntion.add(clause(name, MemberSchema.NAME));
					}
				}
				final StringFilterDTO code = memberFilter.getMemberCode();
				if (code != null) {
					if (code.isConjunction()) {
						conjuntion.add(clause(code, MemberSchema.CODE));
					} else {
						disjuntion.add(clause(code, MemberSchema.CODE));
					}
				}
				final StringFilterDTO displayName = memberFilter.getDisplayName();
				if (displayName != null) {
					if (displayName.isConjunction()) {
						conjuntion.add(parsedClause(displayName, MemberSchema.DISPLAYNAME));
					} else {
						disjuntion.add(parsedClause(displayName, MemberSchema.DISPLAYNAME));
					}
				}

				final StringFilterDTO profile = memberFilter.getProfile();
				if (profile != null) {
					if (profile.isConjunction()) {
						conjuntion.add(clause(profile, MemberSchema.PROFILE));
					} else {
						disjuntion.add(clause(profile, MemberSchema.PROFILE));
					}
				}

				final Map<String, StringFilterDTO> properties = memberFilter.getProperties();
				if (properties != null && !properties.isEmpty()) {
					for (String key : properties.keySet()) {
						final StringFilterDTO sf = properties.get(key);
						if (sf != null) {
							if (sf.isConjunction()) {
								conjuntion.add(parsedClause(sf, MemberSchema.property(key)));
							} else {
								disjuntion.add(parsedClause(sf, MemberSchema.property(key)));
							}
						}
					}
				}

			}

			final BooleanQuery query = (communityQuery.clauses() != null && !communityQuery.clauses().isEmpty()) ? communityQuery
				: new BooleanQuery();
			if (conjuntion.clauses() != null && !conjuntion.clauses().isEmpty()) {
				query.add(conjuntion, Occur.MUST);
			}
			if (disjuntion.clauses() != null && !disjuntion.clauses().isEmpty()) {
				query.add(disjuntion, Occur.MUST);
			}

			return query;
		}

		private BooleanClause community(String communityId) {
			if (communityId == null) {
				return null;
			}
			final Term t = new Term(MemberSchema.COMMUNITY, communityId);
			final TermQuery q = new TermQuery(t);
			return new BooleanClause(q, BooleanClause.Occur.MUST);
		}

		private BooleanClause valid(String communityId, Boolean validated) {
			if (communityId == null || validated == null) {
				return null;
			}

			final Term t = new Term(MemberSchema.COMMUNITYSTATUS, communityId + ":" + validated);
			final TermQuery q = new TermQuery(t);
			return new BooleanClause(q, BooleanClause.Occur.MUST);
		}

		private BooleanClause role(String communityId, String communityRole) {
			if (communityId == null || communityRole == null) {
				return null;
			}

			final Term t = new Term(MemberSchema.COMMUNITYROL, communityId + ":" + communityRole);
			final TermQuery q = new TermQuery(t);
			return new BooleanClause(q, BooleanClause.Occur.MUST);
		}

	};

	private BooleanClause parsedClause(StringFilterDTO str, String field) {
		final Query q = parsedQuery(str, field);

		return new BooleanClause(q, BooleanClause.Occur.MUST);
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
			// parser.setAllowLeadingWildcard(true);
		} else if (StringMatchMode.SUFFIX.equals(str.getMatchMode())) {
			parser = new QueryParser(Factory.get().version(), field, analyzer) {
				protected Query newTermQuery(Term term) {
					return new WildcardQuery(new Term(term.field(), "*" + term.text()));
				};
			};
			// parser.setAllowLeadingWildcard(true);
		} else { // StringMatchMode.IN
			parser = new QueryParser(Factory.get().version(), field, analyzer) {
				protected Query newTermQuery(Term term) {
					return new WildcardQuery(new Term(term.field(), "*" + term.text() + "*"));
				};
			};
			// parser.setAllowLeadingWildcard(true);
		}

		try {
			q = parser.parse(str.getValue());
		} catch (ParseException e) {
			return query(str, field);
		}

		return q;

	}

	private BooleanClause clause(StringFilterDTO str, String field) {
		final Query q = query(str, field);

		return new BooleanClause(q, BooleanClause.Occur.SHOULD);
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

	private static final Function<Page<MemberSelDTO>, PageDTO<MemberSelDTO>> pageMemberMapper = new Function<Page<MemberSelDTO>, PageDTO<MemberSelDTO>>() {
		public PageDTO<MemberSelDTO> apply(Page<MemberSelDTO> input) {
			final PageDTO<MemberSelDTO> page = new PageDTO<MemberSelDTO>();
			page.setElements(input.getItems() == null || input.getItems().isEmpty() ? Lists
				.<MemberSelDTO> newArrayList() : input.getItems());
			page.setFirst(input.getFirstResult());
			page.setSize(input.size());
			page.setTotal(input.getTotalHits());

			return page;
		}
	};

	private static final Function<Page<String>, PageDTO<String>> pageCommunityMembersMapper = new Function<Page<String>, PageDTO<String>>() {
		public PageDTO<String> apply(Page<String> input) {
			final PageDTO<String> page = new PageDTO<String>();
			page.setElements(input.getItems() == null || input.getItems().isEmpty() ? Lists.<String> newArrayList()
				: input.getItems());
			page.setFirst(input.getFirstResult());
			page.setSize(input.size());
			page.setTotal(input.getTotalHits());

			return page;
		}
	};

}
