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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Builder;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.nr.api.Schema;


/**
 * A content builder.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class ContentBuilder implements Builder<Content> {
	private String contentId;
	private ContentType contentType;
	private final Set<String> sets = Sets.newHashSet();
	private final Set<Category> categories = Sets.newHashSet();
	private byte[] content;
	private Long date;
	private Long expirationdate;
	private Long releasedate;
	private String description;
	private boolean defaultLocale;
	private Set<Locale> locales = Sets.newHashSet();
	private Set<Locale> otherLocales = Sets.newHashSet();
	private String mime;
	private final Multimap<String, String> properties = ArrayListMultimap.create();
	private final Map<String, byte[]> blobs = Maps.newHashMap();
	private final Set<ContentKey> related = Sets.newHashSet();
	private String title;
	private final Multimap<String, String> highlighted = ArrayListMultimap.create();
	private final Map<String, Object> local = Maps.newHashMap();

	/** Default constructor. */
	public ContentBuilder() {
	}

	/** Sets content id and return fluid builder. */
	public ContentBuilder setContentId(String contentId) {
		this.contentId = contentId;
		return this;
	}

	/** Sets content type and return fluid builder. */
	public ContentBuilder setContentType(ContentType contentType) {
		this.contentType = contentType;
		return this;
	}

	/** Sets content bytes (array) and return fluid builder. */
	public ContentBuilder setContent(byte[] content) {
		this.content = content;
		return this;
	}

	/** Sets content date and return fluid builder. */
	public ContentBuilder setDate(Long date) {
		this.date = date;
		return this;
	}

	/** Sets content expiration date and return fluid builder. */
	public ContentBuilder setExpirationdate(Long expirationdate) {
		this.expirationdate = expirationdate;
		return this;
	}

	/** Sets content release and return fluid builder. */
	public ContentBuilder setReleasedate(Long releasedate) {
		this.releasedate = releasedate;
		return this;
	}

	/** Sets content description and return fluid builder. */
	public ContentBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public ContentBuilder setDefaultLocale(boolean defaultLocale) {
		this.defaultLocale = defaultLocale;
		return this;
	}

	public ContentBuilder addLocale(Locale locale) {
		if (locale != null) {
			this.locales.add(locale);
		}
		return this;
	}

	public ContentBuilder addOtherLocale(Locale locale) {
		if (locale != null) {
			this.otherLocales.add(locale);
		}
		return this;
	}

	/** Sets content mime type and return fluid builder. */
	public ContentBuilder setMime(String mime) {
		this.mime = mime;
		return this;
	}

	/** Sets content title and return fluid builder. */
	public ContentBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	/** Adds content set and return fluid builder. */
	public ContentBuilder addSet(String set) {
		if (set != null) {
			sets.add(set);
		}
		return this;
	}

	/** Adds content category and return fluid builder. */
	public ContentBuilder addCategory(Category category) {
		if (category != null) {
			categories.add(category);
		}
		return this;
	}

	/** Adds related content and return fluid builder. */
	public ContentBuilder addRelated(ContentKey key) {
		if (key != null) {
			related.add(key);
		}
		return this;
	}

	/** Puts content property and return fluid builder. */
	public ContentBuilder putProperty(String name, String value) {
		if (name != null && value != null) {
			properties.put(name, value);
		}
		return this;
	}

	/**
	 * Adds a new blob property
	 * @param key property name
	 * @param value property value
	 * @return fluid builder
	 */
	public ContentBuilder addBlob(String key, byte[] value) {
		checkNotNull(key);
		checkNotNull(value);
		checkArgument(key.startsWith(Schema.BLOB_PREFIX));
		blobs.put(key, value);
		return this;
	}

	/** Puts content hightlighted field fragments and return fluid builder. */
	public ContentBuilder putHighlighted(String name, String value) {
		if (name != null && value != null) {
			highlighted.put(name, value);
		}
		return this;
	}

	/** Puts content local value and return fluid builder. */
	public ContentBuilder putLocalValue(String name, Object value) {
		if (name != null && value != null) {
			local.put(name, value);
		}
		return this;
	}

	/** Builds Content (ContentImpl) */
	public Content get() {
		return new ContentImpl(this);
	}

	private static final class ContentImpl implements Content {
		private final String contentId;
		private final ContentType contentType;
		private final ContentKey contentKey;
		private final Set<String> sets;
		private final Set<Category> categories;
		private final byte[] content;
		private final Long date;
		private final Long expirationdate;
		private final Long releasedate;
		private final String description;
		private boolean defaultLocale;
		private final Set<Locale> locales;
		private final Set<Locale> otherLocales;
		private final String mime;
		private final Multimap<String, String> properties;
		private final ImmutableMap<String, byte[]> blobs;
		private final Set<ContentKey> related;
		private final String title;
		private final Multimap<String, String> highlighted;
		private final Map<String, Object> local;

		private ContentImpl(ContentBuilder b) {
			this.contentId = b.contentId;
			this.contentType = b.contentType;
			if (b.contentId != null && b.contentType != null) {
				this.contentKey = ContentKey.of(b.contentType, b.contentId);
			} else {
				this.contentKey = null;
			}
			this.sets = ImmutableSet.copyOf(b.sets);
			this.categories = ImmutableSet.copyOf(b.categories);
			this.content = b.content;
			this.date = b.date;
			this.expirationdate = b.expirationdate;
			this.releasedate = b.releasedate;
			this.description = b.description;
			this.defaultLocale = b.defaultLocale || b.locales.isEmpty();
			this.locales = ImmutableSet.copyOf(b.locales);
			this.otherLocales = ImmutableSet.copyOf(b.otherLocales);
			this.mime = b.mime;
			this.properties = ImmutableMultimap.copyOf(b.properties);
			this.blobs = ImmutableMap.copyOf(b.blobs);
			this.related = ImmutableSet.copyOf(b.related);
			this.title = b.title;
			this.highlighted = ImmutableMultimap.copyOf(b.highlighted);
			this.local = Maps.newHashMap(b.local);

		}

		/**
		 * @see com.isotrol.impe3.api.content.Content#getSets()
		 */
		public Set<String> getSets() {
			return sets;
		}

		public Set<Category> getCategories() {
			return categories;
		}

		public InputStream getContent() {
			return content != null ? new ByteArrayInputStream(content) : null;
		}

		public String getContentId() {
			return contentId;
		}

		public ContentKey getContentKey() {
			return contentKey;
		}

		public ContentType getContentType() {
			return contentType;
		}

		private Calendar safeDate(Long l) {
			if (l == null) {
				return null;
			}
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(l);
			return c;
		}

		public Calendar getDate() {
			return safeDate(date);
		}

		public Calendar getReleaseDate() {
			return safeDate(releasedate);
		}

		public Calendar getExpirationDate() {
			return safeDate(expirationdate);
		}

		public String getDescription() {
			return description;
		}

		public Map<String, Collection<String>> getHighlighted() {
			return highlighted.asMap();
		}

		public boolean isDefaultLocale() {
			return defaultLocale;
		}

		public Set<Locale> getLocales() {
			return locales;
		}

		public Set<Locale> getOtherLocales() {
			return otherLocales;
		}

		public Map<String, Object> getLocalValues() {
			return local;
		}

		public String getMime() {
			return mime;
		}

		public Map<String, Collection<String>> getProperties() {
			return properties.asMap();
		}

		public Map<String, byte[]> getBlobs() {
			return blobs;
		}

		public Set<ContentKey> getRelatedContentKey() {
			return related;
		}

		public String getTitle() {
			return title;
		}

	}

}
