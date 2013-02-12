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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.DEVICE;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.Identifiable;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.Page;
import com.isotrol.impe3.core.Pages;
import com.isotrol.impe3.core.engine.PagesImpl;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.model.PageDfn;
import com.isotrol.impe3.pms.model.PortalDfn;


/**
 * Portal collection of pages domain object.
 * @author Andres Rodriguez
 */
public abstract class PortalPagesObject extends PagesObject {
	/**
	 * Constructor.
	 * @param portal Portal Id.
	 */
	PortalPagesObject(UUID portal) {
		super(portal);
	}

	/**
	 * Builds a collection from a portal definition.
	 * @param detailLoader Page detail loader.
	 * @param dfn Portal definition.
	 * @param parent Parent portal pages.
	 * @return The requested collection.
	 */
	public static PortalPagesObject of(final PageDetailLoader detailLoader, PortalDfn dfn, PortalPagesObject parent) {
		final UUID portalId = dfn.getEntity().getId();
		final Set<PageDfn> dfns = dfn.getPages();
		if (dfns.isEmpty() && (parent == null || parent.isEmpty())) {
			return new Empty(portalId);
		}
		final Function<PageDfn, PageObject> dfn2obj = new Function<PageDfn, PageObject>() {
			public PageObject apply(PageDfn from) {
				return new PageObject(detailLoader, from);
			}
		};
		return new Regular(portalId, transform(dfns, dfn2obj), parent);
	}

	abstract Set<UUID> getDevices();

	/**
	 * Filters this collection by device.
	 * @param id Device id.
	 * @return The filtered collection.
	 */
	public abstract DevicePagesObject byDevice(UUID id);

	/**
	 * Filters this collection by device.
	 * @param id Device id.
	 * @return The filtered collection.
	 */
	public final DevicePagesObject byDevice(String id) throws EntityNotFoundException {
		return byDevice(DEVICE.toUUID(id));
	}

	public Pages start(Portal portal, BaseModel model, StartedComponents components) throws PMSException {
		return new PagesImpl(portal, startPages(model, components));
	}

	abstract Iterable<Page> startPages(BaseModel model, StartedComponents components) throws PMSException;

	private static final class Empty extends PortalPagesObject {
		/**
		 * Constructor.
		 * @param portal Portal Id.
		 */
		private Empty(UUID portal) {
			super(portal);
		}

		@Override
		Set<UUID> getDevices() {
			return ImmutableSet.of();
		}

		@Override
		protected Map<UUID, PageObject> delegate() {
			return ImmutableMap.of();
		}

		@Override
		public DevicePagesObject byDevice(UUID id) {
			checkNotNull(id);
			return DevicePagesObject.of(getPortal(), id);
		}

		@Override
		Iterable<Page> startPages(BaseModel model, StartedComponents components) throws PMSException {
			return ImmutableList.of();
		}
	}

	private static final class Regular extends PortalPagesObject {
		/** Pages map. */
		private final ImmutableMap<UUID, PageObject> map;
		/** Devices. */
		private final ImmutableSet<UUID> devices;
		/** Filters by device. */
		private final ImmutableMap<UUID, DevicePagesObject> byDevice;

		/**
		 * Constructor.
		 * @param portal Portal Id.
		 * @param pages Pages.
		 * @param parent Parent portal pages.
		 */
		private Regular(UUID portal, Iterable<PageObject> pages, PortalPagesObject parent) {
			super(portal);
			// Prototype map
			Map<UUID, PageObject> map = Maps.newHashMap(Maps.uniqueIndex(pages, Identifiable.ID));
			// Build the combined set of devices
			final Set<UUID> deviceSet = Sets.newHashSet(transform(map.values(), PageObject.DEVICE));
			if (parent != null) {
				deviceSet.addAll(parent.getDevices());
			}
			this.devices = ImmutableSet.copyOf(deviceSet);
			// Build the by device map.
			final ImmutableMap.Builder<UUID, DevicePagesObject> dmb = ImmutableMap.builder();
			for (UUID deviceId : this.devices) {
				DevicePagesObject pdpo = parent != null ? parent.byDevice(deviceId) : null;
				DevicePagesObject dpo = DevicePagesObject.of(getPortal(), deviceId,
					filter(map.values(), compose(equalTo(deviceId), PageObject.DEVICE)), pdpo);
				dmb.put(deviceId, dpo);
				map.putAll(dpo);
			}
			this.byDevice = dmb.build();
			// Build the combined map
			this.map = ImmutableMap.copyOf(map);
		}

		@Override
		Set<UUID> getDevices() {
			return devices;
		}

		@Override
		protected Map<UUID, PageObject> delegate() {
			return map;
		}

		@Override
		public DevicePagesObject byDevice(UUID id) {
			final DevicePagesObject dpo = byDevice.get(id);
			return dpo != null ? dpo : DevicePagesObject.of(getPortal(), id);
		}

		@Override
		Iterable<Page> startPages(BaseModel model, StartedComponents components) throws PMSException {
			final List<Page> loaded = Lists.newLinkedList();
			for (UUID deviceId : getDevices()) {
				final DevicePagesObject pages = byDevice(deviceId);
				for (PageObject page : pages.values()) {
					if (model.getMode() == EngineMode.OFFLINE || page.getPageClass() != PageClass.TEMPLATE) {
						loaded.add(page.start(pages, model, components));
					}
				}
			}
			return loaded;
		}

	}
}
