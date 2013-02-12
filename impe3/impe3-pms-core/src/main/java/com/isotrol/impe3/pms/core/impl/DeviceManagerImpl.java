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


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.DeviceManager;
import com.isotrol.impe3.pms.core.obj.DevicesObject;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Implementation of DeviceManager.
 * @author Andres Rodriguez.
 */
@Service("deviceManager")
public final class DeviceManagerImpl extends AbstractLoaderComponent implements DeviceManager {
	/**
	 * Constructor.
	 */
	public DeviceManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.DeviceManager#findOrCreate(com.isotrol.impe3.pms.model.UserEntity,
	 * com.isotrol.impe3.api.DeviceType, java.lang.String, java.lang.String, boolean, java.lang.Integer)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public DeviceEntity findOrCreate(UserEntity user, DeviceType type, String name, String description, boolean layout,
		Integer width) throws PMSException {
		DeviceEntity device = getDao().getDeviceByName(name);
		if (device != null) {
			return device;
		}
		device = new DeviceEntity();
		device.setCreated(user);
		device.setUpdated(user);
		device.setType(type.toString());
		device.setName(name);
		device.setDescription(description);
		device.setWidth(width);
		saveNewEntity(device);
		return device;
	}

	private List<DeviceEntity> findAll() {
		return Lists.newArrayList(getDao().findAll(DeviceEntity.class));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.DeviceManager#load(java.util.UUID)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public DevicesObject load(UUID envId) {
		return cache.apply(getEnvironment(envId));
	}

	/** Device cache. */
	private final Cache<DevicesObject> cache = new Cache<DevicesObject>(60) {
		@Override
		int getVersion(EnvironmentEntity e) {
			return e.getDeviceVersion();
		}

		@Override
		DevicesObject compute(EnvironmentEntity e) {
			return DevicesObject.load(findAll());
		}
	};
}
