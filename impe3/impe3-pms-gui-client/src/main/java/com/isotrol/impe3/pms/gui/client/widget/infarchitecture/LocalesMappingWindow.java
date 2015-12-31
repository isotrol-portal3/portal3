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

package com.isotrol.impe3.pms.gui.client.widget.infarchitecture;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.NonEmptyStringValidator;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.gui.api.service.ISessionsServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.LocalizedNameModel;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;


/**
 * The window with locales mapping.<br/> Must be initialized with a Listener that will be bound to "Add localized names"
 * button.
 * 
 * @author Manuel Ruiz
 * 
 */
public class LocalesMappingWindow extends TypicalWindow {
	/**
	 * Width of the window.<br/>
	 */
	private static final String WINDOW_WIDTH = "600";

	/**
	 * Height of the window.<br/>
	 */
	private static final String WINDOW_HEIGHT = "400";

	/**
	 * "Language" column width.<br/>
	 */
	private static final int COLUMN_LOCALE_WIDTH = 150;

	/**
	 * "Name" column width.<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 250;

	/**
	 * "Path" column width.<br/>
	 */
	private static final int COLUMN_PATH_WIDTH = 300;

	/** store with the components */
	private ListStore<LocalizedNameModel> storeLocalesGrid = null;

	/**
	 * Components grid.<br/>
	 */
	private EditorGrid<LocalizedNameModel> gLanguages = null;

	/**
	 * Button for deleting selected localized names
	 */
	private Button ttiDelete = null;

	/**
	 * The widget that must receive the localized names of this widget.<br/>
	 */
	private ILocalizedNamesReceiver localizedNamesReceiver = null;

	/**
	 * Current localized names
	 */
	private Map<String, NameDTO> localizedNames = null;

	/*
	 * Injected deps.
	 */

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Alphabetical store sorter<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;

	/**
	 * Validates non empty strings.<br/>
	 */
	private NonEmptyStringValidator nonEmptyStringValidator = null;

	/**
	 * Field validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;

	/**
	 * Sessions service
	 */
	private ISessionsServiceAsync sessionsService = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	private boolean initialized = false;

	/**
	 * Unique allowed constructor.
	 */
	public LocalesMappingWindow() {
	}

	/**
	 * <br/>
	 * @param acceptListener
	 */
	public void init(ILocalizedNamesReceiver receiver, Map<String, NameDTO> localizedNames) {
		this.localizedNamesReceiver = receiver;
		this.localizedNames = localizedNames != null ? localizedNames : new HashMap<String, NameDTO>();

		setHeadingText(pmsMessages.headerLanguages());
		setClosable(true);
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTOY);

		initComponents();

		initialized = true;
	}

	/**
	 * Inits this container inner locales.
	 */
	private void initComponents() {
		addGrid();

		addToolbar();

		addButtonBar();
	}

	/**
	 * Creates the gLanguages with the list of current category or content type languages
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<LocalizedNameModel> selectionModel = new CheckBoxSelectionModel<LocalizedNameModel>();
		configs.add(selectionModel.getColumn());

		ColumnConfig column = new ColumnConfig();
		column.setId(LocalizedNameModel.PROPERTY_LOCALE);
		column.setHeaderText(pmsMessages.columnHeaderLanguage());
		column.setWidth(COLUMN_LOCALE_WIDTH);
		column.setEditor(new CellEditor(creteTextFieldEditor()));
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LocalizedNameModel.PROPERTY_NAME);
		column.setHeaderText(pmsMessages.columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		column.setEditor(new CellEditor(creteTextFieldEditor()));
		configs.add(column);

		column = new ColumnConfig();
		column.setId(LocalizedNameModel.PROPERTY_PATH);
		column.setHeaderText(pmsMessages.columnHeaderPath());
		column.setWidth(COLUMN_PATH_WIDTH);
		column.setEditor(new CellEditor(creteTextFieldEditor()));
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		storeLocalesGrid = new ListStore<LocalizedNameModel>();
		// fills the store
		if (localizedNames != null) {
			fillsStore();
		}
		storeLocalesGrid.setStoreSorter((StoreSorter) storeSorter);
		storeLocalesGrid.setSortField(LocalizedNameModel.PROPERTY_LOCALE);

		gLanguages = new EditorGrid<LocalizedNameModel>(storeLocalesGrid, cm);

		gLanguages.setSelectionModel(selectionModel);
		gLanguages.addPlugin(selectionModel);

		gLanguages.setAutoExpandColumn(LocalizedNameModel.PROPERTY_PATH);
		gLanguages.setLoadMask(true);
		gLanguages.getView().setForceFit(true);

		gLanguages.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<LocalizedNameModel>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<LocalizedNameModel> se) {
				if (se.getSelection() != null && !se.getSelection().isEmpty()) {
					ttiDelete.enable();
				} else {
					ttiDelete.disable();
				}
			}
		});

		add(gLanguages);

	}

	private void fillsStore() {
		Iterator<Map.Entry<String, NameDTO>> it = localizedNames.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, NameDTO> e = (Map.Entry<String, NameDTO>) it.next();
			storeLocalesGrid.add(new LocalizedNameModel(e.getKey(), e.getValue().getDisplayName(), e.getValue()
				.getPath()));
		}
	}

	private TextField<String> creteTextFieldEditor() {
		TextField<String> tfEditor = new TextField<String>();
		tfEditor.setAllowBlank(false);
		tfEditor.setAutoValidate(true);
		tfEditor.setValidator(nonEmptyStringValidator);
		tfEditor.addListener(Events.Render, validatorListener);

		return tfEditor;
	}

	/**
	 * Creates, configures and adds the components palette toolbar.<br/>
	 * @param componentsReceiver
	 */
	private void addToolbar() {
		// the toolbar
		ToolBar gridToolBar = new ToolBar();
		setTopComponent(gridToolBar);

		SelectionListener<ButtonEvent> ttiAddListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				storeLocalesGrid.add(new LocalizedNameModel());
			}
		};
		buttonsSupport.addGenericButton(messages.labelAdd(), styles.iNew(), gridToolBar, ttiAddListener);
		gridToolBar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<LocalizedNameModel> selectedItems = gLanguages.getSelectionModel().getSelectedItems();
				if (selectedItems != null && !selectedItems.isEmpty()) {
					for (LocalizedNameModel item : selectedItems) {
						storeLocalesGrid.remove(item);
					}
				}
			}
		};
		ttiDelete = buttonsSupport.addDeleteButton(gridToolBar, lDelete, null);
		ttiDelete.disable();

		gridToolBar.add(new FillToolItem());

		StoreFilterField<LocalizedNameModel> filter = new CustomizableStoreFilter<LocalizedNameModel>(Arrays
			.asList(new String[] {LocalizedNameModel.PROPERTY_NAME, LocalizedNameModel.PROPERTY_LOCALE,
				LocalizedNameModel.PROPERTY_PATH}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		filter.bind(storeLocalesGrid);
		gridToolBar.add(filter);

		setTopComponent(gridToolBar);
	}

	/**
	 * Creates and configures the button bar.<br/>
	 */
	private void addButtonBar() {

		SelectionListener<ButtonEvent> lAccept = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				checkAndUpdateLocalizedNames();
			}
		};
		Button bAccept = buttonsSupport.createAcceptButton(lAccept);
		addButton(bAccept);

		SelectionListener<ButtonEvent> lCancel = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		};
		final Button bCancel = buttonsSupport.createCancelButton(lCancel);
		addButton(bCancel);
	}

	private void checkAndUpdateLocalizedNames() {

		mask(pmsMessages.mskLocales());

		List<String> locales = new ArrayList<String>();
		for (LocalizedNameModel model : storeLocalesGrid.getModels()) {
			String locale = model.getLocale();
			String name = model.getName();
			String path = model.getPath();
			if (locales.contains(locale)) {
				util.error(pmsMessages.msgErrorDuplicateLocales());
				unmask();
				return;
			}
			if (locale == null || name == null || path == null || locale.equals("") || name.equals("")
				|| path.equals("")) {
				util.error(pmsMessages.msgErrorRequiredLocaleFields());
				unmask();
				return;
			}

			locales.add(locale);
		}

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
					localizedNamesReceiver.receiveLocalizedNames(storeLocalesGrid.getModels());
					fireEvent(Events.Change);
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
	 * @return true if grid has changed
	 */
	public boolean isDirty() {
		boolean dirty = false;
		if (storeLocalesGrid != null) {
			dirty = !storeLocalesGrid.getModifiedRecords().isEmpty()
				|| storeLocalesGrid.getCount() != localizedNames.size();
		}

		return dirty && initialized;
	}

	/**
	 * Injects the buttons helper.<br/>
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the PMS specific messages bundle.<br/>
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the generic messages bundle.<br/>
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the generic styles bundle.<br/>
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * Injects the grid store sorter.<br/>
	 * @param storeSorter
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
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
	 * @param sessionsService the sessionsService to set
	 */
	@Inject
	public void setSessionsService(ISessionsServiceAsync sessionsService) {
		this.sessionsService = sessionsService;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * @return the storeLocalesGrid
	 */
	public ListStore<LocalizedNameModel> getStoreLocalesGrid() {
		return storeLocalesGrid;
	}

	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}
}
