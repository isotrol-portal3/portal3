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
package com.isotrol.impe3.web20.client.content.counter;


import java.util.List;
import java.util.Map;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.support.listing.ContentListingPage;
import com.isotrol.impe3.web20.api.CounterFilterDTO;
import com.isotrol.impe3.web20.api.CountersService;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;


/**
 * Greatest hits component.
 * @author Andres Rodriguez
 */
public class GreatestHitsComponent implements Component {
	/** Module configuration. */
	private ContentCounterConfig config;
	/** Counters service. */
	private CountersService service;
	/** Content types. */
	private ContentTypes contentTypes;
	/** Content loader. */
	private ContentLoader loader;
	/** Listing. */
	private Listing<Content> listing;

	/** Default constructor. */
	public GreatestHitsComponent() {
	}

	public void setConfig(ContentCounterConfig config) {
		this.config = config;
	}

	public void setService(CountersService service) {
		this.service = service;
	}

	public ComponentResponse execute() {
		final Pagination pag = new Pagination(config.pageSize(), null);
		listing = ContentListingPage.empty(pag);
		// Fetch resources
		final CounterFilterDTO filter = new CounterFilterDTO();
		filter.setCounterType(config.counterType());
		List<ResourceCounterDTO> resources;
		try {
			resources = service.getGreatestHits("", filter, Math.max(1, config.pageSize()));
		} catch (ServiceException e) {
			// TODO log
			return ComponentResponse.OK;
		}
		if (resources == null || resources.isEmpty()) {
			// Nothing to do
			return ComponentResponse.OK;
		}
		// Turn resources into keys
		final List<ContentKey> keys = Lists.newArrayListWithExpectedSize(resources.size());
		for (ResourceCounterDTO resource : Iterables.filter(resources, Predicates.notNull())) {
			final ContentKey ck = r2ck(resource.getResource());
			if (ck != null) {
				keys.add(ck);
			}
		}
		if (keys.isEmpty()) {
			// Nothing to do
			return ComponentResponse.OK;
		}
		// Perform the search
		ContentCriteria cc = loader.newCriteria();
		cc.setBytes(true);
		Map<ContentKey, Content> map = loader.newCriteria().setBytes(true).getContents(keys);
		if (map.isEmpty()) {
			// Nothing found
			return ComponentResponse.OK;
		}
		// Turn to listing
		final List<Content> contents = Lists.newArrayListWithExpectedSize(keys.size());
		for (ContentKey ck : keys) {
			final Content c = map.get(ck);
			if (c != null) {
				contents.add(c);
			}
		}
		listing = new ContentListingPage(null, pag, contents);
		return ComponentResponse.OK;
	}

	private ContentKey r2ck(String r) {
		if (r == null) {
			return null;
		}
		try {
			NodeKey nk = NodeKey.of(r);
			if (!contentTypes.containsKey(nk.getNodeType())) {
				return null;
			}
			final ContentType ct = contentTypes.get(nk.getNodeType());
			return ContentKey.of(ct, nk.getNodeId());
		} catch (Exception e) {
			return null;
		}
	}

	@Inject
	public void setLoader(ContentLoader loader) {
		this.loader = loader;
	}

	@Inject
	public void setContentTypes(ContentTypes contentTypes) {
		this.contentTypes = contentTypes;
	}

	@Extract
	public Listing<Content> getListing() {
		return listing;
	}
}
