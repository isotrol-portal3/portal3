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


import org.springframework.stereotype.Component;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.RoutingDomainManager;
import com.isotrol.impe3.pms.core.obj.RoutingDomainsObject;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.RoutingDomainEntity;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Implementation of RoutingDomainManager.
 * @author Andres Rodriguez.
 */
@Component
public final class RoutingDomainManagerImpl extends AbstractStateLoaderComponent<RoutingDomainsObject> implements
	RoutingDomainManager {

	/** Loader. */
	private final RoutingDomainsLoader loader;

	/** Default constructor. */
	public RoutingDomainManagerImpl() {
		this.loader = new RoutingDomainsLoader();
	}

	@Override
	RoutingDomainsLoader getLoader() {
		return loader;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.RoutingDomainManager#getDefault(com.isotrol.impe3.pms.model.EnvironmentEntity,
	 * com.isotrol.impe3.pms.model.UserEntity)
	 */
	public RoutingDomainEntity getDefault(EnvironmentEntity e, UserEntity user) throws PMSException {
		RoutingDomainEntity entity = getDao().getRoutingDomainByName(e.getId(), DEFAULT);
		if (entity == null) {
			entity = new RoutingDomainEntity();
			entity.setEnvironment(e);
			entity.setCreated(user);
			entity.setUpdated(user);
			entity.setName(DEFAULT);
			entity.setOnlineBase(BASE);
			entity.setOfflineBase(BASE);
			saveNewEntity(entity);
		}
		return entity;
	}

	@Override
	int getOfflineVersion(EnvironmentEntity e) {
		return e.getDomainVersion();
	}

	private class RoutingDomainsLoader implements Loader<RoutingDomainsObject> {
		public RoutingDomainsObject load(EnvironmentEntity e) {
			return RoutingDomainsObject.of(e.getRoutingDomains());
		}

		public RoutingDomainsObject load(EditionEntity e) {
			return load(e.getEnvironment());
		}

		@Override
		public String toString() {
			return "Routing domains";
		}
	}

}
