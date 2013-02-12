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


import static com.isotrol.impe3.pms.core.support.NotFoundProviders.DEVICE;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.Devices;
import com.isotrol.impe3.core.impl.AbstractIdentifiableHierarchy;
import com.isotrol.impe3.core.impl.DevicesFactory;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.device.DeviceTreeDTO;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.model.DeviceEntity;


/**
 * Collection of devices domain object.
 * @author Andres Rodriguez
 */
public final class DevicesObject extends AbstractIdentifiableHierarchy<DeviceObject> {
	private static final Ordering<DeviceEntity> BY_ORDER = Ordering.natural().onResultOf(
		new Function<DeviceEntity, Integer>() {
			public Integer apply(DeviceEntity from) {
				return from.getOrder();
			}
		});

	/** Devices object hierarchy. */
	private final ImmutableHierarchy<UUID, DeviceObject> hierarchy;
	/** Devices hierarchy. */
	private final Devices devices;
	/** By name index. */
	private final Map<String, DeviceObject> byName;

	/**
	 * Builds a collection from a set of entities.
	 * @param entities Device entities.
	 * @return The requested collection.
	 */
	public static DevicesObject load(Iterable<DeviceEntity> entities) {
		return new DevicesObject(entities);
	}

	/**
	 * Constructor.
	 * @param entities Device entities.
	 */
	private DevicesObject(Iterable<DeviceEntity> entities) {
		final Iterable<DeviceEntity> ordered = BY_ORDER.sortedCopy(entities);
		ImmutableHierarchy.Builder<UUID, Device> b = ImmutableHierarchy.builder();
		ImmutableHierarchy.Builder<UUID, DeviceObject> bo = ImmutableHierarchy.builder();
		for (DeviceEntity entity : ordered) {
			final UUID id = entity.getId();
			final UUID parentId = entity.getParentId();
			final DeviceObject device = new DeviceObject(entity);
			bo.add(id, device, parentId);
			b.add(id, device.getDevice(), parentId);
		}
		this.hierarchy = bo.get();
		this.devices = DevicesFactory.of(b.get());
		this.byName = ImmutableMap.copyOf(Maps.uniqueIndex(this.hierarchy.values(), DeviceObject.NAME));
	}

	@Override
	protected Hierarchy<UUID, DeviceObject> delegate() {
		return hierarchy;
	}

	public DeviceObject load(UUID id) throws EntityNotFoundException {
		return DEVICE.checkNotNull(get(id), id);
	}

	public DeviceObject load(DeviceEntity e) {
		if (e == null) {
			return null;
		}
		return get(e.getId());
	}

	public DeviceObject getByName(String name) {
		return byName.get(name);
	}

	public DeviceObject load(String id) throws EntityNotFoundException {
		return load(DEVICE.toUUID(id));
	}

	public List<DeviceTreeDTO> map2tree() {
		if (devices.isEmpty()) {
			return null;
		}
		final Function<DeviceObject, DeviceTreeDTO> tree = new Function<DeviceObject, DeviceTreeDTO>() {
			public DeviceTreeDTO apply(DeviceObject from) {
				final DeviceTreeDTO dto = new DeviceTreeDTO();
				dto.setNode(from.toSelDTO());
				dto.setChildren(Mappers.list(getChildren(from.getId()), this));
				return dto;
			}
		};
		return Mappers.list(getFirstLevel(), tree);
	}

	public Devices getDevices() {
		return devices;
	}

}
