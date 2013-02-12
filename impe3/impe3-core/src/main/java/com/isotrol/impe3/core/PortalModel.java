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

package com.isotrol.impe3.core;


import java.util.UUID;

import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.DeviceCapabilitiesProvider;
import com.isotrol.impe3.api.PageResolver;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.core.modules.StartedModule;


/**
 * Interface to access a portal model.
 * @author Andres Rodriguez
 */
public interface PortalModel extends BaseModel {
	/**
	 * Returns the portal.
	 * @return The portal.
	 */
	Portal getPortal();

	/**
	 * Returns the portal path.
	 * @return The portal path.
	 */
	PathSegments getPath();

	/**
	 * Returns the portal URI Generator.
	 * @return The portal URI Generator.
	 */
	URIGenerator getURIGenerator();

	/**
	 * Returns an started component module.
	 * @param id Component module id.
	 * @return Component module module.
	 */
	StartedModule<?> getComponent(UUID id);

	/**
	 * Returns the device capabilities provider.
	 * @return The device capabilities provider.
	 */
	DeviceCapabilitiesProvider getDeviceCapabilitiesProvider();

	/**
	 * Returns the page resolver.
	 * @return The page resolver.
	 */
	PageResolver getPageResolver();

	/**
	 * Returns a content loader for the portal.
	 * @param context Client request context.
	 * @return A content loader for the portal.
	 */
	ContentLoader getContentLoader(ClientRequestContext context);

	/**
	 * Returns the portal pages.
	 * @return The portal pages.
	 */
	Pages getPages();

	/**
	 * Returns the cache model.
	 * @return The cache model.
	 */
	PortalCacheModel getCacheModel();
}
