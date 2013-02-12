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

package com.isotrol.impe3.pms.gui.api.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.isotrol.impe3.pms.api.device.DeviceDTO;
import com.isotrol.impe3.pms.api.device.DeviceTreeDTO;

/**
 * GWT-RPC service for Devices. It is analog to {@link com.isotrol.impe3.pms.api.device.DevicesService}, but uses
 * GWT-compatible types.
 * 
 * @author Manuel Ruiz
 * 
 */
public interface IDevicesServiceAsync extends RemoteService {

	/**
	 * Returns all registered devices.
	 * @param callback All registered devices.
	 */
	void getDevices(AsyncCallback<List<DeviceTreeDTO>> callback);

	/**
	 * Gets the detail of a device.
	 * @param id ID of the device.
	 * @param callback The requested detail.
	 */
	void get(String id, AsyncCallback<DeviceDTO> callback);

	/**
	 * Creates a device.
	 * @param dto Device to save.
	 * @param parent Parent device id.
	 * @param order Order.
	 * @param callback The saved device.
	 */
	void create(DeviceDTO dto, String parentId, int order, AsyncCallback<DeviceDTO> callback);

	/**
	 * Updates a device.
	 * @param dto Device to update.
	 * @param callback The updated device.
	 */
	void update(DeviceDTO dto, AsyncCallback<DeviceDTO> callback);

	/**
	 * Moves a device.
	 * @param deviceId Device Id.
	 * @param parentId Parent device.
	 * @param order Order.
	 * @param callback The updated device tree.
	 */
	void move(String deviceId, String parentId, int order, AsyncCallback<List<DeviceTreeDTO>> callback);

	/**
	 * Deletes a device.
	 * @param deviceId Device Id.
	 * @param callback The resulting device tree.
	 */
	void delete(String deviceId, AsyncCallback<List<DeviceTreeDTO>> callback);

}
