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
package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component;


import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeSelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.InheritedComponentInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;


/**
 * The window with own portal inherited components that can be exported. Only can be exported components with
 * dependences or configuration overrided
 * 
 * @author Manuel Ruiz
 * 
 */
public class InheritedComponentsExportWindow extends TypicalWindow {
	/**
	 * Width of the window.<br/>
	 */
	private static final String WINDOW_WIDTH = "700";

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
	private ListStore<InheritedComponentInstanceSelModelData> sGrid = null;

	/**
	 * Components grid.<br/>
	 */
	private Grid<InheritedComponentInstanceSelModelData> gComponents = null;

	/**
	 * Export selected connectors button
	 */
	private Button ttiExportSelected = null;

	/**
	 * the current portal id
	 */
	private String portalId = null;

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
	private IComponentsServiceAsync componentsService = null;

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
	 * Pms Styles bundle.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Alphabetical store sorter<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;
	
	/**
	 * Pms Utilities object reference.<br/>
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Unique allowed constructor.
	 */
	public InheritedComponentsExportWindow() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Component#beforeRender()
	 */
	@Override
	protected void beforeRender() {

		setHeadingText(pmsMessages.headerComponentsExport());
		setClosable(true);
		setWidth(WINDOW_WIDTH);
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTOY);

		initComponents();
	}

	/**
	 * Inits this container inner components. That includes requesting the async service for the Components
	 */
	private void initComponents() {
		addGrid();

		addToolbar();

		tryGetComponents();
	}

	/**
	 * Requests the service for the Components DTOs, and shows the results.<br/>
	 */
	private void tryGetComponents() {

		AsyncCallback<List<InheritedComponentInstanceSelDTO>> callback = new AsyncCallback<List<InheritedComponentInstanceSelDTO>>() {

			public void onSuccess(List<InheritedComponentInstanceSelDTO> arg0) {

				List<InheritedComponentInstanceSelModelData> lComponentsModelData = new LinkedList<InheritedComponentInstanceSelModelData>();
				for (InheritedComponentInstanceSelDTO dto : arg0) {
					if ((dto.getDependencies() != null && dto.getDependencies())
						|| (dto.getConfiguration() != null && dto.getConfiguration())) {
						lComponentsModelData.add(new InheritedComponentInstanceSelModelData(dto));
					}
				}
				sGrid.removeAll();
				sGrid.add(lComponentsModelData);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
			 */
			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, null, pmsMessages.msgErrorRetrieveComponents());
			}
		};

		componentsService.getInheritedComponents(portalId, callback);
	}

	/**
	 * Creates the gComponents with the list of components and add it to the panel
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<InheritedComponentInstanceSelModelData> selectionModel = new CheckBoxSelectionModel<InheritedComponentInstanceSelModelData>();
		configs.add(selectionModel.getColumn());

		ColumnConfig column = new ColumnConfig();
		column.setId(InheritedComponentInstanceSelModelData.PROPERTY_NAME);
		column.setHeaderText(pmsMessages.columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(InheritedComponentInstanceSelModelData.PROPERTY_DESCRIPTION);
		column.setHeaderText(pmsMessages.columnHeaderDescription());
		column.setWidth(COLUMN_DESCRIPTION_WIDTH);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		sGrid = new ListStore<InheritedComponentInstanceSelModelData>();
		sGrid.setStoreSorter((StoreSorter) storeSorter);
		sGrid.setSortField(ContentTypeSelModelData.PROPERTY_NAME);

		gComponents = new Grid<InheritedComponentInstanceSelModelData>(sGrid, cm);

		gComponents.setSelectionModel(selectionModel);
		gComponents.addPlugin(selectionModel);

		gComponents.setAutoExpandColumn(InheritedComponentInstanceSelModelData.PROPERTY_DESCRIPTION);
		gComponents.setLoadMask(true);
		gComponents.getView().setForceFit(true);

		gComponents.getSelectionModel().addSelectionChangedListener(
			new SelectionChangedListener<InheritedComponentInstanceSelModelData>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<InheritedComponentInstanceSelModelData> se) {
					if (se.getSelection() != null && !se.getSelection().isEmpty()) {
						ttiExportSelected.enable();
					} else {
						ttiExportSelected.disable();
					}
				}
			});

		add(gComponents);

	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 */
	private void addToolbar() {
		// the toolbar
		ToolBar gridToolBar = new ToolBar();
		setTopComponent(gridToolBar);

		SelectionListener<ButtonEvent> ttiExportSelectedListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				GridSelectionModel<InheritedComponentInstanceSelModelData> selectionModel = gComponents
					.getSelectionModel();
				List<InheritedComponentInstanceSelModelData> lSelected = selectionModel.getSelectedItems();
				if (!lSelected.isEmpty()) {
					Set<String> ids = new HashSet<String>();
					for (InheritedComponentInstanceSelModelData model : lSelected) {
						ids.add(model.getDTO().getComponent().getId());
					}
					tryExporSelectedComponents(ids);
				}
			}
		};
		ttiExportSelected = buttonsSupport.addGenericButton(pmsMessages.labelExportSome(), pmsStyles.exportIcon(),
			gridToolBar, ttiExportSelectedListener);
		ttiExportSelected.disable();

		SelectionListener<ButtonEvent> ttiExportAllListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				tryExportAllComponents();
			}
		};
		buttonsSupport.addGenericButton(pmsMessages.labelExportAll(), pmsStyles.exportIcon(), gridToolBar,
			ttiExportAllListener);

		gridToolBar.add(new FillToolItem());

		StoreFilterField<InheritedComponentInstanceSelModelData> filter = new CustomizableStoreFilter<InheritedComponentInstanceSelModelData>(
			Arrays.asList(new String[] {InheritedComponentInstanceSelModelData.PROPERTY_NAME,
				InheritedComponentInstanceSelModelData.PROPERTY_DESCRIPTION}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		filter.bind(sGrid);
		gridToolBar.add(filter);

		setTopComponent(gridToolBar);
	}

	private void tryExporSelectedComponents(Set<String> ids) {
		mask(pmsMessages.mskExport());

		AsyncCallback<String> callback = new AsyncCallback<String>() {

			public void onSuccess(String result) {
				unmask();
				pmsUtil.exportPmsFile(result);
			}

			public void onFailure(Throwable caught) {
				unmask();
				util.error(pmsMessages.msgExportError());
			}
		};
		componentsService.exportSomeOverrides(portalId, ids, callback);
	}

	private void tryExportAllComponents() {
		mask(pmsMessages.mskExport());

		AsyncCallback<String> callback = new AsyncCallback<String>() {

			public void onSuccess(String result) {
				unmask();
				pmsUtil.exportPmsFile(result);
			}

			public void onFailure(Throwable caught) {
				unmask();
				util.error(pmsMessages.msgExportError());
			}
		};
		componentsService.exportAllOverrides(portalId, callback);
	}

	/**
	 * Injects the Components service.<br/>
	 * @param service
	 */
	@Inject
	public void setComponentsService(IComponentsServiceAsync service) {
		this.componentsService = service;
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
	 * Injects the Pms styles bundle.<br/>
	 * @param styles
	 */
	@Inject
	public void setStyles(PmsStyles styles) {
		this.pmsStyles = styles;
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

	/**
	 * @param id the portal id
	 */
	public void setPortalId(String id) {
		this.portalId = id;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}
}
