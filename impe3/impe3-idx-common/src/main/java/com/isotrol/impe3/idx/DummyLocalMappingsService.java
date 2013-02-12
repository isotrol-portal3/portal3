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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import nu.xom.Document;

/**
 * Dummy implementation.
 * @author Emilio Escobar Reyero
 */
public class DummyLocalMappingsService implements LocalMappingsService  {

	private String source;
	private String environment;
	
	private Map<String, UUID> types = new HashMap<String, UUID>();
	private Map<String, UUID> categories = new HashMap<String, UUID>();
	
	/**
	 * Every time return empty set
	 */
	public Set<UUID> getCategories(String cnt, String path, Set<String> cats) {
		return getRemoteCategories(cnt, path, cats);
	}

	public Set<UUID> getCategories(String cnt, String path, Set<String> cats, Document xml) {
		return getRemoteCategories(cnt, path, cats);
	}
	
	public Set<String> getSets(String cnt, String path, Set<String> cats) {
		return new HashSet<String>();
	}

	public Set<String> getSets(String cnt, String path, Set<String> cats, Document xml) {
		return new HashSet<String>();
	}
	
	private String generateKey (String environment, String source, String key) {
		return generateCategoyKey(environment, source, key);
	}
	
	private String generateCategoyKey (String cnt, String path, String cat) {
		String a = cnt == null ? "null" : cnt;
		String b = path == null ? "null" : path;
		String c = cat == null ? "null" : cat;
		
		return a + ":" + b + ":" + c;
	}
	
	private Set<UUID> getRemoteCategories(String cnt, String path, Set<String> cats) {
		Set<UUID> uuids = new HashSet<UUID>();
		
		if (cats == null || cats.isEmpty()) {
			String key = generateCategoyKey(cnt, path, null);
			uuids.add(getCatUUID(key));
		} else {
			for (String cat : cats) {
				String key = generateCategoyKey(cnt, path, cat);
				uuids.add(getCatUUID(key));
			}
		}
		
		return uuids;
	}
	
	private UUID getCatUUID (String cat) {
		String key = generateKey(environment, source, cat);
		if (!categories.containsKey(key)) {
			categories.put(key, UUID.randomUUID());
		}
		return categories.get(key);
	}
	
	/**
	 * Every time return null;
	 */
	public UUID getContentType(String cnt) {
		String key = generateKey(environment, source, cnt);
		if (!types.containsKey(key)) {
			types.put(key, UUID.randomUUID());
		}
		return types.get(key);
	}
	
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
}
