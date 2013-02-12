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

package com.isotrol.impe3.nr.api;


import static junit.framework.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Basic node test
 * @author Emilio Escobar Reyero
 * 
 */
public class NodeTest {

	@Test
	public void nodeTest() {
		final Node.Builder builder = Node.builder();

		final String title = "title";
		final NodeKey nodeKey = NodeKey.of(UUID.randomUUID(), "nodeid");
		final String description = "description";
		final byte[] bytes = "content content content".getBytes();
		final Calendar date = new GregorianCalendar();
		final String lang = "es";
		final Calendar expirationdate = new GregorianCalendar();
		expirationdate.roll(Calendar.YEAR, true);
		final Calendar releasedate = new GregorianCalendar();
		releasedate.roll(Calendar.YEAR, false);
		final String mime = "text/plain";

		final UUID categoryKey = UUID.randomUUID();
		final NodeKey contentKey = NodeKey.of(UUID.randomUUID(), "relatednodeid");

		builder.setTitle(title);
		builder.setNodeKey(nodeKey);
		builder.setDescription(description);
		builder.setBytes(bytes, true);
		builder.setDate(date);
		builder.addLocale(lang);
		builder.setExpirationdate(expirationdate);
		builder.setMime(mime);
		builder.setReleasedate(releasedate);

		builder.addCategory(categoryKey);
		builder.addProperty("key", "value");
		builder.addRelatedContent(contentKey);

		final Node node = builder.build();

		Assert.assertEquals(title, node.getTitle());
		Assert.assertEquals(nodeKey, node.getNodeKey());
		Assert.assertEquals(description, node.getDescription());
		Assert.assertEquals(date, node.getDate());
		assertTrue(node.getLocales().contains(new Locale(lang)));
		Assert.assertEquals(expirationdate, node.getExpirationdate());
		Assert.assertEquals(releasedate, node.getReleasedate());
		Assert.assertEquals(mime, node.getMime());

		assertTrue(node.getRelatedContent().contains(contentKey));
		assertTrue(node.getCategories().contains(categoryKey));
		assertTrue(node.getProperties().containsEntry("key", "value"));

	}

}
