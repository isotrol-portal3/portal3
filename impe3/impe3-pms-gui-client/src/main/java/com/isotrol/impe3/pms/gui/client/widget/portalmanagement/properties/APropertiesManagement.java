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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.EPortalImportExportType;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalImportWindow;


/**
 * Abstract class representing Widgets that manage the bound Portal "properties". A "property" can be any Object that
 * publishes 2 attributes: a Key and a Value. Currently this widget is a base class for 2 kinds of "properties"
 * management: {@link PropertyDTO} and {@link BaseDTO}. Both are exactly the same thing, excepting that Base values must
 * be URIs.
 * 
 * @author Manuel Ruiz
 * 
 * @param <M>
 */
public abstract class APropertiesManagement<M extends ModelData> extends PmsContentPanel {

	/**
	 * <br/>
	 */
	private static final String BORDER_TOP_VALUE = "none";
	/**
	 * <br/>
	 */
	private static final String BORDER_TOP_KEY = "borderTop";

	/**
	 * Width in pixesls for column <b>uri</b><br/>
	 */
	private static final int COLUMN_URI_WIDTH = 300;
	/**
	 * Width in pixels for column <b>key</b><br/>
	 */
	private static final int COLUMN_KEY_WIDTH = 200;

	/*
	 * Grids
	 */
	/**
	 * Own bases grid.<br/>
	 */
	private Grid<M> grid = null;

	/**
	 * Portal bound to the widget.<br/>
	 */
	private PortalNameDTO portalNameDto = null;

	/*
	 * Injected deps.
	 */
	/**
	 * Service errors processor
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
	 * Generic messages local service reference.<br/>
	 */
	private GuiCommonMessages messages = null;
	/**
	 * PMS specific messages local service reference.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Buttons helper class.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * PMS Styles
	 */
	private PmsStyles pmsStyles = null;
	
	/**
	 * Pms utilities
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Inits the portals proxy wrapper.<br/>
	 */
	protected abstract void initController();

	/**
	 * Default constructor.
	 */
	public APropertiesManagement() {
	}

	/**
	 * Inits this widget. Must be called after dependencies injection.
	 * @param portalData
	 */
	public void init(PortalNameDTO portalData) {
		this.portalNameDto = portalData;

		initThis();
		initComponents();
		initController();
		tryGetProperties(portalData.getId());
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		addGrid();
		addToolBars();
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setHeadingText(getHeaderText());
	}

	/**
	 * @return the header text for the widget.
	 */
	protected abstract String getHeaderText();

	/**
	 * Adds the grid that lists the Portal own Bases.<br/>
	 */
	private void addGrid() {

		// Grid bases:
		List<ColumnConfig> cc = new LinkedList<ColumnConfig>();

		ColumnConfig ccName = new ColumnConfig(getPropertyKey(), pmsMessages.columnHeaderBaseKey(), COLUMN_KEY_WIDTH);
		cc.add(ccName);

		ColumnConfig ccPath = new ColumnConfig(getPropertyValue(), pmsMessages.columnHeaderBaseUri(), COLUMN_URI_WIDTH);
		cc.add(ccPath);

		ColumnModel cmOwn = new ColumnModel(cc);

		ListStore<M> store = new ListStore<M>();

		grid = new Grid<M>(store, cmOwn);
		grid.setAutoExpandColumn(getPropertyValue());
		grid.setStyleAttribute(BORDER_TOP_KEY, BORDER_TOP_VALUE);
		GridView gView = grid.getView();
		gView.setForceFit(true);

		add(grid);
	}

	/**
	 * @return the ModelData property used as value
	 */
	protected abstract String getPropertyValue();

	/**
	 * @return the ModelData property used as key
	 */
	protected abstract String getPropertyKey();

	/**
	 * Adds a tool bar to the own Bases panel.<br/>
	 */
	private void addToolBars() {
		ToolBar toolbar = new ToolBar();
		ToolBar bottomToolbar = new ToolBar();

		SelectionListener<ButtonEvent> lAdmin = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				openAvailableWindowProperties();
			}
		};
		buttonsSupport.addGenericButton(pmsMessages.labelManage(), pmsStyles.iconAdmin(), toolbar, lAdmin);

		toolbar.add(new FillToolItem());

		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetProperties(portalNameDto.getId());
			}
		};
		buttonsSupport.addRefreshButton(toolbar, lRefresh);
		
		SelectionListener<ButtonEvent> lExport = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				exportProperties();
			}
		};
		buttonsSupport.addGenericButton(pmsMessages.labelExport(), pmsStyles.exportIcon(), bottomToolbar,
			lExport);
		buttonsSupport.addSeparator(bottomToolbar);
		
		SelectionListener<ButtonEvent> lImport = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				PortalImportWindow w = PmsFactory.getInstance().getPortalImportWindow();
				w.setPortalId(getPortalNameDto().getId());
				w.setPortalImportType(getImportType());
				w.show();
			}
		};
		buttonsSupport.addGenericButton(pmsMessages.labelImport(), pmsStyles.importIcon(), bottomToolbar,
			lImport);
		
		setTopComponent(toolbar);
		setBottomComponent(bottomToolbar);
	}

	/**
	 * @return the portal import type
	 */
	protected abstract EPortalImportExportType getImportType();

	/**
	 * Calls concrete method export from portals service 
	 */
	protected abstract void exportProperties();

	/**
	 * Shows the window with the inherited and own properties grids
	 */
	protected abstract void openAvailableWindowProperties();

	/**
	 * Requests the service for the portal bases.<br/> On success, repopulates the grids with the retrieved info.
	 * @param portalId
	 */
	protected abstract void tryGetProperties(String portalId);

	/**
	 * Fills the grid store with the passes list
	 * @param properties
	 */
	protected void storeProperties(List<M> properties) {
		ListStore<M> store = grid.getStore();
		store.removeAll();
		store.add(properties);
	}

	/**
	 * Injects the Portals async service.
	 * @param portalsService
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
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
	 * Injects the buttons helper object.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the shared objects container
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
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
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the Error Processor
	 */
	protected final ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * @return the Portals service proxy.
	 */
	protected final IPortalsServiceAsync getPortalsService() {
		return portalsService;
	}

	/**
	 * @return the Error Message Resolver for the Portals service.
	 */
	protected final PortalsServiceErrorMessageResolver getEmrPortals() {
		return emrPortals;
	}

	/**
	 * @return GuiCommon messages bundle.
	 */
	protected final GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * @return the PMS messages bundle.
	 */
	protected final PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the utilities object
	 */
	protected final Util getUtil() {
		return util;
	}

	/**
	 * @return the bound Portal name.
	 */
	protected final PortalNameDTO getPortalNameDto() {
		return portalNameDto;
	}

	/**
	 * @return the buttons support object
	 */
	protected final Buttons getButtonsSupport() {
		return buttonsSupport;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @return the pmsUtil
	 */
	public PmsUtil getPmsUtil() {
		return pmsUtil;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}
}
