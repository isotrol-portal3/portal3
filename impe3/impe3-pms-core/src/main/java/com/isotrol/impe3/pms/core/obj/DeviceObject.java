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


import static com.isotrol.impe3.pms.core.support.Mappers.prop2dto;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.api.UserAgentPattern;
import com.isotrol.impe3.pms.api.device.DeviceDTO;
import com.isotrol.impe3.pms.api.device.DeviceSelDTO;
import com.isotrol.impe3.pms.core.DeviceManager;
import com.isotrol.impe3.pms.model.DeviceEntity;


/**
 * Devices domain object.
 * @author Andres Rodriguez
 */
public final class DeviceObject extends AbstractIdentifiable {
	static final Function<DeviceObject, String> NAME = new Function<DeviceObject, String>() {
		public String apply(DeviceObject from) {
			return from.getName();
		}
	};

	/** Device object. */
	private final Device device;
	/** User agent. */
	private final String userAgent;
	/** Whether the user agent is a regular expression. */
	private final boolean userAgentRE;

	/**
	 * Constructor.
	 * @param entity Device entity.
	 */
	DeviceObject(DeviceEntity entity) {
		super(entity.getId());
		this.userAgentRE = entity.isUserAgentRE();
		this.userAgent = entity.getUserAgent();
		final UserAgentPattern ua = userAgentRE ? UserAgentPattern.safeRE(userAgent) : UserAgentPattern
			.exact(userAgent);
		this.device = new Device(entity.getId(), Enum.valueOf(DeviceType.class, entity.getType()), entity.getName(),
			entity.getDescription(), entity.getWidth(), ua, entity.getProperties());
	}

	public Device getDevice() {
		return device;
	}

	/**
	 * Returns the device type.
	 * @return The device type.
	 */
	public DeviceType getType() {
		return device.getType();
	}

	/**
	 * Returns the device name.
	 * @return The device name.
	 */
	public String getName() {
		return device.getName();
	}

	/**
	 * Returns the device description.
	 * @return The device description.
	 */
	public String getDescription() {
		return device.getDescription();
	}

	/**
	 * Returns whether the device supports layout.
	 * @return True if the device supports layout.
	 */
	public boolean isLayout() {
		return device.isLayout();
	}

	/**
	 * Returns the device width.
	 * @return The device width.
	 */
	public Integer getWidth() {
		return device.getWidth();
	}

	public ImmutableMap<String, String> getProperties() {
		return device.getProperties();
	}

	private <T extends DeviceSelDTO> T fill(T dto) {
		dto.setId(device.getStringId());
		dto.setName(device.getName());
		dto.setType(device.getType());
		dto.setWidth(device.getWidth());
		return dto;
	}

	public DeviceSelDTO toSelDTO() {
		return fill(new DeviceSelDTO());
	}

	public DeviceDTO toDTO() {
		final DeviceDTO dto = new DeviceDTO();
		fill(dto);
		dto.setDefaultDevice(DeviceManager.DEFAULT.equals(device.getName()));
		dto.setDescription(device.getDescription());
		dto.setUserAgent(userAgent);
		dto.setUserAgentRE(userAgentRE);
		dto.setProperties(prop2dto(device.getProperties()));
		return dto;
	}
}
