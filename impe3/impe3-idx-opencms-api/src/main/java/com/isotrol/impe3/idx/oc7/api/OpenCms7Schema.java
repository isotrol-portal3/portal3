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
package com.isotrol.impe3.idx.oc7.api;

import java.text.DecimalFormat;

/**
 * OpenCms 7 schema descriptor
 * 
 * @author Emilio Escobar Reyero
 * @modified Juan Manuel Valverde Ramírez
 * @deprecated see com.isotrol.impe3.idx.oc.api.OpenCmsSchema
 */
@Deprecated
public final class OpenCms7Schema { 

	private OpenCms7Schema() {
		throw new AssertionError();
	}

	private static final String prefix(String name) {
		return SCHEMA_NAME + name;
	}
	
	/** Prefix opencms property schema name */
	public static final String property(String name) {
		return PROPERTY_PREFIX + name;
	}

	/** Prefix opencms custom value schema name */
	public static final String custom(String name) {
		return CUSTOM_PREFIX + name;
	}
	
	/** Reserved schema name. */
	public static final String SCHEMA_NAME = "oc7nr-";

	/** Properties reserved schema name. */
	public static final String PROPERTY_PREFIX = SCHEMA_NAME + "oc7prop-";
	/** Custom fields reserved schema name. */
	public static final String CUSTOM_PREFIX = SCHEMA_NAME + "oc7custom-";
	
	/** Opencms 7 node key*/
	public static final String NODEKEY = prefix("NODEKEY");
	
	/** Parent folder */
	public static final String PARENT = prefix("PARENT");
	/** Predecesors folders */
	public static final String PREDECESORS = prefix("PREDECESORS");
	
	/** Opencms 7 content id */
	public static final String ID = prefix("ID");
	
	/** Meta content type (CONTENT, FOLDER, etc. )*/
	public static final String TYPE = prefix("TYPE");
	/** Opencms 7 content type */
	public static final String CONTENT_TYPE = prefix("CONTENTTYPE");
	
	/** Content path */
	public static final String PATH = prefix("PATH");

	/** Content creation date */
	public static final String DATE_CREATED = prefix("DATECREATED");

	/** Content original categories */
	public static final String CHANNEL = prefix("CHANNEL");

	/** Content last modified date */
	public static final String DATE_LAST_MODIFIED = prefix("DATELASTMODIFIED");
	
	/** Content release date */
	public static final String DATE_RELEASED = prefix("DATERELEASED");
	
	/** Content expiration date */
	public static final String DATE_EXPIRED = prefix("DATEEXPIRED");
	/** Boolean field (0, 1) indicated navigation aspect */
	public static final String IN_NAV = prefix("BROWSEABLE");
	
	/** Public nav formater. */
	public static final DecimalFormat NAVPOSFORMAT = new DecimalFormat("0000000.0000000000");
	
}
