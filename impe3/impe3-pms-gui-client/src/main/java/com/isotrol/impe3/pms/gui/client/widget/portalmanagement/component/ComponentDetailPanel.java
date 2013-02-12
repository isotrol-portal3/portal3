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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ComponentsErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.AModuleDetailPanel;


/**
 * Component creation/edition panel
 * 
 * @author Andrei Cojocaru
 * 
 */
public class ComponentDetailPanel extends AModuleDetailPanel {

	/** portal's id */
	private String idPortal = null;

	/*
	 * Injected deps
	 */
	/**
	 * Proxy of the components service.<br/>
	 */
	private IComponentsServiceAsync componentsService = null;

	/**
	 * Error message resolver for components service.<br/>
	 */
	private ComponentsErrorMessageResolver emrComponents = null;

	/**
	 * Constructor provided with portal ID and the module template DTO.<br/>
	 * 
	 * @param idPortal
	 * @param data
	 */
	public ComponentDetailPanel() {
	}

	/**
	 * Inits the component
	 */
	public void init(String id, ModuleInstanceTemplateDTO data) {
		this.idPortal = id;
		super.init(data);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector
	 * .AModuleDetailPanel#trySaveModuleInstance()
	 */
	@Override
	protected void trySaveModuleInstance(final boolean close) {
		mask(getPmsMessages().mskSaveComponent());

		ModuleInstanceTemplateDTO template = getBoundModule();
		ModuleInstanceDTO dto = template.toModuleInstanceDTO();

		AsyncCallback<ModuleInstanceTemplateDTO> callback = new AsyncCallback<ModuleInstanceTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				unmask();
				getErrorProcessor().processError(arg0, emrComponents, getPmsMessages().msgErrorInstantiateComponent());
			}

			public void onSuccess(ModuleInstanceTemplateDTO arg0) {
				if (close) {
					getButtonsSupport().closeActiveWindow();
				} else {
					getModuleInstanceTemplateDto().setId(arg0.getId());
					disableSaveButtons();
				}
				unmask();
				getUtilities().info(getPmsMessages().msgSuccessSaveComponent());
			}
		};

		componentsService.save(idPortal, dto, callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector
	 * .AModuleDetailPanel#getModuleSaveConfirmText()
	 */
	@Override
	protected String getModuleSaveConfirmText() {
		return getPmsMessages().msgConfirmSaveComponent();
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return getBoundModule().getId() != null;
	}

	/**
	 * Injects the {@link #componentsService Components service proxy}.
	 * @param componentsService
	 */
	@Inject
	public void setComponentsService(IComponentsServiceAsync componentsService) {
		this.componentsService = componentsService;
	}

	/**
	 * Injects the Error Message Resolver for the Components service.
	 * @param emrComponents the emrComponents to set
	 */
	@Inject
	public void setEmrComponents(ComponentsErrorMessageResolver emrComponents) {
		this.emrComponents = emrComponents;
	}
}
