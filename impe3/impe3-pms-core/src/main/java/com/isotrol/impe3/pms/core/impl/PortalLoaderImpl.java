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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ComputationException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.CacheKey;
import com.isotrol.impe3.pms.core.PortalLoader;
import com.isotrol.impe3.pms.core.obj.CIPsObject;
import com.isotrol.impe3.pms.core.obj.ComponentsObject;
import com.isotrol.impe3.pms.core.obj.LayoutObject;
import com.isotrol.impe3.pms.core.obj.PageDetailLoader;
import com.isotrol.impe3.pms.core.obj.PageObject;
import com.isotrol.impe3.pms.core.obj.PageObject.Key;
import com.isotrol.impe3.pms.core.obj.PortalObject;
import com.isotrol.impe3.pms.core.obj.PortalPagesObject;
import com.isotrol.impe3.pms.core.obj.PortalsObject;
import com.isotrol.impe3.pms.core.support.LoaderCaches;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.PageDfn;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEdition;
import com.isotrol.impe3.pms.model.PortalEntity;


/**
 * Implementation of PortalLoader.
 * @author Andres Rodriguez.
 */
@Component
public final class PortalLoaderImpl extends AbstractStateLoaderComponent<PortalsObject> implements PortalLoader {
	/** Portals Loader. */
	private final PortalsLoader loader;
	/** Components computer. */
	private final ComponentsComputer componentComputer;
	/** Components cache. */
	private final LoadingCache<CacheKey, ComponentsObject> componentsCache;
	/** Pages computer. */
	private final PagesComputer pagesComputer;
	/** Pages cache. */
	private final LoadingCache<CacheKey, PortalPagesObject> pagesCache;
	/** CIPs loader. */
	private final LoadingCache<Key, CIPsObject> cips = LoaderCaches.collection(new CIPComputer());
	/** CIPs loader. */
	private final LoadingCache<Key, LayoutObject> layout = LoaderCaches.collection(new LayoutComputer());
	/** Page detail loader. */
	private final PageDetailLoader pageDetailLoader;

	/** Default constructor. */
	public PortalLoaderImpl() {
		super();
		this.loader = new PortalsLoader();
		this.componentComputer = new ComponentsComputer();
		this.componentsCache = LoaderCaches.collection(componentComputer);
		this.pagesComputer = new PagesComputer();
		this.pagesCache = LoaderCaches.collection(pagesComputer);
		// Page detail loader.
		this.pageDetailLoader = new PageDetailLoader() {
			public CIPsObject loadCIPs(PageObject page) {
				return cips.getUnchecked(page.getCacheKey());
			}

			public LayoutObject loadLayout(PageObject page) {
				return layout.getUnchecked(page.getCacheKey());
			}
		};
	}

	@Override
	PortalsLoader getLoader() {
		return loader;
	}

	public void purge() {
		super.purge();
		componentsCache.invalidateAll();
		pagesCache.invalidateAll();
		cips.invalidateAll();
		layout.invalidateAll();
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.PORTAL;
	}

	@Override
	int getOfflineVersion(EnvironmentEntity e) {
		return e.getPortalVersion();
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PortalLoader#loadComponents(com.isotrol.impe3.pms.core.obj.PortalsObject,
	 * com.isotrol.impe3.pms.core.obj.PortalObject)
	 */
	public ComponentsObject loadComponents(PortalsObject portals, PortalObject portal) throws PMSException {
		checkNotNull(portal, "The portal object must be provided");
		if (portal.isOffline()) {
			final CacheKey portalsKey = portal.getPortalsKey();
			PortalEntity e = loadOfflineEntity(portalsKey.getId(), portal.getId());
			final CacheKey key = CacheKey.create(portal.getId(), e.getComponentVersion());
			return componentsCache.getUnchecked(key);
		} else {
			return componentComputer.loadOnline(portals, portal);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PortalLoader#loadPages(com.isotrol.impe3.pms.core.obj.PortalsObject,
	 * com.isotrol.impe3.pms.core.obj.PortalObject)
	 */
	public PortalPagesObject loadPages(PortalsObject portals, PortalObject portal) throws PMSException {
		if (portal.isOffline()) {
			final CacheKey portalsKey = portal.getPortalsKey();
			PortalEntity e = loadOfflineEntity(portalsKey.getId(), portal.getId());
			final CacheKey key = CacheKey.create(portal.getId(), e.getPageVersion());
			return pagesCache.getUnchecked(key);
		} else {
			return pagesComputer.loadOnline(portals, portal);
		}
	}

	private PortalEntity loadOfflineEntity(UUID envId, UUID portalId) throws PMSException {
		final PortalEntity pe = findById(PortalEntity.class, portalId);
		NotFoundProviders.PORTAL.checkNotNull(pe, portalId);
		return findById(PortalEntity.class, portalId);
	}

	private class PortalsLoader implements Loader<PortalsObject> {

		public PortalsObject load(EnvironmentEntity e) {
			return PortalsObject.current(PortalLoaderImpl.this, e, getDao().getOfflinePortals(e.getId()));
		}

		public PortalsObject load(EditionEntity e) {
			return PortalsObject.edition(PortalLoaderImpl.this, e);
		}

		@Override
		public String toString() {
			return "Portals";
		}
	}

	/**
	 * Computer for portal elements.
	 * @author Andres Rodriguez
	 * @param <T> Computed object type.
	 */
	private abstract class PortalElementComputer<T> extends CacheLoader<CacheKey, T> {
		/** Offline computer. */
		public final T load(CacheKey key) throws Exception {
			final PortalEntity pe = findById(PortalEntity.class, key.getId());
			final PortalDfn dfn = pe.getCurrent();
			final CacheKey portalsKey = PortalsObject.key(pe.getEnvironment());
			final PortalsObject portals = loadOffline(portalsKey);
			return load(portals, dfn);
		}

		/** Online computer. */
		final T loadOnline(PortalsObject portals, PortalObject portal) {
			final PortalEdition pe = findById(PortalEdition.class, portal.getEditionId());
			final PortalDfn dfn = pe.getPublished();
			return load(portals, dfn);
		}

		abstract T load(PortalsObject portals, PortalDfn dfn);
	}

	/**
	 * Computer for components.
	 * @author Andres Rodriguez
	 */
	private final class ComponentsComputer extends PortalElementComputer<ComponentsObject> {
		ComponentsObject load(PortalsObject portals, PortalDfn dfn) {
			final ComponentsObject parent;
			final UUID parentId = dfn.getParentId();
			if (parentId != null) {
				try {
					parent = loadComponents(portals, portals.load(parentId));
				} catch (PMSException e) {
					throw new ComputationException(e);
				}
			} else {
				parent = null;
			}
			return ComponentsObject.of(dfn, parent);
		}

		@Override
		public String toString() {
			return "Components";
		}
	}

	/**
	 * Computer for pages.
	 * @author Andres Rodriguez
	 */
	private final class PagesComputer extends PortalElementComputer<PortalPagesObject> {
		PortalPagesObject load(PortalsObject portals, PortalDfn dfn) {
			final PortalPagesObject parent;
			final UUID parentId = dfn.getParentId();
			if (parentId != null) {
				try {
					parent = loadPages(portals, portals.load(parentId));
				} catch (PMSException e) {
					throw new ComputationException(e);
				}
			} else {
				parent = null;
			}
			return PortalPagesObject.of(pageDetailLoader, dfn, parent);
		}

		@Override
		public String toString() {
			return "Pages";
		}

	}

	/**
	 * Computer for page elements.
	 * @author Andres Rodriguez
	 * @param <T> Computed object type.
	 */
	private abstract class PageElementComputer<T> extends CacheLoader<Key, T> {
		public final T load(Key key) throws Exception {
			final PageDfn dfn = checkNotNull(findById(PageDfn.class, key.getId()));
			return load(dfn);
		}

		abstract T load(PageDfn dfn);
	}

	/**
	 * Computer for CIPs.
	 * @author Andres Rodriguez
	 */
	private final class CIPComputer extends PageElementComputer<CIPsObject> {
		CIPsObject load(PageDfn dfn) {
			return CIPsObject.of(dfn);
		}

		@Override
		public String toString() {
			return "CIPs";
		}
	}

	/**
	 * Computer for CIPs.
	 * @author Andres Rodriguez
	 */
	private final class LayoutComputer extends PageElementComputer<LayoutObject> {
		LayoutObject load(PageDfn dfn) {
			return LayoutObject.of(dfn);
		}

		@Override
		public String toString() {
			return "Layout";
		}
	}

}
