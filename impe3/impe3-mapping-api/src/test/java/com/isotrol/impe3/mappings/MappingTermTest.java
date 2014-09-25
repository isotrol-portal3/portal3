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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import nu.xom.Document;
import nu.xom.Element;

import org.junit.Assert;
import org.junit.Test;

/**
 * MApping test 
 * @author Emilio Escobar Reyero
 */
public class MappingTermTest {

	@Test
	public void parseCriteriaTest() {
		final Criteria criteria = createCriteria("cnt:contenido1 AND cat:/h1/h1.1/h1.1.1/ OR pth:/gege/geo"); 
		Assert.assertNotNull(criteria);
		
	}

	@Test
	public void containsCNTTest() {
		final UUID uuid = UUID.randomUUID();
		final List<String> list = new LinkedList<String>();
		list.add("cnt:contenido1 OR cnt:contenido2");
		
		final MappingTerm mappingTerm = new MappingTerm(uuid.toString(), list);
		
		final UUID idok = mappingTerm.containsUUID("contenido2", null, null, emptyXml());
		
		Assert.assertNotNull(idok);
		Assert.assertEquals(uuid, idok);
		
		final UUID idko = mappingTerm.containsUUID("contenido3", null, null, emptyXml());
		Assert.assertNull(idko);
	}
	
	@Test
	public void containsCATTest() {
		final UUID uuid = UUID.randomUUID();
		final List<String> list = new LinkedList<String>();
		list.add("cat:^/h1/(.*)");
		
		final MappingTerm mappingTerm = new MappingTerm(uuid.toString(), list);
		
		final Set<String> cats = new HashSet<String>();
		cats.add("/h2/h3");
		cats.add("/h1");

		final UUID idko = mappingTerm.containsUUID(null, null, cats, emptyXml());
		Assert.assertNull(idko);

		cats.add("/h1/h1.1");
		
		final UUID idok = mappingTerm.containsUUID(null, null, cats, emptyXml());
		
		Assert.assertNotNull(idok);
		Assert.assertEquals(uuid, idok);
		
	}

	@Test
	public void containsPTHTest() {
		final UUID uuid = UUID.randomUUID();
		final List<String> list = new LinkedList<String>();
		list.add("pth:^/path(/?)$");
		
		final MappingTerm mappingTerm = new MappingTerm(uuid.toString(), list);
		
		final UUID idok = mappingTerm.containsUUID(null, "/path", null, emptyXml());
		
		Assert.assertNotNull(idok);
		Assert.assertEquals(uuid, idok);
		
		final UUID idko = mappingTerm.containsUUID(null, "/other/path/", null, emptyXml());
		Assert.assertNull(idko);
	}

	@Test
	public void containsAllTest() {
		final UUID uuid = UUID.randomUUID();
		final List<String> list = new LinkedList<String>();
		list.add("pth:^/path(/?)$");
		list.add("cat:^/h1/(.*)");
		list.add("cnt:contenido1 OR cnt:contenido2");
		
		final MappingTerm mappingTerm = new MappingTerm(uuid.toString(), list);
		
		final Set<String> cats = new HashSet<String>();
		cats.add("/h2/h3");
		cats.add("/h1");
		
		final UUID pthOk = mappingTerm.containsUUID("contenido1", "/path", cats, emptyXml());
		Assert.assertNotNull(pthOk);
		Assert.assertEquals(uuid, pthOk);
		
		final UUID pthKo = mappingTerm.containsUUID("contenido3", "/other/path", cats, emptyXml());
		Assert.assertNull(pthKo);
		
	}
	
	private Criteria createCriteria(String mapped) {
		final MappingTerm mappingTerm = new MappingTerm();
		return mappingTerm.parseCriteria(mapped); 
	}
	
	private Document emptyXml() {
		return new Document(new Element("xml"));
	}
	
}
