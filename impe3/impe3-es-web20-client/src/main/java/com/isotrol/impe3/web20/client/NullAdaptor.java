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
package com.isotrol.impe3.web20.client;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.web20.client.Web20Adaptor;

/**
 * Default service implementation. Only for connector instanciation. 
 * @author Emilio Escobar Reyero
 */
public class NullAdaptor implements Web20Adaptor {

	/**
	 * Return null.
	 * @see com.isotrol.impe3.web20.client.Web20Adaptor#getResource(java.lang.String)
	 */
	public Content getResource(String resource) {
		return null;
	}
	
	/**
	 * Return empty map.
	 * @see com.isotrol.impe3.web20.client.Web20Adaptor#getResources(java.util.Set)
	 */
	public Map<String, Content> getResources(Set<String> resources) {
		return Maps.newHashMap();
	}
	
	/**
	 * Return false.
	 * @see com.isotrol.impe3.web20.client.Web20Adaptor#isResourceInCommunity(java.lang.String, java.lang.String)
	 */
	public boolean isResourceInCommunity(String resource, String communityId) {
		return false;
	}
	
}
