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

package com.isotrol.impe3.pms.gui.client.controllers;


import java.util.List;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.device.DeviceDTO;
import com.isotrol.impe3.pms.api.device.DeviceTreeDTO;
import com.isotrol.impe3.pms.gui.api.service.IDevicesServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;


/**
 * Devices Service proxy decorator with event firing capabilities. Calls to service must be done through this Controller
 * instances.
 * 
 * @author Manuel Ruiz
 * 
 */
public class DevicesController implements IDevicesServiceAsync, ChangeEventSource {

	/**
	 * Async proxy to service.<br/>
	 */
	private IDevicesServiceAsync devicesService = null;

	/**
	 * Delegate object that implements {@link ChangeEventSource} interface.<br/>
	 */
	private ChangeEventSupport changeEventSupport = null;

	/**
	 * Default constructor.<br/>
	 */
	public DevicesController() {
		this.changeEventSupport = new ChangeEventSupport();
	}

	/**
	 * Fires {@link ChangeEventSource#Add} event.<br/> (non-Javadoc)
	 * 
	 */
	public void create(DeviceDTO device, String parentId, int order, final AsyncCallback<DeviceDTO> callback) {
		AsyncCallback<DeviceDTO> realCallback = new AsyncCallback<DeviceDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(DeviceDTO category) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.ADD, category);
				DevicesController.this.notify(event);
				callback.onSuccess(category);
			}
		};
		devicesService.create(device, parentId, order, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IDevicesServiceAsync#delete(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void delete(String deviceId, final AsyncCallback<List<DeviceTreeDTO>> callback) {
		AsyncCallback<List<DeviceTreeDTO>> realCallback = new AsyncCallback<List<DeviceTreeDTO>>() {
			public void onFailure(Throwable exception) {
				callback.onFailure(exception);
			}

			public void onSuccess(List<DeviceTreeDTO> list) {
				ChangeEvent event = new PmsChangeEvent(PmsChangeEvent.DELETE, list);
				DevicesController.this.notify(event);
				callback.onSuccess(list);
			}
		};
		devicesService.delete(deviceId, realCallback);
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IDevicesServiceAsync#get(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void get(String id, AsyncCallback<DeviceDTO> callback) {
		devicesService.get(id, callback);
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IDevicesServiceAsync#getDevices(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getDevices(AsyncCallback<List<DeviceTreeDTO>> callback) {
		devicesService.getDevices(callback);
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IDevicesServiceAsync#move(java.lang.String, java.lang.String, int, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void move(String deviceId, String parentId, int order, final AsyncCallback<List<DeviceTreeDTO>> callback) {
		AsyncCallback<List<DeviceTreeDTO>> realCallback = new AsyncCallback<List<DeviceTreeDTO>>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(List<DeviceTreeDTO> arg0) {
				ChangeEvent event = new PmsChangeEvent(PmsChangeEvent.DELETE, arg0);
				DevicesController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		devicesService.move(deviceId, parentId, order, realCallback);
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IDevicesServiceAsync#update(com.isotrol.impe3.pms.api.device.DeviceDTO, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void update(DeviceDTO dto, final AsyncCallback<DeviceDTO> callback) {
		AsyncCallback<DeviceDTO> realCallback = new AsyncCallback<DeviceDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(DeviceDTO dto) {

				ChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE, dto);
				DevicesController.this.notify(event);
				callback.onSuccess(dto);
			}
		};
		devicesService.update(dto, realCallback);
	}

	/*
	 * Delegate ChangeEventSource methods to changeEventsSupport object:
	 */
	/**
	 * @param listener
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport
	 * #addChangeListener(com.extjs.gxt.ui.client.data.ChangeListener[])
	 */
	public void addChangeListener(ChangeListener... listener) {
		changeEventSupport.addChangeListener(listener);
	}

	/**
	 * @param event
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport#notify(com.extjs.gxt.ui.client.data.ChangeEvent)
	 */
	public void notify(ChangeEvent event) {
		changeEventSupport.notify(event);
	}

	/**
	 * @param listener
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport
	 * #removeChangeListener(com.extjs.gxt.ui.client.data.ChangeListener[])
	 */
	public void removeChangeListener(ChangeListener... listener) {
		changeEventSupport.removeChangeListener(listener);
	}

	/**
	 * 
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport#removeChangeListeners()
	 */
	public void removeChangeListeners() {
		changeEventSupport.removeChangeListeners();
	}

	/**
	 * @param silent
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSupport#setSilent(boolean)
	 */
	public void setSilent(boolean silent) {
		changeEventSupport.setSilent(silent);
	}

	/**
	 * @param devicesService the devicesService to set
	 */
	public void setDevicesService(IDevicesServiceAsync devicesService) {
		this.devicesService = devicesService;
	}
}
