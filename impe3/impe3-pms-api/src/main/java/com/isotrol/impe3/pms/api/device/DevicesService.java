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

package com.isotrol.impe3.pms.api.device;


import java.util.List;

import com.isotrol.impe3.pms.api.PMSException;


/**
 * Devices service.
 * @author Andres Rodriguez.
 */
public interface DevicesService {
	/**
	 * Returns all registered devices.
	 * @return All registered devices.
	 */
	List<DeviceTreeDTO> getDevices() throws PMSException;

	/**
	 * Gets the detail of a device.
	 * @param id ID of the device.
	 * @return The requested detail.
	 * @throws PMSException If an error occurs.
	 */
	DeviceDTO get(String id) throws PMSException;

	/**
	 * Creates a device.
	 * @param dto Device to save.
	 * @param parent Parent device id.
	 * @param order Order.
	 * @return The saved device.
	 * @throws PMSException If an error occurs.
	 */
	DeviceDTO create(DeviceDTO dto, String parentId, int order) throws PMSException;

	/**
	 * Updates a device.
	 * @param dto Device to update.
	 * @return The updated device.
	 * @throws PMSException If an error occurs.
	 */
	DeviceDTO update(DeviceDTO dto) throws PMSException;

	/**
	 * Moves a device.
	 * @param deviceId Device Id.
	 * @param parentId Parent device.
	 * @param order Order.
	 * @return The updated device tree.
	 * @throws PMSException If an error occurs.
	 */
	List<DeviceTreeDTO> move(String deviceId, String parentId, int order) throws PMSException;

	/**
	 * Deletes a device.
	 * @param deviceId Device Id.
	 * @return The resulting device tree.
	 * @throws DeviceNotFoundException if the device is not found.
	 * @throws DeviceInUseException if the device is in use and cannot be deleted.
	 */
	List<DeviceTreeDTO> delete(String deviceId) throws PMSException;
}
