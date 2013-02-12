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

package com.isotrol.impe3.pms.model;


/**
 * Standard export job names.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero 
 */
final class ExportJobNames {
	private ExportJobNames() {
		throw new AssertionError();
	}

	static final String CONTENT_TYPES = "contentTypes";
	static final String CATEGORIES = "categories";
	static final String CONNECTORS = "connectors";
	static final String MAPPINGS = "mappings";
	static final String MAPPINGS_SETS = "mappings_sets";
	static final String MAPPINGS_CONTENT_TYPES = "mappings_contenttypes";
	static final String MAPPINGS_CATEGORIES = "mappings_categories";
	static final String PORTAL_NAME = "portal_name";
	static final String PORTAL_BASE = "portal_bases";
	static final String PORTAL_PROP = "portal_properties";
	static final String PORTAL_SET = "portal_sets";
	static final String PORTAL_PAGE = "portal_pages";
	static final String COMPONENTS = "components";
	static final String OVERRIDEN_COMPONENTS = "components_overriden";
}
