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

package com.isotrol.impe3.api;


/**
 * Enumeration of supported device types.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public enum DeviceType {
	HTML(980), XHTML(980), ATOM(null), SITEMAP(null), XLS(null), XLSX(null), PDF(null);

	/** Default width. */
	private final Integer width;

	DeviceType(final Integer width) {
		this.width = width;
	}

	/**
	 * Returns whether the type supports layout.
	 * @return True if the type supports layout.
	 */
	public boolean isLayout() {
		return width != null;
	}

	/**
	 * Returns the default width.
	 * @return The default width.
	 */
	public Integer getWidth() {
		return width;
	}
}
