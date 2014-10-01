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

package com.isotrol.impe3.core.support;

import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import net.derquinse.common.uuid.UUIDs;
import net.sf.derquinsej.i18n.Locales;

import org.springframework.util.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.Devices;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageKey.ContentPage;
import com.isotrol.impe3.api.PageKey.ErrorPage;
import com.isotrol.impe3.api.PageKey.NavigationPage;
import com.isotrol.impe3.api.PageKey.Special;
import com.isotrol.impe3.api.PageKey.WithNavigation;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Route;

/**
 * Page key to Action Parameters support.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero.
 */
public final class RouteParams {
	/** Not instantiable. */
	private RouteParams() {
		throw new AssertionError();
	}

	private static final String SECURE = "i3sc";
	private static final String TYPE = "i3pt";
	private static final String NAME = "i3pn";
	private static final String CATEGORY = "i3cg";
	private static final String TAG = "i3t";
	private static final String CONTENT_TYPE = "i3ct";
	private static final String CONTENT_ID = "i3id";
	private static final String DEVICE = "i3d";
	private static final String LOCALE = "i3l";
	private static final String SESSIONCSRF = "i3csrf";

	private static final String TYPE_M = "M";
	private static final String TYPE_S = "S";
	private static final String TYPE_N = "N";
	private static final String TYPE_L = "L";
	private static final String TYPE_C = "C";

	public static Multimap<String, String> toParams(UUID csrfToken, Route route) {
		if (route == null) {
			return ImmutableMultimap.of();
		}
		final PageKey key = route.getPage();
		if (key == null) {
			return ImmutableMultimap.of();
		}
		Multimap<String, String> p = ArrayListMultimap.create();
		if (route.isSecure()) {
			p.put(SECURE, "");
		}
		// The pk will only be an error page in case no page was resolved, so we turn to the main page
		if (key == PageKey.main() || key instanceof ErrorPage) {
			p.put(TYPE, TYPE_M);
		} else if (key instanceof Special) {
			p.put(TYPE, TYPE_S);
			p.put(NAME, ((Special) key).getName().toString());
		} else if (key instanceof WithNavigation) {
			WithNavigation wn = (WithNavigation) key;
			final NavigationKey nk = wn.getNavigationKey();
			if (nk != null) {
				if (nk.isCategory()) {
					p.put(CATEGORY, nk.getCategory().getId().toString());
				} else if (nk.isTag()) {
					p.put(TAG, nk.getTag());
				}
				if (nk.isContentType()) {
					p.put(TYPE, TYPE_L);
					p.put(CONTENT_TYPE, nk.getContentType().getId().toString());
				}
			}
			if (wn instanceof NavigationPage) {
				p.put(TYPE, TYPE_N);
			} else if (wn instanceof ContentPage) {
				ContentPage cp = (ContentPage) key;
				final ContentKey ck = cp.getContentKey();
				p.put(CONTENT_TYPE, ck.getContentType().getId().toString());
				p.put(CONTENT_ID, ck.getContentId());
				p.put(TYPE, TYPE_C);
			}
		}
		final Locale locale = route.getLocale();
		if (locale != null) {
			p.put(LOCALE, locale.toString());
		}
		final Device device = route.getDevice();
		if (device != null) {
			p.put(DEVICE, device.getId().toString());
		}
		if (csrfToken != null) {
			p.put(SESSIONCSRF, csrfToken.toString());
		}
		return p;
	}

	/**
	 * Tries to build a route object from an action query parameters.
	 * @param portal Portal.
	 * @param devices Engine devices.
	 * @param p Query parameters.
	 * @return The identified route or {@code null} if unable to create one.
	 */
	public static Route fromParams(Portal portal, Devices devices, MultivaluedMap<String, String> p) {
		PageKey key = null;
		final String type = p.getFirst(TYPE);
		if (type == null) {
			return null;
		}
		if (TYPE_M.equals(type)) {
			key = PageKey.main();
		} else if (TYPE_S.equals(type)) {
			final String name = p.getFirst(NAME);
			key = StringUtils.hasText(name) ? PageKey.special(name) : null;
		} else if (TYPE_N.equals(type)) {
			final NavigationKey nk = getNavigation(portal, p, false);
			key = nk != null ? PageKey.navigation(nk) : null;
		} else if (TYPE_L.equals(type)) {
			final NavigationKey nk = getNavigation(portal, p, true);
			key = nk != null ? PageKey.contentType(nk) : null;
		} else if (TYPE_C.equals(type)) {
			final String ct = p.getFirst(CONTENT_TYPE);
			final String id = p.getFirst(CONTENT_ID);
			if (StringUtils.hasText(ct) && StringUtils.hasText(id)) {
				final ContentType contentType = getContentType(portal, ct);
				if (contentType != null) {
					key = PageKey.content(getNavigation(portal, p, false), ContentKey.of(contentType, id));
				}
			}
		}
		if (key == null) {
			return null;
		}
		Device device = null;
		Locale locale = null;
		String d = p.getFirst(DEVICE);
		if (StringUtils.hasText(d)) {
			try {
				UUID uuid = UUID.fromString(d);
				device = devices.get(uuid);
			} catch (IllegalArgumentException e) {
			}
		}
		String l = p.getFirst(LOCALE);
		if (StringUtils.hasText(l)) {
			try {
				locale = Locales.fromString(l);
			} catch (IllegalArgumentException e) {
			}
		}
		return Route.of(p.containsKey(SECURE), key, device, locale);
	}

	/**
	 * Gets the session CSRF param from an action query parameters.
	 * @param p Query parameters.
	 * @return The recovered token or {@code null} if no valid token is found.
	 */
	public static UUID getSessionCSRF(MultivaluedMap<String, String> p) {
		return UUIDs.safeFromString(p.getFirst(SESSIONCSRF));
	}

	private static NavigationKey getNavigation(Portal portal, MultivaluedMap<String, String> p, boolean withContentType) {
		// 1 - Load content type if needed.
		final ContentType contentType;
		if (withContentType) {
			final String ct = p.getFirst(CONTENT_TYPE);
			contentType = (ct == null) ? null : getContentType(portal, ct);
			if (contentType == null) {
				return null;
			}
		} else {
			contentType = null;
		}
		// 2 - Try category navigation
		final String cg = p.getFirst(CATEGORY);
		if (StringUtils.hasText(cg)) {
			final Category c = getCategory(portal, cg);
			if (c != null && contentType == null) {
				return NavigationKey.category(c);
			} else if (c != null && contentType != null) {
				return NavigationKey.category(c, contentType);
			}
		}
		// 3 - Try tag navigation.
		final String tag = p.getFirst(TAG);
		final boolean hasTag = StringUtils.hasText(tag);
		if (hasTag && contentType == null) {
			return NavigationKey.tag(tag);
		} else if (hasTag && contentType != null) {
			return NavigationKey.tag(tag, contentType);
		}
		// 4 - Try content type navigation
		if (contentType != null) {
			return NavigationKey.contentType(contentType);
		}
		return null;
	}

	private static Category getCategory(Portal portal, String id) {
		try {
			UUID uuid = UUID.fromString(id);
			final Categories c = portal.getCategories();
			return c.containsKey(uuid) ? c.get(uuid) : null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	private static ContentType getContentType(Portal portal, String id) {
		try {
			UUID uuid = UUID.fromString(id);
			final ContentTypes c = portal.getContentTypes();
			return c.containsKey(uuid) ? c.get(uuid) : null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
