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

package com.isotrol.impe3.pms.core.support;


import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.PageMapKey;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.model.PageDfn;
import com.isotrol.impe3.pms.model.PortalDfn;


/**
 * Utility functions for a set (or subset) of a portal pages.
 * @author Andres Rodriguez.
 */
public final class PortalPages {
	private static final Function<PageDfn, UUID> DFN2ID = new Function<PageDfn, UUID>() {
		public UUID apply(PageDfn from) {
			return from.getPage().getId();
		}
	};

	public static PortalPages of(PortalDfn portal) {
		return of(portal, null);
	}

	public static PortalPages of(final PortalDfn portal, final UUID deviceId) {
		Iterable<PageDfn> pages = portal.getPages();
		if (deviceId != null) {
			final Predicate<PageDfn> ofDevice = new Predicate<PageDfn>() {
				public boolean apply(PageDfn input) {
					return deviceId.equals(input.getPage().getDevice().getId());
				}
			};
			pages = Iterables.filter(pages, ofDevice);
		}
		return new PortalPages(pages);
	}

	private final Map<UUID, PageDfn> map;
	private final Map<PageMapKey, PageDfn> byKey;

	/**
	 * Constructor
	 * @param pages Page definitions.
	 */
	private PortalPages(Iterable<PageDfn> pages) {
		map = Maps.uniqueIndex(pages, DFN2ID);
		byKey = Maps.newHashMap();
		for (PageDfn p : pages) {
			final PageMapKey pageKey = PageMapKeySupport.of(p);
			if (pageKey != null) {
				final PageDfn p2 = byKey.get(pageKey);
				if (p2 != null) {
					Loggers.core().warn("Duplicate page key [{}]. Keeping last page.", new Object[] {pageKey});
				}
				byKey.put(pageKey, p);
			}
		}
	}

	public PageDfn get(UUID id) {
		return map.get(id);
	}

	public PageDfn getByKey(PageMapKey key) {
		return byKey.get(key);
	}

	public Collection<PageDfn> getPages() {
		return map.values();
	}

	public PageDfn load(UUID id) throws PMSException {
		final PageDfn dfn = get(id);
		if (dfn == null) {
			throw NotFoundProviders.PAGE.getNotFoundException(id);
		}
		return dfn;
	}

	public PageDfn load(String id) throws PMSException {
		return load(NotFoundProviders.PAGE.toUUID(id));
	}
}
