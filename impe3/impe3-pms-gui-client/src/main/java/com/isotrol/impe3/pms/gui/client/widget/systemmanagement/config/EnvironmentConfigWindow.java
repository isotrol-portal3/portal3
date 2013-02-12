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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.config;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.session.EnvironmentConfigDTO;
import com.isotrol.impe3.pms.gui.api.service.ISessionsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;


/**
 * Panel with the Pms environment configuration
 * 
 * @author Manuel Ruiz
 * 
 */
public class EnvironmentConfigWindow extends TypicalWindow implements IDetailPanel {

	/**
	 * Main container.<br/>
	 */
	private LayoutContainer container = null;
	/*
	 * form fields
	 */

	/**
	 * "Max number of attempts to login" field
	 */
	private NumberField tfMaxLoginAttempts = null;

	/**
	 * "Internal requests segment" field
	 */
	private TextField<String> tfSegment = null;

	/**
	 * fields that may fire Change events.<br/>
	 */
	private List<Component> fields = null;

	/**
	 * The current portal cache
	 */
	private EnvironmentConfigDTO envConfigDto = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Proxy to Sessions async service.<br/>
	 */
	private ISessionsServiceAsync sessionsService = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Forms helper object<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * GuiCommon styles service
	 */
	private GuiCommonStyles styles = null;

	/**
	 * PMS styles service
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;
	/**
	 * PMS events listening strategy.<br/>
	 */
	private IComponentListeningStrategy pmsListeningStrategy = null;

	/**
	 * Constructor.<br/>
	 * 
	 * @param portalTemplate
	 * @param portal
	 */
	public EnvironmentConfigWindow() {
	}

	/**
	 * Inits the widget.
	 * @param templateDto
	 */
	public void initWidget(EnvironmentConfigDTO config) {
		this.envConfigDto = config;

		initThis();
		initComponent();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {

		FormLayout layout = formSupport.getStandardLayout(false, 200);
		layout.setLabelWidth(175);
		container = new LayoutContainer(layout);
		container.setBorders(false);
		container.addStyleName(styles.margin10px());
		add(container);

		addFormFields();

		if (isEdition()) {
			displayModelValues();
		}

		addButtonBar();
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setModal(true);
		setWidth(500);
		setAutoHeight(true);
		setHeading(pmsMessages.menuItem2Configuration());
	}

	private void addFormFields() {

		// field internal requests segment
		tfSegment = new TextField<String>();
		tfSegment.setFieldLabel(pmsMessages.labelInternalRequestSegment());
		tfSegment.setValidateOnBlur(true);
		formSupport.configRequired(tfSegment);
		container.add(tfSegment);

		// field max number of login attempts
		tfMaxLoginAttempts = new NumberField();
		tfMaxLoginAttempts.setPropertyEditorType(Integer.class);
		tfMaxLoginAttempts.setFieldLabel(pmsMessages.labelMaxNumberLogin());
		tfMaxLoginAttempts.setAutoValidate(true);
		tfMaxLoginAttempts.setValidateOnBlur(true);
		container.add(tfMaxLoginAttempts);

		fields = Arrays.asList(new Component[] {tfSegment, tfMaxLoginAttempts});
	}

	/**
	 * Displays data corresponding to currently bound portal.
	 */
	private void displayModelValues() {

		tfSegment.setValue(envConfigDto.getInternalSegment());
		tfSegment.updateOriginalValue(envConfigDto.getInternalSegment());

		tfMaxLoginAttempts.setValue(envConfigDto.getMaxLoginAttempts());
		tfMaxLoginAttempts.updateOriginalValue(envConfigDto.getMaxLoginAttempts());
	}

	/**
	 * Creates, configures & adds the buttons bar.<br/>
	 */
	private void addButtonBar() {

		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button button = we.getButtonClicked();
						if (button.getItemId().equals(Dialog.YES)) { // pressed
							// OK
							trySaveCurrentValues();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSaveConfig(), listener);
			}
		};
		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(this, listener, new LinkedList<Component>(
			fields), pmsListeningStrategy);
		addButton(bAccept);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);

	}

	/**
	 * Retrieves the values from the components, inserts them into a {@link EnvironmentConfigDTO}, and calls the service
	 * <code>setEnvironmentConfig</code> method.<br/>
	 */
	private void trySaveCurrentValues() {

		util.mask(pmsMessages.mskSavingConfiguration());

		envConfigDto.setInternalSegment(tfSegment.getValue());
		envConfigDto.setMaxLoginAttempts(tfMaxLoginAttempts.getValue().intValue());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				util.unmask();
				hide();
				util.info(pmsMessages.msgSuccessSaveConfig());
			}

			public void onFailure(Throwable caught) {
				util.unmask();
				util.error(pmsMessages.msgErrorSaveConfig());
			}
		};
		sessionsService.setEnvironmentConfig(envConfigDto, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		boolean valid = true;
		Iterator<Component> it = fields.iterator();
		while (valid && it.hasNext()) {
			Component component = it.next();
			if (component instanceof Field<?>) {
				valid = valid && ((Field<?>) component).isValid();
			}
		}
		return valid;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		boolean dirty = false;
		Iterator<Component> it = fields.iterator();
		while (!dirty && it.hasNext()) {
			Component component = it.next();
			if (component instanceof Field<?>) {
				dirty = dirty || ((Field<?>) component).isDirty();
			}
		}
		return dirty;
	}

	/**
	 * @return the {@link #messages}
	 */
	protected final GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * @return the {@link #pmsMessages}
	 */
	protected final PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return shared objects container
	 */
	protected final Util getUtilities() {
		return util;
	}

	/**
	 * Injects the generic messages bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS specific messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the Sessions async service proxy.
	 * @param sessionsService
	 */
	@Inject
	public void setSessionsService(ISessionsServiceAsync sessionsService) {
		this.sessionsService = sessionsService;
	}

	/**
	 * Injects the shared objects container.
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the form helper.
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * Injects the buttons helper
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * @return the buttonsSupport
	 */
	public Buttons getButtonsSupport() {
		return buttonsSupport;
	}

	/**
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = (IComponentListeningStrategy) pmsListeningStrategy;
	}

	/**
	 * @param errorProcessor the error processor.
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service error processor
	 */
	protected ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @return the container
	 */
	public LayoutContainer getDataContainer() {
		return container;
	}

	/**
	 * @return the pmsStyles
	 */
	public PmsStyles getPmsStyles() {
		return pmsStyles;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	public boolean isEdition() {
		return true;
	}
}
