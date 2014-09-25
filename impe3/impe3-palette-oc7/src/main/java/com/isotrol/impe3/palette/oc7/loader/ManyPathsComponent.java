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
package com.isotrol.impe3.palette.oc7.loader;

import java.util.Arrays;
import java.util.List;

import net.sf.derquinse.lucis.Item;

import org.springframework.util.StringUtils;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentListing;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.idx.oc.api.OpenCmsSchema;
import com.isotrol.impe3.nr.api.ISO9075;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.support.listing.ContentListingPage;


/**
 * Functional component that loads many contents by OpenCMS path.
 */
public class ManyPathsComponent extends AbstractLoaderComponent {
	
	/** Content OC7 paths (component-level configuration). */
	private ManyPathsConfig config;
	
	/** Loaded contents. */
	private ContentListing contents;
	
	/** Current route. */
	private Route route;

	/**
	 * Constructor.
	 */
	public ManyPathsComponent() {
	}

	/**
	 * Config to load.
	 * @param config Config to load.
	 */
	@Inject
	public void setPath(ManyPathsConfig config) {
		this.config = config;
	}
	
	private Content loadContent(ContentCriteria criteria, String path) {
		if (StringUtils.hasText(path)) {
			final String pathEncode = ISO9075.encode(path);
			final NodeQuery query = NodeQueries.term(OpenCmsSchema.PATH, pathEncode);
			criteria.must(query);
			final Item<Content> item = criteria.getFirst();
			if (item != null) {
				return item.getItem();
			}
		}
		return null;
	}
	
	@Override
	void load(ContentCriteria criteria) {
		final List<Content> list = Lists.newArrayListWithCapacity(10);
		if (config != null) {
			for (String path : Iterables.filter(Arrays.asList(config.path1(), config.path2(), config.path3(), config
					.path4(), config.path5(), config.path6(), config.path7(), config.path8(), config.path9(), config
					.path10()), Predicates.notNull())) {
				final Content c = loadContent(criteria, path);
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
