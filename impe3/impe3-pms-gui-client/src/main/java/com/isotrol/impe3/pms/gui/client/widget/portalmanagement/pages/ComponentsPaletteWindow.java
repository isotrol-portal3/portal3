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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.PaletteModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;


/**
 * The window with selectable components.<br/> Must be initialized with a Listener that will be bound to
 * "Add selected components" button.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class ComponentsPaletteWindow extends TypicalWindow {
	/**
	 * Width of the window.<br/>
	 */
	private static final String PALETTE_WINDOW_WIDTH = "700";

	/*
	 * gComponents columns width
	 */
	/**
	 * "Name" column width.<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 250;
	/**
	 * "Description" column width.<br/>
	 */
	private static final int COLUMN_DESCRIPTION_WIDTH = 300;

	/** store with the components */
	private ListStore<PaletteModelData> sGrid = null;

	/**
	 * Components grid.<br/>
	 */
	private Grid<PaletteModelData> gComponents = null;

	/**
	 * Toolbar button for adding selected elements to dependences tree.<br/> Associated to components palette window
	 * toolbar.
	 */
	private Button ttiAddSelected = null;

	/**
	 * The widget that must receive the selected Components of this widget.<br/>
	 */
	private IPaletteComponentsReceiver componentsReceiver = null;

	/** portal locator */
	private PortalPagesLoc portalPagesLoc = null;

	/*
	 * Injected deps.
	 */
	/**
	 * Service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Utilities object reference.<br/>
	 */
	private Util util = null;

	/**
	 * Components async service proxy.<br/>
	 */
	private IPagesServiceAsync pagesService = null;

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
	 * Unique allowed constructor.
	 */
	public ComponentsPaletteWindow() {
	}

	/**
	 * <br/>
	 * @param acceptListener
	 */
	public void init(IPaletteComponentsReceiver receiver, PortalPagesLoc portalLocator) {
		this.componentsReceiver = receiver;
		this.portalPagesLoc = portalLocator;

		setHeadingText(pmsMessages.headerComponentsPalette());
		setClosable(true);
		setWidth(PALETTE_WINDOW_WIDTH);
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTOY);

		initComponents();
	}

	/**
	 * Inits this container inner components. That includes requesting the async service for the Components
	 * palette.<br/>
	 */
	private void initComponents() {
		addGrid();

		addToolbar();

		tryGetPalette();
	}

	/**
	 * Requests the service for the Palette DTOs, and shows the results.<br/>
	 */
	private void tryGetPalette() {
		util.mask(pmsMessages.mskPalette());

		AsyncCallback<List<PaletteDTO>> callback = new AsyncCallback<List<PaletteDTO>>() {

			public void onSuccess(List<PaletteDTO> arg0) {
				List<PaletteModelData> lPaletteModelData = new LinkedList<PaletteModelData>();
				for (PaletteDTO dto : arg0) {
					lPaletteModelData.add(new PaletteModelData(dto));
				}
				sGrid.removeAll();
				sGrid.add(lPaletteModelData);

				util.unmask();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
			 */
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, null, pmsMessages.msgErrorRetrievePalette());
			}
		};

		pagesService.getPalette(portalPagesLoc, callback);
	}

	/**
	 * Creates the gComponents with the list of components and add it to the panel
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<PaletteModelData> selectionModel = new CheckBoxSelectionModel<PaletteModelData>();
		configs.add(selectionModel.getColumn());

		ColumnConfig column = new ColumnConfig();
		column.setId(PaletteModelData.PROPERTY_NAME);
		column.setHeaderText(pmsMessages.columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		// render to show a tooltip
		column.setRenderer(new GridCellRenderer<PaletteModelData>() {
			public Object render(PaletteModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<PaletteModelData> store, Grid<PaletteModelData> grid) {
				String name = (String) model.get(property);
				Html html = new Html((String) model.get(property));
				html.setToolTip(name);
				html.getToolTip().setAutoWidth(true);
				return html;
			}
		});
		configs.add(column);

		column = new ColumnConfig();
		column.setId(PaletteModelData.PROPERTY_DESCRIPTION);
		column.setHeaderText(pmsMessages.columnHeaderDescription());
		column.setWidth(COLUMN_DESCRIPTION_WIDTH);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		sGrid = new ListStore<PaletteModelData>();
		sGrid.setStoreSorter((StoreSorter) storeSorter);
		sGrid.setSortField(PaletteModelData.PROPERTY_NAME);

		gComponents = new Grid<PaletteModelData>(sGrid, cm);

		gComponents.setSelectionModel(selectionModel);
		gComponents.addPlugin(selectionModel);

		gComponents.setAutoExpandColumn(PaletteModelData.PROPERTY_DESCRIPTION);
		gComponents.setLoadMask(true);
		gComponents.getView().setForceFit(true);

		gComponents.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<PaletteModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<PaletteModelData> se) {
				if (se.getSelection() != null && !se.getSelection().isEmpty()) {
					ttiAddSelected.enable();
				} else {
					ttiAddSelected.disable();
				}
			}
		});

		add(gComponents);

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
				GridSelectionModel<PaletteModelData> selectionModel = gComponents.getSelectionModel();
				List<PaletteModelData> lSelected = selectionModel.getSelectedItems();
				if (!lSelected.isEmpty()) {
					componentsReceiver.receiveComponents(lSelected);
					hide();
					selectionModel.deselectAll();
					ttiAddSelected.disable();
				}
			}
		};
		ttiAddSelected = buttonsSupport.addGenericButton(pmsMessages.labelAddSelected(), styles.iNew(), gridToolBar,
			ttiAddListener);
		ttiAddSelected.disable();

		gridToolBar.add(new FillToolItem());

		StoreFilterField<PaletteModelData> filter = new CustomizableStoreFilter<PaletteModelData>(Arrays
			.asList(new String[] {PaletteModelData.PROPERTY_NAME, PaletteModelData.PROPERTY_DESCRIPTION}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		filter.bind(sGrid);
		gridToolBar.add(filter);

		setTopComponent(gridToolBar);
	}

	/**
	 * Injects the components service.<br/>
	 * @param service
	 */
	@Inject
	public void setComponentsService(IPagesServiceAsync service) {
		this.pagesService = service;
	}

	/**
	 * Injects the Pages service.<br/>
	 * @param pagesService
	 */
	@Inject
	public void setPagesService(IPagesServiceAsync pagesService) {
		this.pagesService = pagesService;
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
	 * Injects the utilities reference.
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}
}
