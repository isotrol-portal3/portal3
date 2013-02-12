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

package com.isotrol.impe3.palette.content.loader;


import java.util.Arrays;
import java.util.List;

import net.sf.derquinse.lucis.Item;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentListing;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.nr.api.ISO9075;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.support.listing.ContentListingPage;


/**
 * Reads some contents using a field=value query.
 * @author Andres Rodriguez
 */
public class ManyByFieldComponent extends AbstractLoaderComponent {
	/** Loaded contents. */
	private ContentListing contents;
	/** Configuration. */
	private ManyByFieldConfig config;

	/**
	 * Constructor.
	 */
	public ManyByFieldComponent() {
	}

	@Inject
	public void setConfig(ManyByFieldConfig config) {
		this.config = config;
	}

	private Content loadContent(ContentCriteria criteria, String v) {
		String f = config.fieldName();
		if (config.encode()) {
			v = ISO9075.encode(v);
		}
		final NodeQuery query = NodeQueries.term(f, v);
		final ContentCriteria c = criteria.clone();
		c.must(query);
		final Item<Content> item = c.getFirst();
		if (item != null) {
			return item.getItem();
		}
		return null;
	}

	@Override
	void load(ContentCriteria criteria) {
		final List<Content> list = Lists.newArrayListWithCapacity(10);
		if (config != null) {
			for (String v : Iterables.filter(Arrays.asList(config.fieldValue(), config.fieldValue2(),
				config.fieldValue3(), config.fieldValue4(), config.fieldValue5(), config.fieldValue6(),
				config.fieldValue7(), config.fieldValue8(), config.fieldValue9(), config.fieldValue10()), Predicates
				.notNull())) {
				final Content c = loadContent(criteria, v);
				if (c != null) {
					list.add(c);
				}

			}
		}
		contents = new ContentListingPage(list.size(), null, list);
	}

	/**
	 * Content extractor.
	 * @return Loaded content.
	 */
	@Extract
	public Listing<?> getContents() {
		return this.contents;
	}

}
