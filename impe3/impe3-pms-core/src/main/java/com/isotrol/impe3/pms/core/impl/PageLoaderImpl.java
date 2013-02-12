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

package com.isotrol.impe3.pms.core.impl;


import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.ImmutableHierarchy;
import net.sf.derquinsej.collect.ImmutableHierarchy.Builder;

import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.core.Templated;
import com.isotrol.impe3.pms.core.PageLoader;
import com.isotrol.impe3.pms.core.support.PortalPages;
import com.isotrol.impe3.pms.model.CIPValue;
import com.isotrol.impe3.pms.model.LayoutValue;
import com.isotrol.impe3.pms.model.PageDfn;
import com.isotrol.impe3.pms.model.PageEntity;
import com.isotrol.impe3.pms.model.WithPosition;


/**
 * Implementation of LayoutLoader.
 * @author Andres Rodriguez.
 */
@Component
public final class PageLoaderImpl implements PageLoader {
	private static <K, F extends WithPosition> Ordering<Entry<K, F>> ordering() {
		final Ordering<Integer> o = Ordering.natural();
		return o.onResultOf(new Function<Entry<K, F>, Integer>() {
			public Integer apply(Entry<K, F> from) {
				return from.getValue().getPosition();
			}
		});
	}

	private static final Ordering<Entry<UUID, CIPValue>> CIP_ORDERING = ordering();
	private static final Ordering<Entry<Integer, LayoutValue>> LV_ORDERING = ordering();

	/**
	 * Constructor.
	 */
	public PageLoaderImpl() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PageLoader#loadPartialCIPs(com.isotrol.impe3.pms.model.PageDfn)
	 */
	public Hierarchy<UUID, CIPValue> loadPartialCIPs(PageDfn dfn) {
		final Map<UUID, CIPValue> map = dfn.getComponents();
		if (map.isEmpty()) {
			return ImmutableHierarchy.of();
		}
		final Builder<UUID, CIPValue> builder = ImmutableHierarchy.builder();
		for (Entry<UUID, CIPValue> e : CIP_ORDERING.sortedCopy(map.entrySet())) {
			final CIPValue v = e.getValue();
			builder.add(e.getKey(), v, v.getParent());
		}
		return builder.get();
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PageLoader#loadCIPs(com.isotrol.impe3.pms.core.support.PortalPages,
	 * com.isotrol.impe3.pms.model.PageDfn)
	 */
	public Hierarchy<UUID, CIPValue> loadCIPs(PortalPages pages, PageDfn dfn) {
		return new Loader(pages, dfn).loadCIPs();
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PageLoader#loadPartialLayout(com.isotrol.impe3.pms.model.PageDfn)
	 */
	public Hierarchy<Integer, Templated<LayoutValue>> loadPartialLayout(PageDfn dfn) {
		final Map<Integer, LayoutValue> map = dfn.getLayout();
		if (map.isEmpty()) {
			return ImmutableHierarchy.of();
		}
		final Builder<Integer, Templated<LayoutValue>> builder = ImmutableHierarchy.builder();
		for (Entry<Integer, LayoutValue> e : LV_ORDERING.sortedCopy(map.entrySet())) {
			final LayoutValue v = e.getValue();
			builder.add(e.getKey(), Templated.of(false, v), v.getParent());
		}
		return builder.get();
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PageLoader#loadLayout(com.isotrol.impe3.pms.core.support.PortalPages,
	 * com.isotrol.impe3.pms.model.PageDfn)
	 */
	public Hierarchy<Integer, Templated<LayoutValue>> loadLayout(PortalPages pages, PageDfn dfn) {
		return new Loader(pages, dfn).loadLayout();
	}

	/** Page definition. */
	private static class Dfn {
		private final PageDfn dfn;

		Dfn(PageDfn dfn) {
			this.dfn = dfn;
		}

		UUID getSpace() {
			if (dfn.getPage().getPageClass() == PageClass.TEMPLATE) {
				for (Entry<UUID, CIPValue> e : dfn.getComponents().entrySet()) {
					final CIPValue cv = e.getValue();
					if (cv.isSpace()) {
						return e.getKey();
					}
				}
			}
			return null;
		}

		Iterable<Entry<Integer, LayoutValue>> getLayout() {
			return LV_ORDERING.sortedCopy(dfn.getLayout().entrySet());
		}

		Iterable<Entry<UUID, CIPValue>> getComponents() {
			return CIP_ORDERING.sortedCopy(dfn.getComponents().entrySet());
		}
	}

	private static class Loader {
		private final LinkedList<Level> levels;
		private final boolean template;

		int n = 0;

		Loader(PortalPages pages, PageDfn dfn) {
			levels = Lists.newLinkedList();
			levels.add(new Level(dfn));
			this.template = dfn.getPage().getPageClass() == PageClass.TEMPLATE;
			PageEntity parentTemplate = dfn.getTemplate();
			while (parentTemplate != null) {
				dfn = pages.get(parentTemplate.getId());
				levels.addFirst(new Level(dfn));
				parentTemplate = dfn.getTemplate();
			}
		}

		Hierarchy<Integer, Templated<LayoutValue>> loadLayout() {
			final Builder<Integer, Templated<LayoutValue>> builder = ImmutableHierarchy.builder();
			loadLayout(builder, levels.remove(0), null);
			return builder.get();
		}

		private void loadLayout(Builder<Integer, Templated<LayoutValue>> builder, Level level, Integer parent) {
			UUID spaceCIP = level.getSpace();
			final boolean inherited = !levels.isEmpty();
			for (Entry<Integer, LayoutValue> e : level.getLayout()) {
				final LayoutValue v = e.getValue();
				Integer vp = v.getParent();
				if (vp == null) {
					vp = parent;
				} else {
					vp = level.getId(vp);
				}
				if (spaceCIP != null && spaceCIP.equals(v.getComponent())) {
					if (levels.isEmpty()) {
						Preconditions.checkState(template);
						final Integer id = level.getId(e.getKey());
						builder.add(id, Templated.of(inherited, v), vp);
					} else {
						final Level next = levels.remove(0);
						if (levels.isEmpty()) {
							// Last level.
							final Builder<Integer, Templated<LayoutValue>> b = ImmutableHierarchy.builder();
							loadLayout(b, next, null);
							builder.add(n++, Templated.of(b.get()), vp);
						} else {
							// More templates left
							loadLayout(builder, next, vp);
						}
					}
				} else {
					final Integer id = level.getId(e.getKey());
					builder.add(id, Templated.of(inherited, v), vp);
				}
			}
		}

		Hierarchy<UUID, CIPValue> loadCIPs() {
			final Builder<UUID, CIPValue> builder = ImmutableHierarchy.builder();
			loadCIPs(builder, levels.remove(0), null);
			return builder.get();
		}

		private void loadCIPs(Builder<UUID, CIPValue> builder, Level level, UUID parent) {
			for (Entry<UUID, CIPValue> e : level.getComponents()) {
				final CIPValue v = e.getValue();
				UUID vp = v.getParent();
				if (vp == null) {
					vp = parent;
				}
				if (v.isSpace()) {
					if (levels.isEmpty()) {
						Preconditions.checkState(template);
					} else {
						loadCIPs(builder, levels.remove(0), vp);
					}
				} else {
					builder.add(e.getKey(), v, vp);
				}
			}
		}

		private class Level extends Dfn {
			private Map<Integer, Integer> map;

			Level(PageDfn dfn) {
				super(dfn);
			}

			Integer getId(Integer levelId) {
				Integer id;
				if (map == null) {
					map = Maps.newHashMap();
					id = null;
				} else {
					id = map.get(levelId);
				}
				if (id == null) {
					id = (n++);
					map.put(levelId, id);
				}
				return id;
			}
		}

	}

}
