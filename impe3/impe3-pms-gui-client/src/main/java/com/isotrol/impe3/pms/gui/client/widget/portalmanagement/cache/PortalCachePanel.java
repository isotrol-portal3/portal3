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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.cache;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.api.ETagMode;
import com.isotrol.impe3.gui.common.data.SimpleModelData;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.portal.PortalCacheDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;


/**
 * Panel with portal cache management
 * 
 * @author Manuel Ruiz
 * 
 */
public class PortalCachePanel extends TypicalWindow implements IDetailPanel {

	/**
	 * Main container.<br/>
	 */
	private LayoutContainer container = null;
	/*
	 * form fields
	 */

	private CheckBox cbParentConfig = null;

	private CheckBox cbUseCache = null;

	private CheckBox cbPublic = null;
	
	/**
	 * The cache "ETag" field.<br/>
	 */
	private ComboBox<SimpleModelData> cbETag = null;

	/**
	 * "Change" field<br/>
	 */
	private NumberField tfModification = null;

	/**
	 * "Expiration" field<br/>
	 */
	private NumberField tfExpiration = null;

	/**
	 * fields that may fire Change events.<br/>
	 */
	private List<Component> fields = null;

	/**
	 * The current portal cache
	 */
	private PortalCacheDTO portalCacheDto = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Proxy to Portals async service.<br/>
	 */
	private IPortalsServiceAsync portalsService = null;

	/**
	 * Error Message Resolver for Portals service.<br/>
	 */
	private PortalsServiceErrorMessageResolver emrPortals = null;

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
	public PortalCachePanel() {
	}

	/**
	 * Inits the widget.
	 * @param templateDto
	 */
	public void initWidget(PortalCacheDTO result) {
		this.portalCacheDto = result;

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
		setHeadingText(pmsMessages.menuItem2Cache());
		setClosable(false);
		setShadow(false);
	}

	private void addFormFields() {

		cbParentConfig = new CheckBox();
		cbParentConfig.addInputStyleName(styles.checkBoxAlignLeft());
		cbParentConfig.setFieldLabel(pmsMessages.labelUseParentConfig());
		cbParentConfig.addListener(Events.Change, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				enableOrDisableFields(cbParentConfig.getValue());
			}
		});
		container.add(cbParentConfig);

		cbUseCache = new CheckBox();
		cbUseCache.addInputStyleName(styles.checkBoxAlignLeft());
		cbUseCache.setFieldLabel(pmsMessages.labelUseCache());
		container.add(cbUseCache);

		cbPublic = new CheckBox();
		cbPublic.addInputStyleName(styles.checkBoxAlignLeft());
		cbPublic.setFieldLabel(pmsMessages.labelPublic());
		container.add(cbPublic);

		// field modification
		tfModification = new NumberField();
		tfModification.setPropertyEditorType(Integer.class);
		tfModification.setFieldLabel(pmsMessages.labelModification());
		tfModification.setAutoValidate(true);
		tfModification.setValidateOnBlur(true);
		container.add(tfModification);

		// field expiration
		tfExpiration = new NumberField();
		tfExpiration.setPropertyEditorType(Integer.class);
		tfExpiration.setFieldLabel(pmsMessages.labelExpiration());
		tfExpiration.setAutoValidate(true);
		tfExpiration.setValidateOnBlur(true);
		container.add(tfExpiration);
		
		cbETag = new ComboBox<SimpleModelData>();
		cbETag.setFieldLabel(pmsMessages.labelETagMode());
		cbETag.setDisplayField(SimpleModelData.PROPERTY_DISPLAY_NAME);
		cbETag.setTriggerAction(TriggerAction.ALL);
		cbETag.setAllowBlank(false);
		cbETag.setForceSelection(true);
		cbETag.setEditable(false);
		// store population:
		ListStore<SimpleModelData> daStore = new ListStore<SimpleModelData>();
		cbETag.setStore(daStore);
		daStore.add(new SimpleModelData(pmsMessages.valueETagModeOff(), ETagMode.OFF));
		daStore.add(new SimpleModelData(pmsMessages.valueETagModeStrict(), ETagMode.STRICT));
		daStore.add(new SimpleModelData(pmsMessages.valueETagModeLax(), ETagMode.LAX));
		container.add(cbETag);

		fields = Arrays.asList(new Component[] {cbParentConfig, cbPublic, cbUseCache, tfModification, tfExpiration, cbETag});
	}

	/**
	 * Displays data corresponding to currently bound portal.
	 */
	private void displayModelValues() {

		cbParentConfig.setValue(portalCacheDto.isInherited());
		cbParentConfig.updateOriginalValue(portalCacheDto.isInherited());
		// enableOrDisableFields(portalCacheDto.isInherited());

		cbUseCache.setValue(portalCacheDto.isActive());
		cbUseCache.updateOriginalValue(portalCacheDto.isActive());

		cbPublic.setValue(portalCacheDto.isPublicCache());
		cbPublic.updateOriginalValue(portalCacheDto.isPublicCache());

		tfModification.setValue(portalCacheDto.getModification());
		tfModification.updateOriginalValue(portalCacheDto.getModification());

		tfExpiration.setValue(portalCacheDto.getExpiration());
		tfExpiration.updateOriginalValue(portalCacheDto.getExpiration());
		
		ETagMode eTagMode = portalCacheDto.getETagMode();
		if (eTagMode != null) {
			SimpleModelData model = cbETag.getStore().findModel(SimpleModelData.PROPERTY_ID, eTagMode);
			cbETag.setValue(model);
		}
	}

	private void enableOrDisableFields(boolean inherited) {
		formSupport.configReadOnly(cbUseCache, inherited);
		formSupport.configReadOnly(cbPublic, inherited);
		formSupport.configReadOnly(tfModification, inherited);
		formSupport.configReadOnly(tfExpiration, inherited);
		formSupport.configReadOnly(cbETag, inherited);
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
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSaveCache(), listener);
			}
		};
		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(this, listener, new LinkedList<Component>(
			fields), pmsListeningStrategy);
		addButton(bAccept);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);

	}

	/**
	 * Retrieves the values from the components, inserts them into a PortalNameDTO, and calls the service
	 * <code>create/setName</code> method.<br/>
	 */
	private void trySaveCurrentValues() {
		
		util.mask(pmsMessages.mskSavingCacheConfig());
		
		portalCacheDto.setInherited(cbParentConfig.getValue());
		portalCacheDto.setActive(cbUseCache.getValue());
		portalCacheDto.setPublicCache(cbPublic.getValue());
		portalCacheDto.setModification((Integer) tfModification.getValue());
		portalCacheDto.setExpiration((Integer) tfExpiration.getValue());
		portalCacheDto.setETagMode((ETagMode) cbETag.getValue().getId());
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			
			public void onSuccess(Void result) {
				util.unmask();
				hide();
				util.info(pmsMessages.msgSuccessSaveCache());
			}
			
			public void onFailure(Throwable caught) {
				util.unmask();
				errorProcessor.processError(caught, emrPortals, pmsMessages.msgErrorSaveCache());
			}
		};
		portalsService.setPortalCache(portalCacheDto, callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
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
	 * <br/> (non-Javadoc)
	 * 
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
	 * @return the emrPortals
	 */
	protected final PortalsServiceErrorMessageResolver getErrorMessageResolver() {
		return emrPortals;
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
	 * Injects the Portals async service proxy.
	 * @param portalsService
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
	}

	/**
	 * @return the portalsService
	 */
	public IPortalsServiceAsync getPortalsService() {
		return portalsService;
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
	 * Injects the Error Message Resolver for Portals service.
	 * @param emrPortals the emrPortals to set
	 */
	@Inject
	public void setEmrPortals(PortalsServiceErrorMessageResolver emrPortals) {
		this.emrPortals = emrPortals;
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
