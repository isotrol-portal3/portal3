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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.RowExpanderEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.AbstractModuleModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ConnectorModuleModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ModuleInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.renderer.CorrectnessCellRenderer;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.ConnectorsDetail;


/**
 * Manages all Connectors.
 * 
 * @author Manuel Ruiz
 * 
 */
public class AllConnectorsManagement extends AConnectorsManagement {

	/**
	 * Width in px for <b>correctness</b> column.<br/>
	 */
	private static final int COLUMN_CORRECTNESS_WIDTH = Constants.COLUMN_ICON_WIDTH;

	/** Window with the modules selector */
	private Window modulesWindow = null;

	/** the listStore to store the connector modules for the selector. */
	private ListStore<ConnectorModuleModelData> modulesStore = null;

	/**
	 * Connectors grid in connectors selector.<br/>
	 */
	private Grid<ConnectorModuleModelData> gConnectors = null;

	/**
	 * Correctness renderer
	 */
	private CorrectnessCellRenderer correctnessRenderer = null;

	/**
	 * Adds the <b>New Component</b> toolbar item.<br/>
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.AConnectorsManagement
	 * #addSpecificToolItems(com.extjs.gxt.ui.client.widget.toolbar.ToolBar)
	 */
	protected void addSpecificToolItems(ToolBar toolbar) {
		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {

				// fills the combo with the connectors modules
				tryGetConnectorModules();

				// show the selector window
				modulesWindow.show();
			}
		};
		Button ttiAdd = getButtonsSupport().createGenericButton(getMessages().labelAdd(), getGuiCommonStyles().iNew(),
			listener);
		toolbar.insert(ttiAdd, 0);
		toolbar.insert(new SeparatorToolItem(), 1);
	}

	/**
	 * Requests the service for the Connectors.<br/> On success, adds the results to the store.
	 */
	private void tryGetConnectorModules() {

		AsyncCallback<List<ConnectorModuleDTO>> callback = new AsyncCallback<List<ConnectorModuleDTO>>() {
			public void onSuccess(List<ConnectorModuleDTO> mods) {

				List<ConnectorModuleModelData> modules = dtoToModel(mods);
				modulesStore.removeAll();
				modulesStore.add(modules);
			}

			private List<ConnectorModuleModelData> dtoToModel(List<ConnectorModuleDTO> mods) {
				List<ConnectorModuleModelData> modules = new LinkedList<ConnectorModuleModelData>();
				for (ConnectorModuleDTO m : mods) {
					ConnectorModuleModelData connector = new ConnectorModuleModelData(m);
					modules.add(connector);
				}

				return modules;
			}

			public void onFailure(Throwable arg0) {
				getErrorProcessor().processError(arg0, getErrorMessageResolver(),
					getPmsMessages().msgErrorRetrieveConnectors());
			}
		};

		getService().getConnectorModules(callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.AConnectorsManagement
	 * #callGetConnectors(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	protected void callGetConnectors(AsyncCallback<List<ModuleInstanceSelDTO>> callback) {
		getService().getConnectors(callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.AConnectorsManagement#specificInit()
	 */
	@Override
	protected void initSpecific() {
		createConnectorModuleSelector();
	}

	/**
	 * Creates the module selector, consisting of a popup window with a combo box.<br/>
	 */
	private void createConnectorModuleSelector() {

		modulesWindow = new Window();
		modulesWindow.setHeading(getPmsMessages().headerConnectorSelector());
		modulesWindow.setModal(true);
		modulesWindow.setResizable(false);
		modulesWindow.setWidth(Constants.SIXTY_FIVE_PERCENT);
		modulesWindow.setShadow(false);
		modulesWindow.setButtonAlign(HorizontalAlignment.LEFT);

		LayoutContainer container = new LayoutContainer(new FitLayout());
		container.setHeight(500);

		// the grid column model:

		final RowExpander expander = new RowExpander();
		expander.addListener(Events.BeforeExpand, new Listener<RowExpanderEvent>() {
			public void handleEvent(RowExpanderEvent be) {
				ConnectorModuleModelData model = (ConnectorModuleModelData) be.getModel();
				if (model != null) {
					ConnectorsDetail detail = PmsFactory.getInstance().getConnectorsDetail();
					detail.init(model);
					expander.setTemplate(XTemplate.create("<div style='margin: 5px 10px 5px 45px'>"
						+ detail.getXTemplate() + "</div>"));
				}

			}
		});

		ColumnConfig cId = new ColumnConfig(ConnectorModuleModelData.PROPERTY_ID, getMessages().columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		cId.setSortable(false);
		cId.setRenderer(getIdCellRenderer());
		
		ColumnConfig cName = new ColumnConfig();
		cName.setId(ConnectorModuleModelData.PROPERTY_NAME);
		cName.setHeader(getPmsMessages().columnHeaderName());
		cName.setWidth(350);

		ColumnModel columnModel = new ColumnModel(Arrays.asList(new ColumnConfig[] {expander, cId, cName}));

		// the grid store:
		modulesStore = new ListStore<ConnectorModuleModelData>();
		modulesStore.setStoreSorter((StoreSorter) getStoreSorter());
		modulesStore.setSortField(AbstractModuleModelData.PROPERTY_NAME);

		gConnectors = new Grid<ConnectorModuleModelData>(modulesStore, columnModel);
		container.add(gConnectors);

		GridSelectionModel<ConnectorModuleModelData> sm = new GridSelectionModel<ConnectorModuleModelData>();
		sm.setSelectionMode(SelectionMode.SINGLE);
		gConnectors.setSelectionModel(sm);

		gConnectors.setLoadMask(true);
		gConnectors.getView().setForceFit(true);
		gConnectors.addPlugin(expander);
		gConnectors.setAutoExpandColumn(AbstractModuleModelData.PROPERTY_DESCRIPTION);

		modulesWindow.add(container);

		// top toolbar:
		ToolBar toolBar = new ToolBar();

		StoreFilterField<ConnectorModuleModelData> selectorFilter = new CustomizableStoreFilter<ConnectorModuleModelData>(
			Arrays.asList(new String[] {ConnectorModuleModelData.PROPERTY_NAME,
				ConnectorModuleModelData.PROPERTY_DESCRIPTION}));
		selectorFilter.setHideLabel(false);
		selectorFilter.setFieldLabel(getMessages().labelFilter());
		selectorFilter.bind(modulesStore);
		toolBar.add(selectorFilter);

		modulesWindow.setTopComponent(toolBar);

		// bottom ButtonBar:
		modulesWindow.addButton(getButtonsSupport().createAcceptButton(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				ConnectorModuleModelData selectedItem = gConnectors.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					tryGetNewTemplate(selectedItem.getDTO().getId());
					modulesWindow.hide();
				}
			}
		}));
	}

	/**
	 * Requests the service for a new Connector template of the passed module key.<br/>
	 * @param moduleKey
	 */
	private void tryGetNewTemplate(String moduleKey) {

		AsyncCallback<ModuleInstanceTemplateDTO> callback = new AsyncCallback<ModuleInstanceTemplateDTO>() {

			public void onFailure(Throwable arg0) {
				getErrorProcessor().processError(arg0, getErrorMessageResolver(),
					getPmsMessages().msgErrorRetrieveConnectorTemplate());
			}

			public void onSuccess(ModuleInstanceTemplateDTO template) {
				showConnectorDetail(template);
			}
		};

		getService().newTemplate(moduleKey, callback);
	}

	@Override
	protected void addSpecificColumns(List<ColumnConfig> configs) {
		// correctness icon
		ColumnConfig column = new ColumnConfig();
		column.setRenderer(correctnessRenderer);
		column.setId(ModuleInstanceSelModelData.PROPERTY_CORRECTNESS);
		column.setHeader(getPmsMessages().columnHeaderValid());
		column.setWidth(COLUMN_CORRECTNESS_WIDTH);
		configs.add(3, column);
	}

	/**
	 * @param correctnessRenderer the correctnessRenderer to set
	 */
	@Inject
	public void setCorrectnessRenderer(CorrectnessCellRenderer correctnessRenderer) {
		this.correctnessRenderer = correctnessRenderer;
	}
}
