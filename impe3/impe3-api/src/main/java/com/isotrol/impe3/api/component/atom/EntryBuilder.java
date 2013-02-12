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

import java.util.Date;
import java.util.List;

import net.sf.derquinsej.This;

import com.google.common.collect.Lists;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndPerson;

/**
 * SyndEntry builder.
 * 
 * @author Emilio Escobar Reyero
 */
public class EntryBuilder extends This<EntryBuilder> {

	private String uri;
	private SyndContent title;
	private String link;
	private List<String> links = Lists.newArrayList();
	private SyndContent description;
	private List<SyndContent> contents = Lists.newArrayList();
	private List<SyndEnclosure> enclosures = Lists.newArrayList();
	private Date publishedDate;
	private Date updatedDate;
	private String author;
	private List<SyndPerson> authors = Lists.newArrayList();
	private List<SyndPerson> contributors = Lists.newArrayList();
	private List<SyndCategory> categories = Lists.newArrayList();

	EntryBuilder() {
	}

	public EntryBuilder setUri(String uri) {
		this.uri = uri;
		return thisValue();
	}

	public EntryBuilder setTitle(SyndContent title) {
		this.title = title;
		return thisValue();
	}

	public EntryBuilder setLink(String link) {
		this.link = link;
		return thisValue();
	}

	public EntryBuilder addLink(String link) {
		if (this.links == null) {
			links = Lists.newArrayList(link);
		} else {
			links.add(link);
		}
		return thisValue();
	}

	public EntryBuilder setLinks(List<String> links) {
		this.links = links;
		return thisValue();
	}

	public EntryBuilder setDescription(SyndContent description) {
		this.description = description;
		return thisValue();
	}

	public EntryBuilder addContent(SyndContent content) {
		if (this.contents == null) {
			contents = Lists.newArrayList(content);
		} else {
			contents.add(content);
		}
		return thisValue();
	}

	public EntryBuilder setContents(List<SyndContent> contents) {
		this.contents = contents;
		return thisValue();
	}

	public EntryBuilder addEnclosure(SyndEnclosure enclosure) {
		if (this.enclosures == null) {
			enclosures = Lists.newArrayList(enclosure);
		} else {
			enclosures.add(enclosure);
		}
		return thisValue();
	}

	public EntryBuilder setEnclosures(List<SyndEnclosure> enclosures) {
		this.enclosures = enclosures;
		return thisValue();
	}

	public EntryBuilder setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
		return thisValue();
	}

	public EntryBuilder setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
		return thisValue();
	}

	public EntryBuilder setAuthor(String author) {
		this.author = author;
		return thisValue();
	}

	public EntryBuilder addAuthor(SyndPerson author) {
		if (this.authors == null) {
			authors = Lists.newArrayList(author);
		} else {
			authors.add(author);
		}
		return thisValue();
	}

	public EntryBuilder setAuthors(List<SyndPerson> authors) {
		this.authors = authors;
		return thisValue();
	}

	public EntryBuilder addContributor(SyndPerson contributor) {
		if (this.contributors == null) {
			contributors = Lists.newArrayList(contributor);
		} else {
			contributors.add(contributor);
		}
		return thisValue();
	}

	public EntryBuilder setContributors(List<SyndPerson> contributors) {
		this.contributors = contributors;
		return thisValue();
	}

	public EntryBuilder addCategory(SyndCategory category) {
		if (this.categories == null) {
			categories = Lists.newArrayList(category);
		} else {
			categories.add(category);
		}
		return thisValue();
	}

	public EntryBuilder setCategories(List<SyndCategory> categories) {
		this.categories = categories;
		return thisValue();
	}

	public SyndEntry build() {
		final SyndEntry entry = new SyndEntryImpl();

		if (uri != null) {
			entry.setUri(uri);
		}
		if (title != null) {
			entry.setTitleEx(title);
		}
		if (link != null) {
			entry.setLink(link);
		}
		if (links != null) {
			entry.setLinks(links);
		}
		if (description != null) {
			entry.setDescription(description);
		}
		if (contents != null) {
			entry.setContents(contents);
		}
		if (enclosures != null) {
			entry.setEnclosures(enclosures);
		}
		if (publishedDate != null) {
			entry.setPublishedDate(publishedDate);
		}
		if (updatedDate != null) {
			entry.setUpdatedDate(updatedDate);
		}
		if (author != null) {
			entry.setAuthor(author);
		}
		if (authors != null) {
			entry.setAuthors(authors);
		}
		if (contributors != null) {
			entry.setContributors(contributors);
		}
		if (categories != null) {
			entry.setCategories(categories);
		}

		return entry;
	}

}
