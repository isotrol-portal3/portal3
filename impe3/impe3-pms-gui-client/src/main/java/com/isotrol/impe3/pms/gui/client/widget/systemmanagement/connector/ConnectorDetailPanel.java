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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ConnectorsErrorMessageResolver;


/**
 * Shows the detail of a connector
 * 
 * @author Manuel Ruiz
 * 
 */
public class ConnectorDetailPanel extends AModuleDetailPanel {

	/**
	 * Connectors async service proxy.<br/>
	 */
	private IConnectorsServiceAsync connectorsService = null;

	/*
	 * Injected deps
	 */
	/**
	 * Error message resolver for connectors service.<br/>
	 */
	private ConnectorsErrorMessageResolver emrConnectors = null;

	/**
	 * Constructor. Creates the mapping panel
	 * @param service
	 */
	public ConnectorDetailPanel() {
	}

	/**
	 * Add a help button
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.AModuleDetailPanel#initComponent()
	 */
	@Override
	protected void initComponent() {
		super.initComponent();
		getHeader()
			.addTool(getButtonsSupport().createHelpToolButton(getSettings().pmsConnectorsAdminPortalManualUrl()));
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.AModuleDetailPanel#trySaveModuleInstance()
	 */
	@Override
	protected final void trySaveModuleInstance(final boolean close) {
		getUtilities().mask(getPmsMessages().mskSaveConnector());

		ModuleInstanceTemplateDTO template = getBoundModule();
		ModuleInstanceDTO dto = template.toModuleInstanceDTO();

		AsyncCallback<ModuleInstanceTemplateDTO> callback = new AsyncCallback<ModuleInstanceTemplateDTO>() {

			public void onFailure(Throwable arg0) {
				getUtilities().unmask();
				getErrorProcessor().processError(arg0, emrConnectors, getPmsMessages().msgErrorInstantiateConnector());
			}

			public void onSuccess(ModuleInstanceTemplateDTO arg0) {
				if (close) {
					getButtonsSupport().closeActiveWindow();
				} else {
					getModuleInstanceTemplateDto().setId(arg0.getId());
					disableSaveButtons();
				}
				getUtilities().unmask();
				getUtilities().info(getPmsMessages().msgSuccessSaveConnector());
			}
		};

		connectorsService.save(dto, callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector
	 * .AModuleDetailPanel#getModuleSaveConfirmText()
	 */
	@Override
	protected String getModuleSaveConfirmText() {
		return getPmsMessages().msgConfirmSaveConnector();
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return getBoundModule().getId() != null;
	}

	/**
	 * Injects the {@link #connectorsService Connectors async service}.
	 * @param connectorsService
	 */
	@Inject
	public void setConnectorsService(IConnectorsServiceAsync connectorsService) {
		this.connectorsService = connectorsService;
	}

	/**
	 * @param emrConnectors the emrConnectors to set
	 */
	@Inject
	public void setEmrConnectors(ConnectorsErrorMessageResolver emrConnectors) {
		this.emrConnectors = emrConnectors;
	}
}
