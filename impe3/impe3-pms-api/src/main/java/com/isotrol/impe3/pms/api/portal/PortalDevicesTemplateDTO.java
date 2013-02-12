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

package com.isotrol.impe3.pms.api.portal;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Template DTO for portal device selection.
 * @author Andres Rodriguez
 */
public class PortalDevicesTemplateDTO extends AbstractPortalDevicesDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 2155318013321842263L;
	/** Whether the portal is a child portal. */
	private boolean child;
	/** Devices. */
	private List<DeviceInPortalTreeDTO> devices;

	/** Default constructor. */
	public PortalDevicesTemplateDTO() {
	}

	/**
	 * Returns whether the portal is a child portal.
	 * @return True if the portal is a child portal.
	 */
	public boolean isChild() {
		return child;
	}

	/**
	 * Sets whether the portal is a child portal.
	 * @param child True if the portal is a child portal.
	 */
	public void setChild(boolean child) {
		this.child = child;
	}

	/**
	 * Returns the devices.
	 * @return The devices.
	 */
	public List<DeviceInPortalTreeDTO> getDevices() {
		return devices;
	}

	/**
	 * Sets the devices.
	 * @param devices The devices.
	 */
	public void setDevices(List<DeviceInPortalTreeDTO> devices) {
		this.devices = devices;
	}

	/**
	 * Turns the template DTO into an update DTO.
	 * @return The update DTO.
	 */
	public PortalDevicesDTO toPortalDevicesDTO() {
		Map<String, DiPDTO> uses = new HashMap<String, DiPDTO>();
		final String defaultId = fill(null, uses, devices);
		if (defaultId == null) {
			throw new IllegalArgumentException("No default device id");
		}
		final PortalDevicesDTO dto = new PortalDevicesDTO();
		dto.setId(getId());
		dto.setInherited(isInherited());
		dto.setDefaultId(defaultId);
		dto.setUses(uses);
		return dto;
	}

	private String fill(String defaultId, Map<String, DiPDTO> uses, Iterable<DeviceInPortalTreeDTO> level) {
		if (level != null) {
			for (DeviceInPortalTreeDTO dto : level) {
				DeviceInPortalDTO d = dto.getNode();
				String id = d.getDevice().getId();
				if (d.isActive()) {
					if (uses.containsKey(id)) {
						throw new IllegalArgumentException("Duplicated device id");
					}
					if (d.isDefaultDevice()) {
						if (defaultId != null) {
							throw new IllegalArgumentException("More than one default device id");
						}
						defaultId = id;
					}
					uses.put(id, new DiPDTO(d.getName(), d.getUse()));
				}
				defaultId = fill(defaultId, uses, dto.getChildren());
			}
		}
		return defaultId;
	}
}
