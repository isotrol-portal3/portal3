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

package com.isotrol.impe3.idx;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.transform;
import static com.isotrol.impe3.idx.SinglePortalQueryable.DIRECTORY_PROVIDER;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.lucis.core.DirectoryProvider;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.support.Queryables;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;


/**
 * Object representing an aggregation of Port@l queryables to be used as a single node repository.
 * @author Andres Rodriguez Chamorro
 */
public final class AggregatedPortalQueryable extends BeanNamePortalQueryable {
	/** Custom name. */
	private String name = null;
	/** Aggregated indexers. */
	private final ImmutableMap<String, SinglePortalQueryable> indexers;

	public AggregatedPortalQueryable(List<SinglePortalQueryable> indexers) {
		checkNotNull(indexers, "Null indexers list provided");
		checkArgument(!indexers.isEmpty(), "No indexer provided");
		final Map<String, SinglePortalQueryable> map = Maps.newHashMap();
		for (SinglePortalQueryable indexer : indexers) {
			checkNotNull(indexer, "Null indexer provided");
			final String name = indexer.getName();
			checkNotNull(name, "Null indexer name provided");
			checkArgument(!map.containsKey(name), "Duplicate indexer [%s]", name);
			map.put(name, indexer);
		}
		this.indexers = ImmutableMap.copyOf(map);
	}

	/**
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
	 */
	public void setBeanName(String beanName) {
		this.name = beanName;
	}

	/**
	 * Returns the aggregation name.
	 * @return The aggregation name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see com.isotrol.impe3.idx.PortalQueryable#getQueryable()
	 */
	public Queryable getQueryable() {
		// TODO: fix lucis
		final Collection<DirectoryProvider> c = ImmutableList.copyOf(transform(indexers.values(),
			DIRECTORY_PROVIDER));
		final Queryable q = Queryables.multi(c);
		return q;
	}
}
