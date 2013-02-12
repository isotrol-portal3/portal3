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

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.sf.derquinse.lucis.Result;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.nr.api.EmptyNodeRepository;
import com.isotrol.impe3.nr.api.NodeRepositoriesService;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.NodeRepositoryRequest;
import com.isotrol.impe3.nr.core.NodeRepositoryImpl;


/**
 * Default implementation for a nodes repositories service.
 * @author Andres Rodriguez Chamorro
 */
public final class DefaultNodeRepositoriesService implements NodeRepositoriesService {
	private static final NodeRepository EMPTY = new EmptyNodeRepository();

	/** Repositories. */
	private final ImmutableMap<String, NodeRepository> repositories;
	/** Descriptions. */
	private final ImmutableMap<String, String> descriptions;
	/** Repository function. */
	private final Function<String, NodeRepository> repoF;

	public DefaultNodeRepositoriesService(@Nullable List<SinglePortalQueryable> singles,
		@Nullable List<AggregatedPortalQueryable> aggregated) {
		if (singles == null) {
			singles = ImmutableList.of();
		}
		if (aggregated == null) {
			aggregated = ImmutableList.of();
		}
		if (singles.isEmpty() && aggregated.isEmpty()) {
			this.repositories = ImmutableMap.of();
			this.descriptions = ImmutableMap.of();
			this.repoF = new Function<String, NodeRepository>() {
				public NodeRepository apply(String input) {
					return EMPTY;
				}
			};
		} else {
			final Map<String, NodeRepository> map = Maps.newHashMap();
			final Map<String, String> desc = Maps.newHashMap();
			for (SinglePortalQueryable indexer : singles) {
				checkNotNull(indexer, "Null single indexer provided");
				final String name = indexer.getName();
				checkNotNull(name, "Null single indexer name provided");
				checkArgument(!map.containsKey(name), "Duplicate indexer [%s]", name);
				final NodeRepository nr = new NodeRepositoryImpl(indexer.getQueryable());
				map.put(name, nr);
				desc.put(name, indexer.getDescription());
			}
			for (AggregatedPortalQueryable indexer : aggregated) {
				checkNotNull(indexer, "Null aggregated indexer provided");
				final String name = indexer.getName();
				checkNotNull(name, "Null aggregated indexer name provided");
				checkArgument(!map.containsKey(name), "Duplicate indexer [%s]", name);
				final NodeRepository nr = new NodeRepositoryImpl(indexer.getQueryable());
				map.put(name, nr);
				desc.put(name, indexer.getDescription());
			}
			this.repositories = ImmutableMap.copyOf(map);
			this.descriptions = ImmutableMap.copyOf(desc);
			this.repoF = Functions.forMap(repositories, EMPTY);
		}

	}

	public Map<String, String> getRepositories() {
		return Maps.newHashMap(descriptions);
	}

	public Result execute(String repositoryKey, NodeRepositoryRequest request) {
		checkNotNull(repositoryKey, "Repository key");
		checkNotNull(request, "Repository request");
		return request.execute(repoF.apply(repositoryKey));
	}

}
