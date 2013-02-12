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

package com.isotrol.impe3.palette.freemarker.model;


import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.GroupItem;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.freemarker.Model;
import com.isotrol.impe3.support.listing.ContentListingPage;


/**
 * Component that stores a listing in a model.
 * @author Emilio Escobar reyero
 * @author Andres Rodriguez
 */
@RequiresLink(Listing.class)
public class ListingModelComponent extends AbstractModelComponent implements Component {

	/** List. */
	private Listing<?> listing;

	@Inject
	public void setListing(Listing<?> listing) {
		this.listing = listing;
	}

	public ComponentResponse execute() {
		if (listing == null) {
			listing = loadSampleListing();
		}
		templateModel = Model.createComponentModel(templateModel, context);
		if (listing != null) {
			templateModel.put(Model.PAGE, listing);
			templateModel.put("listing", listing); // TODO: remove
			if (isXML()) {
				prepareXML(listing);
			}
		}
		return ComponentResponse.OK;
	}

	private Listing<Content> loadSampleListing() {
		final Content sample = loadSample();
		if (sample == null) {
			return ContentListingPage.EMPTY;
		}
		int n = Math.max(10, moduleConfig.sampleCount());
		final List<Content> list = Lists.newArrayListWithCapacity(n);
		for (int i = 0; i < n; i++) {
			list.add(sample);
		}
		return new ContentListingPage(null, context.getPagination(), list);

	}

	private void prepareXML(Iterable<?> page) {
		if (page == null) {
			return;
		}
		for (Object o : page) {
			if (o instanceof GroupItem) {
				final GroupItem<?> g = (GroupItem<?>) o;
				if (g.isElementItem()) {
					prepareContentXML(g.getElement());
				} else {
					prepareXML(g.getGroup().getItems());
				}
			} else {
				prepareContentXML(o);
			}
		}
	}

	private boolean prepareContentXML(Object o) {
		if (o instanceof Content) {
			final Content c = (Content) o;
			final Map<String, Object> local = c.getLocalValues();
			if (local.get(Model.XML) == null) {
				local.put(Model.XML, Model.loadXML(c.getContent()));
			}
			final ContentKey key = c.getContentKey();
			if (key != null) {
				local.put(Model.HREF, context.getURI(PageKey.content(key)));
			}
			return true;
		}
		return false;
	}

}
