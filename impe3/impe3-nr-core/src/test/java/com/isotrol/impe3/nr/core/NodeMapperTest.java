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


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.apache.lucene.document.Document;
import org.junit.Assert;
import org.junit.Test;

import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.Schema;


/**
 * Very simple nodemapper test
 * @author Emilio Escobar Reyero
 */
public class NodeMapperTest {

	private final static UUID categoryKey = UUID.randomUUID();
	private final static UUID nodeType = UUID.randomUUID();
	private final static String nodeId = "123456789";
	private final static NodeKey nodeKey = NodeKey.of(nodeType, nodeId);
	private final static String title = "Title";
	private final static String description = "Description";
	private final static String lang = "es_ES";
	private final static Calendar date = new GregorianCalendar();

	private Document buildDocument() {
		DocumentBuilder builder = new DocumentBuilder();

		builder.setNodeKey(nodeKey).setTitle(title).setDescription(description).addLocale(lang).setDate(date)
			.addCategory(categoryKey);

		return builder.get();
	}

	@Test
	public void buildDocumentTest() {
		Document doc = buildDocument();

		Assert.assertNotNull(doc);
		Assert.assertNotNull(doc.get(Schema.TITLE));
		Assert.assertEquals(title, doc.get(Schema.TITLE));
	}

	@Test
	public void nodeMapperTest() {
		Document doc = buildDocument();

		NodeMapper mapper = new NodeMapper(false);

		Node node = mapper.map(1, 1, doc, null);

		Assert.assertNotNull(node);
		Assert.assertEquals(doc.get(Schema.TITLE), node.getTitle());
	}

}
