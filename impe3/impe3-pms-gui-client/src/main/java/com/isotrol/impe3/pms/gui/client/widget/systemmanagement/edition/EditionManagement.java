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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.edition;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.renderer.BooleanCellRenderer;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.renderer.SimpleDateTimeRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.edition.EditionDTO;
import com.isotrol.impe3.pms.api.edition.EditionsService;
import com.isotrol.impe3.pms.gui.api.service.IEditionsServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.EditionModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;


/**
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class EditionManagement extends ContentPanel {

	/**
	 * "Current" column width<br/>
	 */
	private static final int COLUMN_CURRENT_WIDTH = 50;
	/**
	 * "Created" column width<br/>
	 */
	private static final int COLUMN_CREATED_WIDTH = 150;

	/**
	 * Editions grid.<br/>
	 */
	private Grid<EditionModelData> grid = null;

	/**
	 * Store for {@link #grid}.<br/>
	 */
	private ListStore<EditionModelData> store = null;

	private Button bSetOnline = null;

	/*
	 * Injected services
	 */
	/**
	 * The service errors processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Cell renderer for <b>ID</b> cell.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;
	/**
	 * Date cell renderer.<br/>
	 */
	private SimpleDateTimeRenderer dateCellRenderer = null;
	/**
	 * <b>Current</b> property cell renderer.<br/>
	 */
	private BooleanCellRenderer booleanCellRenderer = null;
	/**
	 * Editions service.<br/>
	 */
	private IEditionsServiceAsync editionsService = null;

	/**
	 * Error message resolver for Editions service.<br/>
	 */
	private IErrorMessageResolver emrEditions = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Pms styles.<br/>
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
	 * Constructor for the editions management panel.<br/>
	 */
	public EditionManagement() {
	}

	/**
	 * Inits the widget. Must be explicitly called once dependencies are injected.
	 */
	public void init() {
		initComponent();

		tryGetLastEditions();
	}

	private void initComponent() {

		initThis();

		addGrid();

		addToolBar();

	}

	/**
	 * Creates, configs and adds the tool bar.<br/>
	 */
	private void addToolBar() {
		ToolBar toolBar = new ToolBar();

		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {

				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							tryPublish();
						}
					}
				};

				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSaveNewEdition(), listener);
			}
		};

		SelectionListener<ButtonEvent> setEditionListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {

				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						EditionModelData model = grid.getSelectionModel().getSelectedItem();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES) && model != null) {
							trySetOnlineEdition(model);
						}
					}
				};

				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSetOnlineEdition(), listener);
			}
		};

		buttonsSupport.addGenericButton(pmsMessages.labelPublishNewEdition(), pmsStyles.iconPublished(), toolBar, listener);
		buttonsSupport.addSeparator(toolBar);
		
		bSetOnline = buttonsSupport.addGenericButton(pmsMessages.labelSetOnlineEdition(), pmsStyles.iconBack(),
			toolBar, setEditionListener);
		bSetOnline.disable();

		setTopComponent(toolBar);
	}

	/**
	 * Creates, configs and adds the grid.<br/>
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		ColumnConfig ccId = new ColumnConfig(EditionModelData.PROPERTY_ID, messages.columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		ccId.setSortable(false);
		ccId.setRenderer(idCellRenderer);
		configs.add(ccId);

		ColumnConfig ccCurrent = new ColumnConfig();
		ccCurrent.setId(EditionModelData.PROPERTY_CURRENT);
		ccCurrent.setWidth(COLUMN_CURRENT_WIDTH);
		ccCurrent.setHeader(pmsMessages.columnHeaderCurrent());
		ccCurrent.setRenderer(booleanCellRenderer);
		configs.add(ccCurrent);

		ColumnConfig ccPublishedBy = new ColumnConfig();
		ccPublishedBy.setId(EditionModelData.PROPERTY_PUBLISHED_BY);
		ccPublishedBy.setWidth(250);
		ccPublishedBy.setHeader(pmsMessages.columnHeaderPublishedBy());
		configs.add(ccPublishedBy);

		ColumnConfig ccPublished = new ColumnConfig();
		ccPublished.setId(EditionModelData.PROPERTY_PUBLISHED);
		ccPublished.setWidth(150);
		ccPublished.setHeader(pmsMessages.columnHeaderPublishedDate());
		ccPublished.setRenderer(dateCellRenderer);
		configs.add(ccPublished);

		ColumnConfig ccCreatedBy = new ColumnConfig();
		ccCreatedBy.setId(EditionModelData.PROPERTY_CREATED_BY);
		ccCreatedBy.setWidth(250);
		ccCreatedBy.setHeader(pmsMessages.columnHeaderCreationBy());
		configs.add(ccCreatedBy);

		ColumnConfig ccCreated = new ColumnConfig();
		ccCreated.setId(EditionModelData.PROPERTY_CREATED);
		ccCreated.setWidth(COLUMN_CREATED_WIDTH);
		ccCreated.setHeader(pmsMessages.columnHeaderCreationDate());
		ccCreated.setRenderer(dateCellRenderer);
		configs.add(ccCreated);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<EditionModelData>();

		grid = new Grid<EditionModelData>(store, cm);
		grid.setAutoExpandColumn(EditionModelData.PROPERTY_CREATED);
		grid.getView().setForceFit(true);

		GridSelectionModel<EditionModelData> sm = new GridSelectionModel<EditionModelData>();
		sm.setSelectionMode(SelectionMode.SINGLE);
		grid.setSelectionModel(sm);

		sm.addSelectionChangedListener(new SelectionChangedListener<EditionModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<EditionModelData> se) {
				if (se.getSelectedItem() != null) {
					bSetOnline.enable();
				} else {
					bSetOnline.disable();
				}
			}
		});

		add(grid);
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setHeaderVisible(false);
		setLayout(new FitLayout());

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetLastEditions();
			}
		};
		buttonsSupport.addRefreshButton(this, lRefresh);
	}

	/**
	 * Calls service method: {@link EditionsService#publish()}.<br/>
	 */
	private void tryPublish() {
		util.mask(pmsMessages.mskPublish());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrEditions, pmsMessages.msgErrorSaveNewEdition());
			}

			public void onSuccess(Void arg0) {
				util.unmask();
				util.info(pmsMessages.msgSuccessSaveNewEdition());

				tryGetLastEditions();
			}
		};

		editionsService.publish(callback);
	}

	/**
	 * Retrieves from service the last editions and displays them on the grid.<br/>
	 */
	private void tryGetLastEditions() {

		util.mask(pmsMessages.mskEditions());

		AsyncCallback<List<EditionDTO>> callback = new AsyncCallback<List<EditionDTO>>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrEditions, pmsMessages.msgErrorRetrieveEditions());
			}

			public void onSuccess(List<EditionDTO> arg0) {

				List<EditionModelData> mdList = new LinkedList<EditionModelData>();
				for (EditionDTO dto : arg0) {
					mdList.add(new EditionModelData(dto));
				}

				store.removeAll();
				store.add(mdList);

				util.unmask();
			}
		};

		editionsService.getLastEditions(callback);
	}

	private void trySetOnlineEdition(EditionModelData model) {
		mask(pmsMessages.mskSetOnlineEdition());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				unmask();
				errorProcessor.processError(arg0, emrEditions, pmsMessages.msgErrorSetOnlineEdition());
			}

			public void onSuccess(Void arg0) {
				unmask();
				util.info(pmsMessages.msgSuccessSetOnlineEdition());

				tryGetLastEditions();
			}
		};

		editionsService.setOnlineEdition(model.getDTO().getId(), callback);

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
	 * Injects the Editions async service.
	 * @param service
	 */
	@Inject
	public void setEditionsService(IEditionsServiceAsync service) {
		this.editionsService = service;
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
	 * Injects the shared objects container
	 * @param utilitise
	 */
	@Inject
	public void setUtil(Util utilitise) {
		this.util = utilitise;
	}

	/**
	 * Injects the cell renderer for <b>current</b> property.
	 * @param cellRenderer the booleanCellRenderer to set
	 */
	@Inject
	public void setBooleanCellRenderer(BooleanCellRenderer cellRenderer) {
		this.booleanCellRenderer = cellRenderer;
	}

	/**
	 * Injcets the cell renderer for the "date" field.
	 * @param dateCellRenderer the dateCellRenderer to set
	 */
	@Inject
	public void setDateCellRenderer(SimpleDateTimeRenderer dateCellRenderer) {
		this.dateCellRenderer = dateCellRenderer;
	}

	/**
	 * Injects the error message resolver for Editions service.
	 * @param emrEditions the emrEditions to set
	 */
	@Inject
	public void setEmrEditions(IErrorMessageResolver emrEditions) {
		this.emrEditions = emrEditions;
	}

	/**
	 * Injects the renderer for cell <b>ID</b>.
	 * @param idCellRenderer the idCellRenderer to set
	 */
	@Inject
	public void setIdCellRenderer(InformationCellRenderer idCellRenderer) {
		this.idCellRenderer = idCellRenderer;
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
}
