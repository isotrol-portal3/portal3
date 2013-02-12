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

package com.isotrol.impe3.api.component.atom;

/**
 * Supported RSS/ATOM versions.
 * 
 * @author Emilio Escobar Reyero
 */
public enum ATOMFeedType {

	RSS_090("rss_0.90"), RSS_091("rss_0.91"), RSS_092("rss_0.92"), RSS_093("rss_0.93"), RSS_094("rss_0.94"), RSS_10(
			"rss_1.0"), RSS_20("rss_2.0"), ATOM_03("atom_0.3"), ATOM_10("atom_1.0");

	private final String type;

	ATOMFeedType(String type) {
		this.type = type;
	}

	/**
	 * Returns the feed type. RSS(0.90, 0.91, 0.92, 0.93, 0.94, 1.0, 2.0)
	 * ATOM(0.3, 1.0).
	 * 
	 * @return The feed type.
	 */
	public String getFeedType() {
		return type;
	}
}
