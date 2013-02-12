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

package com.isotrol.impe3.idx.config;


import java.util.Collection;
import java.util.Set;

import net.sf.lucis.core.DirectoryProvider;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.support.Queryables;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Formatter;
import org.springframework.beans.factory.FactoryBean;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.isotrol.impe3.nr.api.EmptyNodeRepository;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.core.NodeRepositoryImpl;


/**
 * Node repository searcher factory bean.
 * @author Andres Rodriguez Chamorro
 */
public class NodeSearcherFactoryBean implements FactoryBean<NodeRepository> {
	/** Analyzer. */
	private Analyzer analyzer = null;
	/** Formatter. */
	private Formatter formatter = null;
	/** Directories. */
	private Collection<DirectoryProvider> providers = null;
	/** Managed. */
	private boolean managed = false;
	/** Node repository. */
	private volatile NodeRepository repository = null;

	public NodeSearcherFactoryBean() {
	}

	public final void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public final void setFormatter(Formatter formatter) {
		this.formatter = formatter;
	}

	public final void setProviders(Collection<DirectoryProvider> providers) {
		this.providers = providers;
	}

	public final void setManaged(boolean managed) {
		this.managed = managed;
	}

	public final boolean isSingleton() {
		return true;
	}

	public final Class<?> getObjectType() {
		return NodeRepository.class;
	}

	public NodeRepository getObject() throws Exception {
		if (repository == null) {
			Queryable q = getQueryable();
			if (q != null) {
				if (analyzer != null && formatter != null) {
					repository = new NodeRepositoryImpl(q, analyzer, formatter);
				} else if (analyzer != null) {
					repository = new NodeRepositoryImpl(q, analyzer);
				} else {
					repository = new NodeRepositoryImpl(q);
				}
			} else {
				repository = new EmptyNodeRepository();
			}
		}
		return repository;
	}

	private Queryable getQueryable() {
		if (providers == null || providers.isEmpty()) {
			return null;
		}
		Set<DirectoryProvider> set = Sets.newHashSet(Iterables.filter(providers, Predicates.notNull()));
		if (set.isEmpty()) {
			return null;
		}
		if (set.size() == 1) {
			DirectoryProvider p = set.iterator().next();
			return managed ? Queryables.managed(p) : Queryables.simple(p);
		}
		return managed ? Queryables.managed(set) : Queryables.multi(set);
	}

}
