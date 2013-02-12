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

package com.isotrol.impe3.pms.core.engine;


import java.util.UUID;

import com.google.common.base.Function;
import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.DeviceCapabilitiesProvider;
import com.isotrol.impe3.api.PageResolver;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.core.Pages;
import com.isotrol.impe3.core.PortalCacheModel;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.core.content.NRContentLoader;
import com.isotrol.impe3.core.modules.StartedModule;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.obj.StartedComponents;


/**
 * Abstract PMS Portal Model implementation.
 * @author Andres Rodriguez.
 */
abstract class AbstractPortalModel extends BasePMSModel implements PortalModel {
	private final URIGenerator uriGenerator;
	private final StartedComponents components;
	private final Pages pages;
	private final Portal portal;
	/** Device capabilities provider. */
	private final DeviceCapabilitiesProvider deviceCaps;
	/** Node repositories. */
	private final Function<UUID, NodeRepository> nodeRepositories;
	private final PageResolver pageResolver;
	private final PathSegments path;
	/** Portal cache model. */
	private final PortalCacheModel cacheModel;

	public AbstractPortalModel(PortalLoadingSupport pls) throws PMSException {
		super(pls.getBaseModel());
		this.uriGenerator = pls.loadURIGenerator();
		this.nodeRepositories = pls.getNodeRepositories();
		this.components = pls.loadComponents();
		this.deviceCaps = pls.getDeviceCaps();
		this.pages = pls.loadPages();
		this.portal = pls.getPortal();
		this.pageResolver = pls.getPageResolver();
		this.path = pls.getPath();
		this.cacheModel = pls.getCacheModel();
	}

	final void stopComponents() {
		components.stop();
	}

	/**
	 * @see com.isotrol.impe3.core.PortalModel#getPortal()
	 */
	public Portal getPortal() {
		return portal;
	}

	/**
	 * @see com.isotrol.impe3.core.PortalModel#getPath()
	 */
	public PathSegments getPath() {
		return path;
	}

	/**
	 * @see com.isotrol.impe3.core.PortalModel#getContentLoader(com.isotrol.impe3.api.ClientRequestContext)
	 */
	public ContentLoader getContentLoader(ClientRequestContext context) {
		return new NRContentLoader(portal, nodeRepositories, context.getLocale());
	}

	/**
	 * @see com.isotrol.impe3.core.PortalModel#getURIGenerator()
	 */
	public URIGenerator getURIGenerator() {
		return uriGenerator;
	}

	/**
	 * @see com.isotrol.impe3.core.PortalModel#getComponent(java.util.UUID)
	 */
	public StartedModule<?> getComponent(UUID id) {
		return components.apply(id);
	}

	/**
	 * @see com.isotrol.impe3.core.PortalModel#getPages()
	 */
	public Pages getPages() {
		return pages;
	}

	/**
	 * @see com.isotrol.impe3.core.PortalModel#getDeviceCapabilitiesProvider()
	 */
	public DeviceCapabilitiesProvider getDeviceCapabilitiesProvider() {
		return deviceCaps;
	}

	/**
	 * @see com.isotrol.impe3.core.PortalModel#getPageResolver()
	 */
	public PageResolver getPageResolver() {
		return pageResolver;
	}

	/**
	 * @see com.isotrol.impe3.core.PortalModel#getCacheModel()
	 */
	public PortalCacheModel getCacheModel() {
		return cacheModel;
	}

}
