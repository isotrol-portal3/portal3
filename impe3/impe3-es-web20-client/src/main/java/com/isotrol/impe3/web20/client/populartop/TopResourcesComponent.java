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
package com.isotrol.impe3.web20.client.populartop;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.derquinse.lucis.Page;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.InjectLocal;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.support.WithPageSize;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.palette.content.AbstractListingComponent;
import com.isotrol.impe3.web20.api.CounterFilterDTO;
import com.isotrol.impe3.web20.api.CountersService;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;
import com.isotrol.impe3.web20.client.Web20Adaptor;


/**
 * Top resources component.
 * @author Emilio Escobar Reyero
 */
public class TopResourcesComponent extends AbstractListingComponent<Content> {

	/** Repository adaptor. */
	private Web20Adaptor repository;

	/** Counters service. */
	private CountersService counters;

	/** Default module configuration. */
	private TopResourcesConfig moduleConfig;

	/** Configuration. */
	private TopResourcesConfig config;

	
	/** Aggregation from other component. */
	private String aggregation;
	/** Community id from other component. */
	private String communityId;

	public ComponentResponse execute() {
		final CounterFilterDTO filter = protoFilter();
		if (aggregation != null) {
			filter.setAggregation(aggregation);
		}
		if (communityId != null) {
			filter.setCommunityId(communityId);
		} else {
			if (useDefaultCommunity()) {
				filter.setCommunityId(new UUID(0L, 0L).toString());
			}
		}

		try {
			final List<ResourceCounterDTO> results = counters.getGreatestHits("", filter, getMax());
			final Set<String> resources = Sets.newHashSet();

			for (ResourceCounterDTO dto : results) {
				resources.add(dto.getResource());
			}

			long s0 = System.currentTimeMillis();
			final Map<String, Content> translated = repository.getResources(resources);
			long s1 = System.currentTimeMillis();
			
			long time = s1 - s0;
			
			//final Pagination p = loadPagination(translated.size());
			
			final List<Content> contents = Lists.newArrayListWithCapacity(translated.size());

			
			for (ResourceCounterDTO dto : results) {
				Content c = translated.get(dto.getResource());
				
				c.getLocalValues().put("WEB20_COUNTER", dto.getCount());
				
				contents.add(c);
			}
			final Page<Content> page = new Page<Content>(translated.size(), 1, time, 0, contents);

			setPage(page);
			
		} catch (ServiceException e) {
			setPage(new Page<Content>(0, 1, 0, 0, Lists.<Content>newArrayList()));
		}

		return ComponentResponse.OK;
	}

	@Override
	protected WithPageSize getConfiguration() {
		return config.pageSize() != null ? config : moduleConfig;
	}
	
	private int getMax() {
		Integer max = config.pageSize();
		if (max == null) {
			max = moduleConfig.pageSize();
			if (max == null) {
				return 10;
			}
		}
		return max;
	}

	private CounterFilterDTO protoFilter() {
		final String counterType = config.counterType() != null ? config.counterType() : moduleConfig.counterType();
		final CounterFilterDTO filter = new CounterFilterDTO();
		filter.setCounterType(counterType);
		return filter;
	}

	private boolean useDefaultCommunity() {
		boolean useDefaultCommunity = false;

		if (config.useDefaultCommunity() == null) {
			useDefaultCommunity = moduleConfig.useDefaultCommunity() == null ? false : moduleConfig
				.useDefaultCommunity();
		} else {
			useDefaultCommunity = config.useDefaultCommunity();
		}

		return useDefaultCommunity;
	}

	public void setRepository(Web20Adaptor repository) {
		this.repository = repository;
	}

	public void setCounters(CountersService counters) {
		this.counters = counters;
	}

	public void setModuleConfig(TopResourcesConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}

	@Inject
	public void setConfig(TopResourcesConfig config) {
		this.config = config;
	}

	@InjectLocal("web20-top-aggregation")
	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	@InjectLocal("web20-top-communityId")
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

}
