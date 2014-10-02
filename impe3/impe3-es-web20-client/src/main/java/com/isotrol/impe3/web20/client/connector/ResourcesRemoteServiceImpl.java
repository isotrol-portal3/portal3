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
package com.isotrol.impe3.web20.client.connector;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.ResourcesService;

/**
 * Resources remote service implementation. 
 * @author Emilio Escobar Reyero
 */
public class ResourcesRemoteServiceImpl extends WithHessian<ResourcesService> implements ResourcesService {

	@Override
	protected Class<ResourcesService> serviceClass() {
		return ResourcesService.class;
	}
	
	@Override
	protected String serviceUrl() {
		return server() + "/resources";
	}

	/**
	 * @see com.isotrol.impe3.web20.api.ResourcesService#activate(java.lang.String, java.lang.String)
	 */
	public String activate(String serviceId, String resourceId) throws ServiceException {
		return delegate().activate(serviceId, resourceId);
	}
	
	/**
	 * @see com.isotrol.impe3.web20.api.ResourcesService#delete(java.lang.String, java.lang.String)
	 */
	public void delete(String serviceId, String resourceId) throws ServiceException {
		delegate().delete(serviceId, resourceId);
	}
	
}
