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

package com.isotrol.impe3.support.nr;


import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.content.Content;


public class ContentBuilderTest {

	private final static String contentId = UUID.randomUUID().toString().toLowerCase();
	private final static ContentType contentType = ContentType.builder().setId(UUID.randomUUID()).setDefaultName(
		Name.of("Tipo", "tipo")).setNavigable(true).setRoutable(true).get();
	private final static Long date = System.currentTimeMillis();
	private final static String description = "description";
	private final static Long expirationdate = System.currentTimeMillis() + 10000L;
	private final static Locale lang = new Locale("es");
	private final static String mime = "plain/text";
	private final static Long releasedate = System.currentTimeMillis() - 10000L;
	private final static String title = "title";

	private final static Category category = Category.builder().setId(UUID.randomUUID()).setDefaultName(
		Name.of("Categoría", "categoria")).setVisible(true).setRoutable(true).get();

	private final static byte[] bytes = "bytes".getBytes();

	private Content content() {
		final ContentBuilder builder = new ContentBuilder();

		builder.setContentId(contentId);
		builder.setContentType(contentType);
		builder.setDate(date);
		builder.setDescription(description);
		builder.setExpirationdate(expirationdate);
		builder.addLocale(lang);
		builder.setMime(mime);
		builder.setReleasedate(releasedate);
		builder.setTitle(title);

		builder.addCategory(category);
		builder.addRelated(ContentKey.of(contentType, UUID.randomUUID().toString().toLowerCase()));

		builder.putProperty("property_name", "property_value");
		builder.putLocalValue("local_name", "local_value");
		builder.putHighlighted("highlighted_name", "highlighted_value");

		builder.setContent(bytes);

		return builder.get();
	}

	@Test
	public void contentTest() {
		final Content c = content();

		assertEquals(contentId, c.getContentId());
		assertEquals(contentType, c.getContentType());

		assertEquals(ContentKey.of(contentType, contentId), c.getContentKey());

		assertEquals(date, Long.valueOf(c.getDate().getTimeInMillis()));
		assertEquals(description, c.getDescription());
		assertEquals(expirationdate, Long.valueOf(c.getExpirationDate().getTimeInMillis()));
		assertEquals(lang, c.getLocales().iterator().next());
		assertEquals(mime, c.getMime());
		assertEquals(releasedate, Long.valueOf(c.getReleaseDate().getTimeInMillis()));
		assertEquals(title, c.getTitle());
	}

}
