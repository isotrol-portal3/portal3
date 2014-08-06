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
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.web20.server.ResourceManager;


/**
 * Abstract class for resource managers.
 * @author Andres Rodriguez.
 */
public abstract class AbstractResourceManager extends AbstractWeb20Service implements ResourceManager {
	@Autowired
	private ResourceComponent resourceComponent;

	/** Constructor. */
	public AbstractResourceManager() {
	}

	/**
	 * @see com.isotrol.impe3.web20.server.ResourceManager#getResource(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public final long getResource(String key) {
		return resourceComponent.get(key);
	}

	protected final String getResourceById(long id) {
		return resourceComponent.getResourceById(id);
	}
}
