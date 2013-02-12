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

package com.isotrol.impe3.nr.core;

import junit.framework.Assert;

import org.apache.lucene.search.Query;
import org.junit.Test;

import com.isotrol.impe3.lucene.PortalSpanishAnalyzer;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;

public class LuceneTranslatorTest {

	@Test
	public void testString() {
		NodeQuery query = NodeQueries.all(NodeQueries.string("FIELD", "con"),NodeQueries.string("FIELD", "españa"));
		
		LuceneTranslator translator = new LuceneTranslator(new PortalSpanishAnalyzer());
		
		Query lucene = translator.query(query);

		Assert.assertNotNull(lucene);
	}
	
	
}
