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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Sets.newHashSet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.Schema;


/**
 * Document builder.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public final class DocumentBuilder implements Supplier<Document> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Document document = new Document();
	private boolean releaseSet = false;
	private boolean expirationSet = false;
	private boolean descriptionSet = false;
	private boolean titleSet = false;
	private boolean attachmentSet = false;
	private boolean textSet = false;
	private boolean setAdded = false;
	private boolean mainCategorySet = false;
	private Set<String> categories = newHashSet();
	private Set<String> locales = newHashSet();
	private Set<String> otherLocales = newHashSet();
	private boolean defaultLocale = true;
	private static final String O = "0";

	/**
	 * Default constructor.
	 */
	public DocumentBuilder() {
	}

	/**
	 * Adds a stored non analyzed field to document.
	 * @param field field name
	 * @param text value to add
	 */
	private void siu(String field, String text) {
		document.add(new Field(field, text, Field.Store.YES, Field.Index.NOT_ANALYZED));
	}

	/**
	 * Adds a stored analyzed field to document.
	 * @param field field name
	 * @param text value to add
	 */
	private void sia(String field, String text) {
		document.add(new Field(field, text, Field.Store.YES, Field.Index.ANALYZED));
	}

	/**
	 * Adds a non stored analyzed field to document.
	 * @param field field name
	 * @param text value to add
	 */
	private void nsia(String field, String text) {
		document.add(new Field(field, text, Field.Store.NO, Field.Index.ANALYZED));
	}

	/**
	 * Adds a non stored analyzed field to document.
	 * @param field field name
	 * @param text value to add
	 */
	private void nsiu(String field, String text) {
		document.add(new Field(field, text, Field.Store.NO, Field.Index.NOT_ANALYZED));
	}

	/**
	 * Set the node key (id plus type) to document (stored and not analyzed)
	 * @param nodeKey the node key
	 * @return fluid builder
	 */
	public DocumentBuilder setNodeKey(NodeKey nodeKey) {
		siu(Schema.NODEKEY, nodeKey.toString());
		siu(Schema.TYPE, nodeKey.getNodeType().toString().toLowerCase());
		siu(Schema.ID, nodeKey.getNodeId());
		return this;
	}

	/**
	 * Returns whether this document is in the default locale.
	 * @param defaultLocale True if this document is in the default locale.
	 * @return This builder for method chaining.
	 */
	public DocumentBuilder setDefaultLocale(boolean defaultLocale) {
		this.defaultLocale = defaultLocale;
		return this;
	}

	/**
	 * Adds a locale.
	 * @param locale Locale to add.
	 * @return This builder for method chaining.
	 */
	public DocumentBuilder addLocale(String locale) {
		if (locale != null && !Schema.ALL_LOCALES.equals(locale)) {
			locales.add(locale);
			siu(Schema.LOCALE, locale);
		}
		return this;
	}

	/**
	 * Adds a locale.
	 * @param locale Locale to add.
	 * @return This builder for method chaining.
	 */
	public DocumentBuilder addLocale(Locale locale) {
		if (locale != null) {
			addLocale(locale.toString());
		}
		return this;
	}

	/**
	 * Adds an other locale entry.
	 * @param locale Locale to add.
	 * @return This builder for method chaining.
	 */
	public DocumentBuilder addOtherLocale(String locale) {
		if (locale != null && !Schema.ALL_LOCALES.equals(locale)) {
			otherLocales.add(locale);
			siu(Schema.OTHER_LOCALE, locale);
		}
		return this;
	}

	/**
	 * Adds an other locale entry.
	 * @param locale Locale to add.
	 * @return This builder for method chaining.
	 */
	public DocumentBuilder addOtherLocale(Locale locale) {
		if (locale != null) {
			addOtherLocale(locale.toString());
		}
		return this;
	}

	/**
	 * Set document title stored and analyzed
	 * @param title the title
	 * @return fluid builder
	 */
	public DocumentBuilder setTitle(String title) {
		sia(Schema.TITLE, title);
		nsiu(Schema.TITLE_SORT, title);
		titleSet = true;
		return this;
	}

	/**
	 * Set document description stored and analyzed
	 * @param description the description
	 * @return fluid builder
	 */
	public DocumentBuilder setDescription(String description) {
		sia(Schema.DESCRIPTION, description);
		descriptionSet = true;
		return this;
	}

	/**
	 * Set document mime type stored and no analyzed
	 * @param mime the description
	 * @return fluid builder
	 */
	public DocumentBuilder setMime(String mime) {
		siu(Schema.MIME, mime);
		return this;
	}

	/**
	 * Set date to document stored and not analyzed
	 * @param date the date
	 * @return fluid builder
	 */
	public DocumentBuilder setDate(Calendar date) {
		siu(Schema.DATE, Schema.calendarToString(date));
		return this;
	}

	/**
	 * Set date to document stored and not analyzed
	 * @param date the date
	 * @return fluid builder
	 */
	public DocumentBuilder setDate(Date date) {
		siu(Schema.DATE, Schema.dateToString(date));
		return this;
	}

	/**
	 * Sets the release date. This field is stored and not analyzed.
	 * @param date the date
	 * @return fluid builder
	 */
	public DocumentBuilder setReleaseDate(Calendar date) {
		siu(Schema.RELEASEDATE, Schema.calendarToString(date));
		releaseSet = true;
		return this;
	}

	/**
	 * Sets the release date. This field is stored and not analyzed.
	 * @param date the date
	 * @return fluid builder
	 */
	public DocumentBuilder setReleaseDate(Date date) {
		siu(Schema.RELEASEDATE, Schema.dateToString(date));
		releaseSet = true;
		return this;
	}

	/**
	 * Sets the release date. This field is stored and not analyzed.
	 * @param date the date
	 * @return fluid builder
	 */
	public DocumentBuilder setExpirationDate(Calendar date) {
		siu(Schema.EXPIRATIONDATE, Schema.calendarToString(date));
		expirationSet = true;
		return this;
	}

	/**
	 * Sets the release date. This field is stored and not analyzed.
	 * @param date the date
	 * @return fluid builder
	 */
	public DocumentBuilder setExpirationDate(Date date) {
		siu(Schema.EXPIRATIONDATE, Schema.dateToString(date));
		expirationSet = true;
		return this;
	}

	/**
	 * Adds document content text analyzed for searches (stored)
	 * @param text string document
	 * @return fluid builder
	 */
	public DocumentBuilder setText(String text) {
		sia(Schema.CONTENT_IDX, text);
		textSet = true;
		return this;
	}

	/**
	 * Adds bytes to document with compress mode.
	 * @param bytes the byte array
	 * @param compressed if true bytes goes compress
	 * @return fluid builder
	 */
	public DocumentBuilder setBytes(byte[] bytes, boolean compressed) {
		document.add(new Field(Schema.CONTENT_STORE, compressed ? compress(bytes) : bytes));
		siu(Schema.COMPRESSED, compressed ? Schema.COMPRESSED_VALUE : O);
		return this;
	}

	private byte[] compress(byte[] bytes) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			try {
				final GZIPOutputStream gos = new GZIPOutputStream(bos);
				try {
					gos.write(bytes);
				}
				finally {
					gos.close();
				}
			}
			finally {
				bos.close();
			}
		} catch (IOException e) {
			logger.warn("IOException compressing content bytes. {} ", e.getMessage());
			logger.trace("Exception trace, ", e);
		}
		return bos.toByteArray();

	}

	/**
	 * Adds a category to the document.
	 * @param categoryKey the category node key
	 * @return fluid builder
	 */
	public DocumentBuilder addCategory(UUID categoryKey) {
		return addCategory(categoryKey.toString());
	}

	/**
	 * Adds a category to the document.
	 * @param categoryKey the category node key
	 * @return fluid builder
	 */
	public DocumentBuilder addCategory(String categoryKey) {
		final String c = categoryKey.toLowerCase();
		if (!categories.contains(c)) {
			siu(Schema.CATEGORY, c);
			categories.add(c);
		}
		return this;
	}

	/**
	 * Adds a set to the document.
	 * @param set Set to add.
	 * @return fluid builder
	 */
	public DocumentBuilder addSet(String set) {
		siu(Schema.NODESET, checkNotNull(set));
		setAdded = true;
		return this;
	}

	/**
	 * Sets the main category of the document.
	 * @param categoryKey the category key.
	 * @return fluid builder.
	 */
	public DocumentBuilder setMainCategory(String categoryKey) {
		checkState(!mainCategorySet);
		final String c = categoryKey.toLowerCase();
		if (!categories.contains(c)) {
			siu(Schema.CATEGORY, c);
		}
		siu(Schema.MAIN_CATEGORY, c);
		mainCategorySet = true;
		return this;
	}

	/**
	 * Adds a related content to de node
	 * @param relatedNodeKey the related content node key
	 * @return fluid builder
	 */
	public DocumentBuilder addRelated(String relatedNodeKey) {
		siu(Schema.RELATED, relatedNodeKey.toLowerCase());
		return this;
	}

	/**
	 * Adds an user property field to document.
	 * @param name Field name (not null) can't start with schema reserved name
	 * @param value Field value (not null)
	 * @param store if true will store value data
	 * @param analyze if true will analyze value data
	 * @return fluid builder
	 */
	public DocumentBuilder setField(String name, String value, boolean store, boolean analyze) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(value);
		Preconditions.checkArgument(!name.startsWith(Schema.SCHEMA_NAME));

		Store s = store ? Field.Store.YES : Field.Store.NO;
		Index i = analyze ? Field.Index.ANALYZED : Field.Index.NOT_ANALYZED;

		document.add(new Field(name, value, s, i));

		return this;
	}

	/**
	 * Adds an user binary property field to the document.
	 * @param name Field name (not null). Must start with {@code Schema.BLOB_PREFIX}.
	 * @param value Field value (not null)
	 * @return fluid builder
	 */
	public DocumentBuilder setField(String name, byte[] value) {
		checkNotNull(name);
		checkNotNull(value);
		checkArgument(name.startsWith(Schema.BLOB_PREFIX) && name.length() > Schema.BLOB_PREFIX.length());
		document.add(new Field(name, value));
		return this;
	}

	/**
	 * Adds an user date property field to document.
	 * @param name Field name (not null) can't starts with schema reserved name
	 * @param date Field value (not null)
	 * @param store if true will store value data
	 * @param analyze if true will analyze value data
	 * @return fluid builder
	 */
	public DocumentBuilder setField(String name, Calendar date, boolean store, boolean analyze) {
		Preconditions.checkNotNull(date);
		return setField(name, Schema.calendarToString(date), store, analyze);
	}

	/**
	 * Adds an user date property field to document.
	 * @param name Field name (not null) can't starts with schema reserved name
	 * @param date Field value (not null)
	 * @param store if true will store value data
	 * @param analyze if true will analyze value data
	 * @return fluid builder
	 */
	public DocumentBuilder setField(String name, Date date, boolean store, boolean analyze) {
		Preconditions.checkNotNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return setField(name, calendar, store, analyze);
	}

	/**
	 * Adds an user uuid property field to document.
	 * @param name Field name (not null) can't starts with schema reserved name
	 * @param uuid Field value (not null)
	 * @param store if true will store value data
	 * @param analyze if true will analyze value data
	 * @return fluid builder
	 */
	public DocumentBuilder setField(String name, UUID uuid, boolean store, boolean analyze) {
		Preconditions.checkNotNull(uuid);
		return setField(name, uuid.toString().toLowerCase(), store, analyze);
	}

	/**
	 * Adds an user boolean property field to document.
	 * @param name Field name (not null) can't starts with schema reserved name
	 * @param bool Field value
	 * @return fluid builder
	 */
	public DocumentBuilder setField(String name, boolean bool) {
		return setField(name, bool ? "1" : O, true, false);
	}

	/**
	 * Adds attached content
	 * @param content data
	 * @return fluid builder
	 */
	public DocumentBuilder addAttachment(String content) {
		Preconditions.checkNotNull(content);
		nsia(Schema.ATTACHMENT, content);
		attachmentSet = true;
		return this;
	}

	/**
	 * Create a new document
	 * @return the document
	 */
	public Document get() {
		requiredFields();
		final Document d = document;
		document = new Document();
		return d;
	}

	private void requiredFields() {
		if (!releaseSet) {
			setReleaseDate(Schema.getMinDate());
		}
		if (!expirationSet) {
			setExpirationDate(Schema.getMaxDate());
		}
		if (!titleSet) {
			setTitle(" ");
		}
		if (!descriptionSet) {
			setDescription(" ");
		}
		if (!attachmentSet) {
			addAttachment(" ");
		}
		if (!textSet) {
			setText(" ");
		}
		if (!setAdded) {
			siu(Schema.NODESET, Schema.DEFAULT_SET);
		}
		if (categories.isEmpty()) {
			siu(Schema.CATEGORY, Schema.NULL_UUID);
		}
		if (!mainCategorySet) {
			siu(Schema.MAIN_CATEGORY, Schema.NULL_UUID);
		}
		if (defaultLocale || locales.isEmpty()) {
			siu(Schema.LOCALE, Schema.ALL_LOCALES);
		}
		if (otherLocales.isEmpty()) {
			siu(Schema.OTHER_LOCALE, Schema.ALL_LOCALES);
		}
	}
}
