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
package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.routingdomains;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.rd.RoutingDomainDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainSelDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainsService;
import com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.RoutingDomainsController;
import com.isotrol.impe3.pms.gui.client.data.impl.RoutingDomainSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Routing Domains Management widget.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class RoutingDomainsManagement extends LayoutContainer {

	/*
	 * Column widths
	 */
	/**
	 * Width in pixels for column <b>Description</b><br/>
	 */
	private static final int COLUMN_DESCRIPTION_WIDTH = 250;
	/**
	 * Width in pixels for column <b>Name</b>.<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 250;

	/**
	 * Master view container.<br/>
	 */
	private ContentPanel pMaster = null;
	/**
	 * Detail view container.<br/>
	 */
	private Window wDetail = null;

	/**
	 * Grid that lists the routing domains.<br/>
	 */
	private Grid<RoutingDomainSelModelData> grid = null;

	/**
	 * "Edit" button<br/>
	 */
	private Button bEdit = null;
	/**
	 * "Delete" button.<br/>
	 */
	private Button ttiDelete = null;

	/**
	 * Store for {@link #grid}.<br/>
	 */
	private ListStore<RoutingDomainSelModelData> store = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Cell renderer for ID column.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;
	/**
	 * Utilities.<br/>
	 */
	private Util util = null;

	/**
	 * Routing Domains routingDomainsService reference<br/>
	 */
	private IRoutingDomainsServiceAsync routingDomainsService = null;

	/**
	 * Error message resolver.<br/>
	 */
	private IErrorMessageResolver emrRoutingDomains = null;

	/**
	 * Generic messages routingDomainsService.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages routingDomainsService.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Buttons helper.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Settings bundle
	 */
	private Settings settings = null;

	/**
	 * Default constructor.<br/>
	 */
	public RoutingDomainsManagement() {
	}

	/**
	 * Inits the widget. Must be called after all the dependencies are injected.
	 */
	public void init() {
		util.mask();

		initComponents();
		initController();

		tryGetDomains();
	}

	/**
	 * Inits the controller.<br/>
	 */
	private void initController() {
		RoutingDomainsController controller = (RoutingDomainsController) routingDomainsService;

		controller.addChangeListener(new ChangeListener() {
			/**
			 * <br/> (non-Javadoc)
			 * @see com.extjs.gxt.ui.client.data.ChangeListener#modelChanged(com.extjs.gxt.ui.client.data.ChangeEvent)
			 */
			public void modelChanged(ChangeEvent e) {
				PmsChangeEvent event = (PmsChangeEvent) e;
				if (event.getType() == PmsChangeEvent.UPDATE) {
					tryGetDomains();
				}
				// TODO delete not yet implemented.
			}
		});
	}

	/**
	 * Invokes remote procedure call {@link RoutingDomainsService#getDomains()}.<br/>
	 */
	private void tryGetDomains() {
		util.mask(pmsMessages.mskRoutingDomains());

		AsyncCallback<List<RoutingDomainSelDTO>> callback = new AsyncCallback<List<RoutingDomainSelDTO>>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrRoutingDomains, pmsMessages.msgErrorRetrieveRoutingDomains());
			}

			public void onSuccess(List<RoutingDomainSelDTO> arg0) {
				List<RoutingDomainSelModelData> models = new LinkedList<RoutingDomainSelModelData>();
				for (RoutingDomainSelDTO dto : arg0) {
					models.add(new RoutingDomainSelModelData(dto));
				}
				store.removeAll();
				store.add(models);

				util.unmask();
			}
		};
		routingDomainsService.getDomains(callback);
	}

	/**
	 * Inits the container inner components.
	 */
	private void initComponents() {

		setLayout(new FitLayout());

		pMaster = new ContentPanel();
		pMaster.setWidth(Constants.HUNDRED_PERCENT);
		// pMaster.setHeight(MasterDetailSupport.HORIZONTAL_MASTER_PANEL_HEIGHT);
		pMaster.setHeaderVisible(false);
		pMaster.setTitle(pmsMessages.headerRoutingDomainsManagement());
		pMaster.setLayout(new FitLayout());

		addGrid();
		addToolbar();
		addBottomToolbar();

		add(pMaster);
	}

	/**
	 * Creates, configs & adds the tool bar.
	 */
	private void addToolbar() {
		ToolBar toolBar = new ToolBar();

		buttonsSupport.addAddButton(toolBar, new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent tbe) {
				RoutingDomainDTO rdDto = new RoutingDomainDTO();
				showDetailsWindow(rdDto, false);
			};
		}, null);
		buttonsSupport.addSeparator(toolBar);

		bEdit = buttonsSupport.addEditButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				getSelectedAndDisplayDetails();
			}
		});
		bEdit.disable();
		buttonsSupport.addSeparator(toolBar);

		ttiDelete = buttonsSupport.addDeleteButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				// TODO delete
			}
		}, null);
		ttiDelete.disable();
		buttonsSupport.addSeparator(toolBar);

		// 'default routing domain' edit button
		Button bDefaultEdit = buttonsSupport.addEditButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				getDefaultRoutingPanel();
			}
		});
		bDefaultEdit.setText(pmsMessages.labelDefaultEdit());

		toolBar.add(new FillToolItem());
		// "Refresh" button:
		buttonsSupport.addRefreshButton(toolBar, new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetDomains();
			}
		});

		pMaster.setTopComponent(toolBar);
	}

	/**
	 * Creates, configs & adds the bottom tool bar. This toolbar contains the help button
	 */
	private void addBottomToolbar() {
		ToolBar toolBar = new ToolBar();

		toolBar.add(new FillToolItem());
		// "Help" button:
		buttonsSupport.addHelpButton(toolBar, settings.pmsRoutingDomainsAdminPortalManualUrl());

		pMaster.setBottomComponent(toolBar);
	}

	/**
	 * Retrieves the default routing domain data from the service, and displays it in a details window.<br/>
	 */
	private void getDefaultRoutingPanel() {
		util.mask(pmsMessages.mskRoutingDomain());

		AsyncCallback<RoutingDomainDTO> callback = new AsyncCallback<RoutingDomainDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrRoutingDomains, pmsMessages.msgErrorRetrieveRoutingDomain());
			}

			public void onSuccess(RoutingDomainDTO arg0) {
				showDetailsWindow(arg0, true);
				util.unmask();
			}
		};
		routingDomainsService.getDefault(callback);
	}

	/**
	 * Retrieves the selected Routing Domain data from the service, and displays it in a details window.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		RoutingDomainSelModelData selected = grid.getSelectionModel().getSelectedItem();
		if (selected != null) {
			tryGetRoutingDomainDetails(selected.getDTO().getId());
		}
	}

	/**
	 * Creates, configs & adds the grid.
	 */
	private void addGrid() {

		ColumnConfig ccId = new ColumnConfig(RoutingDomainSelModelData.PROPERTY_ID, messages.columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		ccId.setRenderer(idCellRenderer);
		ColumnConfig ccName = new ColumnConfig(RoutingDomainSelModelData.PROPERTY_NAME, pmsMessages.columnHeaderName(),
			COLUMN_NAME_WIDTH);
		ColumnConfig ccDesc = new ColumnConfig(RoutingDomainSelModelData.PROPERTY_DESCRIPTION, pmsMessages
			.columnHeaderDescription(), COLUMN_DESCRIPTION_WIDTH);
		ColumnModel columnModel = new ColumnModel(Arrays.asList(new ColumnConfig[] {ccId, ccName, ccDesc}));

		store = new ListStore<RoutingDomainSelModelData>();

		grid = new Grid<RoutingDomainSelModelData>(store, columnModel);
		grid.setAutoExpandColumn(RoutingDomainSelModelData.PROPERTY_DESCRIPTION);
		grid.getView().setAutoFill(true);

		/*
		 * selection handling:
		 */
		grid.setSelectionModel(new GridSelectionModel<RoutingDomainSelModelData>());

		grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<RoutingDomainSelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<RoutingDomainSelModelData> se) {
				if (se.getSelectedItem() != null) {
					bEdit.enable();
				} else {
					bEdit.disable();
				}
			}
		});

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<RoutingDomainSelModelData>>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			/**
			 * <br/>
			 */
			public void handleEvent(GridEvent<RoutingDomainSelModelData> be) {
				getSelectedAndDisplayDetails();
			}
		});

		pMaster.add(grid);
	}

	/**
	 * Closes the selected Routing Domain details window.
	 */
	private void closeDetailsWindow() {
		if (wDetail != null && wDetail.isAttached()) {
			wDetail.hide();
		}
	}

	/**
	 * Creates a new details window for the selected Routing Domain.
	 * @param rdDto DTO bound to created window.
	 */
	private void showDetailsWindow(RoutingDomainDTO rdDto, boolean isDefault) {
		closeDetailsWindow();
		RoutingDomainDetailsEditor routingDomainDetailsEditor = null;
		if (isDefault) {
			routingDomainDetailsEditor = PmsFactory.getInstance().getDefaultRoutingDomainDetailsEditor();
		} else {
			routingDomainDetailsEditor = PmsFactory.getInstance().getRoutingDomainDetailsEditor();
		}
		routingDomainDetailsEditor.init(rdDto);

		wDetail = routingDomainDetailsEditor;
		wDetail.show();
	}

	/**
	 * Calls remote procedure <b>get</b> on the {@link #routingDomainsService service}. On success, shows the detail
	 * window; on failure, informs.
	 * @param id
	 */
	private void tryGetRoutingDomainDetails(String id) {
		util.mask(pmsMessages.mskRoutingDomain());

		AsyncCallback<RoutingDomainDTO> callback = new AsyncCallback<RoutingDomainDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrRoutingDomains, pmsMessages.msgErrorRetrieveRoutingDomain());
			}

			public void onSuccess(RoutingDomainDTO arg0) {
				showDetailsWindow(arg0, false);
				util.unmask();
			}
		};
		routingDomainsService.get(id, callback);
	}

	/**
	 * Injects the Routing Domains Service
	 * @param routingDomainsService
	 */
	@Inject
	public void setRoutingDomainsService(IRoutingDomainsServiceAsync routingDomainsService) {
		this.routingDomainsService = routingDomainsService;
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
	 * Injects the button helper
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * @param u the util to set
	 */
	@Inject
	public void setUtil(Util u) {
		this.util = u;
	}

	/**
	 * Injects the Error Message Resolver for Routing Domains.
	 * @param emr the Error Message Resolver to set
	 */
	@Inject
	public void setErrorMessageResolver(IErrorMessageResolver emr) {
		this.emrRoutingDomains = emr;
	}

	/**
	 * Injects the cell renderer for ID column.
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
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
