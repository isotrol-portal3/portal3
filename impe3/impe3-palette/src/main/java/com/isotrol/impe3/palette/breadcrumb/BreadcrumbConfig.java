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

package com.isotrol.impe3.palette.breadcrumb;


import com.isotrol.impe3.api.FileBundle;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.freemarker.FreeMarkerConfiguration;


/**
 * Base breadcrumb configuration.
 * @author Andres Rodriguez
 */
public interface BreadcrumbConfig extends FreeMarkerConfiguration {
	/** Template file. */
	@Name("template")
	@FileBundle
	String templateFile();
	
	/** Optional first level text. */
	@Optional
	@Name("prefixText")
	String prefixText();

	/** Optional first level URI. */
	@Name("prefixURI")
	@Optional
	String prefixURI();

	/** Whether the portal must be included. */
	@Name("includePortal")
	Boolean includePortal();

	/** Whether the last URI must be removed. */
	@Name("removeLastURI")
	Boolean removeLastURI();

	/** Optional override of portal name. */
	@Name("portalName")
	@Optional
	String portalName();

	/** Whether a title must be generated. */
	@Name("generateTitle")
	Boolean generateTitle();

	/** Title separator. */
	@Name("titleSeparator")
	@Optional
	String titleSeparator();

	/** Whether to include category in navigation. */
	@Name("withCategoryNav")
	Boolean withCategoryNav();

	/** Whether to include content type in navigation. */
	@Name("withContentTypeNav")
	Boolean withContentTypeNav();

	/** Whether to include category in detail. */
	@Name("withCategoryDetail")
	Boolean withCategoryDetail();

	/** Whether to include content type in detail. */
	@Name("withContentTypeDetail")
	Boolean withContentTypeDetail();

}
