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
package com.isotrol.impe3.oi.nr;


import java.util.List;
import java.util.Map;

import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;
import net.sf.lucis.core.LucisQuery;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.support.Queryables;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.lucene.PortalStandardAnalyzer;
import com.isotrol.impe3.mappings.MappingsService;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.NodeSort;
import com.isotrol.impe3.oi.api.InterviewsService;


/**
 * Online Interview Node repository implementation. Its translate mappings by demand, not on index generation.
 * @author Emilio Escobar Reyero
 */
public class OiNodeRepository implements NodeRepository, InitializingBean {

	/** Local mappings service. */
	private MappingsService mappingsService;

	/** Interviews Service. */
	private InterviewsService interviewsService;

	/** Store */
	private Store<Long> store;
	/** Analyzer. */
	private Analyzer analyzer;

	/** Queryable. */
	private Queryable queryable;

	/** Highlight formatter. */
	private Formatter formatter;

	private QueryTranslator translator;

	/** Constructor */
	public OiNodeRepository() {
	}

	/**
	 * Creates a new queryable from store
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Preconditions.checkNotNull(store, "A valid store required!");
		queryable = Queryables.simple(store);
		if (analyzer == null) {
			analyzer = new PortalStandardAnalyzer();
		}
		if (formatter == null) {
			formatter = new SimpleHTMLFormatter();
		}
		// translator = new QueryTranslator(analyzer);
		// BooleanQuery.setMaxClauseCount(maxClauseCount);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.nr.api.NodeRepository#count(com.isotrol.impe3.nr.api.NodeQuery,
	 * com.isotrol.impe3.nr.api.NodeFilter)
	 */
	public Result count(NodeQuery query, NodeFilter filter) {
		if (filter != null && filter.isEmpty()) {
			return new Result(0, 0.0f, 0L);
		}
		Query q = translator.query(query, filter);
		LucisQuery<Result> lq = LucisQuery.count(q, null);
		return queryable.query(lq);
	}

	public Item<Node> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		Map<String, Integer> highlight) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Node> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult,
		int pageSize, Map<String, Integer> highlight) {
		// TODO Auto-generated method stub
		return null;
	}

	public GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMappingsService(MappingsService mappingsService) {
		this.mappingsService = mappingsService;
	}

	public void setInterviewsService(InterviewsService interviewsService) {
		this.interviewsService = interviewsService;
	}

	public void setStore(Store<Long> store) {
		this.store = store;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public void setFormatter(Formatter formatter) {
		this.formatter = formatter;
	}

}
