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

package com.isotrol.impe3.palette.menu.contentType;


import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.palette.menu.MenuItem;
import com.isotrol.impe3.support.listing.DefaultListingPage;


/**
 * Content type menu component.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class ContentTypeMenuComponent implements Component {

	/** Content Types. */
	private ContentTypes contentTypes;
	/** URI Generator. */
	private URIGenerator uriGenerator;
	/** Route. */
	private Route route = null;
	/** Locale. */
	private Locale locale = null;
	/** Menu items. */
	private Listing<MenuItem> items;
	/** Component configuration */
	private ContentTypeMenuConfig config;
	/** Request context. */
	private ComponentRequestContext context;
	/** External predicate provider. */
	private ContentTypePredicateProvider filterProvider = null;

	public void setFilterProvider(ContentTypePredicateProvider filterProvider) {
		this.filterProvider = filterProvider;
	}

	public ComponentResponse execute() {
		final Set<UUID> uuids = contentTypes.keySet();
		final List<MenuItem> list = Lists.newArrayList();
		final Predicate<ContentType> filter = getFilter();
		for (UUID key : uuids) {
			final ContentType ct = contentTypes.get(key);

			if (filter.apply(ct)) {
				final MenuItem item = createItem(ct);
				list.add(item);
			}
		}

		setItems(createListing(list));

		return ComponentResponse.OK;
	}

	private Predicate<ContentType> getFilter() {
		if (filterProvider == null) {
			return ContentType.IS_NAVIGABLE;
		}
		Predicate<ContentType> filter = filterProvider.getContentTypePredicate(ContentType.IS_NAVIGABLE);
		if (filter == null) {
			return ContentType.IS_NAVIGABLE;
		}
		return filter;
	}

	protected MenuItem createItem(ContentType contentType) {
		final NavigationKey nk;
		if (config.navigation() && context.getNavigationKey() != null && context.getNavigationKey().isCategory()) {
			nk = NavigationKey.category(context.getNavigationKey().getCategory(), contentType);
		} else {
			nk = NavigationKey.contentType(contentType);
		}
		final PageKey pk = PageKey.navigation(nk);
		final Route r = (route != null) ? route.toPage(pk) : Route.of(false, pk, null, null);
		final URI uri = uriGenerator.getURI(r);
		final Name name = (locale != null) ? contentType.getName().get(locale) : contentType.getDefaultName();
		final MenuItem item = new MenuItem(name.getDisplayName(), uri);
		return item;
	}

	private Listing<MenuItem> createListing(List<MenuItem> list) {
		final Integer size = list.size();
		final Pagination pagination = new Pagination(size, size);
		final DefaultListingPage<MenuItem> menu = new DefaultListingPage<MenuItem>(size, pagination, list);
		return menu;
	}

	protected void setItems(Listing<MenuItem> items) {
		this.items = items;
	}

	@Extract
	public Listing<MenuItem> getItems() {
		return items;
	}

	@Inject
	public void setContentTypes(ContentTypes contentTypes) {
		this.contentTypes = contentTypes;
	}

	@Inject
	public void setUriGenerator(URIGenerator uriGenerator) {
		this.uriGenerator = uriGenerator;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	/**
	 * @param config the config to set
	 */
	@Autowired
	public void setConfig(ContentTypeMenuConfig config) {
		this.config = config;
	}

	/**
	 * @param context the context to set
	 */
	@Inject
	public void setContext(ComponentRequestContext context) {
		this.context = context;
	}

}
