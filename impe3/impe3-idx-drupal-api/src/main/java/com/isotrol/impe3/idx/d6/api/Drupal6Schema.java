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
package com.isotrol.impe3.idx.d6.api;

/**
 * Drupal 6 schema descriptor
 */
public final class Drupal6Schema {

	private Drupal6Schema() {
		throw new AssertionError();
	}

	private static final String prefix(String name) {
		return SCHEMA_NAME + name;
	}
	
	/** Prefix drupal property schema name */
	public static final String property(String name) {
		return PROPERTY_PREFIX + name;
	}

	/** Prefix opencms custom value schema name */
	public static final String custom(String name) {
		return CUSTOM_PREFIX + name;
	}
	
	/** Reserved schema name. */
	public static final String SCHEMA_NAME = "d6nr-";

	/** Properties reserved schema name. */
	public static final String PROPERTY_PREFIX = SCHEMA_NAME + "d6prop-";
	
	/** Custom fields reserved schema name. */
	public static final String CUSTOM_PREFIX = SCHEMA_NAME + "d6custom-";
	
	/** Drupal 6 node key*/
	public static final String NODEKEY = prefix("NODEKEY");
	
	/** Drupal 6 translation key*/
	public static final String TRANSLATIONKEY = prefix("TRANSLATIONKEY");
	
	/** Drupal 6 content id */
	public static final String ID = prefix("ID");
	
	/** Meta content type (CONTENT, FOLDER, etc. )*/
	public static final String TYPE = prefix("TYPE");
	
	/** Drupal 6 content type */
	public static final String CONTENT_TYPE = prefix("CONTENTTYPE");
	
	/** Drupal 6 content taxonomy */
	public static final String CATEGORY = prefix("CATEGORY");
	
	/** Content creation date */
	public static final String DATE_CREATED = prefix("DATECREATED");

	/** Content last modified date */
	public static final String DATE_LAST_MODIFIED = prefix("DATELASTMODIFIED");
	
}
