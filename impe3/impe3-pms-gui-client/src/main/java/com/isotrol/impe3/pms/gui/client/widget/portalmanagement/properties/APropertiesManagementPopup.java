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


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeGridCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;


/**
 * Abstract class representing Widgets that manage the bound Portal "properties". A "property" can be any Object that
 * publishes 2 attributes: a Key and a Value. Currently this widget is a base class for 2 kinds of "properties"
 * management: {@link PropertyDTO} and {@link BaseDTO}. Both are exactly the same thing, excepting that Base values must
 * be URIs.
 * 
 * @author Andrei Cojocaru
 * 
 * @param <M>
 */
public abstract class APropertiesManagementPopup<M extends ModelData> extends TypicalWindow implements IDetailPanel {

	/**
	 * Set to <code>true</code>, when a field has been changed, added or removed<br/>
	 */
	private boolean ownGridDirty = false;

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
	private static final int COLUMN_URI_WIDTH = 150;
	/**
	 * Width in pixels for column <b>key</b><br/>
	 */
	private static final int COLUMN_KEY_WIDTH = 100;

	/**
	 * Grid height in px.<br/>
	 */
	private static final int GRID_HEIGHT = 300;

	/*
	 * Grids
	 */
	/**
	 * Own bases grid.<br/>
	 */
	private Grid<M> gOwn = null;
	/**
	 * Inherited bases grid.<br/>
	 */
	private Grid<M> gInherited = null;

	/**
	 * Own bases content panel.<br/>
	 */
	private ContentPanel cpOwn = null;

	/**
	 * Own bases tab item<br/>
	 */
	private TabItem tiOwn = null;
	/**
	 * Inherited bases tab item.<br/>
	 */
	private TabItem tiInherited = null;

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
	 * Events listening strategy.<br/>
	 */
	private PmsListeningStrategy pmsListeningStrategy = null;
	
	/** Generic styles */
	private GuiCommonStyles styles = null;

	/**
	 * Default constructor.
	 */
	public APropertiesManagementPopup() {
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

		tryGetProperties();
	}

	/**
	 * Inits the portals proxy wrapper.<br/>
	 */
	protected abstract void initController();

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {

		addOwnGrid();
		addToolBar();
		addInheritedGrid();
		addButtonBar();
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setSize(600, 400);
		setHeadingText(getHeaderText());
		setClosable(false);

		// own and inherited tabs
		TabPanel basesTabPanel = new TabPanel();
		basesTabPanel.setBodyBorder(false);
		basesTabPanel.setBorders(false);
		basesTabPanel.addStyleName(styles.tabPanelHeader());

		tiOwn = new TabItem(getOwnTabHeaderText());
		tiOwn.setLayout(new FitLayout());
		tiOwn.setHeight(GRID_HEIGHT);
		tiOwn.setBorders(false);

		tiInherited = new TabItem(getInheritedTabHeaderText());
		tiInherited.setLayout(new FitLayout());
		tiInherited.setHeight(GRID_HEIGHT);
		tiInherited.setBorders(false);

		basesTabPanel.add(tiOwn);
		basesTabPanel.add(tiInherited);
		add(basesTabPanel);
	}

	/**
	 * @return the text header for the Inherited properties tab.
	 */
	protected abstract String getInheritedTabHeaderText();

	/**
	 * @return the text header for the Own properties tab.
	 */
	protected abstract String getOwnTabHeaderText();

	/**
	 * @return the header text for the widget.
	 */
	protected abstract String getHeaderText();

	/**
	 * Adds the grid that lists the Portal own Bases.<br/>
	 */
	private void addOwnGrid() {

		cpOwn = new ContentPanel();
		cpOwn.setHeaderVisible(false);
		cpOwn.setBodyBorder(false);
		cpOwn.setHeight(Constants.HUNDRED_PERCENT);
		cpOwn.setLayout(new FitLayout());

		// Grid bases:
		List<ColumnConfig> cc = new LinkedList<ColumnConfig>();

		// añadimos una columna para el CheckBox de seleccion:
		CheckBoxSelectionModel<M> smOwn = new CheckBoxSelectionModel<M>();
		cc.add(smOwn.getColumn());

		ColumnConfig ccName = new ColumnConfig(getPropertyKey(), pmsMessages.columnHeaderBaseKey(), COLUMN_KEY_WIDTH);
		TextField<String> tfNameEditor = new TextField<String>();
		tfNameEditor.setAllowBlank(false);
		tfNameEditor.setAutoValidate(false);
		ccName.setRenderer(new HtmlEncodeGridCellRenderer());
		ccName.setEditor(new CellEditor(tfNameEditor));
		cc.add(ccName);

		ColumnConfig ccPath = new ColumnConfig(getPropertyValue(), pmsMessages.columnHeaderBaseUri(), COLUMN_URI_WIDTH);
		TextField<String> tfPathEditor = new TextField<String>();
		tfPathEditor.setAllowBlank(false);
		tfPathEditor.setAutoValidate(false);
		ccPath.setRenderer(new HtmlEncodeGridCellRenderer());
		ccPath.setEditor(new CellEditor(tfPathEditor));
		cc.add(ccPath);

		ColumnModel cmOwn = new ColumnModel(cc);

		ListStore<M> store = new ListStore<M>();
		StoreListener<M> storeChangeNotifier = new StoreListener<M>() {
			@Override
			public void storeAdd(StoreEvent<M> se) {
				ownGridDirty = true;
				Util.fireChangeEvent(gOwn);
			}

			@Override
			public void storeRemove(StoreEvent<M> se) {
				ownGridDirty = true;
				Util.fireChangeEvent(gOwn);
			}

			@Override
			public void storeUpdate(StoreEvent<M> se) {
				ownGridDirty = true;
				Util.fireChangeEvent(gOwn);
			}
		};
		store.addStoreListener(storeChangeNotifier);

		gOwn = new EditorGrid<M>(store, cmOwn);
		gOwn.setAutoExpandColumn(getPropertyValue());
		gOwn.setStyleAttribute(BORDER_TOP_KEY, BORDER_TOP_VALUE);
		gOwn.setSelectionModel(smOwn);
		gOwn.addPlugin(smOwn);
		GridView gView = gOwn.getView();
		gView.setForceFit(true);

		cpOwn.add(gOwn);
		tiOwn.add(cpOwn);
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
	 * Adds the grid that contains the inherited Properties.<br/>
	 */
	private void addInheritedGrid() {
		ContentPanel cpInherited = new ContentPanel();
		cpInherited.setHeaderVisible(false);
		cpInherited.setBodyBorder(false);
		cpInherited.setHeight(Constants.HUNDRED_PERCENT);
		cpInherited.setLayout(new FitLayout());

		// Grid bases:
		List<ColumnConfig> cc = new LinkedList<ColumnConfig>();

		ColumnConfig ccName = new ColumnConfig(getPropertyKey(), pmsMessages.columnHeaderBaseKey(), COLUMN_KEY_WIDTH);
		cc.add(ccName);

		ColumnConfig ccPath = new ColumnConfig(getPropertyValue(), pmsMessages.columnHeaderBaseUri(), COLUMN_URI_WIDTH);
		cc.add(ccPath);

		ColumnModel cmOwn = new ColumnModel(cc);

		ListStore<M> store = new ListStore<M>();

		gInherited = new Grid<M>(store, cmOwn);

		gInherited.setAutoExpandColumn(getPropertyValue());
		gInherited.setStyleAttribute(BORDER_TOP_KEY, BORDER_TOP_VALUE);
		GridView gView = gInherited.getView();
		gView.setForceFit(true);

		cpInherited.add(gInherited);
		tiInherited.add(cpInherited);
	}

	/**
	 * Adds the button bar to this panel.<br/>
	 */
	private void addButtonBar() {
		// button bar
		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button button = be.getButtonClicked();
						if (button.getItemId().equals(Dialog.YES)) {
							trySaveCurrentChanges();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSaveBases(), listener)
					.setModal(true);
			}
		};
		Button bSave = buttonsSupport.createSaveButtonForDetailPanels(this, listener, Arrays
			.asList(new Component[] {gOwn}), (IComponentListeningStrategy) pmsListeningStrategy);
		addButton(bSave);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Adds a tool bar to the own Bases panel.<br/>
	 */
	private void addToolBar() {
		ToolBar toolbar = new ToolBar();

		SelectionListener<ButtonEvent> lAdd = getAddButtonListener();
		buttonsSupport.addAddButton(toolbar, lAdd, null);
		toolbar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDelete = getDeleteButtonListener();
		buttonsSupport.addDeleteButton(toolbar, lDelete, null);
		toolbar.add(new SeparatorToolItem());

		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetProperties();
			}
		};
		buttonsSupport.addRefreshButton(toolbar, lRefresh);
		cpOwn.setTopComponent(toolbar);
	}

	/**
	 * @return a listener for the "Delete" button
	 */
	protected abstract SelectionListener<ButtonEvent> getDeleteButtonListener();

	/**
	 * @return a listener for the "Add" button
	 */
	protected abstract SelectionListener<ButtonEvent> getAddButtonListener();

	/**
	 * Requests the service for the portal bases.<br/> On success, repopulates the grids with the retrieved info.
	 */
	protected abstract void tryGetProperties();

	/**
	 * Saves the current bases configuration for the current portal.<br/>
	 */
	protected abstract void trySaveCurrentChanges();

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		return ownGridDirty || !gOwn.getStore().getModifiedRecords().isEmpty();
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return true; // disables "Accept" by default.
	}

	/**
	 * Valid if every cell is not empty.<br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		boolean valid = true;
		Iterator<M> it = gOwn.getStore().getModels().iterator();
		while (it.hasNext() && valid) {
			M model = it.next();
			valid = valid && !Util.emptyString((String) model.get(getPropertyKey()))
				&& !Util.emptyString((String) model.get(getPropertyValue()));
		}
		return valid;
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
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = pmsListeningStrategy;
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
	 * @return own Properties grid
	 */
	protected final Grid<M> getOwnGrid() {
		return gOwn;
	}

	/**
	 * @return inherited Properties grid
	 */
	protected final Grid<M> getInheritedGrid() {
		return gInherited;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}
}
