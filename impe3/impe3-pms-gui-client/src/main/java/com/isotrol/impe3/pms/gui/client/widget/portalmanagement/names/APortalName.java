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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.names;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeGridCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.NonEmptyStringValidator;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.ISessionsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.LocaleNameModel;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.EPortalImportExportType;


/**
 * Abstract base class for portal names panel
 * 
 * @author Manuel Ruiz
 * 
 */
public abstract class APortalName extends TypicalWindow implements IDetailPanel {

	private static final int COL_NAME_WIDTH = 150;
	private static final int COL_LOCALE_WIDTH = 75;
	private static final int GRID_PANEL_HEIGHT = 250;

	/**
	 * Main container.<br/>
	 */
	private LayoutContainer container = null;
	/*
	 * form fields
	 */
	/**
	 * "Name" field<br/>
	 */
	private TextField<String> tfName = null;

	/**
	 * "Description" field
	 */
	private TextArea taDescription = null;

	/**
	 * "URL" field<br/>
	 */
	private TextField<String> tfUrl = null;

	/** default locale of portal */
	private TextField<String> tfDefaultLocale = null;

	/**
	 * fields that may fire Change events.<br/>
	 */
	private List<Component> fields = null;

	/**
	 * The current portal name
	 */
	private PortalNameDTO portalNameDto = null;

	/**
	 * Grid to store the alternative locales
	 */
	private EditorGrid<LocaleNameModel> gLocales = null;

	/**
	 * Store for alternative locales
	 */
	private ListStore<LocaleNameModel> sLocales = null;

	/**
	 * Button to remove locales from the grid
	 */
	private Button deleteItem = null;

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
	 * Proxy to Sessions async service.<br/>
	 */
	private ISessionsServiceAsync sessionsService = null;

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
	 * Validates non empty strings.<br/>
	 */
	private NonEmptyStringValidator nonEmptyStringValidator = null;
	/**
	 * Field validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;

	/**
	 * Constructor.<br/>
	 * 
	 * @param portalTemplate
	 * @param portal
	 */
	public APortalName() {
	}

	/**
	 * Inits the widget.
	 * @param templateDto
	 */
	public void initWidget(PortalNameDTO portalNameDto) {
		this.portalNameDto = portalNameDto;

		if (portalNameDto == null) {
			this.portalNameDto = new PortalNameDTO();
			this.portalNameDto.setName(new NameDTO());
		}

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
				if (PmsChangeEvent.IMPORT == pmsEvent.getType() && importType == EPortalImportExportType.NAMES) {
					tryGetPortalName();
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
	
	private void tryGetPortalName() {
		AsyncCallback<PortalNameDTO> callback = new AsyncCallback<PortalNameDTO>() {
			
			public void onSuccess(PortalNameDTO name) {
				portalNameDto = name;
				displayModelValues();
			}
			
			public void onFailure(Throwable caught) {
				util.error(pmsMessages.msgErrorGetPortal());
			}
		};
		portalsService.getName(portalNameDto.getId(), callback);
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

		if (isEdition()) {
			displayModelValues();
		}

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

		// field name
		tfName = new TextField<String>();
		tfName.setFieldLabel(pmsMessages.labelName());
		tfName.setAllowBlank(false);
		tfName.setValidator(nonEmptyStringValidator);
		tfName.addListener(Events.Render, validatorListener);
		tfName.setAutoValidate(true);
		container.add(tfName);

		// field url
		tfUrl = new TextField<String>();
		tfUrl.setFieldLabel(pmsMessages.labelUrl());
		// tfUrl.setAllowBlank(false);
		// tfUrl.setAutoValidate(true);
		// tfUrl.setValidator(nonEmptyStringValidator);
		// tfUrl.addListener(Events.Render, validatorListener);
		container.add(tfUrl);

		// field name
		taDescription = new TextArea();
		taDescription.setFieldLabel(pmsMessages.labelDescription());
		container.add(taDescription);

		// default locale field
		tfDefaultLocale = new TextField<String>();
		tfDefaultLocale.setFieldLabel(pmsMessages.labelDefaultLocale());
		container.add(tfDefaultLocale);

		// other locales
		addGridLocales();

		fields = Arrays.asList(new Component[] {tfName, tfUrl, taDescription, tfDefaultLocale, gLocales});
	}

	/**
	 * Creates and adds the locales grid
	 */
	private void addGridLocales() {
		ContentPanel containerGrid = new ContentPanel(new FitLayout());
		containerGrid.setHeadingText(pmsMessages.labelOtherLocales());
		containerGrid.setStyleAttribute("margin", "0 10px 10px");
		containerGrid.setHeight(GRID_PANEL_HEIGHT);

		/*
		 * gLocales column configs
		 */
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<LocaleNameModel> sm = new CheckBoxSelectionModel<LocaleNameModel>();
		sm.setSelectionMode(SelectionMode.SIMPLE);
		configs.add(sm.getColumn());

		ColumnConfig column = new ColumnConfig();
		column.setId(LocaleNameModel.PROPERTY_LOCALE);
		column.setHeaderText(pmsMessages.columnHeaderLanguage());
		column.setWidth(COL_LOCALE_WIDTH);
		column.setRenderer(new HtmlEncodeGridCellRenderer());
		column.setEditor(new CellEditor(createTextFieldEditor(true)));
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LocaleNameModel.PROPERTY_NAME);
		column.setHeaderText(pmsMessages.columnHeaderName());
		column.setWidth(COL_NAME_WIDTH);
		column.setRenderer(new HtmlEncodeGridCellRenderer());
		column.setEditor(new CellEditor(createTextFieldEditor(false)));
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		sLocales = new ListStore<LocaleNameModel>();
		sLocales.addStoreListener(new StoreListener<LocaleNameModel>() {
			@Override
			public void storeAdd(StoreEvent<LocaleNameModel> se) {
				gLocales.fireEvent(Events.Change);
			}

			@Override
			public void storeRemove(StoreEvent<LocaleNameModel> se) {
				gLocales.fireEvent(Events.Change);
			}

			@Override
			public void storeUpdate(StoreEvent<LocaleNameModel> se) {
				gLocales.fireEvent(Events.Change);
			}
		});
		gLocales = new EditorGrid<LocaleNameModel>(sLocales, cm);

		gLocales.setSelectionModel(sm);
		gLocales.addPlugin(sm);
		gLocales.setAutoExpandColumn(LocaleNameModel.PROPERTY_NAME);
		gLocales.setLoadMask(true);
		gLocales.getView().setForceFit(true);

		gLocales.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<LocaleNameModel>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<LocaleNameModel> se) {
				LocaleNameModel selected = gLocales.getSelectionModel().getSelectedItem();
				if (selected != null) {
					deleteItem.enable();
				} else {
					deleteItem.disable();
				}
			}
		});

		addLocalesToolbar(containerGrid);

		containerGrid.add(gLocales);
		add(containerGrid);
	}

	private void fillOtherLocales() {
		sLocales.removeAll();
		Map<String, String> locales = portalNameDto.getLocales();

		if (locales == null || locales.isEmpty()) {
			return;
		}

		List<LocaleNameModel> lModelData = new LinkedList<LocaleNameModel>();
		Iterator<Entry<String, String>> it = locales.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> locale = (Map.Entry<String, String>) it.next();
			lModelData.add(new LocaleNameModel(locale.getKey(), locale.getValue()));
		}
		sLocales.add(lModelData);
	}

	private TextField<String> createTextFieldEditor(boolean required) {
		TextField<String> tfEditor = new TextField<String>();
		if (required) {
			tfEditor.setAllowBlank(false);
			tfEditor.setAutoValidate(true);
			tfEditor.setValidator(nonEmptyStringValidator);
			tfEditor.addListener(Events.Render, validatorListener);
		}

		return tfEditor;
	}

	/**
	 * Adds the locales toolbar to the locales panel.<br/>
	 * @param containerGrid
	 */
	private void addLocalesToolbar(ContentPanel containerGrid) {
		// create and add the tool bar
		ToolBar toolBar = new ToolBar();
		SelectionListener<ButtonEvent> lAdd = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				sLocales.add(new LocaleNameModel());
			}
		};
		buttonsSupport.addAddButton(toolBar, lAdd, null);
		toolBar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<LocaleNameModel> selectedItems = gLocales.getSelectionModel().getSelectedItems();
				if (selectedItems != null && !selectedItems.isEmpty()) {
					for (LocaleNameModel item : selectedItems) {
						sLocales.remove(item);
					}
				}
			}
		};
		deleteItem = buttonsSupport.addDeleteButton(toolBar, lDelete, null);
		deleteItem.disable();

		containerGrid.setTopComponent(toolBar);
	}

	/**
	 * Displays data corresponding to currently bound portal.
	 */
	protected void displayModelValues() {

		NameDTO name = portalNameDto.getName();
		if (name == null) {
			name = new NameDTO();
		}
		String sName = name.getDisplayName();
		this.tfName.setValue(sName);
		this.tfName.updateOriginalValue(sName);

		String sUrl = name.getPath();
		this.tfUrl.setValue(sUrl);
		tfUrl.updateOriginalValue(sUrl);

		taDescription.setValue(portalNameDto.getDescription());
		taDescription.updateOriginalValue(portalNameDto.getDescription());

		tfDefaultLocale.setValue(portalNameDto.getDefaultLocale());
		tfDefaultLocale.updateOriginalValue(portalNameDto.getDefaultLocale());

		fillOtherLocales();
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
	 * Retrieves the values from the components, inserts them into a PortalNameDTO, and calls the service
	 * <code>create/setName</code> method.<br/>
	 */
	private void trySaveCurrentValues() {

		// portal name
		portalNameDto.getName().setDisplayName(tfName.getValue());

		// portal path
		portalNameDto.getName().setPath(tfUrl.getValue());

		// portal description
		portalNameDto.setDescription(taDescription.getValue());

		// portal default locale
		String defaultLocale = tfDefaultLocale.getValue();
		portalNameDto.setDefaultLocale(defaultLocale);

		// other portal locales
		Map<String, String> mapLocales = new HashMap<String, String>();
		List<LocaleNameModel> names = sLocales.getModels();
		List<String> locales = new ArrayList<String>();
		for (LocaleNameModel name : names) {
			mapLocales.put(name.getLocale(), name.getName());
			locales.add(name.getLocale());
		}
		portalNameDto.setLocales(mapLocales);

		if (defaultLocale != null && !defaultLocale.equals("") || !names.isEmpty()) {
			checkLocalesAndSave(defaultLocale, locales);
		} else {
			util.mask(pmsMessages.mskSavePortal());
			savePortalName(portalNameDto);
			hide();
		}
	}

	private void checkLocalesAndSave(String defaultLocale, List<String> otherLocales) {

		List<String> locales = new ArrayList<String>();

		if (defaultLocale != null && !defaultLocale.equals("")) {
			locales.add(defaultLocale);
		}
		for (String locale : otherLocales) {
			locales.add(locale);
		}

		mask(pmsMessages.mskLocales());

		AsyncCallback<Set<String>> callback = new AsyncCallback<Set<String>>() {

			public void onSuccess(Set<String> result) {
				unmask();
				if (result != null && result.size() > 0) {
					String errors = " ";
					for (String err : result) {
						errors += err + " ";
					}
					util.error(pmsMessages.msgErrorLocales() + errors);
				} else {
					util.mask(pmsMessages.mskSavePortal());
					savePortalName(portalNameDto);
					hide();
				}
			}

			public void onFailure(Throwable caught) {
				unmask();
				util.error(messages.msgError());
			}
		};
		sessionsService.checkLocales(locales, callback);
	}

	/**
	 * @return the portalNameDto
	 */
	public PortalNameDTO getPortalNameDto() {
		return portalNameDto;
	}

	/**
	 * Calls the correct method (create / setName) to save the portal name
	 * @param portalNameDto the portal name to save
	 */
	protected abstract void savePortalName(PortalNameDTO portalNameDto);

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
		Iterator<Component> it = fields.iterator();
		while (valid && it.hasNext()) {
			Component component = it.next();
			if (component instanceof Field<?>) {
				valid = valid && ((Field<?>) component).isValid();
			} else if (component instanceof Grid<?>) {
				valid = valid && localesGridIsValid();
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
			} else if (component instanceof Grid<?>) {
				dirty = dirty || localesGridIsDirty();
			}
		}
		return dirty;
	}

	private boolean localesGridIsDirty() {
		boolean dirty = !sLocales.getModifiedRecords().isEmpty();
		if (portalNameDto.getLocales() != null) {
			dirty = dirty || (portalNameDto.getLocales().size() != sLocales.getCount());
		}

		return dirty;
	}

	private boolean localesGridIsValid() {
		boolean valid = true;
		for (LocaleNameModel locale : sLocales.getModels()) {
			String lang = locale.getLocale();
			valid = valid && lang != null && !lang.equals("");
		}

		return valid;
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
	 * @param nonEmptyStringValidator the nonEmptyStringValidator to set
	 */
	@Inject
	public void setNonEmptyStringValidator(NonEmptyStringValidator nonEmptyStringValidator) {
		this.nonEmptyStringValidator = nonEmptyStringValidator;
	}

	/**
	 * Injects the field validator listener.
	 * @param validatorListener the validatorListener to set
	 */
	@Inject
	public void setValidatorListener(ValidatorListener validatorListener) {
		this.validatorListener = validatorListener;
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
	 * @param sessionsService the sessionsService to set
	 */
	@Inject
	public void setSessionsService(ISessionsServiceAsync sessionsService) {
		this.sessionsService = sessionsService;
	}

	/**
	 * @return the tfName
	 */
	public TextField<String> getTfName() {
		return tfName;
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
