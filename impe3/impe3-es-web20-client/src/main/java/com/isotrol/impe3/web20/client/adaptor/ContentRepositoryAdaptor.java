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
package com.isotrol.impe3.web20.client.adaptor;


import java.util.Map;
import java.util.Set;

import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.support.nr.ContentRepository;
import com.isotrol.impe3.web20.client.Web20Adaptor;

/**
 * Default Web20Adaptor ContentRepository implementation.
 * @author Emilio Escobar Reyero
 */
public class ContentRepositoryAdaptor implements Web20Adaptor {

	private ContentRepository repository;
	private ContentTypes contentTypes;
	
	private ContentRepositoryAdaptorConfiguration config;
	
	
	/**
	 * @param resource Node key
	 * @see com.isotrol.impe3.web20.client.Web20Adaptor#getResource(java.lang.String)
	 */
	public Content getResource(String resource) {
		final NodeQuery query = getResourceQuery(resource); 
		
		if (query == null) {
			return null;
		}
		
		final Item<Content> item = repository.getFirst(query, null, bytes());
		
		return item == null ? null : item.getItem();
	}
	
	private boolean bytes() {
		return config.bytes() == null ? false : config.bytes();
	}
	
	private NodeQuery getResourceQuery(String resource) {
		if (resource == null) {
			return null;
		}
		
		final NodeKey key = NodeKey.of(resource);
		final ContentType contentType = contentTypes.get(key.getNodeType());

		if (contentType == null) {
			return null;
		}
		
		return repository.contentKey(ContentKey.of(contentType, key.getNodeId()));
	}
	
	/**
	 * @param resources Node key set.
	 * @see com.isotrol.impe3.web20.client.Web20Adaptor#getResources(java.util.Set)
	 */
	public Map<String, Content> getResources(Set<String> resources) {
		if (resources == null) {
			return Maps.newHashMap();
		}
		
		final Set<NodeQuery> queries = Sets.newHashSet();
		
		for (String resource : resources) {
			NodeQuery q = getResourceQuery(resource);
			if (q != null) {
				queries.add(q);
			}
		}
		
		final NodeQuery query = NodeQueries.any(queries); 
		final Page<Content> page = repository.getPage(query, null, bytes(), 0, resources.size());

		if (page == null) {
			return Maps.newHashMap();
		}
		
		final Map<String, Content> contents = Maps.newHashMap();
		
		for (Content c : page.getItems()) {
			contents.put(c.getContentId(), c);
		}
		
		return contents;
	}
	
	/**
	 * 
	 * @see com.isotrol.impe3.web20.client.Web20Adaptor#isResourceInCommunity(java.lang.String, java.lang.String)
	 */
	public boolean isResourceInCommunity(String resource, String communityId) {
		if (config.communityIdField() == null) {
			return false;
		}
		if (communityId == null) {
			return false;
		}
		
		final NodeQuery rq = getResourceQuery(resource);
		
		if (rq == null) {
			return false;
		}
			
		
		final NodeQuery cq = NodeQueries.term(config.communityIdField(), communityId);
		
		final Item<Content> item = repository.getFirst(NodeQueries.all(rq, cq), null, false);
		
		
		return item == null ? false : item.getTotalHits() > 0;
	}
	
	@Inject
	public void setContentTypes(ContentTypes contentTypes) {
		this.contentTypes = contentTypes;
	}
	
	public void setRepository(ContentRepository repository) {
		this.repository = repository;
	}
	
	public void setConfig(ContentRepositoryAdaptorConfiguration config) {
		this.config = config;
	}
}
