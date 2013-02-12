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

package com.isotrol.impe3.core.engine;


import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.LocaleRouter;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageKey.ContentPage;
import com.isotrol.impe3.api.PageKey.Special;
import com.isotrol.impe3.api.PageKey.WithNavigation;
import com.isotrol.impe3.api.PageResolver;
import com.isotrol.impe3.api.PageURI;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Route;


/**
 * Portal router.
 * @author Andres Rodriguez.
 */
public abstract class AbstractPortalRouter implements PortalRouter {
	private final Portal portal;
	private final DeviceRouter deviceRouter;
	private final LocaleRouter localeRouter;
	private final PageResolver resolver;

	public AbstractPortalRouter(Portal portal, DeviceRouter deviceRouter, LocaleRouter localeRouter,
		PageResolver resolver) {
		this.portal = portal;
		this.resolver = resolver;
		this.deviceRouter = deviceRouter;
		this.localeRouter = localeRouter;
	}

	public PageURI getRoute(Route route) {
		final PageURI portalUri = getPortalRoute(route);
		PageURI page = null;
		if (resolver != null) {
			page = resolver.getPath(portal, route);
		}
		if (page == null) {
			page = getPageRoute(route);
		}
		if (page != null && deviceRouter != null) {
			page = page.apply(deviceRouter.getTransformer(portal, route.getDevice()));
		}
		if (page != null && localeRouter != null) {
			page = page.apply(localeRouter.getTransformer(portal, route.getLocale()));
		}
		if (page == null) {
			return null;
		}
		return portalUri.add(page);
	}

	protected abstract PageURI getPortalRoute(Route route);

	PageURI getPageRoute(Route route) {
		PathSegments path = PathSegments.of();
		final PageKey key = route.getPage();
		if (key instanceof Special) {
			final Special k = (Special) key;
			path = PathSegments.of(false, k.getName().toString().split("/"));
		} else if (key instanceof WithNavigation) {
			final WithNavigation wn = (WithNavigation) key;
			final NavigationKey nk = wn.getNavigationKey();
			if (nk != null) {
				if (nk.isCategory()) {
					final PathSegments s = getCategory(nk.getCategory(), route.getLocale());
					if (s == null) {
						return null;
					}
					path = path.add(s);
				} else if (nk.isTag()) {
					// TODO
					return null;
				}
				if (nk.isContentType()) {
					final ContentType contentType = nk.getContentType();
					final String p = contentType.getName().get(route.getLocale()).getPath();
					if (p == null) {
						return null;
					}
					path = path.add(PathSegments.segment(p, false));
				}
			}
			if (key instanceof ContentPage) {
				final ContentKey ck = ((ContentPage) key).getContentKey();
				final ContentType contentType = ck.getContentType();
				final String objectId = ck.getContentId();
				final String p = contentType.getName().get(route.getLocale()).getPath();
				if (p == null) {
					return null;
				}
				path = path.add(PathSegments.of(false, p, objectId));
			}
		}
		// We use the main page for the other cases.
		// } else if (!PageKey.main().equals(key)) {
		// return null;
		return new PageURI(path, null);
	}

	PathSegments getCategory(Category category, Locale locale) {
		final Categories categories = portal.getCategories();
		if (!categories.containsKey(category.getId())) {
			return null;
		}
		final List<String> segments = Lists.newLinkedList();
		Category parent = categories.getParent(category.getId());
		while (parent != null) {
			final String p = category.getName().get(locale).getPath();
			if (p == null) {
				return null;
			}
			segments.add(0, p);
			category = parent;
			parent = categories.getParent(category.getId());
		}
		return PathSegments.of(false, segments);
	}
}
