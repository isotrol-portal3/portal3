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

package com.isotrol.impe3.core;


/**
 * Enumeration of page types.
 * @author Andres Rodriguez
 */
enum PageType {
	/** Special page. */
	SPECIAL(true),
	/** Error page. */
	ERROR(false),
	/** Navigation by category page. */
	CATEGORY(true),
	/** Navigation by tag page. */
	TAG(true),
	/** Navigation by content type. */
	CONTENT_LIST(true),
	/** Content page. */
	CONTENT(true),
	/** Main page. */
	MAIN(true),
	/** Default page. */
	DEFAULT(false);

	private final boolean routable;

	private PageType(final boolean routable) {
		this.routable = routable;
	}

	public boolean isRoutable() {
		return routable;
	}
}
