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
package com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes;


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
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;


/**
 * The window with selectable content types that can be exported.<br/> .
 * 
 * @author Manuel Ruiz
 * 
 */
public class ContentTypesExportWindow extends TypicalWindow {
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
	private ListStore<ContentTypeSelModelData> sGrid = null;

	/**
	 * Components grid.<br/>
	 */
	private Grid<ContentTypeSelModelData> gContentTypes = null;

	/**
	 * Export selected content types button
	 */
	private Button ttiExportSelected = null;

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
	private IContentTypesServiceAsync contentTypesService = null;

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
	 * Pms util bundle
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Unique allowed constructor.
	 */
	public ContentTypesExportWindow() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Component#beforeRender()
	 */
	@Override
	protected void beforeRender() {

		setHeading(pmsMessages.headerContentTypesExport());
		setClosable(true);
		setWidth(WINDOW_WIDTH);
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTOY);

		initComponents();
	}

	/**
	 * Inits this container inner components. That includes requesting the async service for the Content Types
	 */
	private void initComponents() {
		addGrid();

		addToolbar();

		tryGetContentTypes();
	}

	/**
	 * Requests the service for the ContentType DTOs, and shows the results.<br/>
	 */
	private void tryGetContentTypes() {
		// mask(pmsMessages.mskContentTypes());

		AsyncCallback<List<ContentTypeSelDTO>> callback = new AsyncCallback<List<ContentTypeSelDTO>>() {

			public void onSuccess(List<ContentTypeSelDTO> arg0) {

				// unmask();

				List<ContentTypeSelModelData> lContentTypeModelData = new LinkedList<ContentTypeSelModelData>();
				for (ContentTypeSelDTO dto : arg0) {
					lContentTypeModelData.add(new ContentTypeSelModelData(dto));
				}
				sGrid.removeAll();
				sGrid.add(lContentTypeModelData);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
			 */
			public void onFailure(Throwable arg0) {
				// unmask();
				errorProcessor.processError(arg0, null, pmsMessages.msgErrorRetrieveContentTypes());
			}
		};

		contentTypesService.getContentTypes(callback);
	}

	/**
	 * Creates the gComponents with the list of components and add it to the panel
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<ContentTypeSelModelData> selectionModel = new CheckBoxSelectionModel<ContentTypeSelModelData>();
		configs.add(selectionModel.getColumn());

		ColumnConfig column = new ColumnConfig();
		column.setId(ContentTypeSelModelData.PROPERTY_NAME);
		column.setHeader(pmsMessages.columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		configs.add(column);

		column = new ColumnConfig();
		column.setId(ContentTypeSelModelData.PROPERTY_DESCRIPTION);
		column.setHeader(pmsMessages.columnHeaderDescription());
		column.setWidth(COLUMN_DESCRIPTION_WIDTH);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		sGrid = new ListStore<ContentTypeSelModelData>();
		sGrid.setStoreSorter((StoreSorter) storeSorter);
		sGrid.setSortField(ContentTypeSelModelData.PROPERTY_NAME);

		gContentTypes = new Grid<ContentTypeSelModelData>(sGrid, cm);

		gContentTypes.setSelectionModel(selectionModel);
		gContentTypes.addPlugin(selectionModel);

		gContentTypes.setAutoExpandColumn(ContentTypeSelModelData.PROPERTY_DESCRIPTION);
		gContentTypes.setLoadMask(true);
		gContentTypes.getView().setForceFit(true);

		gContentTypes.getSelectionModel().addSelectionChangedListener(
			new SelectionChangedListener<ContentTypeSelModelData>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<ContentTypeSelModelData> se) {
					if (se.getSelection() != null && !se.getSelection().isEmpty()) {
						ttiExportSelected.enable();
					} else {
						ttiExportSelected.disable();
					}
				}
			});

		add(gContentTypes);

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
				GridSelectionModel<ContentTypeSelModelData> selectionModel = gContentTypes.getSelectionModel();
				List<ContentTypeSelModelData> lSelected = selectionModel.getSelectedItems();
				if (!lSelected.isEmpty()) {
					Set<String> ids = new HashSet<String>();
					for (ContentTypeSelModelData model : lSelected) {
						ids.add(model.getDTO().getId());
					}
					tryExporSelectedContentTypes(ids);
				}
			}
		};
		ttiExportSelected = buttonsSupport.addGenericButton(pmsMessages.labelExportSome(), pmsStyles.exportIcon(),
			gridToolBar, ttiExportSelectedListener);
		ttiExportSelected.disable();

		SelectionListener<ButtonEvent> ttiExportAllListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				tryExportAllContentTypes();
			}
		};
		buttonsSupport.addGenericButton(pmsMessages.labelExportAll(), pmsStyles.exportIcon(), gridToolBar,
			ttiExportAllListener);

		gridToolBar.add(new FillToolItem());

		StoreFilterField<ContentTypeSelModelData> filter = new CustomizableStoreFilter<ContentTypeSelModelData>(Arrays
			.asList(new String[] {ContentTypeSelModelData.PROPERTY_NAME, ContentTypeSelModelData.PROPERTY_DESCRIPTION}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		filter.bind(sGrid);
		gridToolBar.add(filter);

		setTopComponent(gridToolBar);
	}

	private void tryExporSelectedContentTypes(Set<String> ids) {
		mask(pmsMessages.mskContentTypesExport());

		AsyncCallback<String> callback = new AsyncCallback<String>() {

			public void onSuccess(String result) {
				unmask();
				pmsUtil.exportPmsFile(result);
			}

			public void onFailure(Throwable caught) {
				unmask();
				util.error(pmsMessages.msgErrorExportContentTypes());
			}
		};

		contentTypesService.exportSome(ids, callback);
	}

	private void tryExportAllContentTypes() {
		mask(pmsMessages.mskContentTypesExport());

		AsyncCallback<String> callback = new AsyncCallback<String>() {

			public void onSuccess(String result) {
				unmask();
				pmsUtil.exportPmsFile(result);
			}

			public void onFailure(Throwable caught) {
				unmask();
				util.error(pmsMessages.msgErrorExportContentTypes());
			}
		};

		contentTypesService.exportAll(callback);
	}

	/**
	 * Injects the ContentTypes service.<br/>
	 * @param service
	 */
	@Inject
	public void setContentTypesService(IContentTypesServiceAsync service) {
		this.contentTypesService = service;
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
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}
}
