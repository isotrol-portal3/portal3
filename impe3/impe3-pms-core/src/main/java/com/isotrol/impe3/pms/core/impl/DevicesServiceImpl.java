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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.api.Devices;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.device.DeviceDTO;
import com.isotrol.impe3.pms.api.device.DeviceTreeDTO;
import com.isotrol.impe3.pms.api.device.DevicesService;
import com.isotrol.impe3.pms.core.DeviceManager;
import com.isotrol.impe3.pms.core.obj.DeviceObject;
import com.isotrol.impe3.pms.core.obj.DevicesObject;
import com.isotrol.impe3.pms.core.support.InUseProviders;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Implementation of DevicesService.
 * @author Andres Rodriguez.
 */
@Service("devicesService")
public final class DevicesServiceImpl extends AbstractEntityService<DeviceEntity> implements DevicesService {
	private final DeviceManager deviceManager;

	/** Default constructor. */
	@Autowired
	public DevicesServiceImpl(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.DEVICE;
	}

	private DevicesObject get() {
		return deviceManager.load(getEnvironmentId());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.device.DevicesService#getDevices()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.DEVICE_GET)
	public List<DeviceTreeDTO> getDevices() throws PMSException {
		return get().map2tree();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.device.DevicesService#get(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.DEVICE_GET)
	public DeviceDTO get(String id) throws PMSException {
		return get().load(id).toDTO();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.device.DevicesService#create(com.isotrol.impe3.pms.api.device.DeviceDTO,
	 * java.lang.String, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.DEVICE_SET)
	public DeviceDTO create(DeviceDTO dto, String parentId, int order) throws PMSException {
		validate(dto);
		final DeviceEntity parent = (parentId == null) ? null : load(parentId);
		DeviceEntity entity = new DeviceEntity();
		dto2dfn(dto, entity);
		entity.setOrder(new Tree().add(parent != null ? parent.getId() : null, order));
		entity.setParent(parent);
		entity = saveNew(entity);
		touch();
		sync();
		return get(entity.getId().toString());
	}

	private void touch() throws PMSException {
		UserEntity user = load(UserEntity.class, getUserId(), NotFoundProviders.USER);
		getEnvironment().touchDeviceVersion(user);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.device.DevicesService#update(com.isotrol.impe3.pms.api.device.DeviceDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.DEVICE_SET)
	public DeviceDTO update(DeviceDTO dto) throws PMSException {
		validate(dto);
		final String id = dto.getId();
		final DeviceEntity entity = load(id);
		dto2dfn(dto, entity);
		touch();
		sync();
		return get(id);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.device.DevicesService#move(java.lang.String, java.lang.String, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.DEVICE_SET)
	public List<DeviceTreeDTO> move(String deviceId, String parentId, int order) throws PMSException {
		final Tree tree = new Tree();
		final DevicesObject devices = get();
		final DeviceObject device = devices.load(deviceId);
		final DeviceObject parent = parentId == null ? null : devices.load(parentId);
		final UUID currentParentId = tree.devices.getParentKey(device.getId());
		final UUID deviceUUID = device.getId();
		final UUID parentUUID = parent != null ? parent.getId() : null;
		if (!Objects.equal(currentParentId, parentUUID)) {
			tree.remove(deviceUUID);
			updatePosition(load(deviceUUID), parentUUID != null ? load(parentUUID) : null, tree.add(parentUUID, order));
		} else {
			tree.move(deviceUUID, order);
		}
		touch();
		sync();
		return get().map2tree();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.device.DevicesService#delete(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.DEVICE_SET)
	public List<DeviceTreeDTO> delete(String deviceId) throws PMSException {
		final DeviceObject device = get().load(deviceId);
		InUseProviders.DEVICE.checkUsed(DeviceManager.DEFAULT.equals(device.getName()), deviceId);
		InUseProviders.DEVICE.checkUsed(!getDao().getPagesByDefaultDevice(device.getId()).isEmpty(), deviceId);
		return get().map2tree();
	}

	private static void dto2dfn(DeviceDTO dto, DeviceEntity entity) {
		final DeviceType type = dto.getType();
		if (!DeviceManager.DEFAULT.equals(entity.getName())) {
			entity.setName(dto.getName());
			type2entity(type, entity);
			entity.setWidth(dto.getWidth());
			entity.setUserAgent(dto.getUserAgent());
			entity.setUserAgentRE(dto.isUserAgentRE());
			final Map<String, String> map = entity.getProperties();
			map.clear();
			final List<PropertyDTO> properties = dto.getProperties();
			if (properties != null && !properties.isEmpty()) {
				for (PropertyDTO prop : properties) {
					map.put(checkNotNull(prop.getName()), checkNotNull(prop.getValue()));
				}
			}
		} else if (type == DeviceType.HTML || type == DeviceType.XHTML) {
			type2entity(type, entity);
		}
		entity.setDescription(dto.getDescription());
	}

	private static void type2entity(DeviceType type, DeviceEntity entity) {
		entity.setType(type.name());
		entity.setWidth(type.getWidth());
	}

	private static void validate(DeviceDTO dto) {
		checkNotNull(dto);
		checkArgument(dto.getName() != null);
		checkArgument(dto.getType() != null);
		checkArgument(!dto.getType().isLayout() || dto.getWidth() != null);
	}

	private void updatePosition(DeviceEntity entity, DeviceEntity parent, int order) throws PMSException {
		entity.setOrder(order);
		entity.setParent(parent);
	}

	private class Tree {
		private final Devices devices = get().getDevices();

		Tree() {
		}

		private void setOrder(UUID id, int order) throws PMSException {
			final DeviceEntity entity = load(id);
			if (entity.getOrder() != order) {
				updatePosition(entity, entity.getParent(), order);
			}
		}

		private List<UUID> children(UUID id) {
			if (id == null) {
				return devices.getFirstLevelKeys();
			}
			return devices.getChildrenKeys(id);
		}

		int add(UUID parentId, int order) throws PMSException {
			final int o = Math.max(0, order);
			final List<UUID> children = children(parentId);
			final int n = children.size();
			if (o > n) {
				return n;
			}
			for (int i = 0; i < n; i++) {
				final int newOrder = (i < o) ? i : i + 1;
				setOrder(children.get(i), newOrder);
			}
			return o;
		}

		private List<UUID> siblings(UUID id) {
			return children(devices.getParentKey(id));
		}

		void remove(UUID id) throws PMSException {
			final List<UUID> children = siblings(id);
			final int o = children.indexOf(id);
			final int n = children.size();
			for (int i = 0; i < n; i++) {
				if (i != o) {
					final int newOrder = (i < o) ? i : i - 1;
					setOrder(children.get(i), newOrder);
				}
			}
		}

		void move(UUID id, int order) throws PMSException {
			final List<UUID> children = siblings(id);
			final int n = children.size();
			// Normalize list
			for (int i = 0; i < n; i++) {
				setOrder(children.get(i), i);
			}
			final int current = load(id).getOrder();
			final int o = Math.min(Math.max(0, order), n - 1);
			if (o == current) {
				return;
			}
			setOrder(id, o);
			if (o < current) {
				for (int i = o; i < current; i++) {
					setOrder(children.get(i), i + 1);
				}
			} else {
				for (int i = current + 1; i <= o; i++) {
					setOrder(children.get(i), i - 1);
				}
			}
		}
	}

}
