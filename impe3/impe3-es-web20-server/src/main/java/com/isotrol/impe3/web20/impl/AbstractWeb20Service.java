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


import java.util.UUID;

import com.isotrol.impe3.es.common.server.AbstractService;
import com.isotrol.impe3.web20.api.CommunityNotFoundException;
import com.isotrol.impe3.web20.dao.DAO;
import com.isotrol.impe3.web20.model.CommunityEntity;
import com.isotrol.impe3.web20.server.CommunityManager;


/**
 * Base service implementation.
 * @author Andres Rodriguez.
 */
public abstract class AbstractWeb20Service extends AbstractService<DAO> {
	/** Default constructor. */
	public AbstractWeb20Service() {
	}

	protected final CommunityEntity loadCommunity(UUID id) throws CommunityNotFoundException {
		if (id == null) {
			throw new CommunityNotFoundException(null);
		}
		final CommunityEntity entity = getDao().findById(CommunityEntity.class, id, false);
		if (entity == null || entity.isDeleted()) {
			throw new CommunityNotFoundException(id.toString());
		}
		return entity;
	}

	protected final CommunityEntity loadCommunity(String id) throws CommunityNotFoundException {
		if (id == null) {
			throw new CommunityNotFoundException(id);
		}
		final UUID uuid;
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new CommunityNotFoundException(id);
		}
		return loadCommunity(uuid);
	}

	protected final UUID getOptionalCommunityUUID(String id) throws CommunityNotFoundException {
		if (id == null) {
			return CommunityManager.GLOBAL_ID;
		}
		return loadCommunity(id).getId();
	}

}
