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
package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.RowExpanderEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.gui.api.service.IModuleRegistryServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.AbstractModuleModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;


/**
 * Generic module view (readonly widget) for all Modules.<br/> Lazily initalized.<br/>
 * 
 * @author Andrei Cojocaru
 * 
 * @param <M>
 * @param <D>
 */
public abstract class AModuleView<M extends ModelData, D> extends LayoutContainer {

	/**
	 * Width for grid column <b>name</b>, in pixels.<br/>
	 */
	private static final int COLUMNNAMEWIDTH = 400;

	/**
	 * Grid store.<br/>
	 */
	private ListStore<M> store = new ListStore<M>();

	/*
	 * Injected deps
	 */
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Utilities.<br/>
	 */
	private Util util = null;
	/**
	 * The Modules async modulesService proxy.<br/>
	 */
	private IModuleRegistryServiceAsync modulesService = null;

	/**
	 * Error Message Resolver for the Modules Registry service.<br/>
	 */
	private IErrorMessageResolver emrModuleRegistry = null;

	/**
	 * Generic messages modulesService.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages modulesService.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Injects the store sorter.<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;

	/** Gui common styles */
	private GuiCommonStyles styles = null;

	@Override
	protected void beforeRender() {
		initComponent();

		AsyncCallback<List<D>> cbGetModules = new AsyncCallback<List<D>>() {
			public void onSuccess(List<D> modules) {
				store.removeAll();
				storeModules(modules);

				util.unmask();
			}

			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrModuleRegistry, pmsMessages.msgErrorRetrieveModules());
			}
		};

		util.mask(pmsMessages.mskModules());
		tryGetModules(modulesService, cbGetModules);
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {

		setLayout(new FitLayout());

		/*
		 * Grid:
		 */
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		final RowExpander expander = new RowExpander();
		expander.addListener(Events.BeforeExpand, new Listener<RowExpanderEvent>() {
			public void handleEvent(RowExpanderEvent be) {
				M model = (M) be.getModel();
				if (model != null) {
					expander.setTemplate(XTemplate.create("<div style='margin: 5px 10px 5px 45px'>" + getXDetailTemplate(model)
						+ "</div>"));
				}
			}
		});
		configs.add(expander);

		ColumnConfig column = new ColumnConfig();
		column.setId(AbstractModuleModelData.PROPERTY_NAME);
		column.setHeaderText(pmsMessages.labelName());
		column.setWidth(COLUMNNAMEWIDTH);
		configs.add(column);

		addSpecificColumnConfig(configs);

		ColumnModel cm = new ColumnModel(configs);

		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(AbstractModuleModelData.PROPERTY_NAME);

		final Grid<M> grid = new Grid<M>((ListStore<M>) store, cm);

		grid.setSelectionModel(new GridSelectionModel<M>());
		// grid.addPlugin(expander);
		grid.setLoadMask(true);
		grid.getView().setForceFit(true);
		grid.addPlugin(expander);
		grid.setAutoExpandColumn(AbstractModuleModelData.PROPERTY_NAME);

		ContentPanel cp = new ContentPanel();

		cp.setHeaderVisible(false);
		cp.setLayout(new FitLayout());
		cp.add(grid);

		addToolBar(cp);

		add(cp);
	}

	private void addToolBar(ContentPanel cp) {
		ToolBar tb = new ToolBar();
		Text title = new Text(getContentPanelHeading());
		title.addStyleName(styles.labelInfoMessage());
		title.addStyleName(styles.marginLeft5px());
		tb.add(title);
		tb.add(new FillToolItem());

		// filter:
		StoreFilterField<M> filter = new CustomizableStoreFilter<M>(Arrays.asList(new String[] {
			AbstractModuleModelData.PROPERTY_NAME, AbstractModuleModelData.PROPERTY_DESCRIPTION}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		filter.bind(store);
		tb.add(filter);

		cp.setTopComponent(tb);
	}

	/**
	 * Adds any additional implementation-specific column configs.<br/>
	 * @param configs the column configurations list.
	 */
	protected abstract void addSpecificColumnConfig(List<ColumnConfig> configs);

	/**
	 * Retrieves the modules from the service, and displays them on the grid.<br/>
	 * @param service the async service proxy
	 * @param callback async callback to process the retrieved modules.
	 */
	protected abstract void tryGetModules(IModuleRegistryServiceAsync service, AsyncCallback<List<D>> callback);

	/**
	 * Inserts the passed modules into the grid store.<br/>
	 * @param modules
	 */
	protected abstract void storeModules(List<D> modules);

	/**
	 * @return the contentPanelTitle
	 */
	protected abstract String getContentPanelHeading();

	protected abstract String getXDetailTemplate(M model);

	/**
	 * @return the store
	 */
	protected final ListStore<M> getStore() {
		return store;
	}

	/**
	 * @return the messages
	 */
	protected final GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * <br/>
	 * @return the Util object.
	 */
	protected final Util getUtil() {
		return util;
	}

	/**
	 * @return the pmsMessages
	 */
	protected final PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the modulesService
	 */
	protected final IModuleRegistryServiceAsync getModulesService() {
		return modulesService;
	}

	/**
	 * Injects the modules async service
	 * @param modulesService
	 */
	@Inject
	public void setModulesService(IModuleRegistryServiceAsync modulesService) {
		this.modulesService = modulesService;
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
	 * Injects the alphabetic store sorter.<br/>
	 * @param storeSorter
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}

	/**
	 * @param u the util to set
	 */
	@Inject
	public void setUtil(Util u) {
		this.util = u;
	}

	/**
	 * Injects the Error Message Resolver for the Modules Registry service
	 * @param emrModuleRegistry the emrModuleRegistry to set
	 */
	@Inject
	public void setEmrModuleRegistry(IErrorMessageResolver emrModuleRegistry) {
		this.emrModuleRegistry = emrModuleRegistry;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service error processor
	 */
	protected ServiceErrorsProcessor getAErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}
}
