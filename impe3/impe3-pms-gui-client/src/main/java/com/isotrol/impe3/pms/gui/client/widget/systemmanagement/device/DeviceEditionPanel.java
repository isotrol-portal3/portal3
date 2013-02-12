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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device;


import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.device.DeviceDTO;


/**
 * Creates the devices edition form.
 * 
 * @author Manuel Ruiz
 * 
 */
public class DeviceEditionPanel extends ADeviceDetailsEditor {

	/**
	 * Constructor
	 */
	public DeviceEditionPanel() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device.ADeviceDetailsEditor#getAcceptButtonListener()
	 */
	@Override
	protected SelectionListener<ButtonEvent> getAcceptButtonListener() {
		return new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				// ask confirm:
				Listener<MessageBoxEvent> mbListener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button b = we.getButtonClicked();
						if (b.getItemId().equals(Dialog.YES)) {
							// confirmed:
							trySaveDevice();
						}
					}
				};
				MessageBox.confirm(getPmsMessages().headerConfirmUpdateDevice(),
					getPmsMessages().msgConfirmUpdateDevice(), mbListener).setModal(true);
			}
		};
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device.ADeviceDetailsEditor#getHeadingText()
	 */
	protected String getHeadingText() {
		return getPmsMessages().headerDeviceEditWindow() + ": " + getDevice().getName();
	}

	/**
	 * Fired when user confirmed that he wants to commit the current Device changes.<br/>
	 */
	private void trySaveDevice() {
		getUtilities().mask(getPmsMessages().mskSaveDevice());

		DeviceDTO dDto = getDevice();
		dDto = updateDeviceDto(dDto);

		AsyncCallback<DeviceDTO> callback = new AsyncCallback<DeviceDTO>() {
			public void onFailure(Throwable arg0) {
				getUtilities().unmask();
				getUtilities().error(getPmsMessages().msgErrorSaveDevice());
			}

			public void onSuccess(DeviceDTO arg0) {
				hide();
				getUtilities().unmask();
				getUtilities().info(getPmsMessages().msgSuccessSaveDevice());
			}
		};

		getDevicesService().update(dDto, callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return true;
	}

}
