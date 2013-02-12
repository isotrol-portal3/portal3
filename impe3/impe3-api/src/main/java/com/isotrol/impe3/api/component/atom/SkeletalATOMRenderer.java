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

import com.google.common.collect.ImmutableList;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 * 
 * @author Emilio Escobar Reyero
 */
public class SkeletalATOMRenderer implements ATOMRenderer {

	public SkeletalATOMRenderer() {
	}

	/**
	 * @see com.isotrol.impe3.extensions.api.component.atom.ATOMRenderer#getEntries()
	 */
	public Iterable<SyndEntry> getEntries() {
		return ImmutableList.of();
	}

	/**
	 * @see com.isotrol.impe3.extensions.api.component.atom.ATOMRenderer#getCategories()
	 */
	public Iterable<SyndCategory> getCategories() {
		return ImmutableList.of();
	}

	/**
	 * @see com.isotrol.impe3.extensions.api.component.atom.ATOMRenderer#getFeed(com.sun.syndication.feed.synd.SyndFeed)
	 */
	public SyndFeed getFeed(SyndFeed feed) {
		return null;
	}

}
