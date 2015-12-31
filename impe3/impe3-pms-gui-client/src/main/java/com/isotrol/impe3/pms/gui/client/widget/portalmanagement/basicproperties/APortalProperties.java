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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.data.ModelData;
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
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
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
import com.isotrol.impe3.pms.api.edition.EditionsService;
import com.isotrol.impe3.pms.api.minst.ProvidedTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ProviderDTO;
import com.isotrol.impe3.pms.api.portal.PortalDTO;
import com.isotrol.impe3.pms.api.portal.PortalTemplateDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.ProviderModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.RoutingDomainSelModelData;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.EPortalImportExportType;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalInheritableFlagComboBox;


/**
 * Abstract base class for portal properties panel
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public abstract class APortalProperties extends TypicalWindow implements IDetailPanel {

	/**
	 * Main container.<br/>
	 */
	private LayoutContainer container = null;
	/*
	 * form fields
	 */
	/**
	 * "Routable" flag field.<br/>
	 */
	private CheckBox cbRoutable = null;
	
	/** tag segments field of portal */
	private TextField<String> tfTag = null;
	
	/** page routing of a portal */
	private ComboBox<ModelData> cbRouter = null;
	
	/** locale of a portal */
	private ComboBox<ModelData> cbLocale = null;
	
	/** device of a portal */
	private ComboBox<ModelData> cbDevice = null;
	
	/** Session CSRF flag. */
	private PortalInheritableFlagComboBox cbSessionCSRF = null;
	
	/** routing domanin of a portal */
	private ComboBox<RoutingDomainSelModelData> cbRoutingDomain = null;
	
	/**
	 * Node repository selector.
	 */
	private ComboBox<ModelData> cbNodesRepository = null;
	
	/** device capabilities of a portal */
	private ComboBox<ModelData> cbDeviceCapabilities = null;
	
	/**
	 * fields that may fire Change events.<br/>
	 */
	private List<Field<?>> fields = null;

	/**
	 * Portal data bound to the widget.<br/>
	 */
	private PortalTemplateDTO portalTemplate = null;

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
	public APortalProperties() {
	}

	/**
	 * Inits the widget.
	 * @param templateDto
	 */
	protected void initWidget(PortalTemplateDTO templateDto) {
		this.portalTemplate = templateDto;

		initThis();
		initComponent();
		configPortalController();
	}

	private void configPortalController() {
		final PortalsController portalsController = (PortalsController) portalsService;

		final ChangeListener changeListener = new ChangeListener() {
			public void modelChanged(ChangeEvent event) {
				PmsChangeEvent pmsEvent = (PmsChangeEvent) event;
				EPortalImportExportType importType = (EPortalImportExportType) pmsEvent.getEventInfo();
				if (PmsChangeEvent.IMPORT == pmsEvent.getType() && importType == EPortalImportExportType.CONFIGURATION) {
					displayModelValues();
				}
			}
		};

		portalsController.addChangeListener(changeListener);

		// remove this listener from portal controller when this widget is dettached
		addListener(Events.Detach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				portalsController.removeChangeListener(changeListener);
			}
		});
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {

		container = new LayoutContainer(formSupport.getStandardLayout(false));
		container.setBorders(false);
		container.addStyleName(styles.margin10px());
		add(container);

		addFormFields();

		displayModelValues();

		addButtonBar();
		addSpecificButtons();
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setModal(true);
		setWidth(600);
		setAutoHeight(true);
		setHeadingText(getHeadingText());
		setClosable(false);
		setShadow(false);
	}

	private void addFormFields() {
		// CheckBox property 'routable'
		cbRoutable = new CheckBox();
		cbRoutable.addInputStyleName(styles.checkBoxAlignLeft());
		cbRoutable.setFieldLabel(pmsMessages.labelRoutable());
		container.add(cbRoutable);

		// tag field
		tfTag = new TextField<String>();
		tfTag.setFieldLabel(pmsMessages.labelTags());
		container.add(tfTag);

		// Nodes Repository selector
		cbNodesRepository = new ComboBox<ModelData>();
		cbNodesRepository.setFieldLabel(pmsMessages.labelNodesRepository());
		cbNodesRepository.setDisplayField(ProviderModelData.PROPERTY_TO_DISPLAY);
		cbNodesRepository.setTriggerAction(TriggerAction.ALL);
		cbNodesRepository.setEditable(false);
		cbNodesRepository.setStore(new ListStore<ModelData>());
		// cbNodesRepository.addListener(Events.Render, new Listener<FieldEvent>() {
		// public void handleEvent(FieldEvent be) {
		// cbNodesRepository.validate();
		// };
		// });
		container.add(cbNodesRepository);

		// page routing field
		cbRouter = new ComboBox<ModelData>();
		cbRouter.setFieldLabel(pmsMessages.labelPageRouting());
		cbRouter.setTriggerAction(TriggerAction.ALL);
		cbRouter.setDisplayField(ProviderModelData.PROPERTY_TO_DISPLAY);
		cbRouter.setEditable(false);
		cbRouter.setStore(new ListStore<ModelData>());
		container.add(cbRouter);

		// page's locale field
		cbLocale = new ComboBox<ModelData>();
		cbLocale.setFieldLabel(pmsMessages.labelLocaleDetector());
		cbLocale.setTriggerAction(TriggerAction.ALL);
		cbLocale.setDisplayField(ProviderModelData.PROPERTY_TO_DISPLAY);
		cbLocale.setEditable(false);
		cbLocale.setAllowBlank(true);
		cbLocale.setStore(new ListStore<ModelData>());
		container.add(cbLocale);
		
		// Session CSRF
		cbSessionCSRF = addPIFCombo(pmsMessages.labelSessionCSRF());

		// portal's routing domain field
		cbRoutingDomain = new ComboBox<RoutingDomainSelModelData>();
		cbRoutingDomain.setFieldLabel(pmsMessages.labelRoutingDomain());
		cbRoutingDomain.setTriggerAction(TriggerAction.ALL);
		cbRoutingDomain.setDisplayField(RoutingDomainSelModelData.PROPERTY_NAME);
		cbRoutingDomain.setEditable(false);
		cbRoutingDomain.setAllowBlank(true);
		cbRoutingDomain.setStore(new ListStore<RoutingDomainSelModelData>());
		container.add(cbRoutingDomain);
		populateRoutingDomainSelectorStore();
		
		// portal's device capabilities
		cbDeviceCapabilities = new ComboBox<ModelData>();
		cbDeviceCapabilities.setFieldLabel(pmsMessages.labelDeviceCapabilities());
		cbDeviceCapabilities.setTriggerAction(TriggerAction.ALL);
		cbDeviceCapabilities.setDisplayField(ProviderModelData.PROPERTY_TO_DISPLAY);
		cbDeviceCapabilities.setEditable(false);
		cbDeviceCapabilities.setAllowBlank(true);
		cbDeviceCapabilities.setStore(new ListStore<ModelData>());
		container.add(cbDeviceCapabilities);
		
		// portal's device field
		cbDevice = new ComboBox<ModelData>();
		cbDevice.setFieldLabel(pmsMessages.labelDevice());
		cbDevice.setTriggerAction(TriggerAction.ALL);
		cbDevice.setDisplayField(ProviderModelData.PROPERTY_TO_DISPLAY);
		cbDevice.setEditable(false);
		cbDevice.setAllowBlank(true);
		cbDevice.setStore(new ListStore<ModelData>());
		container.add(cbDevice);

		fields = Arrays.asList(new Field<?>[] {tfTag, cbNodesRepository, cbRoutable, cbRouter, cbLocale,
			cbSessionCSRF, cbRoutingDomain, cbDevice, cbDeviceCapabilities});
	}
	
	/** Adds a portal inheritable flag combo. */
	private PortalInheritableFlagComboBox addPIFCombo(String label) {
		final PortalInheritableFlagComboBox cb = new PortalInheritableFlagComboBox(pmsMessages, label);
		container.add(cb);
		return cb;
	}

	/**
	 * Displays data corresponding to currently bound portal.
	 */
	protected void displayModelValues() {

		ProvidedTemplateDTO nodeRepository = portalTemplate.getNodeRepository();
		if (nodeRepository != null) {
			populateNodesRepositoryStore();
			ProviderDTO currentDto = portalTemplate.getNodeRepository().getCurrent();
			String propertyValue = pmsMessages.emptyValueProvider();
			if (currentDto != null) {
				cbNodesRepository.setValue(new ProviderModelData(currentDto));
			} else {
				ListStore<ModelData> store = cbNodesRepository.getStore();
				cbNodesRepository.setValue(store.findModel(ProviderModelData.PROPERTY_TO_DISPLAY, propertyValue));
			}
		}

		Boolean routable = Boolean.valueOf(portalTemplate.isRoutable());
		this.cbRoutable.setValue(routable);
		cbRoutable.updateOriginalValue(routable);

		String tag = portalTemplate.getTag();
		this.tfTag.setValue(tag);
		tfTag.updateOriginalValue(tag);

		// the provider template
		ProvidedTemplateDTO router = portalTemplate.getRouter();
		if (router != null) {
			populateRouterSelectorStore();
			ProviderDTO currentProvider = router.getCurrent();
			String propertyValue = pmsMessages.emptyValueProvider();
			if (currentProvider != null) {
				cbRouter.setValue(new ProviderModelData(currentProvider));
			} else {
				ListStore<ModelData> store = cbRouter.getStore();
				cbRouter.setValue(store.findModel(ProviderModelData.PROPERTY_TO_DISPLAY, propertyValue));
			}
		}

		// the locale provider template
		if (portalTemplate.getLocale() != null) {
			populateLocaleProviderSelectorStore();
			ProviderDTO currentLocaleProvider = portalTemplate.getLocale().getCurrent();
			String propertyLocaleValue = pmsMessages.emptyValueProvider();
			if (currentLocaleProvider != null) {
				cbLocale.setValue(new ProviderModelData(currentLocaleProvider));
			} else {
				ListStore<ModelData> store = cbLocale.getStore();
				cbLocale.setValue(store.findModel(ProviderModelData.PROPERTY_TO_DISPLAY, propertyLocaleValue));
			}
		}
		
		// Session CSRF
		cbSessionCSRF.setFlagValue(portalTemplate.getSessionCSRF());

		// the routing domain provider template
		RoutingDomainSelDTO currentRoutingDomain = portalTemplate.getDomain();
		if (currentRoutingDomain != null) {
			ListStore<RoutingDomainSelModelData> domainStore = cbRoutingDomain.getStore();
			cbRoutingDomain.setValue(domainStore.findModel(RoutingDomainSelModelData.PROPERTY_ID, currentRoutingDomain
				.getId()));
		}
		
		// the device capabilities template
		if (portalTemplate.getDeviceCaps() != null) {
			populateDeviceCapabilitiesSelectorStore();
			ProviderDTO currentDeviceCapabilities = portalTemplate.getDeviceCaps().getCurrent();
			String propertyDeviceValue = pmsMessages.emptyValueProvider();
			if (currentDeviceCapabilities != null) {
				cbDeviceCapabilities.setValue(new ProviderModelData(currentDeviceCapabilities));
			} else {
				ListStore<ModelData> store = cbDeviceCapabilities.getStore();
				cbDeviceCapabilities.setValue(store.findModel(ProviderModelData.PROPERTY_TO_DISPLAY, propertyDeviceValue));
			}
		}
		
		// the device provider template
		if (portalTemplate.getDevice() != null) {
			populateDeviceResolverSelectorStore();
			ProviderDTO currentDeviceResolver = portalTemplate.getDevice().getCurrent();
			String propertyDeviceValue = pmsMessages.emptyValueProvider();
			if (currentDeviceResolver != null) {
				cbDevice.setValue(new ProviderModelData(currentDeviceResolver));
			} else {
				ListStore<ModelData> store = cbDevice.getStore();
				cbDevice.setValue(store.findModel(ProviderModelData.PROPERTY_TO_DISPLAY, propertyDeviceValue));
			}
		}
	}
	
	private void populateRoutingDomainSelectorStore() {
		List<RoutingDomainSelDTO> availableDomainsDto = portalTemplate.getAvailableDomains();
		List<RoutingDomainSelModelData> availableDomainsModel = new ArrayList<RoutingDomainSelModelData>();
		for (RoutingDomainSelDTO domain : availableDomainsDto) {
			availableDomainsModel.add(new RoutingDomainSelModelData(domain));
		}
		cbRoutingDomain.getStore().add(availableDomainsModel);
	}

	/**
	 * Populates the Nodes Repository combo box store.
	 */
	private void populateNodesRepositoryStore() {
		ListStore<ModelData> sNodesRepositories = cbNodesRepository.getStore();
		sNodesRepositories.removeAll();

		// "--no provider--" value:
		BaseModelData noProvider = new BaseModelData();
		noProvider.set(ProviderModelData.PROPERTY_TO_DISPLAY, pmsMessages.emptyValueProvider());
		sNodesRepositories.add(noProvider);

		for (ProviderDTO dto : portalTemplate.getNodeRepository().getProviders()) {
			sNodesRepositories.add(new ProviderModelData(dto));
		}
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
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSavePortal(), listener);
			}
		};
		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(this, listener, new LinkedList<Component>(
			fields), pmsListeningStrategy);
		addButton(bAccept);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Retrieves the values from the components, inserts them into a PortalDTO, and calls the service <code>save</code>
	 * method.<br/> TODO SHOULD validate and call {@link EditionsService#save(PortalDTO)}<br/>
	 */
	private void trySaveCurrentValues() {
		util.mask(pmsMessages.mskSavePortal());

		// portal property routable
		portalTemplate.setRoutable(cbRoutable.getValue().booleanValue());

		// portal tags segment
		portalTemplate.setTag(tfTag.getValue());

		// portal nodes repository
		List<ModelData> selectedItems = cbNodesRepository.getSelection();
		ModelData selectedItem = null;
		if (!selectedItems.isEmpty()) {
			selectedItem = selectedItems.get(0);
		}
		ProviderDTO nodesRepoDto = null;
		if (selectedItem != null) {
			if (selectedItem instanceof BaseModelData) {
				nodesRepoDto = null;
			} else {
				ProviderModelData pModelData = (ProviderModelData) selectedItem;
				nodesRepoDto = pModelData.getDTO();
			}
		}
		portalTemplate.getNodeRepository().setCurrent(nodesRepoDto);

		// portal routing
		List<ModelData> selectedRoutingItems = cbRouter.getSelection();
		ModelData selectedRoutingItem = null;
		if (!selectedRoutingItems.isEmpty()) {
			selectedRoutingItem = selectedRoutingItems.get(0);
		}
		ProviderDTO routerDto = null;
		if (selectedRoutingItem != null) {
			if (selectedRoutingItem instanceof BaseModelData) {
				routerDto = null;
			} else {
				ProviderModelData pModelData = (ProviderModelData) selectedRoutingItem;
				routerDto = pModelData.getDTO();
			}
		}
		portalTemplate.getRouter().setCurrent(routerDto);

		// portal locale
		List<ModelData> selectedLocaleItems = cbLocale.getSelection();
		ModelData selectedLocaleItem = null;
		if (!selectedLocaleItems.isEmpty()) {
			selectedLocaleItem = selectedLocaleItems.get(0);
		}
		ProviderDTO localeDto = null;
		if (selectedLocaleItem != null) {
			if (selectedLocaleItem instanceof BaseModelData) {
				localeDto = null;
			} else {
				ProviderModelData pModelData = (ProviderModelData) selectedLocaleItem;
				localeDto = pModelData.getDTO();
			}
		}
		portalTemplate.getLocale().setCurrent(localeDto);
		
		// portal device resolver
		List<ModelData> selectedDeviceItems = cbDevice.getSelection();
		ModelData selectedDeviceItem = null;
		if (!selectedDeviceItems.isEmpty()) {
			selectedDeviceItem = selectedDeviceItems.get(0);
		}
		ProviderDTO deviceDto = null;
		if (selectedDeviceItem != null) {
			if (selectedDeviceItem instanceof BaseModelData) {
				deviceDto = null;
			} else {
				ProviderModelData pModelData = (ProviderModelData) selectedDeviceItem;
				deviceDto = pModelData.getDTO();
			}
		}
		portalTemplate.getDevice().setCurrent(deviceDto);
		
		// Session CSRF
		portalTemplate.setSessionCSRF(cbSessionCSRF.getFlagValue()); 

		// portal routing domain
		portalTemplate.setDomain(cbRoutingDomain.getValue().getDTO());
		
		// portal device capabilities
		ModelData selectedDeviceCapsItem = cbDeviceCapabilities.getValue();
		ProviderDTO deviceCapsDto = null;
		if (selectedDeviceCapsItem != null) {
			if (selectedDeviceCapsItem instanceof BaseModelData) {
				deviceCapsDto = null;
			} else {
				ProviderModelData pModelData = (ProviderModelData) selectedDeviceCapsItem;
				deviceCapsDto = pModelData.getDTO();
			}
		}
		portalTemplate.getDeviceCaps().setCurrent(deviceCapsDto);

		PortalDTO portalDto = portalTemplate.toPortalDTO();

		// the callback is subclass-specific:
		AsyncCallback<Void> callback = getSaveCallback();
		hide();

		portalsService.save(portalDto, callback);
	}

	/**
	 * @return callback code for processing the PortalTemplateDTO resulting of a save process.
	 */
	protected abstract AsyncCallback<Void> getSaveCallback();

	private void populateRouterSelectorStore() {
		ListStore<ModelData> store = cbRouter.getStore();
		store.removeAll();

		// "--no provider--" value:
		BaseModelData noProvider = new BaseModelData();
		noProvider.set(ProviderModelData.PROPERTY_TO_DISPLAY, pmsMessages.emptyValueProvider());
		store.add(noProvider);

		// available providers values:
		List<ProviderDTO> providersDto = portalTemplate.getRouter().getProviders();
		for (ProviderDTO provider : providersDto) {
			store.add(new ProviderModelData(provider));
		}
	}

	/**
	 * 
	 */
	private void populateLocaleProviderSelectorStore() {
		ListStore<ModelData> store = cbLocale.getStore();
		store.removeAll();

		// "--no provider--" value:
		BaseModelData noProvider = new BaseModelData();
		noProvider.set(ProviderModelData.PROPERTY_TO_DISPLAY, pmsMessages.emptyValueProvider());
		store.add(noProvider);

		// available providers values:

		List<ProviderDTO> providersDto = portalTemplate.getLocale().getProviders();
		List<ModelData> lModels = new LinkedList<ModelData>();
		for (ProviderDTO provider : providersDto) {
			lModels.add(new ProviderModelData(provider));
		}
		store.add(lModels);
	}

	private void populateDeviceResolverSelectorStore() {
		ListStore<ModelData> store = cbDevice.getStore();
		store.removeAll();

		// "--no provider--" value:
		BaseModelData noProvider = new BaseModelData();
		noProvider.set(ProviderModelData.PROPERTY_TO_DISPLAY, pmsMessages.emptyValueProvider());
		store.add(noProvider);

		// available providers values:

		List<ProviderDTO> providersDto = portalTemplate.getDevice().getProviders();
		List<ModelData> lModels = new LinkedList<ModelData>();
		for (ProviderDTO provider : providersDto) {
			lModels.add(new ProviderModelData(provider));
		}
		store.add(lModels);
	}
	
	private void populateDeviceCapabilitiesSelectorStore() {
		ListStore<ModelData> store = cbDeviceCapabilities.getStore();
		store.removeAll();

		// "--no provider--" value:
		BaseModelData noProvider = new BaseModelData();
		noProvider.set(ProviderModelData.PROPERTY_TO_DISPLAY, pmsMessages.emptyValueProvider());
		store.add(noProvider);

		// available providers values:

		List<ProviderDTO> providersDto = portalTemplate.getDeviceCaps().getProviders();
		List<ModelData> lModels = new LinkedList<ModelData>();
		for (ProviderDTO provider : providersDto) {
			lModels.add(new ProviderModelData(provider));
		}
		store.add(lModels);
	}
	
	/**
	 * Returns the text displayed in the heading.<br/>
	 * 
	 * @return the text displayed in the heading.<br/>
	 */
	protected abstract String getHeadingText();

	/**
	 * Adds specific buttons to the button bar
	 */
	protected abstract void addSpecificButtons();

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		boolean valid = true;
		Iterator<Field<?>> it = fields.iterator();
		while (valid && it.hasNext()) {
			valid = valid && it.next().isValid();
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
		Iterator<Field<?>> it = fields.iterator();
		while (!dirty && it.hasNext()) {
			dirty = dirty || it.next().isDirty();
		}
		return dirty;
	}

	/**
	 * @return the bound portal data.
	 */
	protected final PortalTemplateDTO getPortalTemplate() {
		return portalTemplate;
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
}
