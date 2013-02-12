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

package com.isotrol.impe3.api.content;


import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Link;


/**
 * Generic interface for content.
 * @author Andres Rodriguez
 */
public interface Content extends Link {
	/**
	 * Returns the content id.
	 * @return the content id.
	 */
	String getContentId();

	/**
	 * Returns the content key.
	 * @return The content key.
	 */
	ContentKey getContentKey();
	
	/**
	 * Returns the content sets.
	 * @return The content sets.
	 */
	Set<String> getSets();

	/**
	 * Returns the content categories.
	 * @return The content categories.
	 */
	Set<Category> getCategories();

	/**
	 * Returns the content data.
	 * @return the content data.
	 */
	InputStream getContent();

	/**
	 * Returns the content date.
	 * @return The content date.
	 */
	Calendar getDate();

	/**
	 * Returns the content release date.
	 * @return The content release date.
	 */
	Calendar getReleaseDate();
	
	/**
	 * Returns the content expiration date.
	 * @return The content expiration date.
	 */
	Calendar getExpirationDate();
	
	/**
	 * Returns the content description.
	 * @return The content description.
	 */
	String getDescription();

	/**
	 * Returns whether the content is the default locale.
	 * @return True if the content is the default locale.
	 */
	boolean isDefaultLocale();

	/**
	 * Returns the content locales.
	 * @return The content locales.
	 */
	Set<Locale> getLocales();

	/**
	 * Returns the content other locales.
	 * @return The content other locales.
	 */
	Set<Locale> getOtherLocales();

	/**
	 * Returns the content MIME type.
	 * @return The content MIME type.
	 */
	String getMime();

	/**
	 * Return the content type.
	 * @return The content type.
	 */
	ContentType getContentType();

	/**
	 * Returns the content properties.
	 * @return The content properties.
	 */
	Map<String, Collection<String>> getProperties();

	/**
	 * Returns the content blobs.
	 * @return The content blobs.
	 */
	Map<String, byte[]> getBlobs();
	
	/**
	 * Returns the related content keys.
	 * @return The related content keys.
	 */
	Set<ContentKey> getRelatedContentKey();

	/**
	 * Returns the content title.
	 * @return The content title.
	 */
	String getTitle();

	/**
	 * Returns the highlighted fields as list of string fragments.
	 * @return The highlighted fields as list of string fragments.
	 */
	public Map<String, Collection<String>> getHighlighted();

	/**
	 * Returns the local value store.
	 * @return The local value store.
	 */
	public Map<String, Object> getLocalValues();

}
