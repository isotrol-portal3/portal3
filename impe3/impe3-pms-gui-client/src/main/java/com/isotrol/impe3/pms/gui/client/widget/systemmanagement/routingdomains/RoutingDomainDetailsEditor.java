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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.routingdomains;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
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
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.rd.RoutingDomainDTO;
import com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Popup window that manages the detailed information of a Routing Domain
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class RoutingDomainDetailsEditor extends TypicalWindow implements IDetailPanel {

	/**
	 * Main container.<br/>
	 */
	private LayoutContainer mainContainer = null;

	/**
	 * Change events source fields.<br/>
	 */
	private List<Field<?>> fields = null;

	/*
	 * fields
	 */
	/**
	 * "name" field<br/>
	 */
	private TextField<String> tfName = null;
	/**
	 * "description" field<br/>
	 */
	private TextArea taDesc = null;

	/**
	 * "Online Base" field<br/>
	 */
	private TextField<String> tfOnlineBase = null;

	/**
	 * "Absolute Online Path" field<br/>
	 */
	private TextField<String> tfAbsOnlineBase = null;

	/**
	 * Bound DTO.<br/>
	 */
	private RoutingDomainDTO routingDomainDto = null;

	/*
	 * Injected fields.
	 */
	/**
	 * Generic messages routingDomainsService.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS spefcific messages routingDomainsService.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Routing Domains async routingDomainsService proxy.<br/>
	 */
	private IRoutingDomainsServiceAsync routingDomainsService = null;

	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Error Message Resolver for Routing domains.<br/>
	 */
	private IErrorMessageResolver emrRoutingDomains = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Shared objects provider.<br/>
	 */
	private Util util = null;

	/**
	 * Forms helper service.<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * pms events listening strategy<br/>
	 */
	private PmsListeningStrategy pmsListeningStrategy = null;

	/**
	 * Generic styles bundle
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Settings bundle
	 */
	private Settings settings = null;

	/**
	 * Constructor provided with bound data.<br/>
	 * 
	 * @param rdDto
	 */
	public RoutingDomainDetailsEditor() {
	}

	/**
	 * Inits the widget. Must be called after the properties are set.
	 * @param rdDto
	 */
	public void init(RoutingDomainDTO rdDto) {
		this.routingDomainDto = rdDto;
		initThis();
		initComponent();
	}

	/**
	 * Inits the container inner components.
	 */
	private void initComponent() {
		getHeader().addTool(buttonsSupport.createHelpToolButton(settings.pmsRoutingDomainsAdminPortalManualUrl()));
		addFields();
		displayBoundDataValues();
		addButtonBar();
	}

	/**
	 * Inits this widget properties.
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setButtonAlign(HorizontalAlignment.LEFT);
		setClosable(false);
		setWidth(FormSupport.RECOMMENDED_WIDTH);

		mainContainer = new LayoutContainer(formSupport.getStandardLayout());
		mainContainer.addStyleName(styles.margin10px());
		mainContainer.setAutoHeight(true);
		add(mainContainer);
	}

	/**
	 * Adds the GUI fields for name, description, and bases.
	 */
	protected void addFields() {
		// name
		tfName = new TextField<String>();
		tfName.setFieldLabel(pmsMessages.labelName());
		formSupport.configRequired(tfName);
		tfName.setAutoValidate(true);
		mainContainer.add(tfName);

		// desc
		taDesc = new TextArea();
		taDesc.setFieldLabel(pmsMessages.labelDescription());
		mainContainer.add(taDesc);

		// offline base
		tfOnlineBase = new TextField<String>();
		tfOnlineBase.setFieldLabel(pmsMessages.labelOnlineBase());
		formSupport.configRequired(tfOnlineBase);
		tfOnlineBase.setAutoValidate(true);
		mainContainer.add(tfOnlineBase);

		// absolute offline base
		tfAbsOnlineBase = new TextField<String>();
		tfAbsOnlineBase.setFieldLabel(pmsMessages.labelOnlineAbsBase());
		mainContainer.add(tfAbsOnlineBase);

		fields = new LinkedList<Field<?>>(Arrays.asList(new Field<?>[] {tfName, taDesc, tfOnlineBase, tfAbsOnlineBase}));
	}

	/**
	 * Creates, configures and adds the button bar.
	 */
	private void addButtonBar() {
		// buttons bar

		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent be) {
				Listener<MessageBoxEvent> wListener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button clicked = we.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							trySave();
						}
					}
				};
				MessageBox
					.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSaveRoutingDomain(), wListener)
					.setModal(true);
			}
		};
		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(this, listener, new LinkedList<Component>(
			fields), pmsListeningStrategy);
		addButton(bAccept);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Displays the vallues contained in the bound DTO.
	 */
	protected void displayBoundDataValues() {

		String title = null;
		if (routingDomainDto.getId() == null) {
			title = pmsMessages.headerRoutingDomainEditorNew(); // create
		} else { // edit
			title = pmsMessages.headerRoutingDomainEditorEdit(routingDomainDto.getName());
		}
		setHeadingText(title);

		String name = routingDomainDto.getName();
		tfName.setValue(name);
		tfName.updateOriginalValue(name);

		String desc = routingDomainDto.getDescription();
		taDesc.setValue(desc);
		taDesc.updateOriginalValue(desc);

		String offBase = routingDomainDto.getOnlineBase();
		tfOnlineBase.setValue(offBase);
		tfOnlineBase.updateOriginalValue(offBase);

		String aOffPath = routingDomainDto.getOnlineAbsBase();
		tfAbsOnlineBase.setValue(aOffPath);
		tfAbsOnlineBase.updateOriginalValue(aOffPath);
	}

	/**
	 * Calls <b>save</b> remote procedure on {@link #routingDomainsService the service}.
	 */
	private void trySave() {
		util.mask(pmsMessages.msgSaveRoutingDomain());

		applyValues();

		AsyncCallback<RoutingDomainDTO> callback = new AsyncCallback<RoutingDomainDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrRoutingDomains, pmsMessages.msgErrorSaveRoutingDomain());
			}

			public void onSuccess(RoutingDomainDTO arg0) {
				hide();
				util.unmask();
				util.info(pmsMessages.msgSuccessSaveRoutingDomain());
			}
		};

		saveRoutingDomain(callback);
	}

	protected void saveRoutingDomain(AsyncCallback<RoutingDomainDTO> callback) {
		routingDomainsService.save(routingDomainDto, callback);
	}

	/**
	 * Sets DTO values from GUI.<br/>
	 */
	protected void applyValues() {
		routingDomainDto.setName(tfName.getValue());
		routingDomainDto.setDescription(taDesc.getValue());
		routingDomainDto.setOnlineBase(tfOnlineBase.getValue());
		routingDomainDto.setOnlineAbsBase(tfAbsOnlineBase.getValue());
	}

	/**
	 * Checks if the component is dirty.<br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		boolean dirty = false;
		Iterator<Field<?>> it = fields.iterator();
		while (it.hasNext() && !dirty) {
			dirty = dirty || it.next().isDirty();
		}
		return dirty;
	}

	/**
	 * Checks if input values are valid.<br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		boolean valid = true;
		Iterator<Field<?>> it = fields.iterator();
		while (valid && it.hasNext()) {
			valid = it.next().isValid();
		}
		return valid;
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return routingDomainDto.getId() != null;
	}

	/**
	 * Injects the generic message bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS specific message bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @return the pmsMessages
	 */
	public PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * Injects the Routing Domains async service.
	 * @param routingDomainsService
	 */
	@Inject
	public void setRoutingDomainsService(IRoutingDomainsServiceAsync routingDomainsService) {
		this.routingDomainsService = routingDomainsService;
	}

	/**
	 * @return the routingDomainsService
	 */
	public IRoutingDomainsServiceAsync getRoutingDomainsService() {
		return routingDomainsService;
	}

	/**
	 * Injects the buttons helper.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the shared objects bundle.
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the forms helper
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * @return the formSupport
	 */
	public FormSupport getFormSupport() {
		return formSupport;
	}

	/**
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = pmsListeningStrategy;
	}

	/**
	 * Injects the Error Message Resolver for Routing Domains.
	 * @param emrRoutingDomains the emrRoutingDomains to set
	 */
	@Inject
	public void setEmrRoutingDomains(IErrorMessageResolver emrRoutingDomains) {
		this.emrRoutingDomains = emrRoutingDomains;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @return the mainContainer
	 */
	public LayoutContainer getMainContainer() {
		return mainContainer;
	}

	/**
	 * @return the routingDomainDto
	 */
	public RoutingDomainDTO getRoutingDomainDto() {
		return routingDomainDto;
	}

	/**
	 * @return the fields
	 */
	public List<Field<?>> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field<?>> fields) {
		this.fields = fields;
	}

	/**
	 * @return the tfName
	 */
	public TextField<String> getTfName() {
		return tfName;
	}

	/**
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
