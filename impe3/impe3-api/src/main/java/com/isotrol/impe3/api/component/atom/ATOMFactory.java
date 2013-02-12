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

package com.isotrol.impe3.api.component.atom;


import java.io.IOException;
import java.net.URI;

import net.sf.derquinsej.io.Streams;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.content.Content;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndImage;
import com.sun.syndication.feed.synd.SyndImageImpl;
import com.sun.syndication.feed.synd.SyndPerson;
import com.sun.syndication.feed.synd.SyndPersonImpl;


/**
 * Basic entry factory.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public final class ATOMFactory {

	public static SyndFeed createFeed(Device device) {
		final SyndFeed feed = new SyndFeedImpl();

		feed.setFeedType(ATOMFeedType.ATOM_10.getFeedType());
		feed.setEncoding("UTF-8");

		return feed;
	}

	public static EntryBuilder entryBuilder() {
		return new EntryBuilder();
	}

	public static SyndEntry entry(Content content, URI link, EntryTransformConfig config) {
		final EntryBuilder builder = entryBuilder();
		// Basic metadata
		builder.setLink(link.toASCIIString());
		builder.setTitle(content(content.getTitle()));
		builder.setDescription(content(content.getDescription()));
		builder.setPublishedDate(content.getDate() == null ? null : content.getDate().getTime());
		// Categories
		if (config != null && config.includeCategories() && content.getCategories() != null
			&& !content.getCategories().isEmpty()) {
			for (Category c : content.getCategories()) {
				builder.addCategory(category(c));
			}
		}
		// Bytes
		if (config != null && config.includeBytes()) {
			try {
				builder.addContent(content(new String(Streams.consume(content.getContent(), true)), content.getMime(),
					null));
			} catch (IOException e) {}
		}
		return builder.build();
	}

	public static SyndCategory category(Category category) {
		final SyndCategory c = new SyndCategoryImpl();
		c.setName(category.getDefaultName().getDisplayName());
		return c;
	}

	public static SyndImage image(String url, String title, String description, String link) {
		final SyndImage i = new SyndImageImpl();

		i.setTitle(title);
		i.setDescription(description);
		i.setUrl(url);
		i.setLink(link);

		return i;
	}

	public static SyndCategory category(String name, String taxonomyUri) {
		final SyndCategory c = new SyndCategoryImpl();
		c.setName(name);
		c.setTaxonomyUri(taxonomyUri);
		return c;
	}

	public static SyndEnclosure enclosure(String url, String type, long length) {
		final SyndEnclosure enclosure = new SyndEnclosureImpl();
		enclosure.setUrl(url);
		enclosure.setType(type);
		enclosure.setLength(length);
		return enclosure;
	}

	public static SyndPerson person(String name, String email, String uri) {
		final SyndPerson p = new SyndPersonImpl();

		p.setName(name);
		p.setEmail(email);
		p.setUri(uri);

		return p;
	}

	public static SyndContent content(String value) {
		return content(value, "text/plain", null);
	}

	public static SyndContent content(String value, String mime, String mode) {
		final SyndContent c = new SyndContentImpl();
		c.setValue(value);
		c.setType(mime);
		c.setMode(mode);
		return c;
	}

}
