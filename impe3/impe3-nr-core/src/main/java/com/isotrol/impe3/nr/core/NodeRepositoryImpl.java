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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.derquinse.lucis.Group;
import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;
import net.sf.derquinse.lucis.Result;
import net.sf.lucis.core.DocMapper;
import net.sf.lucis.core.Highlight;
import net.sf.lucis.core.LucisQuery;
import net.sf.lucis.core.Queryable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import com.isotrol.impe3.lucene.PortalStandardAnalyzer;
import com.isotrol.impe3.nr.api.EmptyNodeRepository;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.NodeSort;


/**
 * NodeRepository implementation.
 * @author Emilio Escobar Reyero
 * 
 */
public class NodeRepositoryImpl implements NodeRepository {
	/** Queryable source. */
	private final Queryable queryable;
	private final LuceneTranslator translator;
	private final Analyzer analyzer;
	private final Formatter formatter;

	/**
	 * Constructor.
	 * @param queryable Queryable source.
	 */
	public NodeRepositoryImpl(final Queryable queryable) {
		this(queryable, new PortalStandardAnalyzer());
	}

	public NodeRepositoryImpl(final Queryable queryable, final Analyzer analyzer) {
		this(queryable, analyzer, new SimpleHTMLFormatter());
	}

	/**
	 * Constructor.
	 * @param queryable Queryable source.
	 */
	public NodeRepositoryImpl(final Queryable queryable, final Analyzer analyzer, final Formatter formatter) {
		checkNotNull(queryable);
		checkNotNull(analyzer);
		checkNotNull(formatter);
		this.queryable = queryable;
		this.analyzer = analyzer;
		this.formatter = formatter;
		this.translator = new LuceneTranslator(analyzer);
	}

	public Page<Node> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult,
		int pageSize, Map<String, Integer> highlight) {
		if (NodeFilter.isEmpty(filter)) {
			return EmptyNodeRepository.get().getPage(query, filter, sort, bytes, firstResult, pageSize, highlight);
		}
		DocMapper<Node> mapper = new NodeMapper(bytes);
		Sort s = translator.sort(sort);
		Query q = translator.query(query);
		LucisQuery<Page<Node>> lq = LucisQuery.page(q, translator.getFilter(filter), s, mapper, firstResult, pageSize,
			Highlight.of(analyzer, formatter, highlight != null ? highlight : new HashMap<String, Integer>()));
		return queryable.query(lq);
	}

	public Item<Node> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes,
		Map<String, Integer> highlight) {
		if (NodeFilter.isEmpty(filter)) {
			return EmptyNodeRepository.get().getFirst(query, filter, sort, bytes, highlight);
		}
		DocMapper<Node> mapper = new NodeMapper(bytes);
		Sort s = translator.sort(sort);
		Query q = translator.query(query);
		LucisQuery<Item<Node>> lq = LucisQuery.first(q, translator.getFilter(filter), s, mapper,
			Highlight.of(analyzer, formatter, highlight != null ? highlight : new HashMap<String, Integer>()));
		return queryable.query(lq);
	}

	public Result count(NodeQuery query, NodeFilter filter) {
		if (NodeFilter.isEmpty(filter)) {
			return EmptyNodeRepository.get().count(query, filter);
		}
		Query q = translator.query(query);
		LucisQuery<Result> lq = LucisQuery.count(q, translator.getFilter(filter));
		return queryable.query(lq);
	}

	public GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields) {
		if (NodeFilter.isEmpty(filter)) {
			return EmptyNodeRepository.get().groupBy(query, filter, fields);
		}
		if (filter != null && filter.isEmpty()) {
			return new GroupResult(new Group(), 0.0f, 0L);
		}
		Query q = translator.query(query);
		LucisQuery<GroupResult> lq = LucisQuery.group(q, translator.getFilter(filter), fields);
		return queryable.query(lq);
	}
}
