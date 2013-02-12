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


import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Enumeration of supported export jobs.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public enum ExportJobType {
	CONTENT_TYPE(ExportJobNames.CONTENT_TYPES), CONTENT_TYPE_ALL(ExportJobNames.CONTENT_TYPES), CONNECTOR(
		ExportJobNames.CONNECTORS), CONNECTOR_ALL(ExportJobNames.CONNECTORS), MAPPING(ExportJobNames.MAPPINGS), MAPPING_ALL(
		ExportJobNames.MAPPINGS), MAPPING_SETS(ExportJobNames.MAPPINGS_SETS), MAPPING_CATEGORIES(
		ExportJobNames.MAPPINGS_CATEGORIES), MAPPING_CONTENT_TYPES(ExportJobNames.MAPPINGS_CONTENT_TYPES), CATEGORY_LEVEL(
		ExportJobNames.CATEGORIES), CATEGORY_LEVEL_ALL(ExportJobNames.CATEGORIES), CATEGORY_NODE(
		ExportJobNames.CATEGORIES), CATEGORY_NODE_ALL(ExportJobNames.CATEGORIES), PORTAL_NAME(
		ExportJobNames.PORTAL_NAME), PORTAL_BASE(ExportJobNames.PORTAL_BASE), PORTAL_PROP(ExportJobNames.PORTAL_PROP), PORTAL_SET(
		ExportJobNames.PORTAL_SET), PORTAL_PAGE_ALL(ExportJobNames.PORTAL_PAGE), COMPONENT(ExportJobNames.COMPONENTS), COMPONENT_ALL(
		ExportJobNames.COMPONENTS), COMPONENT_OVR(ExportJobNames.OVERRIDEN_COMPONENTS), COMPONENT_OVR_ALL(
		ExportJobNames.OVERRIDEN_COMPONENTS);

	/** Default file name. */
	private final String fileName;

	private ExportJobType(String fileName) {
		this.fileName = checkNotNull(fileName);
	}

	/**
	 * Returns the default file name.
	 * @return The default file name.
	 */
	public String getFileName() {
		return fileName;
	}
}
