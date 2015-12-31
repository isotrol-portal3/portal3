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
package com.isotrol.impe3.pms.gui.client.widget.nr;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Header;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.CenteredValueCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceDTO;
import com.isotrol.impe3.pms.api.esvc.nr.ContentTypeCountDTO;
import com.isotrol.impe3.pms.api.esvc.nr.NodeRepositoryDTO;
import com.isotrol.impe3.pms.api.esvc.nr.NodeRepositoryExternalService;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeCountModelData;
import com.isotrol.impe3.pms.gui.client.i18n.NrStyles;


/**
 * Represents the summary view for the bound Nodes Repository.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class RepositorySummaryWidget extends ARepositoryWidget {

	/**
	 * Border layout margins.<br/>
	 */
	private static final int MARGINS = 10;

	/**
	 * <b>Name</b> column width in pixels.<br/>
	 */
	private static final int COLUMN_NODE_NAME_WIDTH = 700;

	/**
	 * <b>Count</b> column width in pixels.<br/>
	 */
	private static final int COLUMN_COUNT_WIDTH = 100;

	/**
	 * North panel size inf pixels.<br/>
	 */
	private static final float NORTH_PANEL_HEIGHT = 167;

	/**
	 * Container for repository general info.<br/>
	 */
	private ContentPanel cpTop = null;

	/**
	 * Container for repository summary info.<br/>
	 */
	private ContentPanel cpCenter = null;

	/**
	 * Content Types filter field.<br/>
	 */
	private CustomizableStoreFilter<ContentTypeCountModelData> filter = null;

	/**
	 * Generic styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * <b>Name</b> field.<br/>
	 */
	private LabelField lfName = null;

	/**
	 * <b>Description</b> field.<br/>
	 */
	private TextArea taDescription = null;

	/**
	 * <b>Total nodes</b> field<br/>
	 */
	private LabelField lfTotalNodes = null;

	/**
	 * Content Types info grid: displays name and total nodes.<br/>
	 */
	private Grid<ContentTypeCountModelData> grid = null;

	/*
	 * Injected deps
	 */
	/**
	 * <b>Count</b> property cell renderer.<br/>
	 */
	private CenteredValueCellRenderer countCellRenderer = null;
	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Form layout helper object<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * NR specific styles bundle.<br/>
	 */
	private NrStyles nrStyles = null;

	/**
	 * Default constructor.
	 */
	public RepositorySummaryWidget() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#init(java.lang.Object)
	 */
	/**
	 * <br/>
	 */
	public RepositorySummaryWidget init() {
		super.init();

		initThis();

		addTopPanel();
		addSummaryPanel();

		tryGetServiceAndSummary();

		addTools();

		return this;
	}

	/**
	 * Inits this panel properties.<br/>
	 */
	private void initThis() {
		addStyleName(nrStyles.nrServiceInfo());

		setLayout(new BorderLayout());
		setBorders(false);
		setBodyBorder(false);
		setHeadingText(getNrMessages().menuItem2RepositorySummary());

		Header header = getHeader();
		header.addStyleName(styles.noSideBorders());
		header.addStyleName(styles.noTopBorders());
	}

	/**
	 * Adds the top panel which contains info about: repository name, description and total Nodes.<br/>
	 */
	private void addTopPanel() {
		cpTop = new ContentPanel(formSupport.getStandardLayout(true));
		cpTop.setBorders(true);
		cpTop.setBodyBorder(false);
		cpTop.setHeadingText(getNrMessages().headerRepositoryGeneralInfo());
		cpTop.setBodyStyle("padding:10px");

		Header header = cpTop.getHeader();
		header.addStyleName(styles.noSideBorders());
		header.addStyleName(styles.noTopBorders());

		lfName = new LabelField();
		lfName.setFieldLabel(getNrMessages().labelRepositoryName());
		cpTop.add(lfName);

		taDescription = new TextArea();
		taDescription.setFieldLabel(getNrMessages().labelRepositoryDescription());
		formSupport.configReadOnly(taDescription);
		cpTop.add(taDescription);

		lfTotalNodes = new LabelField();
		lfTotalNodes.setFieldLabel(getNrMessages().labelTotalNodes());
		cpTop.add(lfTotalNodes);

		BorderLayoutData layoutData = new BorderLayoutData(LayoutRegion.NORTH);
		layoutData.setCollapsible(true);
		layoutData.setFloatable(false);
		layoutData.setSize(NORTH_PANEL_HEIGHT);
		layoutData.setMargins(new Margins(MARGINS, MARGINS, 0, MARGINS));
		add(cpTop, layoutData);
	}

	/**
	 * Creates and adds the list of content types.<br/>
	 */
	private void addSummaryPanel() {

		cpCenter = new ContentPanel(new FitLayout());
		cpCenter.setBorders(true);
		cpCenter.setBodyBorder(false);
		cpCenter.setHeadingText(getNrMessages().headerNodesSummary());

		Header header = cpCenter.getHeader();
		header.addStyleName(styles.noSideBorders());
		header.addStyleName(styles.noTopBorders());

		List<ColumnConfig> columns = new LinkedList<ColumnConfig>();

		ColumnConfig ccName = new ColumnConfig(ContentTypeCountModelData.PROPERTY_CT_NAME, getNrMessages()
			.headerNodeName(), COLUMN_NODE_NAME_WIDTH);
		columns.add(ccName);

		ColumnConfig ccCount = new ColumnConfig(ContentTypeCountModelData.PROPERTY_COUNT, getNrMessages()
			.headerNodeCount(), COLUMN_COUNT_WIDTH);
		ccCount.setRenderer(countCellRenderer);
		columns.add(ccCount);

		grid = new Grid<ContentTypeCountModelData>(new ListStore<ContentTypeCountModelData>(), new ColumnModel(columns));

		GridView gridView = grid.getView();
		gridView.setAutoFill(true);
		gridView.setForceFit(true);

		grid.setAutoExpandColumn(ContentTypeCountModelData.PROPERTY_CT_NAME);

		cpCenter.add(grid);

		BorderLayoutData layoutData = new BorderLayoutData(LayoutRegion.CENTER);
		layoutData.setMargins(new Margins(MARGINS));
		add(cpCenter, layoutData);
	}

	/**
	 * Adds a filter and a "Refresh" butotn.<br/>
	 */
	private void addTools() {
		filter = new CustomizableStoreFilter<ContentTypeCountModelData>(Arrays
			.asList(new String[] {ContentTypeCountModelData.PROPERTY_CT_NAME}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		filter.bind(grid.getStore());
		cpCenter.getHeader().addTool(filter);

		buttonsSupport.addRefreshButton(cpCenter, new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				getUtil().mask(getNrMessages().mskService());
				tryGetSummary();
			}
		});
	}

	/**
	 * Retrieves the service general info (method {@link NodeRepositoryExternalService#getService(String)})<br/> On
	 * success, displays it on the upper panel.<br/> On failure, displays an alert message.
	 */
	private void tryGetServiceAndSummary() {
		getUtil().mask(getNrMessages().mskService());

		AsyncCallback<ExternalServiceDTO> callback = new AsyncCallback<ExternalServiceDTO>() {
			public void onSuccess(ExternalServiceDTO arg0) {
				showServiceInfo(arg0);
				tryGetSummary();
			}

			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getNrErrorMessageResolver(),
					getNrMessages().msgErrorGetService());
			}
		};

		getNrService().getService(getRepositoryId(), callback);
	}

	/**
	 * Calls remote service method {@link NodeRepositoryExternalService#getSummary(String)}.<br/> On success, populates
	 * the store of the {@link #listView} with the retrieved data. On failure, shows an alert message.
	 */
	private void tryGetSummary() {

		AsyncCallback<NodeRepositoryDTO> callback = new AsyncCallback<NodeRepositoryDTO>() {

			public void onSuccess(NodeRepositoryDTO arg0) {
				// show total:
				lfTotalNodes.setValue(arg0.getNodeCount());

				// populate store:
				List<ContentTypeCountModelData> list = new LinkedList<ContentTypeCountModelData>();
				for (ContentTypeCountDTO ctcDto : arg0.getContentTypes()) {
					list.add(new ContentTypeCountModelData(ctcDto));
				}

				ListStore<ContentTypeCountModelData> store = grid.getStore();

				filter.setValue(null);

				store.removeAll();
				store.add(list);

				getUtil().unmask();
			}

			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getNrErrorMessageResolver(),
					getNrMessages().msgErrorGetSummary());
			}
		};

		getNrService().getSummary(getRepositoryId(), callback);
	}

	/**
	 * Shows the information relative to the service: <b>name</b> and <b>description</b><br/>
	 * @param serviceDto
	 */
	private void showServiceInfo(ExternalServiceDTO serviceDto) {
		String serviceName = serviceDto.getName();
		// Window.setTitle(serviceName);
		this.lfName.setValue(serviceName);
		this.taDescription.setValue(serviceDto.getDescription());
	}

	/**
	 * Injects the generic messages bundle.
	 * 
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the button support object.
	 * @param buttonsSupport the buttonsSupport to set
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the generic styles bundle.
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * Injects the form helper object.
	 * @param formSupport the formSupport to set
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * Injects the NR specific styles bundle.
	 * @param nrStyles the nrStyles to set
	 */
	@Inject
	public void setNrStyles(NrStyles nrStyles) {
		this.nrStyles = nrStyles;
	}

	/**
	 * Injects the cell renderer.
	 * @param countCellRenderer the countCellRenderer to set
	 */
	@Inject
	public void setCountCellRenderer(CenteredValueCellRenderer countCellRenderer) {
		this.countCellRenderer = countCellRenderer;
	}
}
