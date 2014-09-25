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

package com.isotrol.impe3.mappings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import nu.xom.Document;

/**
 * Mapping term created from some MappingDTO
 * @author Emilio Escobar Reyero
 */
public class MappingTerm {

	private final String id;
	private final List<Criteria> criteria;
	
	/**
	 * just for testing
	 */
	MappingTerm() {
		id = null;
		criteria = Collections.emptyList();
	}
	
	/**
	 * Create a new mapping term from a single line of mapping string
	 * @param id unique id
	 * @param mapping mapping string line
	 */
	public MappingTerm(final String id, final String mapping) {
		List<Criteria> cri = new ArrayList<Criteria>(1);
		cri.add(parseCriteria(mapping));
		
		this.id = isUUID(id) ? id.toString().toLowerCase() : id;
		this.criteria = cri;
	}
	
	/**
	 * Constructor. Create a new mapping term from uuid and list of mapping strings
	 * @param id unique id
	 * @param mappings list of mappings
	 */
	public MappingTerm(final String id, final List<String> mappings) {
		this.id = isUUID(id) ? id.toString().toLowerCase() : id;
		this.criteria = parseMappings(mappings);
	}
	
	final List<Criteria> parseMappings(final List<String> mappings) {
		List<Criteria> cri = new ArrayList<Criteria>(mappings.size());
		
		for (String mapping : mappings) {
			cri.add(parseCriteria(mapping));
		}
		
		return cri;
	}
	
	final Criteria parseCriteria(final String mapping) {
		// cnt:contenido1 AND cat:89 OR pth:/gege/geo
		Criteria cri = null; 		
		
		String[] pieces = mapping.split(" ");
		int size = pieces.length;
		if (size > 0) {
			String typ = pieces[0].substring(0, 3);
			String cnd = formatSpace(pieces[0].substring(4));
			
			MappedType type = MappedType.getMT(typ);
			
			cri = Criteria.simple(type, cnd); 
		}
	
		for (int i = 1; i < size -1; i = i+2) {
			String opt = pieces[i]; 
			String typ = pieces[i+1].substring(0, 3);
			String cnd = formatSpace(pieces[i+1].substring(4));
			
			MappedType type = MappedType.getMT(typ);
			
			if ("AND".equals(opt)) {
				cri = cri.and(type, cnd);
			} else if ("OR".equals(opt)) {
				cri = cri.or(type, cnd);
			} else {
				cri = cri.or(type, cnd);
			}
		}
		
		return cri;
	}
	
	private String formatSpace(final String str) {
		if (str == null) {
			return null;
		}
		
		return str.replaceAll("%20", " ");
	}
	
	/**
	 * Return uuid if content properties matches with mappings list.
	 * @param cnt local content type
	 * @param path local content path
	 * @param cats local categories set
	 * @param xml local xml
	 * @return null if not matches or uuid if matches.
	 */
	public Object contains(final String cnt, final String path, final Set<String> cats, final Document xml) {
		boolean ok = false;
		
		Iterator<Criteria> it = criteria.iterator();
		
		while (!ok && it.hasNext()) {
			ok = it.next().evaluate(cnt, path, cats, xml);
		}
	
		if (!ok) {
			return null;
		}
		
		return isUUID() ? UUID.fromString(id) : id;
	}
	
	public UUID containsUUID(final String cnt, final String path, final Set<String> cats, final Document xml) {
		boolean ok = false;
		
		Iterator<Criteria> it = criteria.iterator();
		
		while (!ok && it.hasNext()) {
			ok = it.next().evaluate(cnt, path, cats, xml);
		}
		
		return ok ? UUID.fromString(id) : null;
	}

	public String containsString(final String cnt, final String path, final Set<String> cats, final Document xml) {
		boolean ok = false;
		
		Iterator<Criteria> it = criteria.iterator();
		
		while (!ok && it.hasNext()) {
			ok = it.next().evaluate(cnt, path, cats, xml);
		}
		
		return ok ? id : null;
	}
	
	private boolean isUUID() {
		return isUUID(id);
	}
	
	private boolean isUUID(String uuid) {
		try {
			UUID.fromString(uuid);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
