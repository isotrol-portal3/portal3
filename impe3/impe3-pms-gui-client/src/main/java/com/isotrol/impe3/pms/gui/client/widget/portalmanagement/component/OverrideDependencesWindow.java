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


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.minst.DependencySetTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.DependencesPanel;


/**
 * Window with component's dependencies
 * @author Manuel Ruiz
 * 
 */
public class OverrideDependencesWindow extends TypicalWindow implements IDetailPanel {

	private String componentId = null;
	
	private String portalId = null;
	
	private DependencySetTemplateDTO dependencySetTemplate = null;
	
	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;
	
	/**
	 * Listener for accept button.<br/>
	 */
	private SelectionListener<ButtonEvent> acceptListener = null;

	/** Panel with the template's dependencies */
	private DependencesPanel depsPanel = null;

	/**
	 * PMS events listening strategy.<br/>
	 */
	private PmsListeningStrategy pmsListeningStrategy = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * Components service 
	 */
	private IComponentsServiceAsync componentsService = null;

	public void init(DependencySetTemplateDTO dependencySetTemplate, String portalId, String componentId) {
		this.portalId = portalId;
		this.componentId = componentId;
		this.dependencySetTemplate = dependencySetTemplate;
		
		initListeners();
		initThis();
		initComponents();
	}

	private void initThis() {
		setWidth(PmsConstants.DETAIL_WINDOW_WIDTH);
		setScrollMode(Scroll.AUTO);
		setHeading(pmsMessages.dependencesWidgetTitle());
	}


	/**
	 * Inits the listeners without binding them to any Observable.<br/>
	 */
	private void initListeners() {
		acceptListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							tryOverrideDependences();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmOverrideDependences(),
					lConfirm).setModal(true);
			}
		};
	}

	private void initComponents() {
		depsPanel.init(dependencySetTemplate.getDependencies());
		add(depsPanel);
		addButtonBar();
	}

	/**
	 * Adds "Accept" and "Cancel" controls.<br/>
	 */
	private void addButtonBar() {

		List<Component> components = new LinkedList<Component>();
		components.add(depsPanel);

		Button bSave = buttonsSupport.createSaveButtonForDetailPanels(this, acceptListener, components,
			pmsListeningStrategy);
		addButton(bSave);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}
	
	private void tryOverrideDependences() {
		
		depsPanel.applyValues();
		List<DependencyDTO> depsDto = dependencySetTemplate.toDependencyListDTO();
		
		AsyncCallback<InheritedComponentInstanceSelDTO> callback = new AsyncCallback<InheritedComponentInstanceSelDTO>() {
			
			public void onSuccess(InheritedComponentInstanceSelDTO result) {
				util.info(pmsMessages.msgSuccessOverrideDependences());
				hide();
			}
			
			public void onFailure(Throwable caught) {
				util.error(pmsMessages.msgErrorOverrideDependences());
			}
		};
		componentsService.overrideDependencies(portalId, componentId, depsDto, callback);
	}

	public boolean isEdition() {
		return true;
	}

	public boolean isDirty() {
		return depsPanel.isDirty();
	}

	public boolean isValid() {
		return depsPanel.isValid();
	}

	/**
	 * @param depsPanel the depsPanel to set
	 */
	@Inject
	public void setDepsPanel(DependencesPanel depsPanel) {
		this.depsPanel = depsPanel;
	}

	/**
	 * @param buttonsSupport the buttonsSupport to set
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @param pmsMessages the pmsMessages to set
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @param componentsService the componentsService to set
	 */
	@Inject
	public void setComponentsService(IComponentsServiceAsync componentsService) {
		this.componentsService = componentsService;
	}

	/**
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = pmsListeningStrategy;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}
}
