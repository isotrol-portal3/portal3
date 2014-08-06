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
package com.isotrol.impe3.web20.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.ResourcesService;
import com.isotrol.impe3.web20.model.ResourceEntity;

/**
 * Resources service implementation.  
 * @author Emilio Escobar Reyero
 */
@Service("resourcesService")
public class ResourcesServiceImpl extends AbstractWeb20Service implements ResourcesService {

	@Autowired
	private ResourceComponent resourceComponent;
	
	/**
	 * @see com.isotrol.impe3.web20.api.ResourcesService#activate(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public String activate(String serviceId, String resourceId) throws ServiceException {
		final long uuid = resourceComponent.get(resourceId);
		final ResourceEntity entity = getDao().findById(ResourceEntity.class, uuid, false);
		entity.setDeleted(false);
		return String.valueOf(entity.getId());
	}

	/**
	 * @see com.isotrol.impe3.web20.api.ResourcesService#delete(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(String serviceId, String resourceId) throws ServiceException {
		final long uuid = resourceComponent.get(resourceId);
		final ResourceEntity entity = getDao().findById(ResourceEntity.class, uuid, false);
		entity.setDeleted(true);
	}
	
	public void setResourceComponent(ResourceComponent resourceComponent) {
		this.resourceComponent = resourceComponent;
	}
}
