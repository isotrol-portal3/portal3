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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.mapping;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeGridCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.NonEmptyStringValidator;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.smap.CategoryMappingDTO;
import com.isotrol.impe3.pms.api.smap.ContentTypeMappingDTO;
import com.isotrol.impe3.pms.api.smap.SetMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingTemplateDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.SourceMappingsController;
import com.isotrol.impe3.pms.gui.client.data.impl.AbstractMappingModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.CategoryMappingModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.CategorySelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeMappingModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeSelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.SetMappingModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.renderer.CategoryCellRenderer;
import com.isotrol.impe3.pms.gui.client.renderer.ContentTypeCellRenderer;
import com.isotrol.impe3.pms.gui.client.store.CategoryTreeStore;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Shows the detail of a source mapping
 * 
 * @author Manuel Ruiz
 * 
 */
public class MappingDetailPanel extends TypicalWindow implements IDetailPanel {

	/**
	 * Template used to render the "Mapping" column.<br/>
	 */
	private static final String MAPPING_COLUMN_TEMPLATE = "<span title='${MAPPING}'>${MAPPING}</span>";

	/**
	 * Pattern used to render the "Mapping" column.<br/> It's a regex, so careful with symbols.
	 */
	private static final String MAPPING_COLUMN_PATTERN = "\\$\\{MAPPING\\}";

	/**
	 * This window height.<br/>
	 */
	private static final int WINDOW_HEIGHT = 520;

	/**
	 * <code>true</code> when a mapping is added/removed.<br/>
	 */
	private boolean gridsModified = false;

	private static final int COL_CONTENT_TYPE_WIDTH = 220;
	private static final int COL_MAPPING_WIDTH = 200;
	private static final int COL_CATEGORY_WIDTH = 220;

	private static final int GRID_PANELS_HEIGHT = 350;

	/*
	 * Categories popup window size:
	 */
	private static final int CATEGORIES_POPUP_HEIGHT = 550;
	private static final int CATEGORIES_POPUP_WIDTH = 500;

	/**
	 * tab panel with the mapping tab items (basic information, mapping sets, mapping categories and mapping content
	 * types)
	 */
	private TabPanel container = null;

	/**
	 * Contains {@link #pSets}, {@link #pContentTypes} and {@link #pCategories}.<br/>
	 */
	private ContentPanel pContentTypes = null;
	private ContentPanel pCategories = null;
	private ContentPanel pSets = null;

	/**
	 * Grid of Content Types<br/>
	 */
	private EditorGrid<ContentTypeMappingModelData> gCtMappings = null;
	/**
	 * Store of Content Types mappings<br/>
	 */
	private ListStore<ContentTypeMappingModelData> sCtMappings = null;
	/**
	 * Possible Content types store.<br/>
	 */
	private ListStore<ContentTypeSelModelData> sPossibleCt = null;

	/**
	 * Store of Sets mappings<br/>
	 */
	private ListStore<SetMappingModelData> sSetMappings = null;
	/**
	 * Grid of Sets<br/>
	 */
	private EditorGrid<SetMappingModelData> gSetMappings = null;

	private Button deleteItem = null;

	/**
	 * categories window for mapping edition<br/>
	 */
	private CategoryTreeStore catTreeStore = null;
	/**
	 * categories window for mapping creation.<br/>
	 */
	private Window catWindow = null;

	/**
	 * Grid of Categories mappings.<br/>
	 */
	private EditorGrid<CategoryMappingModelData> gCatMappings = null;
	/**
	 * Store of Categories mappings<br/>
	 */
	private ListStore<CategoryMappingModelData> sCatMappings = null;
	/**
	 * "Delete category mapping" menu item.<br/>
	 */
	private Button deleteCategoryItem = null;
	/**
	 * "Delete set mapping" menu item.<br/>
	 */
	private Button deleteSetItem = null;
	/**
	 * Categories tree.<br/>
	 */
	private TreePanel<CategorySelModelData> tree = null;

	/**
	 * "Accept" button listener.<br/>
	 */
	private SelectionListener<ButtonEvent> acceptListener = null;
	/**
	 * Mapping name field.<br/>
	 */
	private TextField<String> tfName = null;
	/**
	 * Mapping description field.<br/>
	 */
	private TextArea taDesc = null;

	/**
	 * Change source fields.<br/>
	 */
	private List<Field<?>> fields = null;

	/**
	 * Bound template.<br/>
	 */
	private SourceMappingTemplateDTO sourceMappingTemplateDto = null;

	/** the category tree selection change listener */
	private SelectionChangedListener<CategorySelModelData> treeSelectionChangeListener = null;

	protected enum Type {
		SETS, CATEGORIES, CONTENTS, GENERAL
	};
	/*
	 * Injected deps.
	 */
	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Category cells renderer.<br/>
	 */
	private CategoryCellRenderer catCellRenderer = null;

	/**
	 * Content Type cells renderer<br/>
	 */
	private ContentTypeCellRenderer ctCellRenderer = null;

	/**
	 * Validates non empty strings.<br/>
	 */
	private NonEmptyStringValidator nonEmptyStringValidator = null;
	/**
	 * Mappings service.<br/>
	 */
	private ISourceMappingsServiceAsync sourceMappingsService = null;

	/**
	 * Error Message Resolver for Source Mappings service.<br/>
	 */
	private IErrorMessageResolver emrSourceMappings = null;

	/**
	 * Generic messages sourceMappingsService.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Common styles.<br/>
	 */
	private GuiCommonStyles guiCommonStyles = null;

	/**
	 * Pms styles.
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * Forms helper<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * PMS events listening strategy<br/>
	 */
	private PmsListeningStrategy pmsListeningStrategy = null;

	/**
	 * Field validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;

	/**
	 * Settings bundle
	 */
	private Settings settings = null;

	/**
	 * PmsUtil bundle
	 */
	private PmsUtil pmsUtil = null;
	/**
	 * @param mappingDto
	 */
	@Override
	protected void beforeRender() {
		assert sourceMappingTemplateDto != null : "sourceMappingTemplateDto can't be null";
		initListeners();
		initThis();
		initComponent();
	}
	
	/**
	 * @param mappingDto
	 */
	public void setSourceMappingTemplateDto(SourceMappingTemplateDTO mappingDto) {
		this.sourceMappingTemplateDto = mappingDto;
	}

	/**
	 * inits this container inner components.<br/>
	 */
	private void initComponent() {

		getHeader().addTool(buttonsSupport.createHelpToolButton(settings.pmsMappingsAdminPortalManualUrl()));

		addFields();

		addGrids();

		// create categories popup & tree + populate tree store:
		createCategoryTreePopup();

		// populate stores:
		showMappingValues();

		// must be called after populating stores:
		configGridsAndTreeListeners();

		addButtonBar();

	}

	/**
	 * Inits the panel properties.<br/>
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setLayoutOnChange(true);
		if (isEdition()) {
			setHeadingText(sourceMappingTemplateDto.getMapping().getName());
		} else {
			setHeadingText(messages.headerDetailPanel());
		}
		setWidth(Constants.EIGHTY_FIVE_PERCENT);
		setHeight(WINDOW_HEIGHT);
		setClosable(false);
		setButtonAlign(HorizontalAlignment.LEFT);
		setBodyBorder(false);
		setMaximizable(true);

		// layout on resize:
		addListener(Events.Resize, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent be) {
				layout();
			}
		});

		container = new TabPanel();
		add(container);
	}

	/**
	 * Adds a container and mapping grids.<br/>
	 */
	private void addGrids() {

		addSetsMapping();

		addContentTypesMapping();

		addCategoriesMapping();
	}

	/**
	 * Adds "name" and "description" fields.<br/>
	 */
	private void addFields() {

		TabItem tiBasicProperties = new TabItem(pmsMessages.basicPropertiesWidgetTitle());
		tiBasicProperties.setLayout(new FitLayout());
		tiBasicProperties.setScrollMode(Scroll.AUTOY);
		LayoutContainer lcBasicProperties = new LayoutContainer(formSupport.getStandardLayout());
		lcBasicProperties.addStyleName(guiCommonStyles.margin10px());
		lcBasicProperties.setAutoHeight(true);
		tiBasicProperties.add(lcBasicProperties);

		tfName = new TextField<String>();
		tfName.setFieldLabel(pmsMessages.labelName());
		tfName.setAllowBlank(false);
		tfName.setAutoValidate(true);
		tfName.setValidator(nonEmptyStringValidator);
		// validate-on-render listener:
		tfName.addListener(Events.Render, validatorListener);
		lcBasicProperties.add(tfName);

		taDesc = new TextArea();
		taDesc.setFieldLabel(pmsMessages.labelDescription());
		taDesc.setWidth(Constants.FIELD_WIDTH);
		lcBasicProperties.add(taDesc);

		fields = Arrays.asList(new Field<?>[] {tfName, taDesc});
		container.add(tiBasicProperties);
	}

	/**
	 * Adds the content types mapping panel, which contains the mapping grid and a toolbar.<br/>
	 */
	private void addContentTypesMapping() {

		TabItem tiContentTypes = new TabItem(pmsMessages.headerContentTypesMappingPanel());
		tiContentTypes.setLayout(new FitLayout());
		tiContentTypes.setScrollMode(Scroll.AUTOY);
		pContentTypes = new ContentPanel(new FitLayout());
		pContentTypes.addStyleName(guiCommonStyles.margin10px());
		pContentTypes.setLayoutOnChange(true);
		pContentTypes.setHeight(GRID_PANELS_HEIGHT);
		pContentTypes.setHeaderVisible(false);
		tiContentTypes.add(pContentTypes);
		container.add(tiContentTypes);

		sPossibleCt = new ListStore<ContentTypeSelModelData>();
		fillPossibleContentTypesStore(sourceMappingTemplateDto.getContentTypes());

		ComboBox<ContentTypeSelModelData> cbPossibleCt = new ComboBox<ContentTypeSelModelData>();
		cbPossibleCt.setTriggerAction(TriggerAction.ALL);
		cbPossibleCt.setDisplayField(ContentTypeSelModelData.PROPERTY_NAME);
		cbPossibleCt.setAllowBlank(false);
		cbPossibleCt.setEditable(false);
		cbPossibleCt.addListener(Events.Render, validatorListener);
		cbPossibleCt.setStore(sPossibleCt);

		/*
		 * gCtMappings column configs
		 */
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<ContentTypeMappingModelData> sm = new CheckBoxSelectionModel<ContentTypeMappingModelData>();
		sm.setSelectionMode(SelectionMode.SIMPLE);
		configs.add(sm.getColumn());

		CellEditor editor = new ContentTypeCellEditor(cbPossibleCt);

		ColumnConfig column = new ColumnConfig();
		column.setId(ContentTypeMappingModelData.PROPERTY_CONTENT_TYPE);
		column.setHeaderText(pmsMessages.columnHeaderContentType());
		column.setRenderer(ctCellRenderer);
		column.setWidth(COL_CONTENT_TYPE_WIDTH);
		column.setEditor(editor);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(ContentTypeMappingModelData.PROPERTY_MAPPING);
		column.setHeaderText(pmsMessages.columnHeaderMapping());
		column.setWidth(COL_MAPPING_WIDTH);
		column.setRenderer(new GridCellRenderer<ContentTypeMappingModelData>() {
			public Object render(ContentTypeMappingModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<ContentTypeMappingModelData> store, Grid<ContentTypeMappingModelData> grid) {
				String value = model.getDTO().getMapping();
				if (value == null) {
					return "";
				}
				return MAPPING_COLUMN_TEMPLATE.replaceAll(MAPPING_COLUMN_PATTERN, Format.htmlEncode(value));
			}
		});

		// editor TextField:
		TextField<String> tfCtEditor = new TextField<String>();
		tfCtEditor.setAllowBlank(false);
		tfCtEditor.setAutoValidate(true);
		tfCtEditor.setValidator(nonEmptyStringValidator);
		tfCtEditor.addListener(Events.Render, validatorListener);

		column.setEditor(new CellEditor(tfCtEditor));
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		sCtMappings = new ListStore<ContentTypeMappingModelData>();
		gCtMappings = new EditorGrid<ContentTypeMappingModelData>(sCtMappings, cm);

		gCtMappings.setSelectionModel(sm);
		gCtMappings.addPlugin(sm);
		gCtMappings.setAutoExpandColumn(ContentTypeMappingModelData.PROPERTY_MAPPING);
		gCtMappings.setLoadMask(true);
		gCtMappings.getView().setForceFit(true);

		gCtMappings.getSelectionModel().addSelectionChangedListener(
			new SelectionChangedListener<ContentTypeMappingModelData>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<ContentTypeMappingModelData> se) {
					ContentTypeMappingModelData selected = gCtMappings.getSelectionModel().getSelectedItem();
					if (selected != null) {
						deleteItem.enable();
					} else {
						deleteItem.disable();
					}
				}
			});

		addContentTypesToolbar();

		pContentTypes.add(gCtMappings);

	}

	/**
	 * Adds the content types toolbar to the content type mappings panel.<br/>
	 */
	private void addContentTypesToolbar() {
		// create and add the tool bar
		ToolBar toolBar = new ToolBar();
		SelectionListener<ButtonEvent> lAdd = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				newContentTypeTemplate();
			}
		};
		buttonsSupport.addAddButton(toolBar, lAdd, null);
		toolBar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<ContentTypeMappingModelData> selectedItems = gCtMappings.getSelectionModel().getSelectedItems();
				if (selectedItems != null && !selectedItems.isEmpty()) {
					for (ContentTypeMappingModelData item : selectedItems) {
						sCtMappings.remove(item);
					}
				}
			}
		};
		deleteItem = buttonsSupport.addDeleteButton(toolBar, lDelete, null);
		deleteItem.disable();

		if (isEdition()) {
			addExportImportButtons(toolBar, Type.CONTENTS);
		}
		toolBar.add(new FillToolItem());

		// filter
		CustomizableStoreFilter<ContentTypeMappingModelData> filter = new CustomizableStoreFilter<ContentTypeMappingModelData>(
			Arrays.asList(new String[] {ContentTypeMappingModelData.PROPERTY_CONTENT_TYPE,
				ContentTypeMappingModelData.PROPERTY_MAPPING}));
		filter.bind(sCtMappings);
		toolBar.add(filter);

		pContentTypes.setTopComponent(toolBar);
	}

	/**
	 * Adds the categories mapping panel, which contains the mapping grid and a toolbar.<br/>
	 */
	private void addCategoriesMapping() {

		TabItem tiCategories = new TabItem(pmsMessages.headerCategoriesMappingPanel());
		tiCategories.setLayout(new FitLayout());
		tiCategories.setScrollMode(Scroll.AUTOY);
		pCategories = new ContentPanel(new FitLayout());
		pCategories.addStyleName(guiCommonStyles.margin10px());
		pCategories.setLayoutOnChange(true);
		pCategories.setHeight(GRID_PANELS_HEIGHT);
		pCategories.setHeaderVisible(false);
		tiCategories.add(pCategories);
		container.add(tiCategories);

		final List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<CategoryMappingModelData> sm = new CheckBoxSelectionModel<CategoryMappingModelData>();
		sm.setSelectionMode(SelectionMode.MULTI);
		configs.add(sm.getColumn());

		final ColumnConfig columnTree = new ColumnConfig();
		columnTree.setId(CategoryMappingModelData.PROPERTY_CATEGORY);
		columnTree.setRenderer(catCellRenderer);
		columnTree.setHeaderText(pmsMessages.columnHeaderCategory());
		columnTree.setWidth(COL_CATEGORY_WIDTH);
		configs.add(columnTree);

		ColumnConfig column = new ColumnConfig();
		column.setId(CategoryMappingModelData.PROPERTY_MAPPING);
		column.setHeaderText(pmsMessages.columnHeaderMapping());
		column.setWidth(COL_MAPPING_WIDTH);
		column.setRenderer(new GridCellRenderer<CategoryMappingModelData>() {

			public Object render(CategoryMappingModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<CategoryMappingModelData> store, Grid<CategoryMappingModelData> grid) {
				String value = model.getDTO().getMapping();
				if (value == null) {
					return "";
				}
				return MAPPING_COLUMN_TEMPLATE.replaceAll(MAPPING_COLUMN_PATTERN, Format.htmlEncode(value));
			}
		});

		// TextField editor:
		TextField<String> tfCatEditor = new TextField<String>();
		tfCatEditor.setAllowBlank(false);
		tfCatEditor.setAutoValidate(true);
		tfCatEditor.setValidator(nonEmptyStringValidator);
		tfCatEditor.addListener(Events.Render, validatorListener);
		column.setEditor(new CellEditor(tfCatEditor));
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		sCatMappings = new ListStore<CategoryMappingModelData>();

		gCatMappings = new EditorGrid<CategoryMappingModelData>((ListStore<CategoryMappingModelData>) sCatMappings, cm);

		gCatMappings.setSelectionModel(sm);
		gCatMappings.addPlugin(sm);
		gCatMappings.setLoadMask(true);
		gCatMappings.getView().setForceFit(true);

		// open the category tree window when clicking in category column
		gCatMappings.addListener(Events.CellClick, new Listener<GridEvent<CategorySelModelData>>() {
			public void handleEvent(GridEvent<CategorySelModelData> be) {
				// retrieve clicked model:
				if (be.getColIndex() == configs.indexOf(columnTree)) {
					CategoryMappingModelData mModelData = gCatMappings.getStore().getAt(be.getRowIndex());

					catWindow.show();
					// select current:
					CategorySelDTO cat = mModelData.getDTO().getCategory();
					if (cat != null) {
						// remove selection change listener
						tree.getSelectionModel().removeListener(Events.SelectionChange, treeSelectionChangeListener);
						CategorySelModelData csModelData = catTreeStore.findModel(CategorySelModelData.PROPERTY_ID, cat
							.getId());
						tree.getSelectionModel().select(csModelData, false);
						// add selection change listener again
						tree.getSelectionModel().addSelectionChangedListener(treeSelectionChangeListener);
					}
				}
			}
		});

		gCatMappings.getSelectionModel().addSelectionChangedListener(
			new SelectionChangedListener<CategoryMappingModelData>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<CategoryMappingModelData> se) {
					if (se.getSelection() != null && !se.getSelection().isEmpty()) {
						deleteCategoryItem.enable();
					} else {
						deleteCategoryItem.disable();
					}
				}
			});

		pCategories.add(gCatMappings);

		addCategoryMappingToolbar();
	}

	/**
	 * Adds the categories toolbar to the categories mappings panel.<br/>
	 */
	private void addCategoryMappingToolbar() {
		ToolBar toolBar = new ToolBar();
		Button ttiNew = new Button(messages.labelAdd());
		ttiNew.setIconStyle(guiCommonStyles.iNew());
		ttiNew.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				// async call
				newCategoryTemplate();
			}
		});
		toolBar.add(ttiNew);
		toolBar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDeleteCategory = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<CategoryMappingModelData> selectedItems = gCatMappings.getSelectionModel().getSelectedItems();
				if (selectedItems != null && !selectedItems.isEmpty()) {
					for (CategoryMappingModelData item : selectedItems) {
						sCatMappings.remove(item);
					}
				}
			}
		};
		deleteCategoryItem = buttonsSupport.addDeleteButton(toolBar, lDeleteCategory);
		deleteCategoryItem.disable();
		toolBar.add(deleteCategoryItem);

		if (isEdition()) {
			addExportImportButtons(toolBar, Type.CATEGORIES);
		}
		toolBar.add(new FillToolItem());

		// filter
		CustomizableStoreFilter<CategoryMappingModelData> filter = new CustomizableStoreFilter<CategoryMappingModelData>(
			Arrays.asList(new String[] {CategoryMappingModelData.PROPERTY_CATEGORY,
				CategoryMappingModelData.PROPERTY_MAPPING}));
		filter.bind(sCatMappings);
		toolBar.add(filter);

		pCategories.setTopComponent(toolBar);
	}

	/**
	 * Adds the sets mapping panel, which contains the mapping grid and a toolbar.<br/>
	 */
	private void addSetsMapping() {

		TabItem tiSets = new TabItem(pmsMessages.headerSetsMappingPanel());
		tiSets.setLayout(new FitLayout());
		tiSets.setScrollMode(Scroll.AUTOY);
		pSets = new ContentPanel(new FitLayout());
		pSets.addStyleName(guiCommonStyles.margin10px());
		pSets.setLayoutOnChange(true);
		pSets.setHeight(GRID_PANELS_HEIGHT);
		pSets.setHeaderVisible(false);
		tiSets.add(pSets);
		container.add(tiSets);

		/*
		 * gSetsMappings column configs
		 */
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<SetMappingModelData> sm = new CheckBoxSelectionModel<SetMappingModelData>();
		sm.setSelectionMode(SelectionMode.SIMPLE);
		configs.add(sm.getColumn());

		// editor TextField:
		TextField<String> tfCtEditor = new TextField<String>();
		tfCtEditor.setAllowBlank(false);
		tfCtEditor.setAutoValidate(true);
		tfCtEditor.setValidator(nonEmptyStringValidator);
		tfCtEditor.addListener(Events.Render, validatorListener);

		ColumnConfig column = new ColumnConfig();
		column.setId(SetMappingModelData.PROPERTY_SET);
		column.setHeaderText(pmsMessages.columnHeaderSet());
		column.setWidth(COL_CONTENT_TYPE_WIDTH);
		column.setRenderer(new HtmlEncodeGridCellRenderer());
		column.setEditor(new CellEditor(tfCtEditor));
		configs.add(column);

		column = new ColumnConfig();
		column.setId(SetMappingModelData.PROPERTY_MAPPING);
		column.setHeaderText(pmsMessages.columnHeaderMapping());
		column.setWidth(COL_MAPPING_WIDTH);
		column.setRenderer(new GridCellRenderer<SetMappingModelData>() {
			public Object render(SetMappingModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<SetMappingModelData> store, Grid<SetMappingModelData> grid) {
				String value = model.getDTO().getMapping();
				if (value == null) {
					return "";
				}
				return MAPPING_COLUMN_TEMPLATE.replaceAll(MAPPING_COLUMN_PATTERN, Format.htmlEncode(value));
			}
		});

		column.setEditor(new CellEditor(new TextField<String>()));
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		sSetMappings = new ListStore<SetMappingModelData>();
		gSetMappings = new EditorGrid<SetMappingModelData>(sSetMappings, cm);

		gSetMappings.setSelectionModel(sm);
		gSetMappings.addPlugin(sm);
		gSetMappings.setAutoExpandColumn(SetMappingModelData.PROPERTY_MAPPING);
		gSetMappings.setLoadMask(true);
		gSetMappings.getView().setForceFit(true);

		gSetMappings.getSelectionModel().addSelectionChangedListener(
			new SelectionChangedListener<SetMappingModelData>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<SetMappingModelData> se) {
					SetMappingModelData selected = gSetMappings.getSelectionModel().getSelectedItem();
					if (selected != null) {
						deleteSetItem.enable();
					} else {
						deleteSetItem.disable();
					}
				}
			});

		addSetsToolbar();
		pSets.add(gSetMappings);
	}

	/**
	 * Adds the sets toolbar to the sets mappings panel.<br/>
	 */
	private void addSetsToolbar() {
		// create and add the tool bar
		ToolBar toolBar = new ToolBar();
		SelectionListener<ButtonEvent> lAdd = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				sSetMappings.add(new SetMappingModelData());
			}
		};
		buttonsSupport.addAddButton(toolBar, lAdd, null);
		toolBar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<SetMappingModelData> selectedItems = gSetMappings.getSelectionModel().getSelectedItems();
				if (selectedItems != null && !selectedItems.isEmpty()) {
					for (SetMappingModelData item : selectedItems) {
						sSetMappings.remove(item);
					}
				}
			}
		};
		deleteSetItem = buttonsSupport.addDeleteButton(toolBar, lDelete, null);
		deleteSetItem.disable();

		if (isEdition()) {
			addExportImportButtons(toolBar, Type.SETS);
		}
		toolBar.add(new FillToolItem());

		// filter
		CustomizableStoreFilter<SetMappingModelData> filter = new CustomizableStoreFilter<SetMappingModelData>(Arrays
			.asList(new String[] {SetMappingModelData.PROPERTY_SET, SetMappingModelData.PROPERTY_MAPPING}));
		filter.bind(sSetMappings);
		toolBar.add(filter);

		pSets.setTopComponent(toolBar);
	}

	private void addExportImportButtons(ToolBar toolBar, final Type type) {
		buttonsSupport.addSeparator(toolBar);

		// Export button
		buttonsSupport.addGenericButton(pmsMessages.labelExport(), pmsStyles.exportIcon(), toolBar,
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					String mappingId = sourceMappingTemplateDto.getMapping().getId();
					AsyncCallback<String> callback = new AsyncCallback<String>() {
						public void onSuccess(String url) {
							pmsUtil.exportPmsFile(url);
						}

						public void onFailure(Throwable caught) {
							util.error(pmsMessages.msgExportError());
						}
					};

					switch (type) {
						case CATEGORIES:
							sourceMappingsService.exportCategories(mappingId, callback);
							break;
						case CONTENTS:
							sourceMappingsService.exportContentTypes(mappingId, callback);
							break;
						case SETS:
							sourceMappingsService.exportSets(mappingId, callback);
							break;
						default:
							break;
					}

				}
			});
		buttonsSupport.addSeparator(toolBar);

		// Import button
		buttonsSupport.addGenericButton(pmsMessages.labelImport(), pmsStyles.importIcon(), toolBar,
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					MappingImportWindow importWindow = PmsFactory.getInstance().getMappingImportWindow();
					importWindow.setMappingId(sourceMappingTemplateDto.getMapping().getId());
					importWindow.setType(type);
					importWindow.show();
				}
			});
	}

	/**
	 * Adds a buttons bar with "Accept" and "Cancel" options.<br/>
	 */
	private void addButtonBar() {
		List<Component> changeEventSourceComponents = new LinkedList<Component>(fields);
		changeEventSourceComponents.add(gCtMappings);
		changeEventSourceComponents.add(gCatMappings);
		changeEventSourceComponents.add(gSetMappings);

		Button b = buttonsSupport.createSaveButtonForDetailPanels(this, acceptListener, changeEventSourceComponents,
			pmsListeningStrategy);
		addButton(b);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Requests the service for a new Content Type template.<br/> On success, adds it to the Content types mappings
	 * grid.
	 */
	private void newContentTypeTemplate() {
		AsyncCallback<SourceMappingTemplateDTO> callback = new AsyncCallback<SourceMappingTemplateDTO>() {
			public void onSuccess(SourceMappingTemplateDTO mapping) {

				sCtMappings.add(new ContentTypeMappingModelData());

				List<ContentTypeSelDTO> listDto = mapping.getContentTypes();

				fillPossibleContentTypesStore(listDto);
			}

			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, emrSourceMappings, pmsMessages.msgErrorRetrieveContentTypes());
			}
		};

		sourceMappingsService.newTemplate(callback);
	}

	/**
	 * Requests the service for a new Category template.<br/> On success, adds it to the Categories mappings grid.
	 */
	private void newCategoryTemplate() {
		AsyncCallback<SourceMappingTemplateDTO> callback = new AsyncCallback<SourceMappingTemplateDTO>() {
			public void onSuccess(SourceMappingTemplateDTO mapping) {

				sCatMappings.add(new CategoryMappingModelData());

				// populate tree:
				catTreeStore.removeAll();
				catTreeStore.add(mapping.getCategories(), true);
			}

			public void onFailure(Throwable arg0) {
				MessageBox.info(messages.headerErrorWindow(),
					emrSourceMappings.getMessage(arg0, pmsMessages.msgErrorRetrieveNewCategoryMappingTemplate()), null)
					.setModal(true);
			}
		};

		sourceMappingsService.newTemplate(callback);
	}

	/**
	 * Shows {@link SourceMappingTemplateDTO the bound DTO} values on GUI.
	 */
	private void showMappingValues() {

		sCtMappings.removeAll();
		sCatMappings.removeAll();
		sSetMappings.removeAll();

		SourceMappingDTO mappingDto = sourceMappingTemplateDto.getMapping();
		if (mappingDto != null) {
			String name = mappingDto.getName();
			tfName.setValue(name);
			tfName.updateOriginalValue(name);

			String desc = mappingDto.getDescription();
			taDesc.setValue(desc);
			taDesc.updateOriginalValue(desc);

			// show the content types mapping
			List<ContentTypeMappingDTO> listConTypDTO = mappingDto.getContentTypes();
			List<ContentTypeMappingModelData> lCtMappings = new LinkedList<ContentTypeMappingModelData>();
			for (ContentTypeMappingDTO dto : listConTypDTO) {
				lCtMappings.add(new ContentTypeMappingModelData(dto));
			}
			sCtMappings.add(lCtMappings);

			// show the categories mapping
			List<CategoryMappingDTO> listCatDTO = sourceMappingTemplateDto.getMapping().getCategories();
			List<CategoryMappingModelData> lCatMappings = new LinkedList<CategoryMappingModelData>();
			for (CategoryMappingDTO cDto : listCatDTO) {
				lCatMappings.add(new CategoryMappingModelData(cDto));
			}
			sCatMappings.add(lCatMappings);

			// show the sets mapping
			List<SetMappingDTO> listSetDTO = sourceMappingTemplateDto.getMapping().getSets();
			List<SetMappingModelData> lSetMappings = new LinkedList<SetMappingModelData>();
			for (SetMappingDTO cDto : listSetDTO) {
				lSetMappings.add(new SetMappingModelData(cDto));
			}
			sSetMappings.add(lSetMappings);

		} else {
			// it's new element
			tfName.setValue(null);
			tfName.updateOriginalValue(null);
			taDesc.setValue(null);
			taDesc.updateOriginalValue(null);
		}

	}

	/**
	 * Inits the listeners.
	 */
	private void initListeners() {
		acceptListener = new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button button = be.getButtonClicked();
						if (button != null && button.getItemId().equals(Dialog.YES)) {
							trySaveSourceMapping();
						}
					}
				};

				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSaveMapping(), lConfirm)
					.setModal(true);
			}
		};

		// Categories mappings grid custom config:
		treeSelectionChangeListener = new SelectionChangedListener<CategorySelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<CategorySelModelData> se) {
				CategorySelModelData selected = se.getSelectedItem();
				List<CategorySelModelData> rootItems = tree.getStore().getRootItems();
				CategorySelModelData root = rootItems.isEmpty() ? null : rootItems.get(0);
				if (selected != null && root != null && !selected.equals(root)) {

					CategoryMappingModelData categoryMappingSelected = gCatMappings.getSelectionModel()
						.getSelectedItem();

					ListStore<CategoryMappingModelData> store = gCatMappings.getStore();
					Record record = store.getRecord(categoryMappingSelected);
					// Record.set() fires Update event:
					record.set(CategoryMappingModelData.PROPERTY_CATEGORY, selected.getDTO());

					// clear the tree selection
					tree.getSelectionModel().deselectAll();
					// hide the window when selects the category
					catWindow.hide();
				}
			}
		};

		final ChangeListener controllerListener = new ChangeListener() {
			public void modelChanged(ChangeEvent event) {
				switch (event.getType()) {
					case PmsChangeEvent.IMPORT:
						tryUpdateSourceMapping();
						break;
					default: // shouldn't happen..
						// Logger.getInstance().log(
						// "Unexpected event descriptor for a ChangeEventSource instance :" + event.getType());
				}
			}
		};
		final SourceMappingsController serviceController = (SourceMappingsController) sourceMappingsService;
		serviceController.addChangeListener(controllerListener);

		addListener(Events.Detach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				serviceController.removeChangeListener(controllerListener);

			}
		});
	}

	private void tryUpdateSourceMapping() {

		AsyncCallback<SourceMappingTemplateDTO> callback = new AsyncCallback<SourceMappingTemplateDTO>() {

			public void onSuccess(SourceMappingTemplateDTO result) {
				sourceMappingTemplateDto = result;
				showMappingValues();
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		};
		sourceMappingsService.get(sourceMappingTemplateDto.getMapping().getId(), callback);
	}

	/**
	 * Applies the GUI values on {@link #sourceMappingTemplateDto the bound DTO}, and calls remote procedure <b>save</b>
	 * on the {@link #sourceMappingsService service}.
	 */
	private final void trySaveSourceMapping() {
		mask(pmsMessages.mskSaveMapping());

		// IMPORTANT. Clear filters
		sCtMappings.clearFilters();
		sCatMappings.clearFilters();
		sSetMappings.clearFilters();
		
		SourceMappingDTO mapping = new SourceMappingDTO();

		if (sourceMappingTemplateDto.getMapping() != null) {
			// edition
			mapping = sourceMappingTemplateDto.getMapping();
		}
		mapping.setName(tfName.getValue());
		mapping.setDescription(taDesc.getValue());

		// save content types, first convert (dto to modeldata)
		List<ContentTypeMappingModelData> contenttypes = sCtMappings.getModels();
		List<ContentTypeMappingDTO> contentTypesDto = new LinkedList<ContentTypeMappingDTO>();
		for (ContentTypeMappingModelData cModel : contenttypes) {
			contentTypesDto.add(cModel.getDTO());
		}
		mapping.setContentTypes(contentTypesDto);

		// convert categories, dto to modeldata
		List<CategoryMappingModelData> listCategory = sCatMappings.getModels();
		List<CategoryMappingDTO> listCatDTO = new LinkedList<CategoryMappingDTO>();
		for (CategoryMappingModelData cModel : listCategory) {
			listCatDTO.add(cModel.getDTO());
		}
		mapping.setCategories(listCatDTO);

		// convert sets, dto to modeldata
		List<SetMappingModelData> listSet = sSetMappings.getModels();
		List<SetMappingDTO> listSetDTO = new LinkedList<SetMappingDTO>();
		for (SetMappingModelData cModel : listSet) {
			listSetDTO.add(cModel.getDTO());
		}
		mapping.setSets(listSetDTO);

		AsyncCallback<SourceMappingTemplateDTO> callback = new AsyncCallback<SourceMappingTemplateDTO>() {

			public void onFailure(Throwable arg0) {
				unmask();
				errorProcessor.processError(arg0, emrSourceMappings, pmsMessages.msgErrorSaveMapping());
			}

			public void onSuccess(SourceMappingTemplateDTO arg0) {
				MappingDetailPanel.this.hide();
				unmask();
				util.info(pmsMessages.msgSuccessSaveMapping());
			}
		};

		sourceMappingsService.save(mapping, callback);
	}

	/**
	 * Creates the popup window that contains the available categories tree.<br/> the tree is initially populated with
	 * the bound data categories DTOs. When a new mapping template is retrieved from server, the tree is refreshed with
	 * downloaded category tree info.
	 */
	private void createCategoryTreePopup() {
		catWindow = new Window();
		catWindow.setHeadingText(pmsMessages.headerCategorySelector());
		catWindow.setScrollMode(Scroll.AUTO);
		catWindow.setHeight(CATEGORIES_POPUP_HEIGHT);
		catWindow.setWidth(CATEGORIES_POPUP_WIDTH);
		catWindow.setModal(true);
		catWindow.setLayout(new FitLayout());

		catTreeStore = new CategoryTreeStore(sourceMappingTemplateDto.getCategories());
		tree = new TreePanel<CategorySelModelData>(catTreeStore);
		tree.setDisplayProperty(CategorySelModelData.PROPERTY_NAME);
		tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tree.getStyle().setLeafIcon(IconHelper.createStyle(guiCommonStyles.iconTreeFolder()));

		catWindow.add(tree);

		catWindow.addListener(Events.Show, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				catWindow.layout();
				tree.setExpanded(catTreeStore.getRootItems().get(0), true, false);
			}
		});
	}

	/**
	 * Populates the available Content Types store with the passed list of DTOs.
	 * @param listDto
	 */
	private void fillPossibleContentTypesStore(List<ContentTypeSelDTO> listDto) {

		sPossibleCt.removeAll();

		if (listDto == null || listDto.isEmpty()) {
			return;
		}

		List<ContentTypeSelModelData> lModelData = new LinkedList<ContentTypeSelModelData>();

		for (ContentTypeSelDTO dto : listDto) {
			lModelData.add(new ContentTypeSelModelData(dto));
		}
		sPossibleCt.add(lModelData);
	}

	/**
	 * Configures the grids for firing events when updated.<br/> Configures the tree for setting the selected tree
	 * category on the selected mapping, when a tree Category is clicked.
	 */
	private void configGridsAndTreeListeners() {
		// common config for grids:
		configGridForFiringEventsOnUpdate(gCtMappings);
		configGridForFiringEventsOnUpdate(gCatMappings);
		configGridForFiringEventsOnUpdate(gSetMappings);

		// Content types mappings grid custom config:
		gCtMappings.addListener(Events.AfterEdit, new Listener<GridEvent<ContentTypeMappingModelData>>() {
			public void handleEvent(GridEvent<ContentTypeMappingModelData> be) {
				be.getRecord().setDirty(true);
				Util.fireChangeEvent(gCtMappings);
			}
		});

		// sets mappings grid custom config:
		gSetMappings.addListener(Events.AfterEdit, new Listener<GridEvent<SetMappingModelData>>() {
			public void handleEvent(GridEvent<SetMappingModelData> be) {
				be.getRecord().setDirty(true);
				Util.fireChangeEvent(gSetMappings);
			}
		});

		tree.getSelectionModel().addSelectionChangedListener(treeSelectionChangeListener);
	}

	/**
	 * Common configuration for all grids.<br/>
	 * 
	 * @param grid
	 */
	@SuppressWarnings("unchecked")
	private void configGridForFiringEventsOnUpdate(final EditorGrid<?> grid) {
		grid.getStore().addStoreListener(new StoreListener() {
			@Override
			public void storeAdd(StoreEvent se) {
				gridsModified = true;
				Util.fireChangeEvent(grid);
			}

			@Override
			public void storeRemove(StoreEvent se) {
				gridsModified = true;
				Util.fireChangeEvent(grid);
			}

			@Override
			public void storeUpdate(StoreEvent se) {
				Util.fireChangeEvent(grid);
			}
		});
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		boolean gridsAreDirty = gridsModified || !gCtMappings.getStore().getModifiedRecords().isEmpty()
			|| !gCatMappings.getStore().getModifiedRecords().isEmpty()
			|| !gSetMappings.getStore().getModifiedRecords().isEmpty();
		return tfName.isDirty() || taDesc.isDirty() || gridsAreDirty;
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		boolean fieldsValid = true;
		Iterator<Field<?>> it = fields.iterator();
		while (it.hasNext() && fieldsValid) {
			Field<?> field = it.next();
			fieldsValid = fieldsValid && field.isValid();
		}
		return fieldsValid && gridsAreValid();
	}

	private boolean gridsAreValid() {
		return gridIsValid(gCatMappings) && gridIsValid(gCtMappings) && gridIsValid(gSetMappings);
	}

	private boolean gridIsValid(EditorGrid<? extends AbstractMappingModelData<?>> grid) {

		for (AbstractMappingModelData<?> model : grid.getStore().getModels()) {
			Map<String, Object> properties = model.getProperties();
			Collection<Object> values = properties.values();
			if (values.contains(null)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return sourceMappingTemplateDto.getMapping() != null;
	}

	/**
	 * Injector for {@link #sourceMappingsService}
	 * @param sourceMappingsService
	 */
	@Inject
	public void setSourceMappingsService(ISourceMappingsServiceAsync sourceMappingsService) {
		this.sourceMappingsService = sourceMappingsService;
	}

	/**
	 * Injector for {@link #messages}
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injector for {@link #pmsMessages}
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injector for {@link #guiCommonStyles}
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.guiCommonStyles = styles;
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
	 * Injects the shared objects container.
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * injects the forms helper.
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * @param strat the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy strat) {
		this.pmsListeningStrategy = strat;
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
	 * Injects the Error Message Resolver for Source Mappings.
	 * @param emrSourceMappings the emrSourceMappings to set
	 */
	@Inject
	public void setEmrSourceMappings(IErrorMessageResolver emrSourceMappings) {
		this.emrSourceMappings = emrSourceMappings;
	}

	/**
	 * Injects the Categories cell renderer.
	 * @param catCellRenderer the catCellRenderer to set
	 */
	@Inject
	public void setCatCellRenderer(CategoryCellRenderer catCellRenderer) {
		this.catCellRenderer = catCellRenderer;
	}

	/**
	 * Injects the Content Type cell renderer.
	 * @param ctCellRenderer the ctCellRenderer to set
	 */
	@Inject
	public void setCtCellRenderer(ContentTypeCellRenderer ctCellRenderer) {
		this.ctCellRenderer = ctCellRenderer;
	}

	/**
	 * A {@link CellEditor} for stores of Content Types.
	 * 
	 * @author Andrei Cojocaru
	 * 
	 */
	private static class ContentTypeCellEditor extends CellEditor {

		/**
		 * @param field
		 */
		public ContentTypeCellEditor(Field<?> field) {
			super(field);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.extjs.gxt.ui.client.widget.Editor#preProcessValue(java.lang.Object)
		 */
		/**
		 * <br/>
		 */
		@Override
		public Object preProcessValue(Object value) {
			if (value == null) {
				return null;
			}
			ContentTypeSelDTO ctsDto = (ContentTypeSelDTO) value;
			return new ContentTypeSelModelData(ctsDto);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.extjs.gxt.ui.client.widget.Editor#postProcessValue(java.lang.Object)
		 */
		/**
		 * <br/>
		 */
		@Override
		public Object postProcessValue(Object value) {
			if (value == null) {
				return value;
			}
			ContentTypeSelModelData ctsModelData = (ContentTypeSelModelData) value;
			return ctsModelData.getDTO();
		}
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}
}
