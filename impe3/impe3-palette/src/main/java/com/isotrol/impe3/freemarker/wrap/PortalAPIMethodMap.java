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

package com.isotrol.impe3.freemarker.wrap;


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;


/**
 * Collection of Port@l API method FreeMarker models.
 * @author Andres Rodriguez
 * @param <T> Wrapped object type.
 */
final class PortalAPIMethodMap<T> {
	private final ImmutableMap<String, PortalAPIMethod<? super T>> map;

	private static <T> PortalAPIMethodMap<T> build(PortalAPIMethod<? super T>[]... values) {
		ImmutableMap.Builder<String, PortalAPIMethod<? super T>> builder = ImmutableMap.builder();
		for (PortalAPIMethod<? super T>[] methods : values) {
			for (PortalAPIMethod<? super T> method : methods) {
				builder.put(method.getName(), method);
			}
		}
		return new PortalAPIMethodMap<T>(builder.build());
	}

	private PortalAPIMethodMap(ImmutableMap<String, PortalAPIMethod<? super T>> map) {
		this.map = checkNotNull(map);
	}

	@SuppressWarnings("unchecked")
	static final PortalAPIMethodMap<Route> ROUTE = build(RouteMethods.values());
	@SuppressWarnings("unchecked")
	static final PortalAPIMethodMap<PageKey> PAGE_KEY = build(PageKeyMethods.values());
	@SuppressWarnings("unchecked")
	static final PortalAPIMethodMap<ContentKey> CONTENT_KEY = build(ContentKeyMethods.values());
	@SuppressWarnings("unchecked")
	static final PortalAPIMethodMap<NavigationKey> NAVIGATION_KEY = build(NavigationKeyMethods.values());

	/**
	 * Computes the method value.
	 * @param key Requested key.
	 * @param wrapper Object wrapper.
	 * @param object Wrapped object.
	 * @return The template model or {@code null} if the key is not found.
	 */
	TemplateModel get(String key, PortalObjectWrapper wrapper, T object) throws TemplateModelException {
		if (map.containsKey(key)) {
			return map.get(key).get(wrapper, object);
		}
		return null;
	}

}
